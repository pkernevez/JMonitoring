package org.jmonitoring.hibernate.info;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Types;

import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.jmonitoring.agent.store.StoreManager;
import org.jmonitoring.agent.store.impl.MemoryWriter;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.test.dao.PersistanceTestCase;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestSqlStatementTracer extends PersistanceTestCase
{

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        MemoryWriter.clear();
    }

    public void testConvertToString() throws SQLException
    {
        SqlStatementTracer tTracer = new SqlStatementTracer();
        assertEquals("Unable to log this Statement class= NULL", tTracer.convertToString(null, null));
        assertEquals("Unable to log this Statement class=java.lang.String", tTracer.convertToString("bad class", null));
        JMonitoringStatement tStat = new JMonitoringStatement(null);
        assertEquals("", tTracer.convertToString(tStat, null).toString());

        assertEquals("", tTracer.convertToString(tStat, "RTRT").toString());

    }

    public void testTraceStatementParametersStatement() throws HibernateException, SQLException
    {
        StoreManager.changeStoreWriterClass(MemoryWriter.class);
        Session tSession = getSession();
        Connection tCon = tSession.connection();
        Statement tStat = tCon.createStatement();
        assertEquals(JMonitoringStatement.class, tStat.getClass());
        // SqlStatementTracer tTracer = new SqlStatementTracer();
        tStat.execute("select * from EXECUTION_FLOW");
        closeAndRestartSession();
        assertEquals(1, MemoryWriter.countFlows());
        ExecutionFlowPO tFlow = MemoryWriter.getFlow(0);
        assertEquals("Sql=[select * from EXECUTION_FLOW]\n", tFlow.getFirstMethodCall().getReturnValue());
    }

    public void testTraceStatementParametersPreparedStatement() throws HibernateException, SQLException
    {
        PreparedStatement tPStat = getSession()
                                               .connection()
                                               .prepareStatement(
                                                                 "select * from EXECUTION_FLOW where Id=? and JVM=? and Id=? and Id=? and Id=? and Id=? " + "and BEGIN_TIME_AS_DATE=? and Id=? and Id=? and Id=? and BEGIN_TIME_AS_DATE=?");
        assertEquals(JMonitoringPreparedStatement.class, tPStat.getClass());
        JMonitoringPreparedStatement tPrepStat = (JMonitoringPreparedStatement) tPStat;
        tPrepStat.setInt(1, 34);
        tPrepStat.setString(2, "fsdf");
        tPrepStat.setLong(3, 45);
        tPrepStat.setBigDecimal(4, new BigDecimal(56));
        tPrepStat.setBoolean(5, false);
        tPrepStat.setByte(6, (byte) 67);
        tPrepStat.setDate(7, new Date(2006, 11, 12));
        tPrepStat.setDouble(8, 3.67834);
        tPrepStat.setLong(9, 3434);
        tPrepStat.setNull(10, Types.BIT);
        tPrepStat.setTime(11, new Time(11, 23, 34));
        SqlStatementTracer tTracer = new SqlStatementTracer();
        String tTrace = tTracer.convertToString(tPrepStat, new Object[0]).toString();
        StringBuilder tBuffer = new StringBuilder();
        tBuffer.append("PrepareStatement with Sql=[select * from EXECUTION_FLOW where Id=? and JVM=? ");
        tBuffer.append("and Id=? and Id=? and Id=? and Id=? and BEGIN_TIME_AS_DATE=? and ");
        tBuffer.append("Id=? and Id=? and Id=? and BEGIN_TIME_AS_DATE=?]\n");
        tBuffer.append("Add Int parameter, pos=[1], value=[34]\n");
        tBuffer.append("Add String parameter, pos=[2], value=[fsdf]\n");
        tBuffer.append("Add Long parameter, pos=[3], value=[45]\n");
        tBuffer.append("Add BigDecimal parameter, pos=[4], value=[56]\n");
        tBuffer.append("Add Boolean parameter, pos=[5], value=[false]\n");
        tBuffer.append("Add Byte parameter, pos=[6], value=[67]\n");
        tBuffer.append("Add Date parameter, pos=[7], value=[3906-12-12]\n");
        tBuffer.append("Add Double parameter, pos=[8], value=[3.67834]\n");
        tBuffer.append("Add Long parameter, pos=[9], value=[3434]\n");
        tBuffer.append("Add NULL parameter, pos=[10], type=[-7]\n");
        tBuffer.append("Add Time parameter, pos=[11], value=[11:23:34]\n");

        assertEquals(tBuffer.toString(), tTrace);
    }

    public void testTraceStatementParametersCallStatement() throws HibernateException, SQLException
    {
        StoreManager.changeStoreWriterClass(MemoryWriter.class);

        defineStoredProcedure();
        checkDataStoredProcedure();

        CallableStatement tCallStat = getSession().connection().prepareCall("call SQRT(?)");
        assertEquals(JMonitoringCallableStatement.class, tCallStat.getClass());

        // This is not the real StoredProcedure syntax due to the limitations of HsqlDB support
        tCallStat.setDouble(1, 2.0);
        ResultSet tResult;
        tResult = tCallStat.executeQuery();
        closeAndRestartSession();
        tResult.next();
        assertEquals(1.41, tResult.getDouble(1), 0.01);

        StringBuilder tBuffer = new StringBuilder();
        tBuffer.append("CallableStatement with Sql=[call SQRT(?)]\n");
        tBuffer.append("Add Double parameter, pos=[1], value=[2.0]\n");
        tBuffer.append("Execute query\n");

        // We only want to check the latest ExecutionFlow
        LogFactory.getLog(TestSqlStatementTracer.class).info("CountFlow=" + MemoryWriter.countFlows());
        assertEquals(5, MemoryWriter.countFlows());

        ExecutionFlowPO tFlow = MemoryWriter.getFlow(4);
        LogFactory.getLog(TestSqlStatementTracer.class).info("Writer=" + tFlow.getFirstMethodCall().getReturnValue());
        assertEquals(tBuffer.toString(), tFlow.getFirstMethodCall().getReturnValue());
    }

    private void checkDataStoredProcedure() throws SQLException
    {
        Statement tStat = getSession().connection().createStatement();
        ResultSet tResult;
        tResult = tStat.executeQuery("SELECT MYVALUE , \"java.lang.Math.sqrt\"(MYVALUE) AS SQRT FROM TEST_PROC");
        tResult.next();
        assertEquals(2.0, tResult.getDouble(1), 0.01);
        assertEquals(1.41, tResult.getDouble(2), 0.01);
        tResult.next();
        assertEquals(4.0, tResult.getDouble(1), 0.01);
        assertEquals(2.0, tResult.getDouble(2), 0.01);

    }

    private void defineStoredProcedure() throws SQLException
    {
        Statement tStat = getSession().connection().createStatement();
        tStat.execute("CREATE TABLE TEST_PROC (ID integer not null, MYVALUE integer, primary key (ID))");
        tStat.execute("INSERT INTO TEST_PROC (ID, MYVALUE) values (1, 2)");
        tStat.execute("INSERT INTO TEST_PROC (ID, MYVALUE) values (2, 4)");
        tStat.execute("CREATE ALIAS SQRT FOR \"java.lang.Math.sqrt\"");
        closeAndRestartSession();
        // tStat.executeQuery(sql)
        // TODO Auto-generated method stub

    }
}
