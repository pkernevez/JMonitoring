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
    Session session;

    Transaction transaction;

    private static boolean dataInitialized = false;

    @Resource(name = "sessionFactory")
    SessionFactory sessionFactory;

    private Statistics stats;

    private static Logger sLog = LoggerFactory.getLogger(PersistanceTestCase.class.getName());

    @Before
    public void initDb() throws Exception
    {
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
        stats = sessionFactory.getStatistics();
        stats.clear();
        if (!dataInitialized)
        {
            insertTestData();
            commitAndRestartSession();
            stats.clear();
            dataInitialized = true;
        }
    }

    @After
    public void clearDb()
    {
        transaction.rollback();
        session.close();
        TransactionSynchronizationManager.unbindResource(sessionFactory);
    }

    public void commitAndRestartSession()
    {
        if (session.isOpen())
        {
            if (session.getTransaction().isActive())
            {
                session.getTransaction().commit();
            }
            session.close();
        }
        TransactionSynchronizationManager.unbindResource(sessionFactory);
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
        TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));

    }

    protected void assertStatistics(Class<MethodCallPO> pEntity, int pInserts, int pUpdates, int pLoads, int pFetchs)
    {
        EntityStatistics tStat = stats.getEntityStatistics(pEntity.getName());

        assertEquals("Invalid INSERT statistics", pInserts, tStat.getInsertCount());
        assertEquals("Invalid UPDATE statistics", pUpdates, tStat.getUpdateCount());
        assertEquals("Invalid LOAD statistics", pLoads, tStat.getLoadCount());
        assertEquals("Invalid FECTH statistics", pFetchs, tStat.getFetchCount());

    }

    public Session getSession()
    {
        return session;
    }

    public Transaction getTransaction()
    {
        return session.getTransaction();
    }

    public Statistics getStats()
    {
        return stats;
    }

    public void insertTestData()
    {
        sLog.info("Start Insert test data");
        MethodCallBuilder tBuilder =
            ExecutionFlowBuilder.create(1000000000L).setMethodCall("MainClass", "main", "grp", 100);
        tBuilder.addSubMethod("MainClass", "sub1", "grp1", 0, 15);
        tBuilder.addSubMethod("SubClass1", "meth1", "grp2", 20, 20);
        tBuilder.addSubMethod("SubClass2", "meth2", "grp3", 50, 40);
        saveAll(tBuilder);

        tBuilder = ExecutionFlowBuilder.create(1100000000L).setMethodCall("MainClass", "main", "grp", 110);
        tBuilder.addSubMethod("MainClass", "sub1", "grp1", 0, 15);
        tBuilder.addSubMethod("SubClass1", "meth", "grp1", 25, 10);
        tBuilder.addSubMethod("SubClass2", "meth", "grp3", 50, 10);
        saveAll(tBuilder);

        tBuilder = ExecutionFlowBuilder.create(12000000000L).setMethodCall("MainClass2", "main", "grp", 200);
        tBuilder.addSubMethod("SubClass1_1", "meth1", "grp1", 10, 15);
        tBuilder.addSubMethod("SubClass1_2", "meth2", "grp1", 125, 10);
        tBuilder.addSubMethod("SubClass1_3", "meth3", "grp1", 150, 10);
        saveAll(tBuilder);

        tBuilder =
            ExecutionFlowBuilder.create(1300000000L).setThread("SpecificThread")
                                .setMethodCall("MainClass3", "main2", "grp", 250);
        tBuilder.addSubMethod("SubClass1_1", "meth1", "grp1", 10, 15);
        tBuilder.addSubMethod("SubClass1_2", "meth2", "grp1", 125, 10);
        tBuilder.addSubMethod("SubClass1_3", "meth3", "grp3", 150, 10).setThrowable("Error", "Error message");
        saveAll(tBuilder);

        tBuilder =
            ExecutionFlowBuilder.create(1400000000L).setThread("SpecificThread4")
                                .setMethodCall("MainClass4", "main3", "grp", 80);
        tBuilder.addSubMethod("SubClass2_1", "meth2_1", "grp1", 0, 15);
        tBuilder.addSubMethod("SubClass2_2", "meth2_2", "grp1", 16, 10);
        tBuilder.addSubMethod("SubClass2_3", "meth2_3", "grp3", 27, 10).setThrowable("Error", "Error message");
        saveAll(tBuilder);

        session.flush();
        stats.clear();
        sLog.info("End Insert test data");
    }

    private void saveAll(MethodCallBuilder tBuilder)
    {
        ExecutionFlowPO tFlow = tBuilder.get();
        session.save(tFlow);
        createPK(tFlow.getFirstMethodCall(), 0);
        session.save(tFlow.getFirstMethodCall());
    }

    private void createPK(MethodCallPO pMethodCall, int pCpt)
    {
        pMethodCall.setMethId(new MethodCallPK(pMethodCall.getFlow(), pCpt));
        for (MethodCallPO tCurMeth : pMethodCall.getChildren())
        {
            createPK(tCurMeth, ++pCpt);
        }
    }

    static ExecutionFlowPO buildNewFullFlow(int pVariante)
    {
        MethodCallPO tPoint;
        MethodCallPO tSubPoint, tSubPoint2, tSubPoint3, tSubPoint4, tSubPoint5;
        long tStartTime = System.currentTimeMillis();

        tPoint =
            new MethodCallPO(null, PersistanceTestCase.class.getName(), "builNewFullFlow" + pVariante, "GrDefault",
                             "[]");
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
        ExecutionFlowPO tFlow = new ExecutionFlowPO("TEST-main" + pVariante, tPoint, "myJVM");
        tPoint.setMethId(new MethodCallPK(tFlow, 1));
        tSubPoint.setMethId(new MethodCallPK(tFlow, 2));
        tSubPoint2.setMethId(new MethodCallPK(tFlow, 3));
        tSubPoint3.setMethId(new MethodCallPK(tFlow, 4));
        tSubPoint4.setMethId(new MethodCallPK(tFlow, 5));
        tSubPoint5.setMethId(new MethodCallPK(tFlow, 6));
        return tFlow;
    }
}
