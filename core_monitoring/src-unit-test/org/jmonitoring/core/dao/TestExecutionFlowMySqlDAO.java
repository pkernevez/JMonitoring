package org.jmonitoring.core.dao;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.jmonitoring.core.configuration.Configuration;
import org.jmonitoring.core.dto.ExecutionFlowDTO;
import org.jmonitoring.core.dto.MethodCall;

/**
 * @author pke
 * 
 * @todo To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 *       Templates
 */
public class TestExecutionFlowMySqlDAO extends PersistanceTestCase
{
    public void testGetListOfExecutionFlow() throws SQLException 
    {
        Connection tCon = mPersistenceManager.connection();

        int tExpectedResult = countFlows(tCon);
        assertEquals(3, tExpectedResult);

    }

    /**
     * Get the list of Flows.
     * 
     * @param pConnection Allow to use a Connection transaction context. This allow to see new created instance or not.
     * @return The number of flows in database.
     * @throws SQLException If an error occures during DB access.
     */
    public int countFlows(Connection pConnection) throws SQLException
    {
        Connection tCon = pConnection;
        Statement tStat = null;
        ResultSet tResultSet = null;
        try
        {

            if (tCon == null)
            {
                tCon = new StandAloneConnectionManager(Configuration.getInstance()).getConnection();
            }

            tStat = tCon.createStatement();

            tResultSet = tStat.executeQuery("Select count(*) From EXECUTION_FLOW");

            tResultSet.next();
            return tResultSet.getInt(1);
        } finally
        {
            try
            {
                if (tResultSet != null)
                {
                    tResultSet.close();
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
                    if (pConnection == null && tCon != null)
                    { // The connection has been created in this method, we only
                        // must close it in this case
                        tCon.close();
                    }
                }
            }

        }

    }

}
