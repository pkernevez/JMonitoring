package org.jmonitoring.hibernate.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.stat.Statistics;
import org.jmonitoring.core.dao.PersistanceTestCase;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * This class is exactly the same than <code>org.jmonitoring.core.dao.HibernatePersistanceTestCase</code>.
 * But it overrides all its method for exclusion weaving, because we don't want to weave 
 * the JMonitoring internal sql requests. 
 */
public class HibernatePersistanceTestCase extends PersistanceTestCase
{

    protected void assertStatistics(Class pEntity, int pInsertCount, int pUpdateCount, int pLoadCount, int pFetchCount)
    {
        super.assertStatistics(pEntity, pInsertCount, pUpdateCount, pLoadCount, pFetchCount);
    }

    protected void createDataSet(String pDataSetFileName)
    {
        super.createDataSet(pDataSetFileName);
    }

    public Session getSession()
    {
        return super.getSession();
    }

    public Statistics getStats()
    {
        return super.getStats();
    }

    public Transaction getTransaction()
    {
        return super.getTransaction();
    }

    protected void setUp() throws Exception
    {
        super.setUp();
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

}
