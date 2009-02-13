package org.jmonitoring.agent.sql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jmonitoring.agent.store.impl.MemoryWriter;
import org.jmonitoring.core.configuration.SpringConfigurationUtil;
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
public class JMonitoringCallableStatementTest extends JMonitoringTestCase
{

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
    public void testExecuteQuery() throws SQLException
    {
        Connection tCon = mSession.connection();
        CallableStatement tPStat = tCon.prepareCall("select * from EXECUTION_FLOW where Id=?");
        tPStat.setInt(1, 34);
        tPStat.executeQuery();

        assertEquals(1, MemoryWriter.countFlows());
        StringBuilder tBuffer = new StringBuilder();
        tBuffer.append("CallableStatement with Sql=[select * from EXECUTION_FLOW where Id=?]\n");
        tBuffer.append("Add Int parameter, pos=[1], value=[34]\n");
        tBuffer.append("Execute query\n");
        tBuffer.append("ResultSet=[rs");
        String tLog = MemoryWriter.getFlow(0).getFirstMethodCall().getReturnValue();
        String tExpected = tBuffer.toString();
        assertEquals(tExpected, tLog.substring(0, tExpected.length()));
        tExpected = ": columns: 8 rows: 0 pos: -1]\n";
        assertEquals(tExpected, tLog.substring(tLog.length() - tExpected.length()));
        assertEquals("java.sql.PreparedStatement", MemoryWriter.getFlow(0).getFirstMethodCall().getClassName());
    }

}
