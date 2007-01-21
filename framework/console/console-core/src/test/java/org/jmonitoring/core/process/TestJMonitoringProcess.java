package org.jmonitoring.core.process;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.jmonitoring.core.common.UnknownFlowException;
import org.jmonitoring.core.dao.ExecutionFlowDaoFactory;
import org.jmonitoring.core.dao.FlowSearchCriterion;
import org.jmonitoring.core.dao.IExecutionFlowDAO;
import org.jmonitoring.core.dao.PersistanceTestCase;
import org.jmonitoring.core.dao.TestExecutionFlowDAO;
import org.jmonitoring.core.dto.DtoHelper;
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
        IExecutionFlowDAO tFlowDAO = ExecutionFlowDaoFactory.getExecutionFlowDao(getSession());
        int tNbFlow = tFlowDAO.countFlows();

        // First instert a flow
        ExecutionFlowPO tFlow = TestExecutionFlowDAO.buildNewFullFlow();
        int tFlowId = tFlowDAO.insertFullExecutionFlow(tFlow);
        getSession().flush();
        int tNewNbFlow = tFlowDAO.countFlows();
        assertEquals(tNbFlow + 1, tNewNbFlow);

        tProcess.deleteFlow(tFlowId);
        getSession().flush();
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
        IExecutionFlowDAO tFlowDAO = ExecutionFlowDaoFactory.getExecutionFlowDao(getSession());
        int tNbFlow = tFlowDAO.countFlows();

        // First instert 2 flows
        tFlowDAO.insertFullExecutionFlow(TestExecutionFlowDAO.buildNewFullFlow());
        tFlowDAO.insertFullExecutionFlow(TestExecutionFlowDAO.buildNewFullFlow());
        getSession().flush();

        int tNewNbFlow = tFlowDAO.countFlows();
        assertEquals(tNbFlow + 2, tNewNbFlow);

        JMonitoringProcess tProcess = ProcessFactory.getInstance();
        tProcess.deleteAllFlows();
        getSession().flush();
        tNewNbFlow = tFlowDAO.countFlows();
        assertEquals(tNbFlow, tNewNbFlow);
    }

    public void testReadFullExecutionFlow()
    {
        IExecutionFlowDAO tFlowDAO = ExecutionFlowDaoFactory.getExecutionFlowDao(getSession());
        ExecutionFlowPO tFlowPO = TestExecutionFlowDAO.buildNewFullFlow();
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
        IExecutionFlowDAO tFlowDAO = ExecutionFlowDaoFactory.getExecutionFlowDao(getSession());

        int tExpectedResult = tFlowDAO.countFlows();
        tFlowDAO.insertFullExecutionFlow(TestExecutionFlowDAO.buildNewFullFlow());
        tFlowDAO.insertFullExecutionFlow(TestExecutionFlowDAO.buildNewFullFlow());
        tFlowDAO.insertFullExecutionFlow(TestExecutionFlowDAO.buildNewFullFlow());
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
        IExecutionFlowDAO tFlowDAO = ExecutionFlowDaoFactory.getExecutionFlowDao(getSession());
        tFlowDAO.insertFullExecutionFlow(TestExecutionFlowDAO.buildNewFullFlow());
        ExecutionFlowPO tExecPo = TestExecutionFlowDAO.buildNewFullFlow();
        tExecPo.setThreadName("TEST-13main");
        tFlowDAO.insertFullExecutionFlow(tExecPo);

        JMonitoringProcess tProcess = ProcessFactory.getInstance();
        getSession().flush();

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
        getSession().flush();
        tCriterion.setThreadName("TEST");
        assertEquals(3, tProcess.getListOfExecutionFlowDto(tCriterion).size());
    }

    public void testGetListOfExecutionFlowWithDurationMin()
    {
        IExecutionFlowDAO tFlowDAO = ExecutionFlowDaoFactory.getExecutionFlowDao(getSession());
        tFlowDAO.insertFullExecutionFlow(TestExecutionFlowDAO.buildNewFullFlow());
        getSession().flush();

        FlowSearchCriterion tCriterion = new FlowSearchCriterion();
        tCriterion.setDurationMin(new Long(10));
        JMonitoringProcess tProcess = ProcessFactory.getInstance();

        assertEquals(1, tProcess.getListOfExecutionFlowDto(tCriterion).size());

        tCriterion.setDurationMin(new Long(35));
        assertEquals(0, tProcess.getListOfExecutionFlowDto(tCriterion).size());
    }

    public void testGetListOfExecutionFlowWithBeginDate()
    {
        IExecutionFlowDAO tFlowDAO = ExecutionFlowDaoFactory.getExecutionFlowDao(getSession());
        ExecutionFlowPO tFlow = TestExecutionFlowDAO.buildNewFullFlow();
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
        tCriterion.setBeginDate(new Date(tToday.getTime() - IExecutionFlowDAO.ONE_DAY));
        assertEquals(0, tProcess.getListOfExecutionFlowDto(tCriterion).size());

        // Tomorrow
        tCriterion.setBeginDate(new Date(tToday.getTime() + IExecutionFlowDAO.ONE_DAY));
        assertEquals(0, tProcess.getListOfExecutionFlowDto(tCriterion).size());
    }

    public void testGetListOfExecutionFlowWithGroupName()
    {
        IExecutionFlowDAO tFlowDAO = ExecutionFlowDaoFactory.getExecutionFlowDao(getSession());
        ExecutionFlowPO tFlow = TestExecutionFlowDAO.buildNewFullFlow();
        tFlowDAO.insertFullExecutionFlow(tFlow);
        getSession().flush();

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
        IExecutionFlowDAO tFlowDAO = ExecutionFlowDaoFactory.getExecutionFlowDao(getSession());
        ExecutionFlowPO tFlow = TestExecutionFlowDAO.buildNewFullFlow();
        tFlowDAO.insertFullExecutionFlow(tFlow);
        getSession().flush();

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
        IExecutionFlowDAO tFlowDAO = ExecutionFlowDaoFactory.getExecutionFlowDao(getSession());
        ExecutionFlowPO tFlow = TestExecutionFlowDAO.buildNewFullFlow();
        tFlowDAO.insertFullExecutionFlow(tFlow);
        getSession().flush();

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
        IExecutionFlowDAO tFlowDAO = ExecutionFlowDaoFactory.getExecutionFlowDao(getSession());
        tFlowDAO.insertFullExecutionFlow(TestExecutionFlowDAO.buildNewFullFlow());
        getSession().flush();

        JMonitoringProcess tProcess = ProcessFactory.getInstance();

        List tMethodsDto = tProcess.getListOfMethodCallFromClassAndMethodName(null, null);
        assertEquals(0, tMethodsDto.size());

        tMethodsDto = tProcess.getListOfMethodCallFromClassAndMethodName(TestExecutionFlowDAO.class.getName(), "");
        assertEquals(6, tMethodsDto.size());

        tMethodsDto = tProcess.getListOfMethodCallFromClassAndMethodName(TestExecutionFlowDAO.class.getName()
            .substring(0, 5), "");
        assertEquals(6, tMethodsDto.size());

        tMethodsDto = tProcess.getListOfMethodCallFromClassAndMethodName("3333", "");
        assertEquals(0, tMethodsDto.size());

        tMethodsDto = tProcess.getListOfMethodCallFromClassAndMethodName("", "builNewFullFlow");
        assertEquals(6, tMethodsDto.size());

        tMethodsDto = tProcess.getListOfMethodCallFromClassAndMethodName("", "builNewFullFlow2");
        assertEquals(1, tMethodsDto.size());

        tMethodsDto = tProcess.getListOfMethodCallFromClassAndMethodName("", "builNewFullFlow3");
        assertEquals(4, tMethodsDto.size());

        tMethodsDto = tProcess.getListOfMethodCallFromClassAndMethodName(TestExecutionFlowDAO.class.getName()
            .substring(0, 25), "builNewFullFlow");
        assertEquals(6, tMethodsDto.size());

        tMethodsDto = tProcess.getListOfMethodCallFromClassAndMethodName(TestExecutionFlowDAO.class.getName()
            .substring(0, 25), "builNewFullFlow2");
        assertEquals(1, tMethodsDto.size());

    }

    public void testGetMethodCallFullExtract()
    {
        IExecutionFlowDAO tFlowDAO = ExecutionFlowDaoFactory.getExecutionFlowDao(getSession());
        tFlowDAO.insertFullExecutionFlow(TestExecutionFlowDAO.buildNewFullFlow());
        getSession().flush();

        JMonitoringProcess tProcess = ProcessFactory.getInstance();
        String tClassName = TestExecutionFlowDAO.class.getName();

        List tResult = tProcess.getListOfMethodCallFullExtract(tClassName, "builNewFullFlow", 5L, 5L);
        assertEquals(0, tResult.size());

        tResult = tProcess.getListOfMethodCallFullExtract(tClassName, "builNewFullFlow", 36L, 36L);
        assertEquals(0, tResult.size());

        tResult = tProcess.getListOfMethodCallFullExtract(tClassName, "builNewFullFlow", 15L, 36L);
        assertEquals(1, tResult.size());

        tResult = tProcess.getListOfMethodCallFullExtract("kj", "builNewFullFlow", 15L, 36L);
        assertEquals(0, tResult.size());

        tResult = tProcess.getListOfMethodCallFullExtract(tClassName, "builNewFullFlow2", 15L, 36L);
        assertEquals(0, tResult.size());

    }

    public void testGetFlowAsXml()
    {
        JMonitoringProcess tProcess = ProcessFactory.getInstance();
        IExecutionFlowDAO tFlowDAO = ExecutionFlowDaoFactory.getExecutionFlowDao(getSession());

        // Firstt instert a flow
        ExecutionFlowPO tFlow = TestExecutionFlowDAO.buildNewFullFlow();

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
        IExecutionFlowDAO tFlowDAO = ExecutionFlowDaoFactory.getExecutionFlowDao(getSession());

        // Firstt instert a flow
        ExecutionFlowPO tFlow = TestExecutionFlowDAO.buildNewFullFlow();

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
        IExecutionFlowDAO tFlowDAO = ExecutionFlowDaoFactory.getExecutionFlowDao(getSession());

        // Firstt instert a flow
        ExecutionFlowPO tFlowPo = TestExecutionFlowDAO.buildNewFullFlow();
        int tFlowId = tFlowDAO.insertFullExecutionFlow(tFlowPo);
        getSession().flush();
        ExecutionFlowDTO tFlowDto = DtoHelper.getDeepCopy(tFlowPo);

        // ExecutionFlowDTO tFlowDto = DtoHelper.getDeepCopy(tFlow);
        byte[] tFlowAsBytes = tProcess.getFlowAsXml(tFlowDto);
        ExecutionFlowDTO tNewFlow = tProcess.insertFlowFromXml(tFlowAsBytes);
        assertTrue("The Id must be different [" + tFlowId + "]", tFlowId != tNewFlow.getId());
    }
}