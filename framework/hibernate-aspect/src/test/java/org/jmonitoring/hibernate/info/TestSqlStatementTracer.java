package org.jmonitoring.hibernate.info;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.jmonitoring.core.dao.PersistanceTestCase;
import org.jmonitoring.hibernate.info.JMonitoringPreparedStatement;
import org.jmonitoring.hibernate.info.JMonitoringStatement;
import org.jmonitoring.hibernate.info.SqlStatementTracer;
import org.omg.CORBA.TCKind;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestSqlStatementTracer extends PersistanceTestCase
{

    public void testConvertToString() throws SQLException
    {
        SqlStatementTracer tTracer = new SqlStatementTracer();
        assertEquals("Unable to log this Statement class= NULL", tTracer.convertToString(null, null));
        assertEquals("Unable to log this Statement class=java.lang.String", tTracer.convertToString("bad class", null));
        JMonitoringStatement tStat = new JMonitoringStatement(null);
        assertEquals("", tTracer.convertToString(tStat, null));

        assertEquals("", tTracer.convertToString(tStat, "RTRT"));

    }

    public void testTraceStatementParametersStatement() throws HibernateException, SQLException
    {
        Statement tStat = getSession().connection().createStatement();
        assertEquals(JMonitoringStatement.class, tStat.getClass());
        SqlStatementTracer tTracer = new SqlStatementTracer();
        tStat.execute("select * from EXECUTION_FLOW");
        String tTrace = tTracer.convertToString(tStat, new Object[] {Boolean.TRUE });
        assertEquals("Sql=[select * from EXECUTION_FLOW]\n", tTrace.toString());
    }

    public void testTraceStatementParametersPreparedStatement() throws HibernateException, SQLException
    {
        PreparedStatement tPStat = getSession().connection().prepareStatement(
            "select * from EXECUTION_FLOW where Id=? and JVM=? and Id=? and Id=? and Id=? and Id=? "
                + "and BEGIN_TIME_AS_DATE=? and Id=? and Id=? and Id=? and BEGIN_TIME_AS_DATE=?");
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
        String tTrace = tTracer.convertToString(tPrepStat, new Object[0]);
        StringBuffer tBuffer = new StringBuffer();
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
        defineStoredProcedure();
        checkDataStoredProcedure();

        CallableStatement tCallStat = getSession().connection().prepareCall("call SQRT(?)");

        // This is not the real StoredProcedure syntax due to the limitations of HsqlDB support
        tCallStat.setDouble(1, 2.0);
        ResultSet tResult;
        tResult = tCallStat.executeQuery();
        tResult.next();
        assertEquals(1.41, tResult.getDouble(1), 0.01);

        SqlStatementTracer tTracer = new SqlStatementTracer();
        String tTrace = tTracer.convertToString(tCallStat, null);
        StringBuffer tBuffer = new StringBuffer();
        tBuffer.append("CallableStatement with Sql=[call SQRT(?)]\n");
        tBuffer.append("Add Double parameter, pos=[1], value=[2.0]\n");
        tBuffer.append("Execute query\n");

        assertEquals(tBuffer.toString(), tTrace);
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
        // tStat.executeQuery(sql)
        // TODO Auto-generated method stub

    }
}
