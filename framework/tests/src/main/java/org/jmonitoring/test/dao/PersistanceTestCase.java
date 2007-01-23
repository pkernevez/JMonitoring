package org.jmonitoring.test.dao;


import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.Statistics;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPK;
import org.jmonitoring.core.domain.MethodCallPO;
import org.jmonitoring.core.persistence.HibernateManager;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public abstract class PersistanceTestCase extends TestCase
{

    private Session mSession;

    private Transaction mTransaction;

    private Statistics mStats;

    private static Log sLog = LogFactory.getLog(PersistanceTestCase.class.getName());

    protected void setUp() throws Exception
    {
        super.setUp();

        mSession = HibernateManager.getSession();
        mTransaction = mSession.beginTransaction();

        mStats = HibernateManager.getStats();
        mStats.clear();

        Configuration tConfig = HibernateManager.getConfig();
        SchemaExport tDdlexport = new SchemaExport(tConfig);

        tDdlexport.create(true, true);

    }

    protected void assertStatistics(Class pEntity, int pInsertCount, int pUpdateCount, int pLoadCount, int pFetchCount)
    {
        EntityStatistics tStat = mStats.getEntityStatistics(pEntity.getName());

        assertEquals("Invalid INSERT statistics", pInsertCount, tStat.getInsertCount());
        assertEquals("Invalid UPDATE statistics", pUpdateCount, tStat.getUpdateCount());
        assertEquals("Invalid LOAD statistics", pLoadCount, tStat.getLoadCount());
        assertEquals("Invalid FECTH statistics", pFetchCount, tStat.getFetchCount());

    }

    protected void createDataSet(String pDataSetFileName)
    {
        Session tSession = HibernateManager.getSession();

        if (pDataSetFileName != null)
        {
            IDataSet tDataSet;
            try
            {
                sLog.debug("Before CLEAN INSERT");
                tDataSet = new XmlDataSet(PersistanceTestCase.class.getResourceAsStream(pDataSetFileName));
                DatabaseOperation.CLEAN_INSERT.execute(new DatabaseConnection(tSession.connection()), tDataSet);
                sLog.debug("Now flush data");
                mSession.flush();
            } catch (DataSetException e)
            {
                throw new RuntimeException(e);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        sLog.debug("end createDataSet");
    }

    protected void tearDown() throws Exception
    {
        try
        {
            super.tearDown();
            Configuration tConfig = HibernateManager.getConfig();
            SchemaExport tDdlexport = new SchemaExport(tConfig);

            tDdlexport.drop(true, true);
            mTransaction.rollback();
        } finally
        {
            mSession.close();
            sLog.info("Hibernate Session Closed");
        }
    }

    public Session getSession()
    {
        return mSession;
    }

    public Transaction getTransaction()
    {
        return mTransaction;
    }

    public Statistics getStats()
    {
        return mStats;
    }

    public static ExecutionFlowPO buildNewFullFlow()
    {
        MethodCallPO tPoint;
        MethodCallPO tSubPoint, tSubPoint2, tSubPoint3, tSubPoint4, tSubPoint5;
        long tStartTime = System.currentTimeMillis();

        tPoint = new MethodCallPO(null, PersistanceTestCase.class.getName(), "builNewFullFlow", "GrDefault",
            "[]");
        tPoint.setBeginTime(tStartTime); // 35
        tSubPoint = new MethodCallPO(tPoint, PersistanceTestCase.class.getName(), "builNewFullFlow2",
            "GrChild1", "[]");
        tSubPoint.setBeginTime(tStartTime + 2); // 3
        tSubPoint.setEndTime(tStartTime + 5);
        tSubPoint.setRuntimeClassName(PersistanceTestCase.class.getName() + "iuiu");

        tSubPoint2 = new MethodCallPO(tPoint, PersistanceTestCase.class.getName(), "builNewFullFlow3",
            "GrChild2", "[]");
        tSubPoint2.setBeginTime(tStartTime + 8);// 21

        tSubPoint3 = new MethodCallPO(tSubPoint2, PersistanceTestCase.class.getName(), "builNewFullFlow3",
            "GrChild2", "[]");
        tSubPoint3.setBeginTime(tStartTime + 14);// 1
        tSubPoint3.setEndTime(tStartTime + 15);

        tSubPoint4 = new MethodCallPO(tSubPoint2, PersistanceTestCase.class.getName(), "builNewFullFlow3",
            "GrChild2", "[]");
        tSubPoint4.setBeginTime(tStartTime + 16);// 12

        tSubPoint5 = new MethodCallPO(tSubPoint4, PersistanceTestCase.class.getName(), "builNewFullFlow3",
            "GrChild2", "[]");
        tSubPoint5.setBeginTime(tStartTime + 26);// 1
        tSubPoint5.setEndTime(tStartTime + 27);

        tSubPoint4.setEndTime(tStartTime + 28);
        tSubPoint2.setEndTime(tStartTime + 29);

        tPoint.setEndTime(tStartTime + 35);
        ExecutionFlowPO tFlow = new ExecutionFlowPO("TEST-main", tPoint, "myJVM");
        tPoint.setMethId(new MethodCallPK(tFlow, 1));
        tSubPoint.setMethId(new MethodCallPK(tFlow, 2));
        tSubPoint2.setMethId(new MethodCallPK(tFlow, 3));
        tSubPoint3.setMethId(new MethodCallPK(tFlow, 4));
        tSubPoint4.setMethId(new MethodCallPK(tFlow, 5));
        tSubPoint5.setMethId(new MethodCallPK(tFlow, 6));
        return tFlow;
    }

}
