package org.jmonitoring.core.process;

import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.jmonitoring.core.common.UnknownFlowException;
import org.jmonitoring.core.configuration.FormaterBean;
import org.jmonitoring.core.dao.ConsoleDao;
import org.jmonitoring.core.dao.FlowSearchCriterion;
import org.jmonitoring.core.dao.ConsoleDaoTest;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.jmonitoring.core.dto.DtoManager;
import org.jmonitoring.core.dto.ExecutionFlowDTO;
import org.jmonitoring.core.dto.MethodCallDTO;
import org.jmonitoring.core.dto.MethodCallFullExtractDTO;
import org.jmonitoring.test.dao.PersistanceTestCase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.NotTransactional;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

@ContextConfiguration(locations = {"/console.xml" })
//@TestExecutionListeners( {TransactionalTestExecutionListener.class })
//@TransactionConfiguration(transactionManager="transactionManager")
//@Transactional
public class TestConsoleManager extends PersistanceTestCase
{
    @Autowired
    private DtoManager dtoManager;

    @Resource(name = "dao")
    private ConsoleDao mDao;

    @Autowired
    private ConsoleManager mManager;

    @Resource(name = "hibernateConfiguration")
    private Configuration mConfiguation;

    @Autowired
    private FormaterBean mFormater;

    @Before
    public void cleanDb()
    {
        mDao.deleteAllFlows();
    }

    @Test
    public void testDoDatabaseExist() throws SQLException
    {
        assertTrue(mManager.doDatabaseExist());

        Connection tCon = getSession().connection();
        SchemaExport tDdlexport = new SchemaExport(mConfiguation, tCon);
        tDdlexport.drop(true, true);
        tCon.close();
        getSession().flush();

        assertFalse("The database should not exist", mManager.doDatabaseExist());

        mManager.createDataBase();

        commitAndRestartSession();
        assertTrue(mManager.doDatabaseExist());

    }

    @Test
    public void testDeleteFlow() throws UnknownFlowException
    {
        int tNbFlow = mDao.countFlows();

        // First insert a flow
        ExecutionFlowPO tFlow = ConsoleDaoTest.buildNewFullFlow();
        int tFlowId = mDao.insertFullExecutionFlow(tFlow);
        getSession().flush();
        int tNewNbFlow = mDao.countFlows();
        assertEquals(tNbFlow + 1, tNewNbFlow);

        mManager.deleteFlow(tFlowId);

        commitAndRestartSession();
        tNewNbFlow = mDao.countFlows();
        assertEquals(tNbFlow, tNewNbFlow);

        try
        {
            mManager.deleteFlow(3456);
        } catch (UnknownFlowException e)
        {
            assertEquals("Flow with Id=3456 could not be retreive from database, and can't be delete", e.getMessage());
        }
    }

    @Test
    public void testDeleteAllFlows()
    {
        int tNbFlow = mDao.countFlows();

        mDao.insertFullExecutionFlow(ConsoleDaoTest.buildNewFullFlow());
        mDao.insertFullExecutionFlow(ConsoleDaoTest.buildNewFullFlow());
        getSession().flush();

        int tNewNbFlow = mDao.countFlows();
        assertEquals(tNbFlow + 2, tNewNbFlow);

        mManager.deleteAllFlows();
        commitAndRestartSession();

        tNewNbFlow = mDao.countFlows();
        assertEquals(tNbFlow, tNewNbFlow);
    }

