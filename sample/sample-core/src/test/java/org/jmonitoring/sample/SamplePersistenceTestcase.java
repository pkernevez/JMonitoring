package org.jmonitoring.sample;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Resource;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.stat.Statistics;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.jmonitoring.agent.store.impl.MemoryWriter;
import org.jmonitoring.core.configuration.SpringConfigurationUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners( {DependencyInjectionTestExecutionListener.class })
@ContextConfiguration(locations = {"/jmonitoring-sample.xml" })
@Ignore
public abstract class SamplePersistenceTestcase extends TestCase implements ApplicationContextAware
{
    @Autowired
    protected SessionFactory mSampleSessionFactory;

    @Resource(name = "hibernateConfiguration")
    private Configuration mConfiguration;

    // protected Session mSampleSession;

    private Statistics mSampleStats;

    private ApplicationContext mApplicationContext;

    private static Log sLog = LogFactory.getLog(SamplePersistenceTestcase.class);

    @Before
    public void init() throws Exception
    {
        super.setUp();
        Session tSession = mSampleSessionFactory.openSession();
        tSession.beginTransaction();
        TransactionSynchronizationManager.bindResource(mSampleSessionFactory, new SessionHolder(tSession));

        MemoryWriter.clear();

        mSampleStats = mSampleSessionFactory.getStatistics();
        mSampleStats.clear();
        createDropDataBase(true);
    }

    @After
    public void clean() throws Exception
    {
        SessionHolder tHolder = (SessionHolder) TransactionSynchronizationManager.unbindResource(mSampleSessionFactory);
        Session tSession = tHolder.getSession();
        if (tSession.getTransaction().isActive())
        {
            tSession.getTransaction().rollback();
        }
        if (tSession.isOpen())
        {
            tSession.close();
        }
        // SampleHibernateManager.dropSchema();
    }

    public void createDropDataBase(boolean pCreate)
    {
        sLog.info("Creating new Schema for the DataBase");
        Connection tCon = mSampleSessionFactory.getCurrentSession().connection();
        try
        {
            SchemaExport tDdlexport = new SchemaExport(mConfiguration, tCon);
            if (pCreate)
            {
                tDdlexport.create(true, true);
            } else
            {
                tDdlexport.drop(true, true);
            }
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
        }
    }

    // public void closeAndRestartSampleSession()
    // {
    // if (mSampleSession.isOpen())
    // {
    // if (mSampleTransaction.isActive())
    // {
    // mSampleTransaction.commit();
    // }
    // mSampleSession.close();
    // }
    //
    // mSampleSession = HibernateManager.getSession();
    // mSampleTransaction = mSampleSession.beginTransaction();
    // }

    public Session getSampleSession()
    {
        return mSampleSessionFactory.getCurrentSession();
    }

    public Statistics getSampleStats()
    {
        return mSampleStats;
    }

    public void setApplicationContext(ApplicationContext pApplicationContext) throws BeansException
    {
        mApplicationContext = pApplicationContext;
    }

    protected Object getBean(String pName)
    {
        return mApplicationContext.getBean(pName);
    }

    /**
     * @return the applicationContext
     */
    public ApplicationContext getApplicationContext()
    {
        return mApplicationContext;
    }

    public void registerAgentSession()
    {
        SessionFactory tFact = (SessionFactory) SpringConfigurationUtil.getBean("sessionFactory");
        Session tSession = tFact.openSession();
        tSession.beginTransaction();
        TransactionSynchronizationManager.bindResource(tFact, new SessionHolder(tSession));
    }

    public void rollbackAgentSession()
    {
        SessionFactory tFact = (SessionFactory) SpringConfigurationUtil.getBean("sessionFactory");
        SessionHolder tHolder = (SessionHolder) TransactionSynchronizationManager.unbindResource(tFact);
        Session tSession = tHolder.getSession();
        if (tSession.getTransaction().isActive())
        {
            tSession.getTransaction().rollback();
        }
        if (tSession.isOpen())
        {
            tSession.close();
        }
    }

}
