package org.jmonitoring.core.dao;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import junit.framework.AssertionFailedError;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.jmonitoring.core.dto.ExecutionFlowDTO;
import org.jmonitoring.core.dto.MethodCallDTO;
import org.jmonitoring.core.persistence.ExecutionFlowPO;
import org.jmonitoring.core.persistence.HibernateManager;
import org.jmonitoring.core.persistence.MethodCallPO;

/**
 * @author pke
 * 
 * @todo To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 *       Templates
 */
public class TestExecutionFlowMySqlDAO extends PersistanceTestCase
{
    public void testGetListOfExecutionFlowWithoutCriteria()
    {
        int tExpectedResult = countFlows();

        ExecutionFlowMySqlDAO tFlowDAO = new ExecutionFlowMySqlDAO(mPersistenceManager);
        tFlowDAO.insertFullExecutionFlow(buildNewFullFlow());
        tFlowDAO.insertFullExecutionFlow(buildNewFullFlow());
        tFlowDAO.insertFullExecutionFlow(buildNewFullFlow());

        List tFlows = tFlowDAO.getListOfExecutionFlowDTO(new FlowSearchCriterion());
        assertEquals(tExpectedResult + 3, tFlows.size());

    }

    /**
     * @todo Add test with criteria.
     * @throws SQLException
     */
    public void testGetListOfExecutionFlowWithThreadName()
    {
        ExecutionFlowMySqlDAO tFlowDAO = new ExecutionFlowMySqlDAO(mPersistenceManager);
        tFlowDAO.insertFullExecutionFlow(buildNewFullFlow());

        FlowSearchCriterion tCriterion = new FlowSearchCriterion();
        tCriterion.setThreadName("rr");
        assertEquals(0, tFlowDAO.getListOfExecutionFlowDTO(tCriterion).size());

        tCriterion.setThreadName("TEST-main");
        assertEquals(1, tFlowDAO.getListOfExecutionFlowDTO(tCriterion).size());

        tCriterion.setThreadName("TEST");
        assertEquals(1, tFlowDAO.getListOfExecutionFlowDTO(tCriterion).size());
    }

    public void testGetListOfExecutionFlowWithDurationMin()
    {
        ExecutionFlowMySqlDAO tFlowDAO = new ExecutionFlowMySqlDAO(mPersistenceManager);
        tFlowDAO.insertFullExecutionFlow(buildNewFullFlow());

        FlowSearchCriterion tCriterion = new FlowSearchCriterion();
        tCriterion.setDurationMin(10);
        assertEquals(1, tFlowDAO.getListOfExecutionFlowDTO(tCriterion).size());

        tCriterion.setDurationMin(25);
        assertEquals(0, tFlowDAO.getListOfExecutionFlowDTO(tCriterion).size());
    }

    public void testGetListOfExecutionFlowWithBeginDate()
    {
        ExecutionFlowMySqlDAO tFlowDAO = new ExecutionFlowMySqlDAO(mPersistenceManager);
        ExecutionFlowPO tFlow = buildNewFullFlow();
        tFlowDAO.insertFullExecutionFlow(tFlow);
        Date tToday = new Date(tFlow.getBeginTime());
        tToday.setSeconds(0);
        tToday.setMinutes(0);
        tToday.setHours(0);

        FlowSearchCriterion tCriterion = new FlowSearchCriterion();
        // Today
        tCriterion.setBeginDate(tToday);
        assertEquals(1, tFlowDAO.getListOfExecutionFlowDTO(tCriterion).size());

        // Yesterday
        tCriterion.setBeginDate(new Date(tToday.getTime() - ExecutionFlowMySqlDAO.ONE_DAY));
        assertEquals(0, tFlowDAO.getListOfExecutionFlowDTO(tCriterion).size());

        // Tomorrow
        tCriterion.setBeginDate(new Date(tToday.getTime() + ExecutionFlowMySqlDAO.ONE_DAY));
        assertEquals(0, tFlowDAO.getListOfExecutionFlowDTO(tCriterion).size());
    }

