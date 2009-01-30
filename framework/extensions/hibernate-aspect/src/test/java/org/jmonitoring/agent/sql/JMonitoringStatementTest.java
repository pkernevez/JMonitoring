package org.jmonitoring.agent.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jmonitoring.agent.store.impl.MemoryWriter;
import org.jmonitoring.core.configuration.SpringConfigurationUtil;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.tests.JMonitoringTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

@ContextConfiguration(locations = {"/memory-test.xml" })
public class JMonitoringStatementTest extends JMonitoringTestCase
{
    static String UPDATE_1 =
        "INSERT INTO execution_flow (ID, THREAD_NAME, DURATION, BEGIN_TIME_AS_DATE, "
            + "END_TIME, JVM, BEGIN_TIME) VALUES (";

    static String UPDATE_2 =
        ",'ExecuteThread: ''23'' for queue: ''Default''',151,"
            + "'2005-04-22 15:03:58',1114175038609,'Kerny''s Babasse',1114175038458);";

    private static int sCurId = 0;

    private ClassPathXmlApplicationContext mAContext;

    private Statement mStat;

    private Session mSession;

    @Before
    public void initContext() throws SQLException
    {
        SpringConfigurationUtil.setContext(getApplicationContext());
        MemoryWriter.clear();

        ClassPathXmlApplicationContext tAContext =
            new ClassPathXmlApplicationContext(new String[] {"/jmonitoring-agent-test.xml", "/memory-test.xml" });
        SpringConfigurationUtil.setContext(tAContext);
        SessionFactory tFacto = (SessionFactory) tAContext.getBean("sessionFactory");
        MemoryWriter.clear();
        mSession = tFacto.openSession();
        mSession.beginTransaction();
        Connection tCon = mSession.connection();
        mStat = tCon.createStatement();
        assertEquals(JMonitoringStatement.class, mStat.getClass());
    }

    @After
    public void clear()
    {
        mSession.getTransaction().rollback();
        mSession.beginTransaction();
    }

    @Test
    public void testExecuteString() throws SQLException
    {
        mStat.execute("select * from EXECUTION_FLOW");
        assertEquals(1, MemoryWriter.countFlows());
        ExecutionFlowPO tFlow = MemoryWriter.getFlow(0);
        assertEquals("Sql=[select * from EXECUTION_FLOW]\nResult=[true]\n", tFlow.getFirstMethodCall().getReturnValue());
        assertEquals("java.sql.Statement", tFlow.getFirstMethodCall().getClassName());
    }

    @Test
    public void testExecuteStringInt() throws SQLException
    {
        mStat.execute("select * from EXECUTION_FLOW", 0);
        assertEquals(1, MemoryWriter.countFlows());
        ExecutionFlowPO tFlow = MemoryWriter.getFlow(0);
        assertEquals("Sql=[select * from EXECUTION_FLOW]\nResult=[true]\n", tFlow.getFirstMethodCall().getReturnValue());
        assertEquals("java.sql.Statement", tFlow.getFirstMethodCall().getClassName());
    }

    @Test
    public void testExecuteStringIntArray() throws SQLException
    {
        mStat.execute("select * from EXECUTION_FLOW", new int[0]);
        assertEquals(1, MemoryWriter.countFlows());
        ExecutionFlowPO tFlow = MemoryWriter.getFlow(0);
        assertEquals("Sql=[select * from EXECUTION_FLOW]\nResult=[true]\n", tFlow.getFirstMethodCall().getReturnValue());
        assertEquals("java.sql.Statement", tFlow.getFirstMethodCall().getClassName());
    }

    @Test
    public void testExecuteStringStringArray() throws SQLException
    {
        mStat.execute("select * from EXECUTION_FLOW", new String[] {"ID" });
        assertEquals(1, MemoryWriter.countFlows());
        ExecutionFlowPO tFlow = MemoryWriter.getFlow(0);
        assertEquals("Sql=[select * from EXECUTION_FLOW]\nResult=[true]\n", tFlow.getFirstMethodCall().getReturnValue());
        assertEquals("java.sql.Statement", tFlow.getFirstMethodCall().getClassName());
    }

    @Test
    public void testExecuteBatch() throws SQLException
    {
        mStat.executeBatch();
        assertEquals(1, MemoryWriter.countFlows());
        ExecutionFlowPO tFlow = MemoryWriter.getFlow(0);
        assertEquals("Batch executed\nReturn array size=[0]\n", tFlow.getFirstMethodCall().getReturnValue());
        assertEquals("java.sql.Statement", tFlow.getFirstMethodCall().getClassName());
    }

    @Test
    public void testExecuteQuery() throws SQLException
    {
        mStat.executeQuery("select * from EXECUTION_FLOW");
        assertEquals(1, MemoryWriter.countFlows());
        ExecutionFlowPO tFlow = MemoryWriter.getFlow(0);
        String tLog = tFlow.getFirstMethodCall().getReturnValue();
        assertTrue(tLog, tLog.startsWith("Query executed=[select * from EXECUTION_FLOW]\nResultSet=[rs"));
        assertTrue(tLog, tLog.endsWith(": columns: 8 rows: 0 pos: -1]\n"));
        assertEquals("java.sql.Statement", tFlow.getFirstMethodCall().getClassName());
    }

    @Test
    public void testExecuteUpdateString() throws SQLException
    {
        String tSql = UPDATE_1 + sCurId++ + UPDATE_2;
        mStat.executeUpdate(tSql);
        assertEquals(1, MemoryWriter.countFlows());
        ExecutionFlowPO tFlow = MemoryWriter.getFlow(0);
        assertEquals("Execute Update=[" + tSql + "]\nResult=[1]\n", tFlow.getFirstMethodCall().getReturnValue());
        assertEquals("java.sql.Statement", tFlow.getFirstMethodCall().getClassName());
    }

    @Test
    public void testExecuteUpdateStringInt() throws SQLException
    {
        String tSql = UPDATE_1 + sCurId++ + UPDATE_2;
        mStat.executeUpdate(tSql, 0);
        assertEquals(1, MemoryWriter.countFlows());
        ExecutionFlowPO tFlow = MemoryWriter.getFlow(0);
        assertEquals("Execute Update=[" + tSql + "]\nResult=[1]\n", tFlow.getFirstMethodCall().getReturnValue());
        assertEquals("java.sql.Statement", tFlow.getFirstMethodCall().getClassName());
    }

    @Test
    public void testExecuteUpdateStringIntArray() throws SQLException
    {
        String tSql = UPDATE_1 + sCurId++ + UPDATE_2;
        mStat.executeUpdate(tSql, new int[0]);
        assertEquals(1, MemoryWriter.countFlows());
        ExecutionFlowPO tFlow = MemoryWriter.getFlow(0);
        assertEquals("Execute Update=[" + tSql + "]\nResult=[1]\n", tFlow.getFirstMethodCall().getReturnValue());
        assertEquals("java.sql.Statement", tFlow.getFirstMethodCall().getClassName());
    }

    @Test
    public void testExecuteUpdateStringStringArray() throws SQLException
    {
        String tSql = UPDATE_1 + sCurId++ + UPDATE_2;
        mStat.executeUpdate(tSql, new String[] {"ID" });
        assertEquals(1, MemoryWriter.countFlows());
        ExecutionFlowPO tFlow = MemoryWriter.getFlow(0);
        assertEquals("Execute Update=[" + tSql + "]\nResult=[1]\n", tFlow.getFirstMethodCall().getReturnValue());
        assertEquals("java.sql.Statement", tFlow.getFirstMethodCall().getClassName());
    }

}
