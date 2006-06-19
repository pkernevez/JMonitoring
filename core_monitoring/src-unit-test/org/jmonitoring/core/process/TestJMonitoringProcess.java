package org.jmonitoring.core.process;

import java.util.Date;
import java.util.List;

import org.hibernate.ObjectNotFoundException;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.jmonitoring.core.common.UnknownFlowException;
import org.jmonitoring.core.dao.ExecutionFlowDAO;
import org.jmonitoring.core.dao.FlowSearchCriterion;
import org.jmonitoring.core.dao.PersistanceTestCase;
import org.jmonitoring.core.dao.TestExecutionFlowDAO;
import org.jmonitoring.core.dto.ExecutionFlowDTO;
import org.jmonitoring.core.dto.MethodCallDTO;
import org.jmonitoring.core.persistence.ExecutionFlowPO;
import org.jmonitoring.core.persistence.HibernateManager;
import org.jmonitoring.core.persistence.MethodCallPO;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestJMonitoringProcess extends PersistanceTestCase
{
    public void testDoDatabaseExist()
    {
        JMonitoringProcess tProcess = ProcessFactory.getInstance();
        assertTrue(tProcess.doDatabaseExist());

        Configuration tConfig = HibernateManager.getConfig();
        SchemaExport tDdlexport = new SchemaExport(tConfig);
        tDdlexport.drop(true, true);

        assertFalse(tProcess.doDatabaseExist());

        tProcess.createDataBase();

        assertTrue(tProcess.doDatabaseExist());

    }

    public void testDeleteFlow() throws UnknownFlowException
    {
        JMonitoringProcess tProcess = ProcessFactory.getInstance();
        ExecutionFlowDAO tFlowDAO = new ExecutionFlowDAO(mSession);
        int tNbFlow = tFlowDAO.countFlows();

        // First instert a flow
        ExecutionFlowPO tFlow = TestExecutionFlowDAO.buildNewFullFlow();
        int tFlowId = tFlowDAO.insertFullExecutionFlow(tFlow);
        mSession.flush();
        int tNewNbFlow = tFlowDAO.countFlows();
        assertEquals(tNbFlow + 1, tNewNbFlow);

        tProcess.deleteFlow(tFlowId);
        mSession.flush();
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
        ExecutionFlowDAO tFlowDAO = new ExecutionFlowDAO(mSession);
        int tNbFlow = tFlowDAO.countFlows();

        // First instert 2 flows
        tFlowDAO.insertFullExecutionFlow(TestExecutionFlowDAO.buildNewFullFlow());
        tFlowDAO.insertFullExecutionFlow(TestExecutionFlowDAO.buildNewFullFlow());
        mSession.flush();

        int tNewNbFlow = tFlowDAO.countFlows();
        assertEquals(tNbFlow + 2, tNewNbFlow);

        JMonitoringProcess tProcess = ProcessFactory.getInstance();
        tProcess.deleteAllFlows();
        mSession.flush();
        tNewNbFlow = tFlowDAO.countFlows();
        assertEquals(tNbFlow, tNewNbFlow);
    }

    public void testReadFullExecutionFlow()
    {
        ExecutionFlowDAO tFlowDAO = new ExecutionFlowDAO(mSession);
        ExecutionFlowPO tFlowPO = TestExecutionFlowDAO.buildNewFullFlow();
        int tId = tFlowDAO.insertFullExecutionFlow(tFlowPO);
        mSession.flush();

        int tNewNbFlow = tFlowDAO.countFlows();
        assertEquals(1, tNewNbFlow);
        JMonitoringProcess tProcess = ProcessFactory.getInstance();
        mSession.flush();
        mSession.clear();

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
        assertEquals(2, tReadPoint.getChildren().size());
        tInitialPoint = (MethodCallPO) tInitialPoint.getChildren().get(0);
        tReadPoint = (MethodCallDTO) tReadPoint.getChildren().get(0);
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
        tReadPoint = (MethodCallDTO) tReadPoint.getParent().getChildren().get(1);
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
        ExecutionFlowDAO tFlowDAO = new ExecutionFlowDAO(mSession);

        int tExpectedResult = tFlowDAO.countFlows();
        tFlowDAO.insertFullExecutionFlow(TestExecutionFlowDAO.buildNewFullFlow());
        tFlowDAO.insertFullExecutionFlow(TestExecutionFlowDAO.buildNewFullFlow());
        tFlowDAO.insertFullExecutionFlow(TestExecutionFlowDAO.buildNewFullFlow());
        mSession.flush();

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
        ExecutionFlowDAO tFlowDAO = new ExecutionFlowDAO(mSession);
        tFlowDAO.insertFullExecutionFlow(TestExecutionFlowDAO.buildNewFullFlow());
        ExecutionFlowPO tExecPo = TestExecutionFlowDAO.buildNewFullFlow();
        tExecPo.setThreadName("TEST-13main");
        tFlowDAO.insertFullExecutionFlow(tExecPo);

        JMonitoringProcess tProcess = ProcessFactory.getInstance();
        mSession.flush();

        FlowSearchCriterion tCriterion = new FlowSearchCriterion();
        tCriterion.setThreadName("rr");
        assertEquals(0, tProcess.getListOfExecutionFlowDto(tCriterion).size());

        tCriterion.setThreadName("");
        assertEquals(2, tProcess.getListOfExecutionFlowDto(tCriterion).size());

        tCriterion.setThreadName("TEST-main");
        assertEquals(1, tProcess.getListOfExecutionFlowDto(tCriterion).size());

        tCriterion.setThreadName("TEST");
        assertEquals(2, tProcess.getListOfExecutionFlowDto(tCriterion).size());

        tExecPo = TestExecutionFlowDAO.buildNewFullFlow();
        tExecPo.setThreadName("TEST-13main");
        tFlowDAO.insertFullExecutionFlow(tExecPo);
        mSession.flush();
        tCriterion.setThreadName("TEST");
        assertEquals(3, tProcess.getListOfExecutionFlowDto(tCriterion).size());
    }

    public void testGetListOfExecutionFlowWithDurationMin()
    {
        ExecutionFlowDAO tFlowDAO = new ExecutionFlowDAO(mSession);
        tFlowDAO.insertFullExecutionFlow(TestExecutionFlowDAO.buildNewFullFlow());
        mSession.flush();

        FlowSearchCriterion tCriterion = new FlowSearchCriterion();
        tCriterion.setDurationMin(new Long(10));
        JMonitoringProcess tProcess = ProcessFactory.getInstance();

        assertEquals(1, tProcess.getListOfExecutionFlowDto(tCriterion).size());

        tCriterion.setDurationMin(new Long(25));
        assertEquals(0, tProcess.getListOfExecutionFlowDto(tCriterion).size());
    }

    public void testGetListOfExecutionFlowWithBeginDate()
    {
        ExecutionFlowDAO tFlowDAO = new ExecutionFlowDAO(mSession);
        ExecutionFlowPO tFlow = TestExecutionFlowDAO.buildNewFullFlow();
        tFlowDAO.insertFullExecutionFlow(tFlow);
        mSession.flush();

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
        tCriterion.setBeginDate(new Date(tToday.getTime() - ExecutionFlowDAO.ONE_DAY));
        assertEquals(0, tProcess.getListOfExecutionFlowDto(tCriterion).size());

        // Tomorrow
        tCriterion.setBeginDate(new Date(tToday.getTime() + ExecutionFlowDAO.ONE_DAY));
        assertEquals(0, tProcess.getListOfExecutionFlowDto(tCriterion).size());
    }

    public void testGetListOfExecutionFlowWithGroupName()
    {
        ExecutionFlowDAO tFlowDAO = new ExecutionFlowDAO(mSession);
        ExecutionFlowPO tFlow = TestExecutionFlowDAO.buildNewFullFlow();
        tFlowDAO.insertFullExecutionFlow(tFlow);
        mSession.flush();

        FlowSearchCriterion tCriterion = new FlowSearchCriterion();
        JMonitoringProcess tProcess = ProcessFactory.getInstance();
        tCriterion.setGroupName("GrDefault");
        assertEquals(1, tProcess.getListOfExecutionFlowDto(tCriterion).size());

        tCriterion.setGroupName("GrDefa");
        assertEquals(1, tProcess.getListOfExecutionFlowDto(tCriterion).size());

        tCriterion.setGroupName("toto");
        assertEquals(0, tProcess.getListOfExecutionFlowDto(tCriterion).size());
        // Todo : groupName, className et methodName
    }

    public void testGetListOfExecutionFlowWithClassName()
    {
        ExecutionFlowDAO tFlowDAO = new ExecutionFlowDAO(mSession);
        ExecutionFlowPO tFlow = TestExecutionFlowDAO.buildNewFullFlow();
        tFlowDAO.insertFullExecutionFlow(tFlow);
        mSession.flush();

        FlowSearchCriterion tCriterion = new FlowSearchCriterion();
        JMonitoringProcess tProcess = ProcessFactory.getInstance();

        String tClassName = TestExecutionFlowDAO.class.getName();
        tCriterion.setClassName(tClassName);
        assertEquals(1, tProcess.getListOfExecutionFlowDto(tCriterion).size());

        tCriterion.setClassName(tClassName.substring(0, 3));
        assertEquals(1, tProcess.getListOfExecutionFlowDto(tCriterion).size());

        tCriterion.setClassName("toto");
        assertEquals(0, tProcess.getListOfExecutionFlowDto(tCriterion).size());
        // Todo : groupName, className et methodName
    }

    public void testGetListOfExecutionFlowWithMethodName()
    {
        ExecutionFlowDAO tFlowDAO = new ExecutionFlowDAO(mSession);
        ExecutionFlowPO tFlow = TestExecutionFlowDAO.buildNewFullFlow();
        tFlowDAO.insertFullExecutionFlow(tFlow);
        mSession.flush();

        FlowSearchCriterion tCriterion = new FlowSearchCriterion();

        JMonitoringProcess tProcess = ProcessFactory.getInstance();
        tCriterion.setMethodName("builNewFullFlow");
        assertEquals(1, tProcess.getListOfExecutionFlowDto(tCriterion).size());

        tCriterion.setMethodName("builNewFull");
        assertEquals(1, tProcess.getListOfExecutionFlowDto(tCriterion).size());

        tCriterion.setMethodName("toto");
        assertEquals(0, tProcess.getListOfExecutionFlowDto(tCriterion).size());
    }

    public void testGetListOfMethodCallFromClassAndMethodName()
    {
        ExecutionFlowDAO tFlowDAO = new ExecutionFlowDAO(mSession);
        tFlowDAO.insertFullExecutionFlow(TestExecutionFlowDAO.buildNewFullFlow());
        mSession.flush();

        JMonitoringProcess tProcess = ProcessFactory.getInstance();

        List tMethodsDto = tProcess.getListOfMethodCallFromClassAndMethodName(null, null);
        assertEquals(0, tMethodsDto.size());

        tMethodsDto = tProcess.getListOfMethodCallFromClassAndMethodName(TestExecutionFlowDAO.class.getName(), "");
        assertEquals(3, tMethodsDto.size());

        tMethodsDto = tProcess.getListOfMethodCallFromClassAndMethodName(TestExecutionFlowDAO.class.getName()
                        .substring(0, 5), "");
        assertEquals(3, tMethodsDto.size());

        tMethodsDto = tProcess.getListOfMethodCallFromClassAndMethodName("3333", "");
        assertEquals(0, tMethodsDto.size());

        tMethodsDto = tProcess.getListOfMethodCallFromClassAndMethodName("", "builNewFullFlow");
        assertEquals(3, tMethodsDto.size());

        tMethodsDto = tProcess.getListOfMethodCallFromClassAndMethodName("", "builNewFullFlow2");
        assertEquals(1, tMethodsDto.size());

        tMethodsDto = tProcess.getListOfMethodCallFromClassAndMethodName("", "builNewFullFlow3");
        assertEquals(1, tMethodsDto.size());

        tMethodsDto = tProcess.getListOfMethodCallFromClassAndMethodName(TestExecutionFlowDAO.class.getName()
                        .substring(0, 25), "builNewFullFlow");
        assertEquals(3, tMethodsDto.size());

        tMethodsDto = tProcess.getListOfMethodCallFromClassAndMethodName(TestExecutionFlowDAO.class.getName()
                        .substring(0, 25), "builNewFullFlow2");
        assertEquals(1, tMethodsDto.size());

    }

    public void testGetMethodCallFullExtract()
    {
        ExecutionFlowDAO tFlowDAO = new ExecutionFlowDAO(mSession);
        tFlowDAO.insertFullExecutionFlow(TestExecutionFlowDAO.buildNewFullFlow());
        mSession.flush();

        JMonitoringProcess tProcess = ProcessFactory.getInstance();
        String tClassName = TestExecutionFlowDAO.class.getName();
        
        List tResult = tProcess.getListOfMethodCallFullExtract(tClassName, "builNewFullFlow", 5L, 5L);
        assertEquals(0, tResult.size());
        
        tResult = tProcess.getListOfMethodCallFullExtract(tClassName, "builNewFullFlow", 21L, 21L);
        assertEquals(0, tResult.size());

        tResult = tProcess.getListOfMethodCallFullExtract(tClassName, "builNewFullFlow", 5L, 21L);
        assertEquals(1, tResult.size());

        tResult = tProcess.getListOfMethodCallFullExtract("kj", "builNewFullFlow", 5L, 21L);
        assertEquals(0, tResult.size());

        tResult = tProcess.getListOfMethodCallFullExtract(tClassName, "builNewFullFlow2", 5L, 21L);
        assertEquals(0, tResult.size());


    }
}