    public void testGetListOfExecutionFlowWithGroupName()
    {
        ExecutionFlowMySqlDAO tFlowDAO = new ExecutionFlowMySqlDAO(mPersistenceManager);
        ExecutionFlowPO tFlow = buildNewFullFlow();
        tFlowDAO.insertFullExecutionFlow(tFlow);

        FlowSearchCriterion tCriterion = new FlowSearchCriterion();
        tCriterion.setGroupName("GrDefault");
        assertEquals(1, tFlowDAO.getListOfExecutionFlowDTO(tCriterion).size());

        tCriterion.setGroupName("GrDefa");
        assertEquals(1, tFlowDAO.getListOfExecutionFlowDTO(tCriterion).size());

        tCriterion.setGroupName("toto");
        assertEquals(0, tFlowDAO.getListOfExecutionFlowDTO(tCriterion).size());
        // Todo : groupName, className et methodName
    }

    public void testGetListOfExecutionFlowWithClassName()
    {
        ExecutionFlowMySqlDAO tFlowDAO = new ExecutionFlowMySqlDAO(mPersistenceManager);
        ExecutionFlowPO tFlow = buildNewFullFlow();
        tFlowDAO.insertFullExecutionFlow(tFlow);
        FlowSearchCriterion tCriterion = new FlowSearchCriterion();
        String tClassName = TestExecutionFlowMySqlDAO.class.getName();
        tCriterion.setClassName(tClassName);
        assertEquals(1, tFlowDAO.getListOfExecutionFlowDTO(tCriterion).size());

        tCriterion.setClassName(tClassName.substring(0, 3));
        assertEquals(1, tFlowDAO.getListOfExecutionFlowDTO(tCriterion).size());

        tCriterion.setClassName("toto");
        assertEquals(0, tFlowDAO.getListOfExecutionFlowDTO(tCriterion).size());
        // Todo : groupName, className et methodName
    }

    public void testGetListOfExecutionFlowWithMethodName()
    {
        ExecutionFlowMySqlDAO tFlowDAO = new ExecutionFlowMySqlDAO(mPersistenceManager);
        ExecutionFlowPO tFlow = buildNewFullFlow();
        tFlowDAO.insertFullExecutionFlow(tFlow);
        FlowSearchCriterion tCriterion = new FlowSearchCriterion();

        tCriterion.setMethodName("builNewFullFlow");
        assertEquals(1, tFlowDAO.getListOfExecutionFlowDTO(tCriterion).size());

        tCriterion.setMethodName("builNewFull");
        assertEquals(1, tFlowDAO.getListOfExecutionFlowDTO(tCriterion).size());

        tCriterion.setMethodName("toto");
        assertEquals(0, tFlowDAO.getListOfExecutionFlowDTO(tCriterion).size());
    }

    public void testCountOk() throws SQLException
    {
        assertEquals(0, countFlows());
    }

    /**
     * Get the list of Flows.
     * 
     * @return The number of flows in database.
     * @throws SQLException If an error occures during DB access.
     */
    public int countFlows()
    {
        SQLQuery tQuery = mPersistenceManager.createSQLQuery("Select Count(*) as myCount From EXECUTION_FLOW");
        Object tResult = tQuery.addScalar("myCount", Hibernate.INTEGER).list().get(0);
        return ((Integer) tResult).intValue();
    }

    public void testDeleteOneFlows() throws SQLException
    {
        ExecutionFlowMySqlDAO tFlowDAO = new ExecutionFlowMySqlDAO(mPersistenceManager);
        int tNbFlow = countFlows();

        // First instert a flow
        ExecutionFlowPO tFlow = buildNewFullFlow();
        int tFlowId = tFlowDAO.insertFullExecutionFlow(tFlow);
        int tNewNbFlow = countFlows();
        assertEquals(tNbFlow + 1, tNewNbFlow);

        tFlowDAO.deleteFlow(tFlowId);
        mPersistenceManager.flush();
        tNewNbFlow = countFlows();
        assertEquals(tNbFlow, tNewNbFlow);

        // Now we restore the DB
        tNewNbFlow = countFlows();
        assertEquals(tNbFlow, tNewNbFlow);

    }

