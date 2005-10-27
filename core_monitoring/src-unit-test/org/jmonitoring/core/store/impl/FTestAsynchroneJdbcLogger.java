package org.jmonitoring.core.store.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import junit.framework.TestCase;

import org.jmonitoring.core.configuration.Configuration;
import org.jmonitoring.core.dao.FTestExecutionFlowMySqlDAO;
import org.jmonitoring.core.dao.StandAloneConnectionManager;
import org.jmonitoring.core.utils.AspectLoggerEmulator;

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
public class FTestAsynchroneJdbcLogger extends TestCase
{
    private static final int TIME_TO_WAIT = 1000;

    private int mInitialMaxFlowId;

    /**
     * Check the number of toString() method called on the object associated to MethodCall. This is important because of
     * the cost of a toString method on complexe objects.
     * 
     * @throws InterruptedException no doc
     * @throws SQLException no doc
     */
    public void testNbToStringMethodCall() throws InterruptedException, SQLException
    {
        Connection tCon = null;
        int tInitialFlowCount;
        //We check the result into DB
        int tFinalFlowCount;
        try
        {
            tCon = new StandAloneConnectionManager(Configuration.getInstance()).getConnection();
            FTestExecutionFlowMySqlDAO tFTestDao = new FTestExecutionFlowMySqlDAO();
            tInitialFlowCount = tFTestDao.countFlows(tCon);
            tCon.commit();

            MockAbstractAsynchroneLogger.resetNbLog();
            MockAbstractAsynchroneLogger.resetNbPublish();
            AspectLoggerEmulator tEmulator = new AspectLoggerEmulator(new AsynchroneJdbcLogger());
            tEmulator.simulateExecutionFlow();
            Thread.sleep(TIME_TO_WAIT);

            //Now check the number of toString called
            assertEquals(4, AspectLoggerEmulator.Param.getNbToString());
            assertEquals(0, AspectLoggerEmulator.Parent.getNbToString());
            assertEquals(0, AspectLoggerEmulator.Child1.getNbToString());
            assertEquals(0, AspectLoggerEmulator.Child2.getNbToString());

            tFinalFlowCount = tFTestDao.countFlows(tCon);
            tCon.commit();
        } finally
        {
            //Restore configuration
            if (tCon != null)
            {
                tCon.close();
            }
        }
        assertEquals(tInitialFlowCount + 1, tFinalFlowCount);
    }

    /**
     * no doc
     * 
     * @throws Exception no doc
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        Connection tCon = null;
        Statement tStat = null;
        ResultSet tResult = null;
        try
        {
            tCon = new StandAloneConnectionManager(Configuration.getInstance()).getConnection();
            tStat = tCon.createStatement();
            tResult = tStat.executeQuery("Select MAX(ID) As Max from EXECUTION_FLOW");
            tResult.next();
            mInitialMaxFlowId = tResult.getInt("Max");
        } finally
        {
            try
            {
                if (tResult != null)
                {
                    tResult.close();
                }
            } finally
            {
                try
                {
                    if (tStat != null)
                    {
                        tStat.close();
                    }
                } finally
                {
                    if (tCon != null)
                    {
                        tCon.close();
                    }
                }
            }

        }
    }

    /**
     * no doc
     * 
     * @throws Exception no doc
     */
    protected void tearDown() throws Exception
    {
        Connection tCon = null;
        Statement tStat = null;
        try
        {
            tCon = new StandAloneConnectionManager(Configuration.getInstance()).getConnection();
            tStat = tCon.createStatement();
            tStat.executeUpdate("DELETE From METHOD_CALL where EXECUTION_FLOW_ID>" + mInitialMaxFlowId);
            tStat.executeUpdate("DELETE From EXECUTION_FLOW where ID>" + mInitialMaxFlowId);
            tCon.commit();
        } finally
        {
            try
            {
                if (tStat != null)
                {
                    tStat.close();
                }
            } finally
            {
                if (tCon != null)
                {
                    tCon.close();
                }
            }

        }

        super.tearDown();
    }
}