    @Test
    public void testReadFullExecutionFlow()
    {
        ExecutionFlowPO tFlowPO = ConsoleDaoTest.buildNewFullFlow();
        int tId = mDao.insertFullExecutionFlow(tFlowPO);
        getSession().flush();

        int tNewNbFlow = mDao.countFlows();
        assertEquals(1, tNewNbFlow);

        getSession().flush();
        getSession().clear();

        ExecutionFlowDTO tReadFlow = mManager.readFullExecutionFlow(tId);

        // Check the equality of the Flow
        assertEquals(tFlowPO.getJvmIdentifier(), tReadFlow.getJvmIdentifier());
        assertEquals(tFlowPO.getThreadName(), tReadFlow.getThreadName());
        assertEquals(mFormater.formatDateTime(tFlowPO.getBeginTime()), tReadFlow.getBeginTime());
        assertEquals(tFlowPO.getDuration(), tReadFlow.getDuration());
        assertEquals(mFormater.formatDateTime(tFlowPO.getEndTime()), tReadFlow.getEndTime());

        // Check equality of the first measure point
        MethodCallPO tInitialPoint = tFlowPO.getFirstMethodCall();
        MethodCallDTO tReadPoint = tReadFlow.getFirstMethodCall();
        assertEquals(tInitialPoint.getClassName(), tReadPoint.getClassName());
        assertEquals(tInitialPoint.getMethodName(), tReadPoint.getMethodName());
        assertEquals("[]", tReadPoint.getParams());
        assertEquals(tInitialPoint.getReturnValue(), tReadPoint.getReturnValue());
        assertEquals(tInitialPoint.getThrowableClass(), tReadPoint.getThrowableClass());
        assertEquals(tInitialPoint.getThrowableMessage(), tReadPoint.getThrowableMessage());
        assertEquals(mFormater.formatDateTime(tInitialPoint.getBeginTime()), tReadPoint.getBeginTimeString());
        assertEquals(tInitialPoint.getBeginTime(), tReadPoint.getBeginMilliSeconds());
        assertEquals(mFormater.formatDateTime(tInitialPoint.getEndTime()), tReadPoint.getEndTimeString());
        assertEquals(tInitialPoint.getEndTime(), tReadPoint.getEndMilliSeconds());
        assertEquals(tInitialPoint.getGroupName(), tReadPoint.getGroupName());

        // Check equality of the first child measure point
        assertEquals(2, tInitialPoint.getChildren().size());
        assertEquals(2, tReadPoint.getChildren().length);
        tInitialPoint = tInitialPoint.getChildren().get(0);
        tReadPoint = tReadPoint.getChild(0);
        assertEquals(tInitialPoint.getClassName(), tReadPoint.getClassName());
        assertEquals(tInitialPoint.getMethodName(), tReadPoint.getMethodName());
        assertEquals("[]", tReadPoint.getParams());
        assertEquals(tInitialPoint.getReturnValue(), tReadPoint.getReturnValue());
        assertEquals(tInitialPoint.getThrowableClass(), tReadPoint.getThrowableClass());
        assertEquals(tInitialPoint.getThrowableMessage(), tReadPoint.getThrowableMessage());
        assertEquals(mFormater.formatDateTime(tInitialPoint.getBeginTime()), tReadPoint.getBeginTimeString());
        assertEquals(mFormater.formatDateTime(tInitialPoint.getEndTime()), tReadPoint.getEndTimeString());
        assertEquals(tInitialPoint.getGroupName(), tReadPoint.getGroupName());

        // Check equality of the second child measure point
        tInitialPoint = tInitialPoint.getParentMethodCall().getChildren().get(1);
        tReadPoint = tReadPoint.getParent().getChild(1);
        assertEquals(tInitialPoint.getClassName(), tReadPoint.getClassName());
        assertEquals(tInitialPoint.getMethodName(), tReadPoint.getMethodName());
        assertEquals("[]", tReadPoint.getParams());
        assertEquals(tInitialPoint.getReturnValue(), tReadPoint.getReturnValue());
        assertEquals(tInitialPoint.getThrowableClass(), tReadPoint.getThrowableClass());
        assertEquals(tInitialPoint.getThrowableMessage(), tReadPoint.getThrowableMessage());
        assertEquals(mFormater.formatDateTime(tInitialPoint.getBeginTime()), tReadPoint.getBeginTimeString());
        assertEquals(mFormater.formatDateTime(tInitialPoint.getEndTime()), tReadPoint.getEndTimeString());
        assertEquals(tInitialPoint.getGroupName(), tReadPoint.getGroupName());

    }

    @Test
    public void testGetListOfExecutionFlowWithoutCriteria()
    {
        int tExpectedResult = mDao.countFlows();
        mDao.insertFullExecutionFlow(ConsoleDaoTest.buildNewFullFlow());
        mDao.insertFullExecutionFlow(ConsoleDaoTest.buildNewFullFlow());
        mDao.insertFullExecutionFlow(ConsoleDaoTest.buildNewFullFlow());
        getSession().flush();

        List<ExecutionFlowDTO> tFlows = mManager.getListOfExecutionFlowDto(new FlowSearchCriterion());
        assertEquals(tExpectedResult + 3, tFlows.size());

    }

