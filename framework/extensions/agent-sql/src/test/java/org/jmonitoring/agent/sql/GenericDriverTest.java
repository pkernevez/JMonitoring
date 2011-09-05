package org.jmonitoring.agent.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import junit.framework.TestCase;

import org.h2.Driver;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jmonitoring.agent.store.impl.MemoryWriter;
import org.jmonitoring.core.configuration.SpringConfigurationUtil;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class GenericDriverTest extends TestCase
{
    private static Session mSession;

    @Override
    public void setUp()
    {
        ClassPathXmlApplicationContext tAContext =
            new ClassPathXmlApplicationContext(new String[] {"/jmonitoring-agent-test.xml", "/memory-test.xml" });
        SpringConfigurationUtil.setContext(tAContext);
        SessionFactory tFacto = (SessionFactory) tAContext.getBean("sessionFactory");
        MemoryWriter.clear();
        mSession = tFacto.openSession();
        mSession.beginTransaction();
    }

    @Override
    public void tearDown()
    {
        mSession.getTransaction().rollback();
        mSession.close();
    }

    @Test
    public void testAcceptUrl() throws SQLException
    {
        assertTrue(new Driver().acceptsURL("jdbc:h2:mem:jmonitoringsample"));
        assertFalse(new Driver().acceptsURL("jdbc:jmonitoringsample"));

        assertFalse(new H2Driver().acceptsURL("jmorwererkjewlkjrnitoring:org.h2.Driver:jdbc:h2:mem:jmonitoringsample"));
        assertTrue(new H2Driver().acceptsURL("jmonitoring:jdbc:h2:mem:jmonitoringsample"));
        assertFalse(new H2Driver().acceptsURL("jmonitoring:jdbc:em:jmonitoringsample"));
    }

    @Test
    public void testConnect() throws SQLException
    {
        Properties tProps = new Properties();
        tProps.setProperty("user", "sa");
        assertNotNull(new Driver().connect("jdbc:h2:mem:jmonitoringsample", tProps));
        assertNull(new Driver().connect("jdbc:jmonitoringsample", tProps));

        assertNull(new H2Driver().connect("jmorwererkje:org.h2.Driver:jdbc:h2:mem:jmonitoringsample", tProps));
        assertNotNull(new H2Driver().connect("jmonitoring:jdbc:h2:mem:jmonitoringsample", tProps));
        assertNull(new H2Driver().connect("jmonitoring:jdbc:em:jmonitoringsample", tProps));
    }

    @Test
    public void testConnectStatement() throws Exception
    {
        Statement tStat = mSession.connection().createStatement();
        assertEquals(JMonitoringStatement.class, tStat.getClass());
    }

    @Test
    public void testConnectPrepareStatement() throws SQLException
    {
        Connection tCon = mSession.connection();
        assertEquals(JMonitoringPreparedStatement.class, tCon.prepareStatement("select * from EXECUTION_FLOW")
                                                             .getClass());
    }

    @Test
    public void testConnectCallStatement() throws SQLException
    {
        Connection tCon = mSession.connection();
        assertEquals(JMonitoringCallableStatement.class, tCon.prepareCall("select * from EXECUTION_FLOW").getClass());
    }

    @Test
    public void testAcceptsURL()
    {

    }

}
