package org.jmonitoring.agent.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jmonitoring.agent.store.impl.MemoryWriter;
import org.jmonitoring.core.domain.MethodCallPO;
import org.junit.Before;
import org.junit.Test;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class JMonitoringResultSetTest extends SqlTestCase
{
    ResultSet mResultSet;

    private static int sCurId;

    @Before
    public void initResultSet() throws SQLException
    {
        Connection tCon = mSession.connection();
        Statement tStat = tCon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        String tSql = JMonitoringStatementTest.UPDATE_1 + sCurId++ + JMonitoringStatementTest.UPDATE_2;
        tStat.executeUpdate(tSql);

        mResultSet = new JMonitoringResultSet(tStat.executeQuery("select * from EXECUTION_FLOW"), "Jdbc", null);

        MemoryWriter.clear();
    }

    @Test
    public void testClose() throws SQLException
    {
        assertEquals("No flow should be log at this time", 0, MemoryWriter.countFlows());
        mResultSet.close();
        assertEquals("One flow should be log at this time", 1, MemoryWriter.countFlows());
        MethodCallPO tCall = MemoryWriter.getFlow(0).getFirstMethodCall();
        assertEquals("GroupName test", "JdbcCloseRS", tCall.getGroupName());
        StringBuilder tExpectedTrace = new StringBuilder();
        tExpectedTrace.append("Statistics of resultSet :\n");
        tExpectedTrace.append("Inserted=[0]\n");
        tExpectedTrace.append("Updated=[0]\n");
        tExpectedTrace.append("Delete=[0]\n");
        tExpectedTrace.append("Previous=[0]\n");
        tExpectedTrace.append("Next=[0]");
        assertEquals("Check of the trace", tExpectedTrace.toString(), tCall.getReturnValue());
    }

    @Test
    public void testDeleteRow() throws SQLException
    {
        mResultSet.next();
        mResultSet.deleteRow();
        mResultSet.close();
        assertEquals("One flow should be log at this time", 1, MemoryWriter.countFlows());
        MethodCallPO tCall = MemoryWriter.getFlow(0).getFirstMethodCall();
        assertTrue("Should contain log, " + tCall.getReturnValue(), tCall.getReturnValue().contains("Delete=[1]\n"));
    }

    @Test
    public void testInsertRow() throws SQLException
    {
        mResultSet.moveToInsertRow();
        mResultSet.updateInt("ID", sCurId++);
        mResultSet.updateString("THREAD_NAME", "ExecuteThread: '23' for queue: 'Default'");
        mResultSet.updateLong("DURATION", 123);
        mResultSet.updateString("BEGIN_TIME_AS_DATE", "2005-04-22 15:03:58");
        mResultSet.updateLong("END_TIME", 1114175038609L);
        mResultSet.updateString("JVM", "Kerny's Babasse");
        mResultSet.updateLong("BEGIN_TIME", 1114175038458L);
        mResultSet.insertRow();
        mResultSet.close();
        assertEquals("One flow should be log at this time", 1, MemoryWriter.countFlows());
        MethodCallPO tCall = MemoryWriter.getFlow(0).getFirstMethodCall();
        assertTrue("Should contain log, " + tCall.getReturnValue(), tCall.getReturnValue().contains("Inserted=[1]\n"));
    }

    @Test
    public void testNext() throws SQLException
    {
        mResultSet.next();
        mResultSet.close();
        assertEquals("One flow should be log at this time", 1, MemoryWriter.countFlows());
        MethodCallPO tCall = MemoryWriter.getFlow(0).getFirstMethodCall();
        assertTrue("Should contain log, " + tCall.getReturnValue(), tCall.getReturnValue().contains("Next=[1]"));
    }

    @Test
    public void testPrevious() throws SQLException
    {
        mResultSet.next();
        mResultSet.previous();
        mResultSet.close();
        assertEquals("One flow should be log at this time", 1, MemoryWriter.countFlows());
        MethodCallPO tCall = MemoryWriter.getFlow(0).getFirstMethodCall();
        assertTrue("Should contain log, " + tCall.getReturnValue(), tCall.getReturnValue().contains("Previous=[1]\n"));
    }

    @Test
    public void testUpdateRow() throws SQLException
    {
        mResultSet.next();
        mResultSet.updateLong("DURATION", 156);
        mResultSet.updateRow();
        mResultSet.close();
        assertEquals("One flow should be log at this time", 1, MemoryWriter.countFlows());
        MethodCallPO tCall = MemoryWriter.getFlow(0).getFirstMethodCall();
        assertTrue("Should contain log, " + tCall.getReturnValue(), tCall.getReturnValue().contains("Updated=[1]\n"));
    }
}
