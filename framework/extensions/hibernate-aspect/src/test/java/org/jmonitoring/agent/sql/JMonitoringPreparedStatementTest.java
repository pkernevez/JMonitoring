package org.jmonitoring.agent.sql;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;

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
public class JMonitoringPreparedStatementTest extends JMonitoringTestCase
{
    private static int sCurId = 0;

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
    }

    @After
    public void clear()
    {
        mSession.getTransaction().rollback();
        mSession.close();
    }

    @Test
    public void testExecute() throws SQLException
    {
        Connection tCon = mSession.connection();
        PreparedStatement tPStat =
            tCon.prepareStatement("select * from EXECUTION_FLOW where Id=? and "
                + "JVM=? and Id=? and Id=? and Id=? and Id=? "
                + "and BEGIN_TIME_AS_DATE=? and Id=? and Id=? and Id=? and BEGIN_TIME_AS_DATE=?");
        tPStat.setInt(1, 34);
        tPStat.setString(2, "fsdf");
        tPStat.setLong(3, 45);
        tPStat.setBigDecimal(4, new BigDecimal(56));
        tPStat.setBoolean(5, false);
        tPStat.setByte(6, (byte) 67);
        tPStat.setDate(7, new Date(2006, 11, 12));
        tPStat.setDouble(8, 3.67834);
        tPStat.setLong(9, 3434);
        tPStat.setNull(10, Types.BIT);
        tPStat.setTime(11, new Time(11, 23, 34));

        tPStat.execute();
        assertEquals(1, MemoryWriter.countFlows());
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
        tBuffer.append("Execute \n");
        tBuffer.append("Result=[true]\n");

        assertEquals(tBuffer.toString(), MemoryWriter.getFlow(0).getFirstMethodCall().getReturnValue());
    }

    @Test
    public void testExecuteQuery() throws SQLException
    {
        Connection tCon = mSession.connection();
        PreparedStatement tPStat = tCon.prepareStatement("select * from EXECUTION_FLOW where Id=?");
        tPStat.setInt(1, 34);
        tPStat.executeQuery();

        assertEquals(1, MemoryWriter.countFlows());
        StringBuilder tBuffer = new StringBuilder();
        tBuffer.append("PrepareStatement with Sql=[select * from EXECUTION_FLOW where Id=?]\n");
        tBuffer.append("Add Int parameter, pos=[1], value=[34]\n");
        tBuffer.append("Execute query\n");
        tBuffer.append("ResultSet=[rs");
        String tLog = MemoryWriter.getFlow(0).getFirstMethodCall().getReturnValue();
        String tExpected = tBuffer.toString();
        assertEquals(tExpected, tLog.substring(0, tExpected.length()));
        tExpected = ": columns: 8 rows: 0 pos: -1]\n";
        assertEquals(tExpected, tLog.substring(tLog.length() - tExpected.length()));
    }

    @Test
    public void testExecuteUpdate() throws SQLException
    {
        String tSql = JMonitoringStatementTest.UPDATE_1 + sCurId++ + JMonitoringStatementTest.UPDATE_2;
        Connection tCon = mSession.connection();
        PreparedStatement tPStat = tCon.prepareStatement(tSql);
        tPStat.executeUpdate();

        assertEquals(1, MemoryWriter.countFlows());
        ExecutionFlowPO tFlow = MemoryWriter.getFlow(0);
        StringBuilder tBuffer = new StringBuilder();
        tBuffer.append("PrepareStatement with Sql=[").append(tSql).append("]\nExecute update\nResult=[1]\n");
        assertEquals(tBuffer.toString(), tFlow.getFirstMethodCall().getReturnValue());
    }

    @Test
    public void testClearParameters() throws SQLException
    {
        Connection tCon = mSession.connection();
        PreparedStatement tPStat = tCon.prepareStatement("select * from EXECUTION_FLOW where Id=?");
        tPStat.setInt(1, 34);
        tPStat.executeQuery();
        tPStat.clearParameters();
        tPStat.setInt(1, 20);
        tPStat.executeQuery();

        assertEquals(2, MemoryWriter.countFlows());
        StringBuilder tBuffer = new StringBuilder();
        tBuffer.append("PrepareStatement with Sql=[select * from EXECUTION_FLOW where Id=?]\n");
        tBuffer.append("Add Int parameter, pos=[1], value=[20]\n");
        tBuffer.append("Execute query\n");
        tBuffer.append("ResultSet=[rs");
        String tLog = MemoryWriter.getFlow(1).getFirstMethodCall().getReturnValue();
        String tExpected = tBuffer.toString();
        assertEquals(tExpected, tLog.substring(0, tExpected.length()));
        tExpected = ": columns: 8 rows: 0 pos: -1]\n";
        assertEquals(tExpected, tLog.substring(tLog.length() - tExpected.length()));
    }

}
