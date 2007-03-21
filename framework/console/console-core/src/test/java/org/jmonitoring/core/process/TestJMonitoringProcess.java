package org.jmonitoring.core.process;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.jmonitoring.common.hibernate.HibernateManager;
import org.jmonitoring.core.common.UnknownFlowException;
import org.jmonitoring.core.configuration.ConfigurationHelper;
import org.jmonitoring.core.dao.ConsoleDao;
import org.jmonitoring.core.dao.FlowSearchCriterion;
import org.jmonitoring.core.dao.TestConsoleDao;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.jmonitoring.core.dto.DtoHelper;
import org.jmonitoring.core.dto.ExecutionFlowDTO;
import org.jmonitoring.core.dto.MethodCallDTO;
import org.jmonitoring.test.dao.PersistanceTestCase;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestJMonitoringProcess extends PersistanceTestCase
{
    public void testThereIsOnlyOneSession()
    {
        JMonitoringProcess tProcess = ProcessFactory.getInstance();
        assertSame(getSession(), tProcess.getASession());
    }

    public void testDoDatabaseExist()
    {
        JMonitoringProcess tProcess = ProcessFactory.getInstance();
        assertTrue(tProcess.doDatabaseExist());

        closeAndRestartSession();
        Configuration tConfig = HibernateManager.getConfig();
        SchemaExport tDdlexport = new SchemaExport(tConfig);
        tDdlexport.drop(true, true);

        assertFalse(tProcess.doDatabaseExist());

        tProcess.createDataBase();

        closeAndRestartSession();
        assertTrue(tProcess.doDatabaseExist());

    }

    public void testDeleteFlow() throws UnknownFlowException
    {
        JMonitoringProcess tProcess = ProcessFactory.getInstance();
        ConsoleDao tFlowDAO = new ConsoleDao(getSession());
        int tNbFlow = tFlowDAO.countFlows();

        // First instert a flow
        ExecutionFlowPO tFlow = TestConsoleDao.buildNewFullFlow();
        int tFlowId = tFlowDAO.insertFullExecutionFlow(tFlow);
        getSession().flush();
        int tNewNbFlow = tFlowDAO.countFlows();
        assertEquals(tNbFlow + 1, tNewNbFlow);

        tProcess.deleteFlow(tFlowId);

        closeAndRestartSession();
        tFlowDAO = new ConsoleDao(getSession());
        tNewNbFlow = tFlowDAO.countFlows();
        assertEquals(tNbFlow, tNewNbFlow);

        try
        {
            tProcess.deleteFlow(3456);
        } catch (UnknownFlowException e)
        {
            assertEquals("Flow with Id=3456 could not be retreive from database, and can't be delete", e.getMessage());
        }
    }

    public void testDeleteAllFlows()
    {
        ConsoleDao tFlowDAO = new ConsoleDao(getSession());
        int tNbFlow = tFlowDAO.countFlows();

        // First instert 2 flows
        tFlowDAO.insertFullExecutionFlow(TestConsoleDao.buildNewFullFlow());
        tFlowDAO.insertFullExecutionFlow(TestConsoleDao.buildNewFullFlow());
        getSession().flush();

        int tNewNbFlow = tFlowDAO.countFlows();
        assertEquals(tNbFlow + 2, tNewNbFlow);

        JMonitoringProcess tProcess = ProcessFactory.getInstance();
        tProcess.deleteAllFlows();
        closeAndRestartSession();
        tFlowDAO = new ConsoleDao(getSession());

        tNewNbFlow = tFlowDAO.countFlows();
        assertEquals(tNbFlow, tNewNbFlow);
    }

    public void testReadFullExecutionFlow()
    {
        ConsoleDao tFlowDAO = new ConsoleDao(getSession());
        ExecutionFlowPO tFlowPO = TestConsoleDao.buildNewFullFlow();
        int tId = tFlowDAO.insertFullExecutionFlow(tFlowPO);
        getSession().flush();

        int tNewNbFlow = tFlowDAO.countFlows();
        assertEquals(1, tNewNbFlow);
        JMonitoringProcess tProcess = ProcessFactory.getInstance();
        getSession().flush();
        getSession().clear();

        ExecutionFlowDTO tReadFlow = tProcess.readFullExecutionFlow(tId);

        // Check the equality of the Flow
        assertEquals(tFlowPO.getJvmIdentifier(), tReadFlow.getJvmIdentifier());
        assertEquals(tFlowPO.getThreadName(), tReadFlow.getThreadName());
        assertEquals(new Date(tFlowPO.getBeginTime()), tReadFlow.getBeginTime());
        assertEquals(tFlowPO.getDuration(), tReadFlow.getDuration());
        assertEquals(new Date(tFlowPO.getEndTime()), tReadFlow.getEndTime());

        // Check equality of the first measure point
        MethodCallPO tInitialPoint = tFlowPO.getFirstMethodCall();
        MethodCallDTO tReadPoint = tReadFlow.getFirstMethodCall();
        assertEquals(tInitialPoint.getClassName(), tReadPoint.getClassName());
        assertEquals(tInitialPoint.getMethodName(), tReadPoint.getMethodName());
        assertEquals("[]", tReadPoint.getParams());
        assertEquals(tInitialPoint.getReturnValue(), tReadPoint.getReturnValue());
        assertEquals(tInitialPoint.getThrowableClass(), tReadPoint.getThrowableClassName());
        assertEquals(tInitialPoint.getThrowableMessage(), tReadPoint.getThrowableMessage());
        assertEquals(new Date(tInitialPoint.getBeginTime()), tReadPoint.getBeginTime());
        assertEquals(new Date(tInitialPoint.getEndTime()), tReadPoint.getEndTime());
        assertEquals(tInitialPoint.getGroupName(), tReadPoint.getGroupName());

        // Check equality of the first child measure point
        assertEquals(2, tInitialPoint.getChildren().size());
        assertEquals(2, tReadPoint.getChildren().length);
        tInitialPoint = (MethodCallPO) tInitialPoint.getChildren().get(0);
        tReadPoint = (MethodCallDTO) tReadPoint.getChild(0);
        assertEquals(tInitialPoint.getClassName(), tReadPoint.getClassName());
        assertEquals(tInitialPoint.getMethodName(), tReadPoint.getMethodName());
        assertEquals("[]", tReadPoint.getParams());
        assertEquals(tInitialPoint.getReturnValue(), tReadPoint.getReturnValue());
        assertEquals(tInitialPoint.getThrowableClass(), tReadPoint.getThrowableClassName());
        assertEquals(tInitialPoint.getThrowableMessage(), tReadPoint.getThrowableMessage());
        assertEquals(new Date(tInitialPoint.getBeginTime()), tReadPoint.getBeginTime());
        assertEquals(new Date(tInitialPoint.getEndTime()), tReadPoint.getEndTime());
        assertEquals(tInitialPoint.getGroupName(), tReadPoint.getGroupName());

        // Check equality of the second child measure point
        tInitialPoint = (MethodCallPO) tInitialPoint.getParentMethodCall().getChildren().get(1);
        tReadPoint = (MethodCallDTO) tReadPoint.getParent().getChild(1);
        assertEquals(tInitialPoint.getClassName(), tReadPoint.getClassName());
        assertEquals(tInitialPoint.getMethodName(), tReadPoint.getMethodName());
        assertEquals("[]", tReadPoint.getParams());
        assertEquals(tInitialPoint.getReturnValue(), tReadPoint.getReturnValue());
        assertEquals(tInitialPoint.getThrowableClass(), tReadPoint.getThrowableClassName());
        assertEquals(tInitialPoint.getThrowableMessage(), tReadPoint.getThrowableMessage());
        assertEquals(new Date(tInitialPoint.getBeginTime()), tReadPoint.getBeginTime());
        assertEquals(new Date(tInitialPoint.getEndTime()), tReadPoint.getEndTime());
        assertEquals(tInitialPoint.getGroupName(), tReadPoint.getGroupName());

    }

    public void testGetListOfExecutionFlowWithoutCriteria()
    {
        ConsoleDao tFlowDAO = new ConsoleDao(getSession());

        int tExpectedResult = tFlowDAO.countFlows();
        tFlowDAO.insertFullExecutionFlow(TestConsoleDao.buildNewFullFlow());
        tFlowDAO.insertFullExecutionFlow(TestConsoleDao.buildNewFullFlow());
        tFlowDAO.insertFullExecutionFlow(TestConsoleDao.buildNewFullFlow());
        getSession().flush();

        JMonitoringProcess tProcess = ProcessFactory.getInstance();
        List tFlows = tProcess.getListOfExecutionFlowDto(new FlowSearchCriterion());
        assertEquals(tExpectedResult + 3, tFlows.size());

    }

    /**
     * @todo Add test with criteria.
     * @throws SQLException
     */
    public void testGetListOfExecutionFlowWithThreadName()
    {
        ConsoleDao tFlowDAO = new ConsoleDao(getSession());
        tFlowDAO.insertFullExecutionFlow(TestConsoleDao.buildNewFullFlow());
        ExecutionFlowPO tExecPo = TestConsoleDao.buildNewFullFlow();
        tExecPo.setThreadName("TEST-13main");
        tFlowDAO.insertFullExecutionFlow(tExecPo);

        JMonitoringProcess tProcess = ProcessFactory.getInstance();
        getSession().flush();

        FlowSearchCriterion tCriterion = new FlowSearchCriterion();
        tCriterion.setThreadName("rr");
        assertEquals(0, tProcess.getListOfExecutionFlowDto(tCriterion).size());

        tCriterion.setThreadName("");
        closeAndRestartSession();
        assertEquals(2, tProcess.getListOfExecutionFlowDto(tCriterion).size());

        tCriterion.setThreadName("TEST-main");
        closeAndRestartSession();
        assertEquals(1, tProcess.getListOfExecutionFlowDto(tCriterion).size());

        tCriterion.setThreadName("TEST");
        closeAndRestartSession();
        assertEquals(2, tProcess.getListOfExecutionFlowDto(tCriterion).size());

        tExecPo = TestConsoleDao.buildNewFullFlow();
        tExecPo.setThreadName("TEST-13main");

        closeAndRestartSession();
        tFlowDAO = new ConsoleDao(getSession());
        tFlowDAO.insertFullExecutionFlow(tExecPo);
        getSession().flush();
        tCriterion.setThreadName("TEST");
        assertEquals(3, tProcess.getListOfExecutionFlowDto(tCriterion).size());
    }

    public void testGetListOfExecutionFlowWithDurationMin()
    {
        ConsoleDao tFlowDAO = new ConsoleDao(getSession());
        tFlowDAO.insertFullExecutionFlow(TestConsoleDao.buildNewFullFlow());
        getSession().flush();

        FlowSearchCriterion tCriterion = new FlowSearchCriterion();
        tCriterion.setDurationMin(new Long(10));
        JMonitoringProcess tProcess = ProcessFactory.getInstance();

        assertEquals(1, tProcess.getListOfExecutionFlowDto(tCriterion).size());

        tCriterion.setDurationMin(new Long(35));
        closeAndRestartSession();
        assertEquals(0, tProcess.getListOfExecutionFlowDto(tCriterion).size());
    }

    public void testGetListOfExecutionFlowWithBeginDate()
    {
        ConsoleDao tFlowDAO = new ConsoleDao(getSession());
        ExecutionFlowPO tFlow = TestConsoleDao.buildNewFullFlow();
        tFlowDAO.insertFullExecutionFlow(tFlow);
        getSession().flush();

        Date tToday = new Date(tFlow.getBeginTime());
        tToday.setSeconds(0);
        tToday.setMinutes(0);
        tToday.setHours(0);

        FlowSearchCriterion tCriterion = new FlowSearchCriterion();
        JMonitoringProcess tProcess = ProcessFactory.getInstance();
        // Today
        tCriterion.setBeginDate(tToday);
        assertEquals(1, tProcess.getListOfExecutionFlowDto(tCriterion).size());

        // Yesterday
        tCriterion.setBeginDate(new Date(tToday.getTime() - ConsoleDao.ONE_DAY));
        closeAndRestartSession();
        assertEquals(0, tProcess.getListOfExecutionFlowDto(tCriterion).size());

        // Tomorrow
        tCriterion.setBeginDate(new Date(tToday.getTime() + ConsoleDao.ONE_DAY));
        closeAndRestartSession();
        assertEquals(0, tProcess.getListOfExecutionFlowDto(tCriterion).size());
    }

    public void testGetListOfExecutionFlowWithGroupName()
    {
        ConsoleDao tFlowDAO = new ConsoleDao(getSession());
        ExecutionFlowPO tFlow = TestConsoleDao.buildNewFullFlow();
        tFlowDAO.insertFullExecutionFlow(tFlow);
        getSession().flush();

        FlowSearchCriterion tCriterion = new FlowSearchCriterion();
        JMonitoringProcess tProcess = ProcessFactory.getInstance();
        tCriterion.setGroupName("GrDefault");
        assertEquals(1, tProcess.getListOfExecutionFlowDto(tCriterion).size());

        tCriterion.setGroupName("GrDefa");
        closeAndRestartSession();
        assertEquals(1, tProcess.getListOfExecutionFlowDto(tCriterion).size());

        tCriterion.setGroupName("toto");
        closeAndRestartSession();
        assertEquals(0, tProcess.getListOfExecutionFlowDto(tCriterion).size());
        // Todo : groupName, className et methodName
    }

    public void testGetListOfExecutionFlowWithClassName()
    {
        ConsoleDao tFlowDAO = new ConsoleDao(getSession());
        ExecutionFlowPO tFlow = TestConsoleDao.buildNewFullFlow();
        tFlowDAO.insertFullExecutionFlow(tFlow);
        getSession().flush();

        FlowSearchCriterion tCriterion = new FlowSearchCriterion();
        JMonitoringProcess tProcess = ProcessFactory.getInstance();

        String tClassName = PersistanceTestCase.class.getName();
        tCriterion.setClassName(tClassName);
        assertEquals(1, tProcess.getListOfExecutionFlowDto(tCriterion).size());

        tCriterion.setClassName(tClassName.substring(0, 3));
        closeAndRestartSession();
        assertEquals(1, tProcess.getListOfExecutionFlowDto(tCriterion).size());

        tCriterion.setClassName("toto");
        closeAndRestartSession();
        assertEquals(0, tProcess.getListOfExecutionFlowDto(tCriterion).size());
        // Todo : groupName, className et methodName
    }

    public void testGetListOfExecutionFlowWithMethodName()
    {
        ConsoleDao tFlowDAO = new ConsoleDao(getSession());
        ExecutionFlowPO tFlow = TestConsoleDao.buildNewFullFlow();
        tFlowDAO.insertFullExecutionFlow(tFlow);
        getSession().flush();

        FlowSearchCriterion tCriterion = new FlowSearchCriterion();

        JMonitoringProcess tProcess = ProcessFactory.getInstance();
        tCriterion.setMethodName("builNewFullFlow");
        assertEquals(1, tProcess.getListOfExecutionFlowDto(tCriterion).size());

        tCriterion.setMethodName("builNewFull");
        closeAndRestartSession();
        assertEquals(1, tProcess.getListOfExecutionFlowDto(tCriterion).size());

        tCriterion.setMethodName("toto");
        closeAndRestartSession();
        assertEquals(0, tProcess.getListOfExecutionFlowDto(tCriterion).size());
    }

    public void testGetListOfMethodCallFromClassAndMethodName()
    {
        ConsoleDao tFlowDAO = new ConsoleDao(getSession());
        tFlowDAO.insertFullExecutionFlow(TestConsoleDao.buildNewFullFlow());
        getSession().flush();

        JMonitoringProcess tProcess = ProcessFactory.getInstance();

        closeAndRestartSession();
        List tMethodsDto = tProcess.getListOfMethodCallFromClassAndMethodName(null, null);
        assertEquals(0, tMethodsDto.size());

        closeAndRestartSession();
        tMethodsDto = tProcess.getListOfMethodCallFromClassAndMethodName(PersistanceTestCase.class.getName(), "");
        assertEquals(6, tMethodsDto.size());

        closeAndRestartSession();
        tMethodsDto = tProcess.getListOfMethodCallFromClassAndMethodName(
            PersistanceTestCase.class.getName().substring(0, 5), "");
        assertEquals(6, tMethodsDto.size());

        closeAndRestartSession();
        tMethodsDto = tProcess.getListOfMethodCallFromClassAndMethodName("3333", "");
        assertEquals(0, tMethodsDto.size());

        closeAndRestartSession();
        tMethodsDto = tProcess.getListOfMethodCallFromClassAndMethodName("", "builNewFullFlow");
        assertEquals(6, tMethodsDto.size());

        closeAndRestartSession();
        tMethodsDto = tProcess.getListOfMethodCallFromClassAndMethodName("", "builNewFullFlow2");
        assertEquals(1, tMethodsDto.size());

        closeAndRestartSession();
        tMethodsDto = tProcess.getListOfMethodCallFromClassAndMethodName("", "builNewFullFlow3");
        assertEquals(4, tMethodsDto.size());

        closeAndRestartSession();
        tMethodsDto = tProcess.getListOfMethodCallFromClassAndMethodName(PersistanceTestCase.class.getName()
            .substring(0, 25), "builNewFullFlow");
        assertEquals(6, tMethodsDto.size());

        closeAndRestartSession();
        tMethodsDto = tProcess.getListOfMethodCallFromClassAndMethodName(PersistanceTestCase.class.getName()
            .substring(0, 25), "builNewFullFlow2");
        assertEquals(1, tMethodsDto.size());

    }

    public void testGetMethodCallFullExtract()
    {
        ConsoleDao tFlowDAO = new ConsoleDao(getSession());
        tFlowDAO.insertFullExecutionFlow(TestConsoleDao.buildNewFullFlow());
        getSession().flush();

        JMonitoringProcess tProcess = ProcessFactory.getInstance();
        String tClassName = PersistanceTestCase.class.getName();

        List tResult = tProcess.getListOfMethodCallFullExtract(tClassName, "builNewFullFlow", 5L, 5L);
        assertEquals(0, tResult.size());

        closeAndRestartSession();
        tResult = tProcess.getListOfMethodCallFullExtract(tClassName, "builNewFullFlow", 36L, 36L);
        assertEquals(0, tResult.size());

        closeAndRestartSession();
        tResult = tProcess.getListOfMethodCallFullExtract(tClassName, "builNewFullFlow", 15L, 36L);
        assertEquals(1, tResult.size());

        closeAndRestartSession();
        tResult = tProcess.getListOfMethodCallFullExtract("kj", "builNewFullFlow", 15L, 36L);
        assertEquals(0, tResult.size());

        closeAndRestartSession();
        tResult = tProcess.getListOfMethodCallFullExtract(tClassName, "builNewFullFlow2", 15L, 36L);
        assertEquals(0, tResult.size());

    }

    public void testGetFlowAsXml()
    {
        JMonitoringProcess tProcess = ProcessFactory.getInstance();
        ConsoleDao tFlowDAO = new ConsoleDao(getSession());

        // Firstt instert a flow
        ExecutionFlowPO tFlow = TestConsoleDao.buildNewFullFlow();

        int tFlowId = tFlowDAO.insertFullExecutionFlow(tFlow);
        getSession().flush();
        assertTrue(tFlowId > 0);

        ExecutionFlowDTO tFlowDto = DtoHelper.getDeepCopy(tFlow);
        byte[] tFlowAsXml = tProcess.getFlowAsXml(tFlowDto);
        assertTrue("The byte[] is to small...[" + tFlowAsXml.length + "]", tFlowAsXml.length > 100);
    }

    public void testSerialisationConversion()
    {
        JMonitoringProcess tProcess = ProcessFactory.getInstance();
        ConsoleDao tFlowDAO = new ConsoleDao(getSession());
        ConfigurationHelper.getInstance().addProperty("format.ihm.date", "dd/MM/yy");
        ConfigurationHelper.getInstance().addProperty("format.ihm.time", "HH:mm:ss");
        
        // Firstt instert a flow
        ExecutionFlowPO tFlow = TestConsoleDao.buildNewFullFlow();

        int tFlowId = tFlowDAO.insertFullExecutionFlow(tFlow);
        assertTrue(tFlowId > 0);
        getSession().flush();

        ExecutionFlowDTO tFlowDto = DtoHelper.getDeepCopy(tFlow);
        byte[] tFlowAsXml = tProcess.getFlowAsXml(tFlowDto);

        ExecutionFlowDTO tNewFlow = tProcess.getFlowFromXml(tFlowAsXml);
        assertNotSame(tFlowDto, tNewFlow);
        assertEquals(tFlowDto.getDuration(), tNewFlow.getDuration());
        assertEquals(tFlowDto.getBeginDateAsString(), tNewFlow.getBeginDateAsString());
        assertEquals(tFlowDto.getBeginTime(), tNewFlow.getBeginTime());
        assertEquals(tFlowDto.getClassName(), tNewFlow.getClassName());
        assertEquals(tFlowDto.getEndTime(), tNewFlow.getEndTime());
        assertEquals(tFlowDto.getId(), tNewFlow.getId());
        assertEquals(tFlowDto.getJvmIdentifier(), tNewFlow.getJvmIdentifier());
        assertEquals(tFlowDto.getMeasureCount(), tNewFlow.getMeasureCount());
        assertEquals(tFlowDto.getMethodName(), tNewFlow.getMethodName());
        assertEquals(tFlowDto.getThreadName(), tNewFlow.getThreadName());
        MethodCallDTO tMeth = tFlowDto.getFirstMethodCall();
        MethodCallDTO tNewMeth = tNewFlow.getFirstMethodCall();
        assertNotSame(tMeth, tNewMeth);
        assertEquals(tMeth.getDuration(), tNewMeth.getDuration());
        assertEquals(tMeth.isReturnCallException(), tNewMeth.isReturnCallException());
        assertEquals(tMeth.getBeginTime(), tNewMeth.getBeginTime());
        assertEquals(tMeth.getClassName(), tNewMeth.getClassName());
        assertEquals(tMeth.getEndTime(), tNewMeth.getEndTime());
        assertEquals(tMeth.getFlowId(), tNewMeth.getFlowId());
        assertEquals(tMeth.getGroupName(), tNewMeth.getGroupName());
        assertEquals(tMeth.getMethodName(), tNewMeth.getMethodName());
        assertEquals(tMeth.getParams(), tNewMeth.getParams());
        assertEquals(tMeth.getParent(), tNewMeth.getParent());
        assertEquals(tMeth.getPosition(), tNewMeth.getPosition());
        assertEquals(tMeth.getReturnValue(), tNewMeth.getReturnValue());
        assertEquals(tMeth.getThrowableClassName(), tNewMeth.getThrowableClassName());
        assertEquals(tMeth.getThrowableMessage(), tNewMeth.getThrowableMessage());
    }

    public void testInsertFlowFromXml()
    {
        JMonitoringProcess tProcess = ProcessFactory.getInstance();
        ConsoleDao tFlowDAO = new ConsoleDao(getSession());

        // Firstt instert a flow
        ExecutionFlowPO tFlowPo = TestConsoleDao.buildNewFullFlow();
        int tFlowId = tFlowDAO.insertFullExecutionFlow(tFlowPo);
        getSession().flush();
        ExecutionFlowDTO tFlowDto = DtoHelper.getDeepCopy(tFlowPo);

        // ExecutionFlowDTO tFlowDto = DtoHelper.getDeepCopy(tFlow);
        byte[] tFlowAsBytes = tProcess.getFlowAsXml(tFlowDto);
        ExecutionFlowDTO tNewFlow = tProcess.insertFlowFromXml(tFlowAsBytes);
        assertTrue("The Id must be different [" + tFlowId + "]", tFlowId != tNewFlow.getId());
    }
}
