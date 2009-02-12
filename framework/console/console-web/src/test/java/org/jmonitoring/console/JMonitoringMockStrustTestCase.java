package org.jmonitoring.console;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.jmonitoring.core.configuration.SpringConfigurationUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@ContextConfiguration(locations = {"/console.xml", "/core-test.xml", "/default-test.xml", "/persistence-test.xml" })
@Ignore
public abstract class JMonitoringMockStrustTestCase extends MockStrutsTestCase implements ApplicationContextAware
{
    private Session mSession;

    private Transaction mTransaction;

    protected SessionFactory mSessionFactory;

    private static Logger sLog = LoggerFactory.getLogger(JMonitoringMockStrustTestCase.class.getName());

    @Before
    public void initDb() throws Exception
    {
        super.setUp();

        mSessionFactory = (SessionFactory) mApplicationContext.getBean("sessionFactory");
        // dropCreate();
        mSession = mSessionFactory.openSession();
        mTransaction = mSession.beginTransaction();
        TransactionSynchronizationManager.bindResource(mSessionFactory, new SessionHolder(mSession));
        SpringConfigurationUtil.setContext(mApplicationContext);
    }

    public void clear()
    {
        mSession.clear();
    }

    @Resource(name = "hibernateConfiguration")
    private Configuration mConfiguration;

    @After
    public void closeDb() throws Exception
    {
        try
        {
            mSessionFactory.getCurrentSession().getTransaction().rollback();
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

    protected void dropCreate()
    {
        sLog.info("Creating new Schema for the DataBase");
        Session tSession = mSessionFactory.openSession();
        Connection tCon = tSession.connection();
        try
        {
            SchemaExport tDdlexport = new SchemaExport(mConfiguration, tCon);
            tDdlexport.drop(true, true);
            sLog.info("End of the Schema creation for the DataBase");
        } finally
        {
            try
            {
                tCon.close();
            } catch (SQLException e)
            {
                sLog.error("Unable to release resources", e);
            }
            tSession.close();
        }
        tSession = mSessionFactory.openSession();
        tCon = tSession.connection();
        try
        {
            SchemaExport tDdlexport = new SchemaExport(mConfiguration, tCon);
            tDdlexport.create(true, true);
            sLog.info("End of the Schema creation for the DataBase");
        } finally
        {
            try
            {
                tCon.close();
            } catch (SQLException e)
            {
                sLog.error("Unable to release resources", e);
            }
            tSession.close();
        }

    }

    public Session getSessionHib()
    {
        return mSession;
    }

    // public Transaction getTransaction()
    // {
    // return mTransaction;
    // }

    protected StaticApplicationContext mApplicationContext;

    public StaticApplicationContext getApplicationContext()
    {
        return mApplicationContext;
    }

    public void setApplicationContext(ApplicationContext pApplicationContext)
    {
        mApplicationContext = new StaticApplicationContext(pApplicationContext);
    }

}