    /**
     * @todo Add test with criteria.
     * @throws SQLException
     */
    @Test
    public void testGetListOfExecutionFlowWithThreadName()
    {
        mDao.insertFullExecutionFlow(ConsoleDaoTest.buildNewFullFlow());
        ExecutionFlowPO tExecPo = ConsoleDaoTest.buildNewFullFlow();
        tExecPo.setThreadName("TEST-13main");
        mDao.insertFullExecutionFlow(tExecPo);

        getSession().flush();

        FlowSearchCriterion tCriterion = new FlowSearchCriterion();
        tCriterion.setThreadName("rr");
        assertEquals(0, mManager.getListOfExecutionFlowDto(tCriterion).size());

        tCriterion.setThreadName("");
        commitAndRestartSession();
        assertEquals(2, mManager.getListOfExecutionFlowDto(tCriterion).size());

        tCriterion.setThreadName("TEST-main");
        commitAndRestartSession();
        assertEquals(1, mManager.getListOfExecutionFlowDto(tCriterion).size());

        tCriterion.setThreadName("TEST");
        commitAndRestartSession();
        assertEquals(2, mManager.getListOfExecutionFlowDto(tCriterion).size());

        tExecPo = ConsoleDaoTest.buildNewFullFlow();
        tExecPo.setThreadName("TEST-13main");

        commitAndRestartSession();
        mDao.insertFullExecutionFlow(tExecPo);
        getSession().flush();
        tCriterion.setThreadName("TEST");
        assertEquals(3, mManager.getListOfExecutionFlowDto(tCriterion).size());

        mDao.deleteAllFlows();
        commitAndRestartSession();

    }

    @Test
    public void testGetListOfExecutionFlowWithDurationMin()
    {
        mDao.insertFullExecutionFlow(ConsoleDaoTest.buildNewFullFlow());
        getSession().flush();

        FlowSearchCriterion tCriterion = new FlowSearchCriterion();
        tCriterion.setDurationMin(new Long(10));

        assertEquals(1, mManager.getListOfExecutionFlowDto(tCriterion).size());

        tCriterion.setDurationMin(new Long(35));
        commitAndRestartSession();
        assertEquals(0, mManager.getListOfExecutionFlowDto(tCriterion).size());
    }

    @Test
    public void testGetListOfExecutionFlowWithBeginDate()
    {
        ExecutionFlowPO tFlow = ConsoleDaoTest.buildNewFullFlow();
        mDao.insertFullExecutionFlow(tFlow);
        getSession().flush();

        Date tToday = new Date(tFlow.getBeginTime());
        tToday.setSeconds(0);
        tToday.setMinutes(0);
        tToday.setHours(0);

        FlowSearchCriterion tCriterion = new FlowSearchCriterion();

        // Today
        tCriterion.setBeginDate(tToday);
        assertEquals(1, mManager.getListOfExecutionFlowDto(tCriterion).size());

        // Yesterday
        tCriterion.setBeginDate(new Date(tToday.getTime() - ConsoleDao.ONE_DAY));
        commitAndRestartSession();
        assertEquals(0, mManager.getListOfExecutionFlowDto(tCriterion).size());

        // Tomorrow
        tCriterion.setBeginDate(new Date(tToday.getTime() + ConsoleDao.ONE_DAY));
        commitAndRestartSession();
        assertEquals(0, mManager.getListOfExecutionFlowDto(tCriterion).size());
    }

    @Test
    public void testGetListOfExecutionFlowWithGroupName()
    {
        ExecutionFlowPO tFlow = ConsoleDaoTest.buildNewFullFlow();
        mDao.insertFullExecutionFlow(tFlow);
        getSession().flush();

        FlowSearchCriterion tCriterion = new FlowSearchCriterion();

        tCriterion.setGroupName("GrDefault");
        assertEquals(1, mManager.getListOfExecutionFlowDto(tCriterion).size());

        tCriterion.setGroupName("GrDefa");
        commitAndRestartSession();
        assertEquals(1, mManager.getListOfExecutionFlowDto(tCriterion).size());

        tCriterion.setGroupName("toto");
        commitAndRestartSession();
        assertEquals(0, mManager.getListOfExecutionFlowDto(tCriterion).size());
        // Todo : groupName, className et methodName
    }

    @Test
    public void testGetListOfExecutionFlowWithClassName()
    {
        ExecutionFlowPO tFlow = ConsoleDaoTest.buildNewFullFlow();
        mDao.insertFullExecutionFlow(tFlow);
        getSession().flush();

        FlowSearchCriterion tCriterion = new FlowSearchCriterion();

        String tClassName = PersistanceTestCase.class.getName();
        tCriterion.setClassName(tClassName);
        assertEquals(1, mManager.getListOfExecutionFlowDto(tCriterion).size());

        tCriterion.setClassName(tClassName.substring(0, 3));
        commitAndRestartSession();
        assertEquals(1, mManager.getListOfExecutionFlowDto(tCriterion).size());

        tCriterion.setClassName("toto");
        commitAndRestartSession();
        assertEquals(0, mManager.getListOfExecutionFlowDto(tCriterion).size());
        // Todo : groupName, className et methodName
    }