    // Supprimé car il n'y a pas de transaction sur les TRUNCATE ==> impossible
    // de laisser la base
    // dans l'état initial
    // public void testDeleteAllFlows() throws SQLException
    // {
    // int tOldResult = countFlows();
    //
    // Connection tCon = null;
    // try
    // {
    // tCon = new StandAloneConnectionManager().getConnection();
    // ExecutionFlowDTOMySqlDAO tFlowDAO = new ExecutionFlowDTOMySqlDAO(tCon);
    // tFlowDAO.deleteAllFlows();
    //
    // int tNewResult = countFlows();
    // assertEquals(0, tNewResult);
    //
    // tCon.rollback();
    // tNewResult = countFlows();
    // // Plus de test sur le rollback car les opérations Truncate sont en
    // // dehors des transactions
    // // assertEquals(tOldResult, tNewResult);
    //
    // } catch (AssertionFailedError e)
    // {
    // throw e;
    // } catch (Throwable e)
    // {
    // e.printStackTrace();
    // fail(e.getMessage());
    // } finally
    // {
    // if (tCon != null)
    // {
    // tCon.close();
    // }
    // }
    //
    // }

    public void testInsertNewFlows() throws SQLException
    {
        int tOldResult = countFlows();

        ExecutionFlowMySqlDAO tFlowDAO = new ExecutionFlowMySqlDAO(mPersistenceManager);

        ExecutionFlowPO tFlow = buildNewFullFlow();
        tFlowDAO.insertFullExecutionFlow(tFlow);

        int tNewResult = countFlows();
        assertEquals(tOldResult + 1, tNewResult);
    }

    public void testInsertMethodCall() throws SQLException
    {
        int tOldResult = countMethods();

        MethodCallPO tMethodCall = new MethodCallPO(null, TestExecutionFlowMySqlDAO.class.getName(), "builNewFullFlow",
                        "GrDefault", new Object[0]);
        mPersistenceManager.save(tMethodCall);

        int tNewResult = countMethods();
        assertEquals(tOldResult + 1, tNewResult);
    }

    public void testInsertMethodCalls() throws SQLException
    {
        int tOldResult = countMethods();

        MethodCallPO tMethodCall1 = new MethodCallPO(null, TestExecutionFlowMySqlDAO.class.getName(),
                        "builNewFullFlow", "GrDefault", new Object[0]);
        MethodCallPO tMethodCall2 = new MethodCallPO(null, TestExecutionFlowMySqlDAO.class.getName(),
                        "builNewFullFlow", "GrDefault", new Object[0]);
        MethodCallPO tMethodCall3 = new MethodCallPO(null, TestExecutionFlowMySqlDAO.class.getName(),
                        "builNewFullFlow", "GrDefault", new Object[0]);
        tMethodCall1.addChildren(tMethodCall2);
        tMethodCall1.addChildren(tMethodCall3);

        mPersistenceManager.save(tMethodCall1);
        mPersistenceManager.flush();
        int tNewResult = countMethods();
        assertEquals(tOldResult + 3, tNewResult);
        assertEquals(1, tMethodCall1.getId());
        assertEquals(2, tMethodCall2.getId());
        assertEquals(3, tMethodCall3.getId());

    }

    public void testLinkedMethodCalls() throws SQLException
    {
        int tOldResult = countMethods();

        MethodCallPO tMethodCall1 = new MethodCallPO(null, TestExecutionFlowMySqlDAO.class.getName(),
                        "builNewFullFlow", "GrDefault", new Object[0]);
        assertEquals(-1, tMethodCall1.getId());
        mPersistenceManager.save(tMethodCall1);
        mPersistenceManager.flush();
        assertEquals(1, tMethodCall1.getId());

        MethodCallPO tMethodCall2 = new MethodCallPO(null, TestExecutionFlowMySqlDAO.class.getName(),
                        "builNewFullFlow", "GrDefault", new Object[0]);
        assertEquals(-1, tMethodCall2.getId());
        mPersistenceManager.save(tMethodCall2);
        mPersistenceManager.flush();
        assertEquals(1, tMethodCall1.getId());
        assertEquals(2, tMethodCall2.getId());

        assertEquals(0, countMethodsWithParent());
        tMethodCall1.addChildren(tMethodCall2);
        mPersistenceManager.save(tMethodCall1);
        mPersistenceManager.flush();
        assertEquals(1, tMethodCall1.getId());
        assertEquals(2, tMethodCall2.getId());

        assertEquals(1, countMethodsWithParent());

    }

