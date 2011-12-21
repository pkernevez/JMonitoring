package org.jmonitoring.agent.sql;

import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jmonitoring.agent.store.IStoreWriter;
import org.jmonitoring.agent.store.impl.MemoryWriter;
import org.jmonitoring.core.configuration.SpringConfigurationUtil;
import org.jmonitoring.core.tests.JMonitoringTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

@ContextConfiguration(locations = {"/memory-test.xml" })
@Ignore
public abstract class SqlTestCase extends JMonitoringTestCase
{
    /** This is the connection use to simulate an application database interaction. */
    protected Session mSession;

    @Before
    public void initContext() throws SQLException
    {
        SpringConfigurationUtil.setContext(getApplicationContext());
        MemoryWriter.clear();
        JMonitoringStatement.refresh();

        ClassPathXmlApplicationContext tAContext =
            new ClassPathXmlApplicationContext(new String[] {"/jmonitoring-agent-test.xml", "/memory-test.xml" });
        SessionFactory tFacto = (SessionFactory) tAContext.getBean("sessionFactory");
        IStoreWriter tSore = (IStoreWriter) tAContext.getBean("storeWriter");
        MemoryWriter.clear();
        assertTrue(tAContext.getBean("storeWriter") instanceof org.jmonitoring.agent.store.impl.MemoryWriter);
        mSession = tFacto.openSession();
        mSession.beginTransaction();
    }

    @After
    public void clear()
    {
        mSession.getTransaction().rollback();
        mSession.close();
    }

}