    @Test
    public void testGetListOfExecutionFlowWithMethodName()
    {
        ExecutionFlowPO tFlow = ConsoleDaoTest.buildNewFullFlow();
        mDao.insertFullExecutionFlow(tFlow);
        getSession().flush();

        FlowSearchCriterion tCriterion = new FlowSearchCriterion();

        tCriterion.setMethodName("builNewFullFlow");
        assertEquals(1, mManager.getListOfExecutionFlowDto(tCriterion).size());

        tCriterion.setMethodName("builNewFull");
        commitAndRestartSession();
        assertEquals(1, mManager.getListOfExecutionFlowDto(tCriterion).size());

        tCriterion.setMethodName("toto");
        commitAndRestartSession();
        assertEquals(0, mManager.getListOfExecutionFlowDto(tCriterion).size());
    }

    @Test
    public void testGetListOfMethodCallFromClassAndMethodName()
    {
        mDao.insertFullExecutionFlow(ConsoleDaoTest.buildNewFullFlow());
        getSession().flush();

        commitAndRestartSession();
        List<MethodCallDTO> tMethodsDto = mManager.getListOfMethodCallFromClassAndMethodName(null, null);
        assertEquals(0, tMethodsDto.size());

        commitAndRestartSession();
        tMethodsDto = mManager.getListOfMethodCallFromClassAndMethodName(PersistanceTestCase.class.getName(), "");
        assertEquals(6, tMethodsDto.size());

        commitAndRestartSession();
        tMethodsDto =
            mManager.getListOfMethodCallFromClassAndMethodName(PersistanceTestCase.class.getName().substring(0, 5), "");
        assertEquals(6, tMethodsDto.size());

        commitAndRestartSession();
        tMethodsDto = mManager.getListOfMethodCallFromClassAndMethodName("3333", "");
        assertEquals(0, tMethodsDto.size());

        commitAndRestartSession();
        tMethodsDto = mManager.getListOfMethodCallFromClassAndMethodName("", "builNewFullFlow");
        assertEquals(6, tMethodsDto.size());

        commitAndRestartSession();
        tMethodsDto = mManager.getListOfMethodCallFromClassAndMethodName("", "builNewFullFlow2");
        assertEquals(1, tMethodsDto.size());

        commitAndRestartSession();
        tMethodsDto = mManager.getListOfMethodCallFromClassAndMethodName("", "builNewFullFlow3");
        assertEquals(4, tMethodsDto.size());

        commitAndRestartSession();
        tMethodsDto =
            mManager.getListOfMethodCallFromClassAndMethodName(PersistanceTestCase.class.getName().substring(0, 25),
                                                               "builNewFullFlow");
        assertEquals(6, tMethodsDto.size());

        commitAndRestartSession();
        tMethodsDto =
            mManager.getListOfMethodCallFromClassAndMethodName(PersistanceTestCase.class.getName().substring(0, 25),
                                                               "builNewFullFlow2");
        assertEquals(1, tMethodsDto.size());

    }

    @Test
    public void testGetMethodCallFullExtract()
    {
        mDao.insertFullExecutionFlow(ConsoleDaoTest.buildNewFullFlow());
        getSession().flush();

        String tClassName = PersistanceTestCase.class.getName();

        List<MethodCallFullExtractDTO> tResult =
            mManager.getListOfMethodCallFullExtract(tClassName, "builNewFullFlow", 5L, 5L);
        assertEquals(0, tResult.size());

        commitAndRestartSession();
        tResult = mManager.getListOfMethodCallFullExtract(tClassName, "builNewFullFlow", 36L, 36L);
        assertEquals(0, tResult.size());

        commitAndRestartSession();
        tResult = mManager.getListOfMethodCallFullExtract(tClassName, "builNewFullFlow", 15L, 36L);
        assertEquals(1, tResult.size());

        commitAndRestartSession();
        tResult = mManager.getListOfMethodCallFullExtract("kj", "builNewFullFlow", 15L, 36L);
        assertEquals(0, tResult.size());

        commitAndRestartSession();
        tResult = mManager.getListOfMethodCallFullExtract(tClassName, "builNewFullFlow2", 15L, 36L);
        assertEquals(0, tResult.size());
    }

