package org.jmonitoring.sample;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.stat.Statistics;
import org.jmonitoring.core.dao.PersistanceTestCase;
import org.jmonitoring.core.persistence.HibernateManager;
import org.jmonitoring.sample.persistence.SampleHibernateManager;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public abstract class SamplePersistenceTestcase extends PersistanceTestCase
{
    protected Session mSampleSession;

    private Transaction mSampleTransaction;

    private Statistics mSampleStats;

    private static Log sLog = LogFactory.getLog(SamplePersistenceTestcase.class);

    protected void setUp() throws Exception
    {
        super.setUp();
        mSampleSession = SampleHibernateManager.getSession();
        mSampleTransaction = mSampleSession.beginTransaction();

        mSampleStats = HibernateManager.getStats();
        mSampleStats.clear();
        SampleHibernateManager.createSchema();
    }

    protected void tearDown() throws Exception
    {
        try
        {
            try
            {
                SampleHibernateManager.dropSchema();
                mSampleTransaction.rollback();
            } finally
            {
                mSampleSession.close();
                sLog.info("Hibernate Session Closed");
            }
        } finally
        {
            super.tearDown();
        }
    }

    public Session getSampleSession()
    {
        return mSampleSession;
    }

    public Statistics getSampleStats()
    {
        return mSampleStats;
    }

}
