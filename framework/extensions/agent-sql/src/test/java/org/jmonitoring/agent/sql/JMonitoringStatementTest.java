package org.jmonitoring.agent.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jmonitoring.agent.store.impl.MemoryWriter;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class JMonitoringStatementTest extends SqlTestCase
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

    @Before
    public void initStat() throws SQLException
    {
        Connection tCon = mSession.connection();
        mStat = tCon.createStatement();
        assertEquals(JMonitoringStatement.class, mStat.getClass());
    }

    @After
    public void closeStat() throws SQLException
    {
        mStat.close();
    }

    @Test
    public void testExecuteString() throws SQLException
    {
        mStat.execute("select * from EXECUTION_FLOW");
        assertEquals(1, MemoryWriter.countFlows());
        ExecutionFlowPO tFlow = MemoryWriter.getFlow(0);
        assertEquals("Sql=[select * from EXECUTION_FLOW]\nResult=[true]\n", tFlow.getFirstMethodCall().getReturnValue());
        assertEquals("Bad group name", "Jdbc", tFlow.getFirstMethodCall().getGroupName());
        assertEquals("java.sql.Statement", tFlow.getFirstMethodCall().getClassName());
    }

    @Test
    public void testExecuteStringWithError()
    {
        try
        {
            mStat.execute("select Eejrbekjre");
            fail("Should not be called !");
        } catch (SQLException e)
        {
        }
        assertEquals(1, MemoryWriter.countFlows());
        ExecutionFlowPO tFlow = MemoryWriter.getFlow(0);
        assertEquals("org.h2.jdbc.JdbcSQLSyntaxErrorException", tFlow.getFirstMethodCall().getThrowableClass());
        assertEquals("Column \"EEJRBEKJRE\" not found; SQL statement:\n"
            + "select Eejrbekjre [42122-199]\nSql=[select Eejrbekjre]\n", tFlow.getFirstMethodCall()
                                                                              .getThrowableMessage());
        assertEquals("Bad group name", "Jdbc", tFlow.getFirstMethodCall().getGroupName());
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
    public void testExecuteStringIntWithError()
    {
        try
        {
            mStat.execute("select Eejrbekjre", 0);
            fail("Should not be called !");
        } catch (SQLException e)
        {
        }
        assertEquals(1, MemoryWriter.countFlows());
        ExecutionFlowPO tFlow = MemoryWriter.getFlow(0);
        assertEquals("org.h2.jdbc.JdbcSQLSyntaxErrorException", tFlow.getFirstMethodCall().getThrowableClass());
        assertEquals("Column \"EEJRBEKJRE\" not found; SQL statement:\n"
            + "select Eejrbekjre [42122-199]\nSql=[select Eejrbekjre]\n", tFlow.getFirstMethodCall()
                                                                              .getThrowableMessage());
        assertEquals("Bad group name", "Jdbc", tFlow.getFirstMethodCall().getGroupName());
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
    public void testExecuteStringIntArrayWithError()
    {
        try
        {
            mStat.execute("select Eejrbekjre", new int[0]);
            fail("Should not be called !");
        } catch (SQLException e)
        {
        }
        assertEquals(1, MemoryWriter.countFlows());
        ExecutionFlowPO tFlow = MemoryWriter.getFlow(0);
        assertEquals("org.h2.jdbc.JdbcSQLSyntaxErrorException", tFlow.getFirstMethodCall().getThrowableClass());
        assertEquals("Column \"EEJRBEKJRE\" not found; SQL statement:\n"
            + "select Eejrbekjre [42122-199]\nSql=[select Eejrbekjre]\n", tFlow.getFirstMethodCall()
                                                                              .getThrowableMessage());
        assertEquals("Bad group name", "Jdbc", tFlow.getFirstMethodCall().getGroupName());
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
    public void testExecuteStringStringArrayWithError()
    {
        try
        {
            mStat.execute("select Eejrbekjre", new String[] {"ID" });
            fail("Should not be called !");
        } catch (SQLException e)
        {
        }
        assertEquals(1, MemoryWriter.countFlows());
        ExecutionFlowPO tFlow = MemoryWriter.getFlow(0);
        assertEquals("org.h2.jdbc.JdbcSQLSyntaxErrorException", tFlow.getFirstMethodCall().getThrowableClass());
        assertEquals("Column \"EEJRBEKJRE\" not found; SQL statement:\n"
            + "select Eejrbekjre [42122-199]\nSql=[select Eejrbekjre]\n", tFlow.getFirstMethodCall()
                                                                              .getThrowableMessage());
        assertEquals("Bad group name", "Jdbc", tFlow.getFirstMethodCall().getGroupName());
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
    public void testExecuteBatchWithError()
    {
        try
        {
            mStat.addBatch("select Eejrbekjre");
            mStat.executeBatch();
            fail("Should not be called !");
        } catch (SQLException e)
        {
        }
        assertEquals(1, MemoryWriter.countFlows());
        ExecutionFlowPO tFlow = MemoryWriter.getFlow(0);
        assertEquals("org.h2.jdbc.JdbcBatchUpdateException", tFlow.getFirstMethodCall().getThrowableClass());
        assertEquals("Column \"EEJRBEKJRE\" not found; SQL statement:\n" +
                     "select Eejrbekjre [42122-199]\nAdd Batch=[select Eejrbekjre]\nBatch executed\n", tFlow.getFirstMethodCall()
                                                                             .getThrowableMessage());
        assertEquals("Bad group name", "Jdbc", tFlow.getFirstMethodCall().getGroupName());
        assertEquals("java.sql.Statement", tFlow.getFirstMethodCall().getClassName());
    }

    @Test
    public void testExecuteQuery() throws SQLException
    {
        ResultSet tResultSet = mStat.executeQuery("select * from EXECUTION_FLOW");
        assertEquals("The resultset should be wrapped", JMonitoringResultSet.class, tResultSet.getClass());
        assertEquals(1, MemoryWriter.countFlows());
        ExecutionFlowPO tFlow = MemoryWriter.getFlow(0);
        String tLog = tFlow.getFirstMethodCall().getReturnValue();
        assertTrue(tLog, tLog.startsWith("Query executed=[select * from EXECUTION_FLOW]\nResultSet=[rs"));
        assertTrue(tLog, tLog.endsWith(" columns: 10 rows: 0 pos: -1]\n"));
        assertEquals("java.sql.Statement", tFlow.getFirstMethodCall().getClassName());
    }

    @Test
    public void testExecuteQueryWithError()
    {
        try
        {
            mStat.executeQuery("select Eejrbekjre");
            fail("Should not be called !");
        } catch (SQLException e)
        {
        }
        assertEquals(1, MemoryWriter.countFlows());
        ExecutionFlowPO tFlow = MemoryWriter.getFlow(0);
        assertEquals("org.h2.jdbc.JdbcSQLSyntaxErrorException", tFlow.getFirstMethodCall().getThrowableClass());
        assertEquals("Column \"EEJRBEKJRE\" not found; SQL statement:\n"
            + "select Eejrbekjre [42122-199]\nQuery executed=[select Eejrbekjre]\n", tFlow.getFirstMethodCall()
                                                                                         .getThrowableMessage());
        assertEquals("Bad group name", "Jdbc", tFlow.getFirstMethodCall().getGroupName());
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
    public void testExecuteUpdateWithError()
    {
        try
        {
            mStat.executeUpdate("update Eejrbekjre");
            fail("Should not be called !");
        } catch (SQLException e)
        {
        }
        assertEquals(1, MemoryWriter.countFlows());
        ExecutionFlowPO tFlow = MemoryWriter.getFlow(0);
        assertEquals("org.h2.jdbc.JdbcSQLSyntaxErrorException", tFlow.getFirstMethodCall().getThrowableClass());
        assertEquals("Table \"EEJRBEKJRE\" not found; SQL statement:\n"
            + "update Eejrbekjre [42102-199]\nExecute Update=[update Eejrbekjre]\n", tFlow.getFirstMethodCall()
                                                                                         .getThrowableMessage());
        assertEquals("Bad group name", "Jdbc", tFlow.getFirstMethodCall().getGroupName());
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
    public void testExecuteUpdateStringIntWithError()
    {
        try
        {
            mStat.executeUpdate("update Eejrbekjre", 0);
            fail("Should not be called !");
        } catch (SQLException e)
        {
        }
        assertEquals(1, MemoryWriter.countFlows());
        ExecutionFlowPO tFlow = MemoryWriter.getFlow(0);
        assertEquals("org.h2.jdbc.JdbcSQLSyntaxErrorException", tFlow.getFirstMethodCall().getThrowableClass());
        assertEquals("Table \"EEJRBEKJRE\" not found; SQL statement:\n"
            + "update Eejrbekjre [42102-199]\nExecute Update=[update Eejrbekjre]\n", tFlow.getFirstMethodCall()
                                                                                         .getThrowableMessage());
        assertEquals("Bad group name", "Jdbc", tFlow.getFirstMethodCall().getGroupName());
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
    public void testExecuteUpdateStringIntArrayWithError()
    {
        try
        {
            mStat.executeUpdate("update Eejrbekjre", new int[0]);
            fail("Should not be called !");
        } catch (SQLException e)
        {
        }
        assertEquals(1, MemoryWriter.countFlows());
        ExecutionFlowPO tFlow = MemoryWriter.getFlow(0);
        assertEquals("org.h2.jdbc.JdbcSQLSyntaxErrorException", tFlow.getFirstMethodCall().getThrowableClass());
        assertEquals("Table \"EEJRBEKJRE\" not found; SQL statement:\n"
            + "update Eejrbekjre [42102-199]\nExecute Update=[update Eejrbekjre]\n", tFlow.getFirstMethodCall()
                                                                                         .getThrowableMessage());
        assertEquals("Bad group name", "Jdbc", tFlow.getFirstMethodCall().getGroupName());
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

    @Test
    public void testExecuteUpdateStringStringArrayWithError()
    {
        try
        {
            mStat.executeUpdate("update Eejrbekjre", new String[] {"ID" });
            fail("Should not be called !");
        } catch (SQLException e)
        {
        }
        assertEquals(1, MemoryWriter.countFlows());
        ExecutionFlowPO tFlow = MemoryWriter.getFlow(0);
        assertEquals("org.h2.jdbc.JdbcSQLSyntaxErrorException", tFlow.getFirstMethodCall().getThrowableClass());
        assertEquals("Table \"EEJRBEKJRE\" not found; SQL statement:\n"
            + "update Eejrbekjre [42102-199]\nExecute Update=[update Eejrbekjre]\n", tFlow.getFirstMethodCall()
                                                                                         .getThrowableMessage());
        assertEquals("Bad group name", "Jdbc", tFlow.getFirstMethodCall().getGroupName());
        assertEquals("java.sql.Statement", tFlow.getFirstMethodCall().getClassName());
    }

    @Test
    public void testGetGeneratedKeys() throws SQLException
    {
        ResultSet tResultSet = mStat.getGeneratedKeys();
        assertEquals("The resultset should be wrapped", JMonitoringResultSet.class, tResultSet.getClass());
    }

    @Test
    public void testGetResultSet() throws SQLException
    {
        assertNull("Should not have a resultset without query", mStat.getResultSet());
        ResultSet tResultSet = mStat.executeQuery("select * from EXECUTION_FLOW");
        assertNotSame("Should be the same as previous", tResultSet, mStat.getResultSet());

        mStat.execute("select * from EXECUTION_FLOW");
        ResultSet tResultSet2 = mStat.getResultSet();
        assertNotSame("Request are different, resulset are different", tResultSet, tResultSet2);
        assertEquals("The resultset should be wrapped", JMonitoringResultSet.class, tResultSet2.getClass());
    }

    @Test
    public void testExecuteUpdate()
    {
    }

}
