package org.jmonitoring.sample;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.stat.Statistics;
import org.jmonitoring.agent.store.StoreManager;
import org.jmonitoring.agent.store.impl.MemoryWriter;
import org.jmonitoring.common.hibernate.HibernateManager;
import org.jmonitoring.sample.persistence.SampleHibernateManager;
import org.jmonitoring.test.dao.PersistanceTestCase;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public abstract class SamplePersistenceTestcase extends PersistanceTestCase {
    protected Session mSampleSession;

    private Transaction mSampleTransaction;

    private Statistics mSampleStats;

    private static Log sLog = LogFactory.getLog(SamplePersistenceTestcase.class);

    protected void setUp() throws Exception {
        super.setUp();
        StoreManager.clear();
        MemoryWriter.clear();
        mSampleSession = SampleHibernateManager.getSession();
        mSampleTransaction = mSampleSession.beginTransaction();

        mSampleStats = HibernateManager.getStats();
        mSampleStats.clear();
        SampleHibernateManager.createSchema();
    }

    protected void tearDown() throws Exception {
        try {
            try {
                SampleHibernateManager.dropSchema();
                mSampleTransaction.rollback();
            } finally {
                mSampleSession.close();
                sLog.info("Hibernate Session Closed");
            }
        } finally {
            super.tearDown();
        }
    }

    public void closeAndRestartSampleSession() {
        if (mSampleSession.isOpen()) {
            if (mSampleTransaction.isActive()) {
                mSampleTransaction.commit();
            }
            mSampleSession.close();
        }

        mSampleSession = HibernateManager.getSession();
        mSampleTransaction = mSampleSession.beginTransaction();
    }

    public Session getSampleSession() {
        return mSampleSession;
    }

    public Statistics getSampleStats() {
        return mSampleStats;
    }

}
