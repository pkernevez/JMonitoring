package org.jmonitoring.core.dao;

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

    // protected String getDataSetFileName() throws Exception
    // {
    // return null;
    // }

}
