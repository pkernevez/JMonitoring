package org.jmonitoring.agent.sql;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jmonitoring.agent.store.Filter;
import org.jmonitoring.agent.store.impl.MemoryWriter;
import org.jmonitoring.core.domain.MethodCallPO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Mockito.*;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/
//@RunWith( MockitoJUnitRunner.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class JMonitoringResultSetTest extends SqlTestCase
{
    ResultSet mResultSet;

    private static int sCurId;

    @Mock
    private Filter filter; 
    
    @Mock
    private Driver mockDriver;
    
    @Before
    public void initResultSet() throws SQLException
    {
        Connection tCon = mSession.connection();
        Statement tStatement = tCon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        String tSql = JMonitoringStatementTest.UPDATE_1 + sCurId++ + JMonitoringStatementTest.UPDATE_2;
        tStatement.executeUpdate(tSql);

        mResultSet = new JMonitoringResultSet(tStatement.executeQuery("select * from EXECUTION_FLOW"), "Jdbc", null);

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
    
    private class TestDriver extends GenericDriver {

        public TestDriver()
        {
            super(mockDriver);
        }
        
    }
    //TODO Restore this test : it requires to have both Mockito and Spring @RunWith
//    @Test
//    public void testFilterIsUsed() throws SQLException{
//        when(mockDriver.acceptsURL("customUrl")).thenReturn(true);
//        DriverManager.getConnection("jmonitoring:customUrl");
//    }
    
}
