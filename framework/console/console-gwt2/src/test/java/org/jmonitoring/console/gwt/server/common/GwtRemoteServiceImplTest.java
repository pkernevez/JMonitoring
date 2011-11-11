package org.jmonitoring.console.gwt.server.common;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.jmonitoring.console.gwt.server.flow.ConsoleDao;
import org.jmonitoring.console.gwt.server.flow.Distribution;
import org.jmonitoring.console.gwt.server.flow.Stats;
import org.jmonitoring.console.gwt.shared.flow.ExecutionFlowDTO;
import org.jmonitoring.console.gwt.shared.flow.MethodCallDTO;
import org.jmonitoring.console.gwt.shared.flow.MethodCallExtractDTO;
import org.jmonitoring.console.gwt.shared.flow.UnknownEntity;
import org.jmonitoring.console.gwt.shared.method.MethodCallDistributionDTO;
import org.jmonitoring.core.configuration.FormaterBean;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

public class GwtRemoteServiceImplTest extends PersistanceTestCase
{
    @Resource(name = "formater")
    private FormaterBean formater;

    @Mock
    protected ConsoleDao mockDao;

    @Autowired
    private GwtRemoteServiceImpl service;

    public GwtRemoteServiceImplTest()
    {
        dataInitialized = true;// Don't insert data before those test
    }

    @Test
    public void testLoadFull() throws UnknownEntity
    {
        ExecutionFlowPO tFlowPO = PersistanceTestCase.buildNewFullFlow();
        int tId = dao.insertFullExecutionFlow(tFlowPO);
        getSession().flush();

        int tNewNbFlow = dao.countFlows();
        assertEquals(1, tNewNbFlow);
        getSession().flush();
        getSession().clear();

        ExecutionFlowDTO tReadFlow = service.loadFull(tId);

        // Check the equality of the Flow
        assertEquals(tFlowPO.getJvmIdentifier(), tReadFlow.getJvmIdentifier());
        assertEquals(tFlowPO.getThreadName(), tReadFlow.getThreadName());
        assertEquals(formater.formatDateTime(tFlowPO.getBeginTime()), tReadFlow.getBeginTime());
        assertEquals(String.valueOf(tFlowPO.getDuration()), tReadFlow.getDuration());
        assertEquals(formater.formatDateTime(tFlowPO.getEndTime()), tReadFlow.getEndTime());

        // Check equality of the first measure point
        MethodCallPO tInitialPoint = tFlowPO.getFirstMethodCall();
        MethodCallDTO tReadPoint = tReadFlow.getFirstMethodCall();
        assertNotNull(tReadPoint);
        assertEquals(tInitialPoint, tReadPoint);

        tInitialPoint = tInitialPoint.getChildren().get(0);
        MethodCallExtractDTO tExtractPoint = tReadPoint.getChild(0);
        assertEquals(tInitialPoint.getClassName() + "." + tInitialPoint.getMethodName() + "()",
                     tExtractPoint.getFullMethodName());
        assertEquals("" + tInitialPoint.getDuration(), tExtractPoint.getDuration());
        assertEquals(tInitialPoint.getGroupName(), tExtractPoint.getGroupName());

        // Check equality of the second child measure point
        tInitialPoint = tInitialPoint.getParentMethodCall().getChildren().get(1);
        tExtractPoint = tReadPoint.getChild(1);
        assertEquals(tInitialPoint.getClassName() + "." + tInitialPoint.getMethodName() + "()",
                     tExtractPoint.getFullMethodName());
        assertEquals("" + tInitialPoint.getDuration(), tExtractPoint.getDuration());
        assertEquals(tInitialPoint.getGroupName(), tExtractPoint.getGroupName());

    }

    @Test
    public void testLoadFulNotFoundl()
    {
        try
        {
            service.loadFull(314159265);
            fail("Should not pass !");
        } catch (UnknownEntity e)
        {
            assertTrue(e.getMessage().contains("314159265"));
        }
    }

    private void assertEquals(MethodCallPO tInitialPoint, MethodCallDTO tReadPoint)
    {
        assertNotNull("The flow didn't load sub calls", tReadPoint);
        assertEquals(tInitialPoint.getClassName(), tReadPoint.getClassName());
        MethodCallPO tParentMethodCall = tInitialPoint.getParentMethodCall();
        assertEquals((tParentMethodCall == null ? null : String.valueOf(tParentMethodCall.getPosition())),
                     tReadPoint.getParentPosition());
        assertEquals(tInitialPoint.getClassName(), tReadPoint.getClassName());
        assertEquals(tInitialPoint.getMethodName(), tReadPoint.getMethodName());
        assertEquals(tInitialPoint.getParams(), tReadPoint.getParams());
        assertEquals(tInitialPoint.getReturnValue(), tReadPoint.getReturnValue());
        assertEquals(tInitialPoint.getThrowableClass(), tReadPoint.getThrowableClass());
        assertEquals(tInitialPoint.getThrowableMessage(), tReadPoint.getThrowableMessage());
        assertEquals(formater.formatDateTime(tInitialPoint.getBeginTime()), tReadPoint.getBeginTimeString());
        assertEquals(tInitialPoint.getBeginTime(), tReadPoint.getBeginMilliSeconds());
        assertEquals(formater.formatDateTime(tInitialPoint.getEndTime()), tReadPoint.getEndTimeString());
        assertEquals(tInitialPoint.getEndTime(), tReadPoint.getEndMilliSeconds());
        assertEquals(tInitialPoint.getGroupName(), tReadPoint.getGroupName());
        // Check equality of the first child measure point
        assertEquals(tInitialPoint.getChildren().size(), tReadPoint.getChildren().length);
    }

