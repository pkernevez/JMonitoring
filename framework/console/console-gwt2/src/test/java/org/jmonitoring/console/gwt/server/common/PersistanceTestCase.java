package org.jmonitoring.console.gwt.server.common;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.Statistics;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPK;
import org.jmonitoring.core.domain.MethodCallPO;
import org.jmonitoring.core.tests.JMonitoringTestCase;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

@ContextConfiguration(locations = {"/persistence-test.xml" })
public abstract class PersistanceTestCase extends JMonitoringTestCase
{
    Session mSession;

    Transaction transaction;

    @Resource(name = "sessionFactory")
    SessionFactory sessionFactory;

    private Statistics mStats;

    private static Logger sLog = LoggerFactory.getLogger(PersistanceTestCase.class.getName());

    @Before
    public void initDb() throws Exception
    {
        super.setUp();
        mSession = sessionFactory.openSession();
        transaction = mSession.beginTransaction();
        TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(mSession));
        mStats = sessionFactory.getStatistics();
        mStats.clear();
    }

    @After
    public void clearDb()
    {
        transaction.rollback();
        mSession.close();
        TransactionSynchronizationManager.unbindResource(sessionFactory);
    }

    public void commitAndRestartSession()
    {
        if (mSession.isOpen())
        {
            if (mSession.getTransaction().isActive())
            {
                mSession.getTransaction().commit();
            }
            mSession.close();
        }
        TransactionSynchronizationManager.unbindResource(sessionFactory);
        mSession = sessionFactory.openSession();
        transaction = mSession.beginTransaction();
        TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(mSession));

    }

    protected void assertStatistics(Class<MethodCallPO> pEntity, int pInserts, int pUpdates, int pLoads, int pFetchs)
    {
        EntityStatistics tStat = mStats.getEntityStatistics(pEntity.getName());

        assertEquals("Invalid INSERT statistics", pInserts, tStat.getInsertCount());
        assertEquals("Invalid UPDATE statistics", pUpdates, tStat.getUpdateCount());
        assertEquals("Invalid LOAD statistics", pLoads, tStat.getLoadCount());
        assertEquals("Invalid FECTH statistics", pFetchs, tStat.getFetchCount());

    }

    public Session getSession()
    {
        return mSession;
    }

    public Transaction getTransaction()
    {
        return mSession.getTransaction();
    }

    public Statistics getStats()
    {
        return mStats;
    }

     static ExecutionFlowPO buildNewFullFlow(int pVariante)
    {
        MethodCallPO tPoint;
        MethodCallPO tSubPoint, tSubPoint2, tSubPoint3, tSubPoint4, tSubPoint5;
        long tStartTime = System.currentTimeMillis();

        tPoint = new MethodCallPO(null, PersistanceTestCase.class.getName(), "builNewFullFlow"+pVariante, "GrDefault", "[]");
        tPoint.setBeginTime(tStartTime); // 35
        tSubPoint = new MethodCallPO(tPoint, PersistanceTestCase.class.getName(), "builNewFullFlow2", "GrChild1", "[]");
        tSubPoint.setBeginTime(tStartTime + 2); // 3
        tSubPoint.setEndTime(tStartTime + 5);
        tSubPoint.setRuntimeClassName(PersistanceTestCase.class.getName() + "iuiu");
        tSubPoint.setThrowableClass(RuntimeException.class.getName());
        tSubPoint.setThrowableMessage("An error occured !");

        tSubPoint2 =
            new MethodCallPO(tPoint, PersistanceTestCase.class.getName(), "builNewFullFlow3", "GrChild2", "[]");
        tSubPoint2.setBeginTime(tStartTime + 8);// 21

        tSubPoint3 =
            new MethodCallPO(tSubPoint2, PersistanceTestCase.class.getName(), "builNewFullFlow3", "GrChild2", "[]");
        tSubPoint3.setBeginTime(tStartTime + 14);// 1
        tSubPoint3.setEndTime(tStartTime + 15);

        tSubPoint4 =
            new MethodCallPO(tSubPoint2, PersistanceTestCase.class.getName(), "builNewFullFlow3", "GrChild2", "[]");
        tSubPoint4.setBeginTime(tStartTime + 16);// 12

        tSubPoint5 =
            new MethodCallPO(tSubPoint4, PersistanceTestCase.class.getName(), "builNewFullFlow3", "GrChild2", "[]");
        tSubPoint5.setBeginTime(tStartTime + 26);// 1
        tSubPoint5.setEndTime(tStartTime + 27);

        tSubPoint4.setEndTime(tStartTime + 28);
        tSubPoint2.setEndTime(tStartTime + 29);

        tPoint.setEndTime(tStartTime + 35);
        ExecutionFlowPO tFlow = new ExecutionFlowPO("TEST-main"+pVariante, tPoint, "myJVM");
        tPoint.setMethId(new MethodCallPK(tFlow, 1));
        tSubPoint.setMethId(new MethodCallPK(tFlow, 2));
        tSubPoint2.setMethId(new MethodCallPK(tFlow, 3));
        tSubPoint3.setMethId(new MethodCallPK(tFlow, 4));
        tSubPoint4.setMethId(new MethodCallPK(tFlow, 5));
        tSubPoint5.setMethId(new MethodCallPK(tFlow, 6));
        return tFlow;
    }
}
