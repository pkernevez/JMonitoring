package org.jmonitoring.hibernate.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.stat.Statistics;
import org.jmonitoring.core.domain.MethodCallPO;
import org.jmonitoring.test.dao.PersistanceTestCase;
import org.junit.Ignore;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * This class is exactly the same than <code>org.jmonitoring.core.dao.HibernatePersistanceTestCase</code>. But it
 * overrides all its method for exclusion weaving, because we don't want to weave the JMonitoring internal sql requests.
 * 
 * @deprecated
 */
@Deprecated
@Ignore
public class HibernatePersistanceTestCase extends PersistanceTestCase
{

    @Override
    protected void assertStatistics(Class<MethodCallPO> pEntity, int pInserts, int pUpdates, int pLoads, int pFetchs)
    {
        super.assertStatistics(pEntity, pInserts, pUpdates, pLoads, pFetchs);
    }

    @Override
    public Session getSession()
    {
        return super.getSession();
    }

    @Override
    public Statistics getStats()
    {
        return super.getStats();
    }

    @Override
    public Transaction getTransaction()
    {
        return super.getTransaction();
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    /**
     * Only because Maven fail without any test.
     */
    public void testBidon()
    {

    }
}
