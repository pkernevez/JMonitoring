package org.jmonitoring.console.gwt.server.common;

import javax.annotation.Resource;

import org.jmonitoring.console.gwt.server.flow.ConsoleDao;
import org.jmonitoring.console.gwt.shared.flow.ExecutionFlowDTO;
import org.jmonitoring.console.gwt.shared.flow.MethodCallDTO;
import org.jmonitoring.console.gwt.shared.flow.MethodCallExtractDTO;
import org.jmonitoring.core.configuration.FormaterBean;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class GwtRemoteServiceImplTest extends PersistanceTestCase
{
    @Resource(name = "consoleDao")
    protected ConsoleDao dao;

    @Resource(name = "formater")
    private FormaterBean formater;

    @Autowired
    private GwtRemoteServiceImpl service;

    public GwtRemoteServiceImplTest()
    {
        dataInitialized = true;// Don't insert data before those test
    }

    @Test
    public void testLoadFull()
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
    public void testConvertToDtoDeeply()
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
    public void testLoadMethodCallLoadingAttribute()
    {
        insertTestData();
        MethodCallPO tRootPo = dao.loadFullFlow(1).getFirstMethodCall();
        MethodCallDTO tReadDto = service.loadMethodCall(1, 1);
        assertEquals(tRootPo, tReadDto);
        assertEquals("1", tReadDto.getFlowId());

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
}