    @Test
    public void testConvertToDtoDeeply() throws UnknownEntity
    {
        ExecutionFlowPO tFlowPO = PersistanceTestCase.buildNewFullFlow();
        int tId = dao.insertFullExecutionFlow(tFlowPO);
        getSession().flush();
        getSession().clear();
        getStats().clear();

        assertEquals("We don't not need to update the execution flow", 0, getStats().getEntityUpdateCount());
        service.convertToDtoDeeply(dao.loadFullFlow(tId)).getFirstMethodCall();
        sessionFactory.getCurrentSession().flush();
        getSession().flush();
        assertEquals("We don not need to update the execution flow", 0, getStats().getEntityUpdateCount());

        tFlowPO = dao.loadFullFlow(tId);
        MethodCallDTO tRoot = service.convertToDtoDeeply(tFlowPO).getFirstMethodCall();
        MethodCallExtractDTO tExtract = tRoot.getChild(0);
        // From first children : time from Parent
        assertEquals("2", tExtract.getTimeFromPrevChild());
        tExtract = tRoot.getChild(1);
        // From 2nd child : time from Brother
        assertEquals("3", tExtract.getTimeFromPrevChild());

    }

    @Test
    public void testLoadMethodCallLoadingAttribute() throws UnknownEntity
    {
        insertTestData();
        MethodCallPO tRootPo = dao.loadFullFlow(1).getFirstMethodCall();
        MethodCallDTO tReadDto = service.loadMethodCall(1, 1);
        assertEquals(tRootPo, tReadDto);
        assertEquals("1", tReadDto.getFlowId());

        tReadDto = service.loadMethodCall(4, 1);
        assertEquals("MainClass3", tReadDto.getClassName());
        assertEquals(false, tReadDto.getChild(0).hasThrownException());
        assertEquals(true, tReadDto.getChild(2).hasThrownException());

    }

    @Test
    public void testLoadMethodCallLoadingCollection()
    {
        insertTestData();
        // MethodCallPO tRootPo = dao.loadFullFlow(1).getFirstMethodCall();
        MethodCallDTO tReadDto = service.loadMethodCall(4, 1);
        assertNotNull(tReadDto.getChildren());
        assertEquals(3, tReadDto.getChildren().length);
        // Don't load grandchild collection when having no grandchild
        assertNull(tReadDto.getChild(0).getChildren()); // FAIL
        // Don't load grandchild collection when having grandchilds
        assertNull(tReadDto.getChild(1).getChildren());
        // Check DB Call
        assertEquals(1, getStats().getCollectionFetchCount());// 4
        assertEquals(4, getStats().getEntityLoadCount());// 4
        assertEquals(0, getStats().getEntityFetchCount());
        assertEquals(0, getStats().getEntityUpdateCount());// 0
    }

    @Test
    public void testGetDefaultGapDuration()
    {
        // TODO Use mockito every where here ?
        // mock(null)
        MockitoAnnotations.initMocks(this);
        // @MockitoJUnitRunner.
        GwtRemoteServiceImpl tService = new GwtRemoteServiceImpl();
        tService.dao = mockDao;

        // when(mockDao.getDurationMinMax(anyString(), anyString())).thenReturn(10L);
        assertEquals(1, tService.getDefaultGapDuration(10L));
        assertEquals(1, tService.getDefaultGapDuration(249L));
        assertEquals(5, tService.getDefaultGapDuration(250L));
        assertEquals(10, tService.getDefaultGapDuration(500L));
    }

    @Test
    public void testGetDistributionAndGenerateImage()
    {
        MockitoAnnotations.initMocks(this);
        // @MockitoJUnitRunner.
        GwtRemoteServiceImpl tService = new GwtRemoteServiceImpl();
        tService.dao = mockDao;
        tService.formater = formater;

        MethodCallPO tMeth = new MethodCallPO();
        tMeth.setClassName("TestClassName");
        tMeth.setMethodName("Methode");
        when(mockDao.loadMethodCall(2, 4)).thenReturn(tMeth);
        Stats tStat = new Stats(2, 234, 50.4567, 1256).setStdDeviation(12.678809);
        when(mockDao.getDurationStats("TestClassName", "Methode")).thenReturn(tStat);
        List<Distribution> tDistribList = new ArrayList<Distribution>();
        when(mockDao.getDistribution("2", "4", 10)).thenReturn(tDistribList);

        MethodCallDistributionDTO tResult = tService.getDistributionAndGenerateImage(2, 4, 10);
        assertEquals("2", tResult.getMinDuration());
        assertEquals("234", tResult.getMaxDuration());
        assertEquals("50.46", tResult.getAvgDuration());
        assertEquals("1256", tResult.getNbOccurences());
        assertEquals("12.68", tResult.getStdDeviationDuration());
    }

    @Test
    public void testRound()
    {
        GwtRemoteServiceImpl tService = new GwtRemoteServiceImpl();
        tService.formater = formater;
        assertEquals("45", tService.round(45.0));
        assertEquals("78.2", tService.round(78.2));
        assertEquals("45.98", tService.round(45.9846));
        assertEquals("45.99", tService.round(45.9876));
    }
}
