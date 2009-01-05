package org.jmonitoring.console;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import servletunit.struts.MockStrutsTestCase;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners( {DependencyInjectionTestExecutionListener.class })
@ContextConfiguration(locations = {"/default-test.xml", "/persistence-test.xml" })
@Ignore
public abstract class JMonitoringMockStrustTestCase extends MockStrutsTestCase implements ApplicationContextAware
{
    private Session mSession;

    private Transaction mTransaction;

    private SessionFactory mSessionFactory;

    private static Log sLog = LogFactory.getLog(JMonitoringMockStrustTestCase.class.getName());

    @Before
    public void initDb() throws Exception
    {
        super.setUp();

        mSessionFactory = (SessionFactory) applicationContext.getBean("sessionFactory");
        mSession = mSessionFactory.openSession();
        mTransaction = mSession.beginTransaction();
        TransactionSynchronizationManager.bindResource(mSessionFactory, new SessionHolder(mSession));
    }

    public void clear()
    {
        mSession.clear();
    }

    public void createSchema()
    {
    }

    @Resource(name = "hibernateConfiguration")
    private Configuration mConfiguration;

    public void closeAndRestartSession()
    {
        if (mSession.isOpen())
        {
            if (mTransaction.isActive())
            {
                mTransaction.commit();
            }
            mSession.close();
        }

        TransactionSynchronizationManager.unbindResource(mSessionFactory);
        mSession = mSessionFactory.openSession();
        TransactionSynchronizationManager.bindResource(mSessionFactory, new SessionHolder(mSession));
        mTransaction = mSession.beginTransaction();
    }

    @After
    public void closeDb() throws Exception
    {
        try
        {
            TransactionSynchronizationManager.unbindResource(mSessionFactory);
            super.tearDown();
        } finally
        {
            if (mSession.isOpen())
            {
                if (mTransaction.isActive())
                {
                    mTransaction.rollback();
                }
                mSession.close();
            }
            sLog.info("Hibernate Session Closed");
        }
    }

    public Session getSessionHib()
    {
        return mSession;
    }

    public Transaction getTransaction()
    {
        return mTransaction;
    }

    protected StaticApplicationContext applicationContext;

    public StaticApplicationContext getApplicationContext()
    {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext pApplicationContext)
    {
        applicationContext = new StaticApplicationContext(pApplicationContext);
    }

}
