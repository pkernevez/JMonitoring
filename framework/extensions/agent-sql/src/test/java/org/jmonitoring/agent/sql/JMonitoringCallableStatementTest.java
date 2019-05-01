package org.jmonitoring.agent.sql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.jmonitoring.agent.store.impl.MemoryWriter;
import org.junit.Test;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class JMonitoringCallableStatementTest extends SqlTestCase
{

    @Test
    public void testExecuteQuery() throws SQLException
    {
        Connection tCon = mSession.connection();
        CallableStatement tPStat = tCon.prepareCall("select * from EXECUTION_FLOW where Id=?");
        assertEquals(JMonitoringCallableStatement.class, tPStat.getClass());
        tPStat.setInt(1, 34);
        tPStat.executeQuery();
        
        assertEquals(1, MemoryWriter.countFlows());
        StringBuilder tBuffer = new StringBuilder();
        tBuffer.append("CallableStatement with Sql=[select * from EXECUTION_FLOW where Id=?]\n");
        tBuffer.append("Add Int, pos=[1], value=[34]\n");
        tBuffer.append("Execute query\n");
        tBuffer.append("ResultSet=[rs");
        String tLog = MemoryWriter.getFlow(0).getFirstMethodCall().getReturnValue();
        assertEquals("Bad group name", "Jdbc", MemoryWriter.getFlow(0).getFirstMethodCall().getGroupName());
        String tExpected = tBuffer.toString();
        assertEquals(tExpected, tLog.substring(0, tExpected.length()));
        tExpected = " columns: 10 rows: 0 pos: -1]\n";
        assertEquals(tExpected, tLog.substring(tLog.length() - tExpected.length()));
        assertEquals("java.sql.PreparedStatement", MemoryWriter.getFlow(0).getFirstMethodCall().getClassName());
    }
}
