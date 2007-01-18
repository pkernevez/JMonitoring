package org.jmonitoring.core.store.impl;

import java.sql.SQLException;

import org.jmonitoring.core.dao.ExecutionFlowDaoFactory;
import org.jmonitoring.core.dao.IExecutionFlowDAO;
import org.jmonitoring.core.dao.PersistanceTestCase;
import org.jmonitoring.core.store.AspectLoggerEmulator;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * @author pke
 * 
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 * Templates
 */
public class TestAsynchroneJdbcLogger extends PersistanceTestCase
{
    private static final int TIME_TO_WAIT = 1500;

    // private int mInitialMaxFlowId;

    /**
     * Check the number of toString() method called on the object associated to MethodCallDTO. This is important because
     * of the cost of a toString method on complexe objects.
     * 
     * @throws InterruptedException no doc
     * @throws SQLException no doc
     */
    public void testNbToStringMethodCall() throws InterruptedException
    {
        int tInitialFlowCount;
        // We check the result into DB
        int tFinalFlowCount;
        IExecutionFlowDAO tFlowDao = ExecutionFlowDaoFactory.getExecutionFlowDao(getSession());
        tInitialFlowCount = tFlowDao.countFlows();

        MockAbstractAsynchroneLogger.resetNbLog();
        MockAbstractAsynchroneLogger.resetNbPublish();
        AspectLoggerEmulator tEmulator = new AspectLoggerEmulator(new AsynchroneJdbcLogger(true));
        tEmulator.simulateExecutionFlow(true);
        Thread.sleep(TIME_TO_WAIT);

        // Now check the number of toString called
        assertEquals(3, AspectLoggerEmulator.Param.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Parent.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Child1.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Child2.getNbToString());

        tFinalFlowCount = tFlowDao.countFlows();

        getSession().flush();
        assertEquals(tInitialFlowCount + 1, tFinalFlowCount);
    }

    // /**
    // * no doc
    // *
    // * @throws Exception no doc
    // */
    // protected void setUp() throws Exception
    // {
    // super.setUp();
    // SQLQuery tQuery = mSession.createSQLQuery("Select Max(id) As MyMax from EXECUTION_FLOW");
    // Object tResult = tQuery.addScalar("MyMax", Hibernate.INTEGER).list().get(0);
    // mInitialMaxFlowId = ((Integer) tResult).intValue();
    // }

    // /**
    // * no doc
    // *
    // * @todo Refactor the connection
    // * @throws Exception no doc
    // */
    // protected void tearDown() throws Exception
    // {
    // Connection tCon = null;
    // Statement tStat = null;
    // try
    // {
    // tCon = mSession.connection();
    // tStat = tCon.createStatement();
    // tStat.executeUpdate("DELETE From METHOD_CALL where EXECUTION_FLOW_ID>" + mInitialMaxFlowId);
    // tStat.executeUpdate("DELETE From EXECUTION_FLOW where ID>" + mInitialMaxFlowId);
    // tCon.commit();
    // } finally
    // {
    // try
    // {
    // if (tStat != null)
    // {
    // tStat.close();
    // }
    // } finally
    // {
    // if (tCon != null)
    // {
    // tCon.close();
    // }
    // }
    //
    // }
    //
    // super.tearDown();
    // }
}