    @Test
    public void testConvertFlowToXml()
    {
        // First insert a flow
        ExecutionFlowPO tFlow = ConsoleDaoTest.buildNewFullFlow();

        int tFlowId = mDao.insertFullExecutionFlow(tFlow);
        getSession().flush();
        assertTrue(tFlowId > 0);

        ExecutionFlowDTO tFlowDto = dtoManager.getDeepCopy(tFlow);
        byte[] tFlowAsXml = mManager.convertFlowToXml(tFlowDto);
        assertTrue("The byte[] is to small...[" + tFlowAsXml.length + "]", tFlowAsXml.length > 100);
    }

    @Test
    public void testConvertFlowToXmlThrowableClass()
    {
        // First insert a flow
        ExecutionFlowPO tFlow = ConsoleDaoTest.buildNewFullFlow();

        int tFlowId = mDao.insertFullExecutionFlow(tFlow);
        getSession().flush();
        assertTrue(tFlowId > 0);

        ExecutionFlowDTO tFlowDto = dtoManager.getDeepCopy(tFlow);
        ByteArrayOutputStream tStream = new ByteArrayOutputStream();
        XMLEncoder tEncoder = new XMLEncoder(tStream);
        tEncoder.writeObject(tFlowDto);
        tEncoder.close();
        String tOutput = tStream.toString();
        assertTrue(tOutput.contains("throwableMessage"));
        assertTrue(tOutput.contains("throwableClass"));
        byte[] tFlowAsXml = mManager.convertFlowToXml(tFlowDto);
        assertTrue("The byte[] is to small...[" + tFlowAsXml.length + "]", tFlowAsXml.length > 100);
    }

    @Test
    public void testSerialisationConversion() throws IOException {
        // First insert a flow
        ExecutionFlowPO tFlow = ConsoleDaoTest.buildNewFullFlow();

        int tFlowId = mDao.insertFullExecutionFlow(tFlow);
        assertTrue(tFlowId > 0);
        getSession().flush();

        ExecutionFlowDTO tFlowDto = dtoManager.getDeepCopy(tFlow);
        byte[] tFlowAsXml =  mManager.ungzip(mManager.convertFlowToXml(tFlowDto));

        ExecutionFlowDTO tNewFlow = dtoManager.getDeepCopy(mManager.convertFlowFromXml(tFlowAsXml));
        assertNotSame(tFlowDto, tNewFlow);
        assertEquals(tFlowDto.getDuration(), tNewFlow.getDuration());
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
        assertEquals(tMeth.getBeginTimeString(), tNewMeth.getBeginTimeString());
        assertEquals(tMeth.getClassName(), tNewMeth.getClassName());
        assertEquals(tMeth.getEndTimeString(), tNewMeth.getEndTimeString());
        assertEquals(tMeth.getFlowId(), tNewMeth.getFlowId());
        assertEquals(tMeth.getGroupName(), tNewMeth.getGroupName());
        assertEquals(tMeth.getMethodName(), tNewMeth.getMethodName());
        assertEquals(tMeth.getParams(), tNewMeth.getParams());
        assertEquals(tMeth.getParent(), tNewMeth.getParent());
        assertEquals(tMeth.getPosition(), tNewMeth.getPosition());
        assertEquals(tMeth.getReturnValue(), tNewMeth.getReturnValue());
        assertEquals(tMeth.getThrowableClass(), tNewMeth.getThrowableClass());
        assertEquals(tMeth.getThrowableMessage(), tNewMeth.getThrowableMessage());
    }

    @Test
    public void testInsertFlowFromXml()
    {

        // First insert a flow
        ExecutionFlowPO tFlowPo = ConsoleDaoTest.buildNewFullFlow();
        int tFlowId = mDao.insertFullExecutionFlow(tFlowPo);
        getSession().flush();
        ExecutionFlowDTO tFlowDto = dtoManager.getDeepCopy(tFlowPo);

        // ExecutionFlowDTO tFlowDto = DtoHelper.getDeepCopy(tFlow);
        byte[] tFlowAsBytes = mManager.convertFlowToXml(tFlowDto);
        ExecutionFlowDTO tNewFlow = mManager.insertFlowFromXml(tFlowAsBytes);
        assertTrue("The Id must be different [" + tFlowId + "]", tFlowId != tNewFlow.getId());
    }

    private byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    @Test
    public void testUngzip() throws IOException {
        String totoGZipHexa = "1f8b08002c41cf5c00032bc92fc9e70200bd087df805000000";
        byte[] totoGZipByte = hexStringToByteArray(totoGZipHexa);
        byte[] totoByte =  mManager.ungzip(totoGZipByte);
        assertEquals("toto\n", new String(totoByte, "UTF-8"));
    }

}