    private int countMethods()
    {
        SQLQuery tQuery = mPersistenceManager.createSQLQuery("Select Count(*) as myCount From METHOD_CALL");
        Object tResult = tQuery.addScalar("myCount", Hibernate.INTEGER).list().get(0);
        return ((Integer) tResult).intValue();
    }

    private int countMethodsWithParent()
    {
        SQLQuery tQuery = mPersistenceManager
                        .createSQLQuery("Select Count(*) as myCount From METHOD_CALL Where PARENT_ID IS NOT NULL");
        Object tResult = tQuery.addScalar("myCount", Hibernate.INTEGER).list().get(0);
        return ((Integer) tResult).intValue();
    }

    public static ExecutionFlowPO buildNewFullFlow()
    {
        ExecutionFlowPO tFlow;
        MethodCallPO tPoint;
        MethodCallPO tSubPoint;
        long tStartTime = System.currentTimeMillis();

        tPoint = new MethodCallPO(null, TestExecutionFlowMySqlDAO.class.getName(), "builNewFullFlow", "GrDefault",
                        new Object[0]);
        tPoint.setBeginTime(tStartTime);

        tSubPoint = new MethodCallPO(tPoint, TestExecutionFlowMySqlDAO.class.getName(), "builNewFullFlow2", "GrChild1",
                        new Object[0]);
        tSubPoint.setEndTime(System.currentTimeMillis());

        tSubPoint = new MethodCallPO(tPoint, TestExecutionFlowMySqlDAO.class.getName(), "builNewFullFlow3", "GrChild2",
                        new Object[0]);
        tPoint.setEndTime(tStartTime + 20);
        tFlow = new ExecutionFlowPO("TEST-main", tPoint, "myJVM");
        return tFlow;
    }

    /**
     * @todo S'assurer qu'une méthode VOID est bien loguée comme une méthode void.
     */
    public void testGetListOfMethodCall()
    {
        int tOldResult = countFlows();
        int tOldResultM = countMethods();

        ExecutionFlowMySqlDAO tFlowDAO = new ExecutionFlowMySqlDAO(mPersistenceManager);

        // First insert the new Flow in DB.
        ExecutionFlowPO tInitialFlow = buildNewFullFlow();
        int tId = tFlowDAO.insertFullExecutionFlow(tInitialFlow);
        // Check the insertion
        int tNewResult = countFlows();
        assertEquals(tOldResult + 1, tNewResult);

        mPersistenceManager.flush();
        mPersistenceManager.clear();

        assertEquals(tOldResultM + 3, countMethods());

        ExecutionFlowPO tReadFlow = tFlowDAO.readFullExecutionFlow(tId);
        assertNotSame(tInitialFlow, tReadFlow);

        // Check the equality of the Flow
        assertEquals(tInitialFlow.getJvmIdentifier(), tReadFlow.getJvmIdentifier());
        assertEquals(tInitialFlow.getThreadName(), tReadFlow.getThreadName());
        assertEquals(tInitialFlow.getBeginTime(), tReadFlow.getBeginTime());
        assertEquals(tInitialFlow.getDuration(), tReadFlow.getDuration());
        assertEquals(tInitialFlow.getEndTime(), tReadFlow.getEndTime());

        // Check equality of the first measure point
        MethodCallPO tInitialPoint = tInitialFlow.getFirstMethodCall();
        MethodCallPO tReadPoint = tReadFlow.getFirstMethodCall();
        assertEquals(tInitialPoint.getClassName(), tReadPoint.getClassName());
        assertEquals(tInitialPoint.getMethodName(), tReadPoint.getMethodName());
        assertEquals("[]", tReadPoint.getParams());
        assertEquals(tInitialPoint.getReturnValue(), tReadPoint.getReturnValue());
        assertEquals(tInitialPoint.getThrowableClass(), tReadPoint.getThrowableClass());
        assertEquals(tInitialPoint.getThrowableMessage(), tReadPoint.getThrowableMessage());
        assertEquals(tInitialPoint.getBeginTime(), tReadPoint.getBeginTime());
        assertEquals(tInitialPoint.getEndTime(), tReadPoint.getEndTime());
        assertEquals(tInitialPoint.getGroupName(), tReadPoint.getGroupName());

        // Check equality of the first child measure point
        assertEquals(2, tInitialPoint.getChildren().size());
        assertEquals(2, tReadPoint.getChildren().size());
        tInitialPoint = (MethodCallPO) tInitialPoint.getChildren().get(0);
        tReadPoint = (MethodCallPO) tReadPoint.getChildren().get(0);
        assertEquals(tInitialPoint.getClassName(), tReadPoint.getClassName());
        assertEquals(tInitialPoint.getMethodName(), tReadPoint.getMethodName());
        assertEquals("[]", tReadPoint.getParams());
        assertEquals(tInitialPoint.getReturnValue(), tReadPoint.getReturnValue());
        assertEquals(tInitialPoint.getThrowableClass(), tReadPoint.getThrowableClass());
        assertEquals(tInitialPoint.getThrowableMessage(), tReadPoint.getThrowableMessage());
        assertEquals(tInitialPoint.getBeginTime(), tReadPoint.getBeginTime());
        assertEquals(tInitialPoint.getEndTime(), tReadPoint.getEndTime());
        assertEquals(tInitialPoint.getGroupName(), tReadPoint.getGroupName());

        // Check equality of the second child measure point
        tInitialPoint = (MethodCallPO) tInitialPoint.getParentMethodCall().getChildren().get(1);
        tReadPoint = (MethodCallPO) tReadPoint.getParentMethodCall().getChildren().get(1);
        assertEquals(tInitialPoint.getClassName(), tReadPoint.getClassName());
        assertEquals(tInitialPoint.getMethodName(), tReadPoint.getMethodName());
        assertEquals("[]", tReadPoint.getParams());
        assertEquals(tInitialPoint.getReturnValue(), tReadPoint.getReturnValue());
        assertEquals(tInitialPoint.getThrowableClass(), tReadPoint.getThrowableClass());
        assertEquals(tInitialPoint.getThrowableMessage(), tReadPoint.getThrowableMessage());
        assertEquals(tInitialPoint.getBeginTime(), tReadPoint.getBeginTime());
        assertEquals(tInitialPoint.getEndTime(), tReadPoint.getEndTime());
        assertEquals(tInitialPoint.getGroupName(), tReadPoint.getGroupName());

    }

