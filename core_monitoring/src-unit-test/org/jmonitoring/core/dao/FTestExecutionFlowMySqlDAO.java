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
public class FTestExecutionFlowMySqlDAO extends TestCase
{
    public void testGetListOfExecutionFlow() throws SQLException
    {
        int tExpectedResult = countFlows(null);
        Connection tCon = null;
        try
        {
            tCon = new StandAloneConnectionManager(Configuration.getInstance()).getConnection();
            ExecutionFlowMySqlDAO tFlowDAO = new ExecutionFlowMySqlDAO(tCon);
            ExecutionFlowDTO[] tFlows = tFlowDAO.getListOfExecutionFlowDTO(new FlowSearchCriterion());

            assertEquals(tExpectedResult, tFlows.length);
        } finally
        {
            if (tCon != null)
            {
                tCon.close();
            }
        }

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

    public void testDeleteOneFlows() throws SQLException
    {
        int tOldResult = countFlows(null);

        Connection tCon = null;
        try
        {
            tCon = new StandAloneConnectionManager(Configuration.getInstance()).getConnection();
            ExecutionFlowMySqlDAO tFlowDAO = new ExecutionFlowMySqlDAO(tCon);
            int tNbFlow = countFlows(tCon);

            //First instert a flow
            ExecutionFlowDTO tFlow = buildNewFullFlow();
            int tFlowId = tFlowDAO.insertFullExecutionFlow(tFlow);
            int tNewNbFlow = countFlows(tCon);
            assertEquals(tNbFlow + 1, tNewNbFlow);

            tFlowDAO.deleteFlow(tFlowId);
            tNewNbFlow = countFlows(tCon);
            assertEquals(tNbFlow, tNewNbFlow);

            //Now we restore the DB
            tCon.rollback();
            tNewNbFlow = countFlows(null);
            assertEquals(tNbFlow, tNewNbFlow);

        } catch (AssertionFailedError e)
        {
            throw e;
        } catch (Throwable e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        } finally
        {
            if (tCon != null)
            {
                tCon.close();
            }
        }

    }

    // Supprimé car il n'y a pas de transaction sur les TRUNCATE ==> impossible
    // de laisser la base
    //    dans l'état initial
    //    public void testDeleteAllFlows() throws SQLException
    //    {
    //        int tOldResult = countFlows(null);
    //
    //        Connection tCon = null;
    //        try
    //        {
    //            tCon = new StandAloneConnectionManager().getConnection();
    //            ExecutionFlowDTOMySqlDAO tFlowDAO = new ExecutionFlowDTOMySqlDAO(tCon);
    //            tFlowDAO.deleteAllFlows();
    //
    //            int tNewResult = countFlows(tCon);
    //            assertEquals(0, tNewResult);
    //
    //            tCon.rollback();
    //            tNewResult = countFlows(null);
    //            // Plus de test sur le rollback car les opérations Truncate sont en
    //            // dehors des transactions
    //            // assertEquals(tOldResult, tNewResult);
    //
    //        } catch (AssertionFailedError e)
    //        {
    //            throw e;
    //        } catch (Throwable e)
    //        {
    //            e.printStackTrace();
    //            fail(e.getMessage());
    //        } finally
    //        {
    //            if (tCon != null)
    //            {
    //                tCon.close();
    //            }
    //        }
    //
    //    }

    public void testInsertNewFlows() throws SQLException
    {
        int tOldResult = countFlows(null);

        Connection tCon = null;
        try
        {
            tCon = new StandAloneConnectionManager(Configuration.getInstance()).getConnection();
            ExecutionFlowMySqlDAO tFlowDAO = new ExecutionFlowMySqlDAO(tCon);

            ExecutionFlowDTO tFlow = buildNewFullFlow();
            tFlowDAO.insertFullExecutionFlow(tFlow);

            int tNewResult = countFlows(tCon);
            assertEquals(tOldResult + 1, tNewResult);

            tCon.rollback();
            tNewResult = countFlows(null);
            assertEquals(tOldResult, tNewResult);

        } catch (AssertionFailedError e1)
        {
            throw e1;
        } catch (Throwable e)
        {
            fail(e.getMessage());
            e.printStackTrace();
        } finally
        {
            if (tCon != null)
            {
                tCon.close();
            }
        }

    }

    /**
     * @return
     */
    public static ExecutionFlowDTO buildNewFullFlow()
    {
        ExecutionFlowDTO tFlow;
        MethodCall tPoint;

        tPoint = new MethodCall(null, FTestExecutionFlowMySqlDAO.class.getName(), "builNewFullFlow", "GrDefault",
                        new Object[0]);
        //This local variable is indireclty used by its parent
        new MethodCall(tPoint, FTestExecutionFlowMySqlDAO.class.getName(), "builNewFullFlow2", "GrChild1",
                        new Object[0]);
        //This local variable is indireclty used by its parent
        new MethodCall(tPoint, FTestExecutionFlowMySqlDAO.class.getName(), "builNewFullFlow3", "GrChild2",
                        new Object[0]);
        tFlow = new ExecutionFlowDTO("TEST-main", tPoint, "myJVM");
        return tFlow;
    }

    public void testGetListOfMeasurePoint() throws SQLException
    {
        int tOldResult = countFlows(null);

        Connection tCon = null;
        try
        {
            tCon = new StandAloneConnectionManager(Configuration.getInstance()).getConnection();
            ExecutionFlowMySqlDAO tFlowDAO = new ExecutionFlowMySqlDAO(tCon);

            // First insert the new Flow in DB.
            ExecutionFlowDTO tInitialFlow = buildNewFullFlow();
            int tId = tFlowDAO.insertFullExecutionFlow(tInitialFlow);
            // Check the insertion
            int tNewResult = countFlows(tCon);
            assertEquals(tOldResult + 1, tNewResult);

            // Now check the read of the full Flows
            ExecutionFlowDTO tReadFlow = tFlowDAO.readFullExecutionFlowDTO(tId);

            // Check the equality of the Flow
            assertEquals(tInitialFlow.getJvmIdentifier(), tReadFlow.getJvmIdentifier());
            assertEquals(tInitialFlow.getThreadName(), tReadFlow.getThreadName());
            assertEquals(tInitialFlow.getBeginDate(), tReadFlow.getBeginDate());
            assertEquals(tInitialFlow.getDuration(), tReadFlow.getDuration());
            assertEquals(tInitialFlow.getEndTime(), tReadFlow.getEndTime());

            // Check equality of the first measure point
            MethodCall tInitialPoint = tInitialFlow.getFirstMeasure();
            MethodCall tReadPoint = tReadFlow.getFirstMeasure();
            assertEquals(tInitialPoint.getClassName(), tReadPoint.getClassName());
            assertEquals(tInitialPoint.getMethodName(), tReadPoint.getMethodName());
            assertEquals("[]", tReadPoint.getParams());
            assertEquals(null, tInitialPoint.getReturnValue());
            assertEquals("void", tReadPoint.getReturnValue());
            assertEquals(tInitialPoint.getThrowableClassName(), tReadPoint.getThrowableClassName());
            assertEquals(tInitialPoint.getThrowableMessage(), tReadPoint.getThrowableMessage());
            assertEquals(tInitialPoint.getBeginTime(), tReadPoint.getBeginTime());
            assertEquals(tInitialPoint.getEndTime(), tReadPoint.getEndTime());
            assertEquals(tInitialPoint.getGroupName(), tReadPoint.getGroupName());

            // Check equality of the first child measure point
            tInitialPoint = (MethodCall) tInitialPoint.getChildren().get(0);
            tReadPoint = (MethodCall) tReadPoint.getChildren().get(0);
            assertEquals(tInitialPoint.getClassName(), tReadPoint.getClassName());
            assertEquals(tInitialPoint.getMethodName(), tReadPoint.getMethodName());
            assertEquals("[]", tReadPoint.getParams());
            assertEquals(null, tInitialPoint.getReturnValue());
            assertEquals("void", tReadPoint.getReturnValue());
            assertEquals(tInitialPoint.getThrowableClassName(), tReadPoint.getThrowableClassName());
            assertEquals(tInitialPoint.getThrowableMessage(), tReadPoint.getThrowableMessage());
            assertEquals(tInitialPoint.getBeginTime(), tReadPoint.getBeginTime());
            assertEquals(tInitialPoint.getEndTime(), tReadPoint.getEndTime());
            assertEquals(tInitialPoint.getGroupName(), tReadPoint.getGroupName());

            // Check equality of the second child measure point
            tInitialPoint = (MethodCall) tInitialPoint.getParent().getChildren().get(1);
            tReadPoint = (MethodCall) tReadPoint.getParent().getChildren().get(1);
            assertEquals(tInitialPoint.getClassName(), tReadPoint.getClassName());
            assertEquals(tInitialPoint.getMethodName(), tReadPoint.getMethodName());
            assertEquals("[]", tReadPoint.getParams());
            assertEquals(null, tInitialPoint.getReturnValue());
            assertEquals("void", tReadPoint.getReturnValue());
            assertEquals(tInitialPoint.getThrowableClassName(), tReadPoint.getThrowableClassName());
            assertEquals(tInitialPoint.getThrowableMessage(), tReadPoint.getThrowableMessage());
            assertEquals(tInitialPoint.getBeginTime(), tReadPoint.getBeginTime());
            assertEquals(tInitialPoint.getEndTime(), tReadPoint.getEndTime());
            assertEquals(tInitialPoint.getGroupName(), tReadPoint.getGroupName());

            tNewResult = countFlows(null);
            assertEquals(tOldResult, tNewResult);

            // } catch (Throwable e)
            // {
            // e.printStackTrace();
            // fail(e.getMessage());
        } finally
        {
            if (tCon != null)
            {
                // Don't update database with this flow
                tCon.rollback();
                tCon.close();
            }
        }

    }

    public void testGetListOfMeasure() throws SQLException
    {
        int tOldResult = countFlows(null);

        Connection tCon = null;
        Statement tStat = null;
        try
        {
            tCon = new StandAloneConnectionManager(Configuration.getInstance()).getConnection();
            ExecutionFlowMySqlDAO tFlowDAO = new ExecutionFlowMySqlDAO(tCon);
            int tNbFlow = countFlows(tCon);

            //First delete all flow, we don't use the DeleteAll Method of the
            // Dao Object because, it doesn't support transactions.
            tStat = tCon.createStatement();
            tStat.execute("Delete FROM method_call");
            tStat.execute("Delete FROM execution_flow");

            //Now insert the TestFlow
            ExecutionFlowDTO tFlow = buildNewFullFlow();
            int tFlowId = tFlowDAO.insertFullExecutionFlow(tFlow);

            List tMeasureExtracts = tFlowDAO.getListOfMeasure();
            MeasureExtract curExtrat = (MeasureExtract) tMeasureExtracts.get(0);
            assertEquals("org.jmonitoring.core.dao.FTestExecutionFlowMySqlDAO.builNewFullFlow", curExtrat.getName());
            assertEquals("GrDefault", curExtrat.getGroupName());
            assertEquals(1, curExtrat.getOccurenceNumber());

            curExtrat = (MeasureExtract) tMeasureExtracts.get(1);
            assertEquals("org.jmonitoring.core.dao.FTestExecutionFlowMySqlDAO.builNewFullFlow2", curExtrat.getName());
            assertEquals("GrChild1", curExtrat.getGroupName());
            assertEquals(1, curExtrat.getOccurenceNumber());

            curExtrat = (MeasureExtract) tMeasureExtracts.get(2);
            assertEquals("org.jmonitoring.core.dao.FTestExecutionFlowMySqlDAO.builNewFullFlow3", curExtrat.getName());
            assertEquals("GrChild2", curExtrat.getGroupName());
            assertEquals(1, curExtrat.getOccurenceNumber());

            //Now we restore the DB
            tCon.rollback();
            int tNewNbFlow = countFlows(null);
            assertEquals(tNbFlow, tNewNbFlow);

        } catch (AssertionFailedError e)
        {
            throw e;
        } catch (Throwable e)
        {
            e.printStackTrace();
            fail(e.getMessage());
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