    public void testGetListOfMethodCallBis() throws SQLException
    {
        ExecutionFlowMySqlDAO tFlowDAO = new ExecutionFlowMySqlDAO(mPersistenceManager);

        // First delete all flow, we don't use the DeleteAll Method of the
        // Dao Object because, it doesn't support transactions.
        mPersistenceManager.createQuery("Delete FROM MethodCallPO").executeUpdate();
        mPersistenceManager.createQuery("Delete FROM ExecutionFlowPO").executeUpdate();

        // Now insert the TestFlow
        ExecutionFlowPO tFlow = buildNewFullFlow();
        int tFlowId = tFlowDAO.insertFullExecutionFlow(tFlow);

        List tMeasureExtracts = tFlowDAO.getListOfMeasure();
        MeasureExtract curExtrat = (MeasureExtract) tMeasureExtracts.get(0);
        assertEquals("org.jmonitoring.core.dao.TestExecutionFlowMySqlDAO.builNewFullFlow", curExtrat.getName());
        assertEquals("GrDefault", curExtrat.getGroupName());
        assertEquals(1, curExtrat.getOccurenceNumber());

        curExtrat = (MeasureExtract) tMeasureExtracts.get(1);
        assertEquals("org.jmonitoring.core.dao.TestExecutionFlowMySqlDAO.builNewFullFlow2", curExtrat.getName());
        assertEquals("GrChild1", curExtrat.getGroupName());
        assertEquals(1, curExtrat.getOccurenceNumber());

        curExtrat = (MeasureExtract) tMeasureExtracts.get(2);
        assertEquals("org.jmonitoring.core.dao.TestExecutionFlowMySqlDAO.builNewFullFlow3", curExtrat.getName());
        assertEquals("GrChild2", curExtrat.getGroupName());
        assertEquals(1, curExtrat.getOccurenceNumber());

    }

}
