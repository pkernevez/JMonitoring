package org.jmonitoring.core.dao;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/


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
public class TestExecutionFlowDAO extends PersistanceTestCase
{

    public void testCountOk() 
    {
        assertEquals(0, new ExecutionFlowDAO(mPersistenceManager).countFlows());
    }

    public void testInsertNewFlows() 
    {
                ExecutionFlowDAO tFlowDAO = new ExecutionFlowDAO(mPersistenceManager);
                int tOldResult = tFlowDAO.countFlows();


        ExecutionFlowPO tFlow = buildNewFullFlow();
        tFlowDAO.insertFullExecutionFlow(tFlow);

        int tNewResult = tFlowDAO.countFlows();
        assertEquals(tOldResult + 1, tNewResult);
    }

    public void testInsertMethodCall() 
    {
        int tOldResult = countMethods();

        MethodCallPO tMethodCall = new MethodCallPO(null, TestExecutionFlowDAO.class.getName(), "builNewFullFlow",
                        "GrDefault", new Object[0]);
        mPersistenceManager.save(tMethodCall);

        int tNewResult = countMethods();
        assertEquals(tOldResult + 1, tNewResult);
    }

    public void testInsertMethodCalls() 
    {
        int tOldResult = countMethods();

        MethodCallPO tMethodCall1 = new MethodCallPO(null, TestExecutionFlowDAO.class.getName(), "builNewFullFlow",
                        "GrDefault", new Object[0]);
        MethodCallPO tMethodCall2 = new MethodCallPO(null, TestExecutionFlowDAO.class.getName(), "builNewFullFlow",
                        "GrDefault", new Object[0]);
        MethodCallPO tMethodCall3 = new MethodCallPO(null, TestExecutionFlowDAO.class.getName(), "builNewFullFlow",
                        "GrDefault", new Object[0]);
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

    public void testLinkedMethodCalls() 
    {
        int tOldResult = countMethods();

        MethodCallPO tMethodCall1 = new MethodCallPO(null, TestExecutionFlowDAO.class.getName(), "builNewFullFlow",
                        "GrDefault", new Object[0]);
        assertEquals(-1, tMethodCall1.getId());
        mPersistenceManager.save(tMethodCall1);
        mPersistenceManager.flush();
        assertEquals(1, tMethodCall1.getId());

        MethodCallPO tMethodCall2 = new MethodCallPO(null, TestExecutionFlowDAO.class.getName(), "builNewFullFlow",
                        "GrDefault", new Object[0]);
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

        tPoint = new MethodCallPO(null, TestExecutionFlowDAO.class.getName(), "builNewFullFlow", "GrDefault",
                        new Object[0]);
        tPoint.setBeginTime(tStartTime);

        tSubPoint = new MethodCallPO(tPoint, TestExecutionFlowDAO.class.getName(), "builNewFullFlow2", "GrChild1",
                        new Object[0]);
        tSubPoint.setEndTime(System.currentTimeMillis());

        tSubPoint = new MethodCallPO(tPoint, TestExecutionFlowDAO.class.getName(), "builNewFullFlow3", "GrChild2",
                        new Object[0]);
        tPoint.setEndTime(tStartTime + 20);
        tFlow = new ExecutionFlowPO("TEST-main", tPoint, "myJVM");
        return tFlow;
    }

    /**
     * @todo S'assurer qu'une méthode VOID est bien loguée comme une méthode void.
     */
    public void testGetMethodCallOfTheFlow()
    {
        ExecutionFlowDAO tFlowDAO = new ExecutionFlowDAO(mPersistenceManager);

        int tOldResult = tFlowDAO.countFlows();
        int tOldResultM = countMethods();

        // First insert the new Flow in DB.
        ExecutionFlowPO tInitialFlow = buildNewFullFlow();
        int tId = tFlowDAO.insertFullExecutionFlow(tInitialFlow);
        // Check the insertion
        int tNewResult = tFlowDAO.countFlows();
        assertEquals(tOldResult + 1, tNewResult);

        mPersistenceManager.flush();
        mPersistenceManager.clear();

        assertEquals(tOldResultM + 3, countMethods());

        ExecutionFlowPO tReadFlow = tFlowDAO.readExecutionFlow(tId);
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

    public void testReadMethodCall()
    {
        ExecutionFlowDAO tFlowDAO = new ExecutionFlowDAO(mPersistenceManager);
        ExecutionFlowPO tFlow = buildNewFullFlow();
        tFlowDAO.insertFullExecutionFlow(tFlow);
        mPersistenceManager.flush();
        mPersistenceManager.clear();
        
        MethodCallPO tInitialPoint =(MethodCallPO)tFlow.getFirstMethodCall().getChildren().get(0); 
        MethodCallPO tReadPoint = tFlowDAO.readMethodCall(tInitialPoint.getId());
        assertNotSame(tInitialPoint, tReadPoint);
        
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
    
    public void testGetListOfMethodCallExtract() 
    {
        ExecutionFlowDAO tFlowDAO = new ExecutionFlowDAO(mPersistenceManager);

        // First delete all flow, we don't use the DeleteAll Method of the
        // Dao Object because, it doesn't support transactions.
        mPersistenceManager.createQuery("Delete FROM MethodCallPO").executeUpdate();
        mPersistenceManager.createQuery("Delete FROM ExecutionFlowPO").executeUpdate();

        // Now insert the TestFlow
        ExecutionFlowPO tFlow = buildNewFullFlow();
        int tFlowId = tFlowDAO.insertFullExecutionFlow(tFlow);

        List tMeasureExtracts = tFlowDAO.getListOfMethodCallExtract();
        MethodCallExtract curExtrat = (MethodCallExtract) tMeasureExtracts.get(0);
        assertEquals("org.jmonitoring.core.dao.TestExecutionFlowDAO.builNewFullFlow", curExtrat.getName());
        assertEquals("GrDefault", curExtrat.getGroupName());
        assertEquals(1, curExtrat.getOccurenceNumber());

        curExtrat = (MethodCallExtract) tMeasureExtracts.get(1);
        assertEquals("org.jmonitoring.core.dao.TestExecutionFlowDAO.builNewFullFlow2", curExtrat.getName());
        assertEquals("GrChild1", curExtrat.getGroupName());
        assertEquals(1, curExtrat.getOccurenceNumber());

        curExtrat = (MethodCallExtract) tMeasureExtracts.get(2);
        assertEquals("org.jmonitoring.core.dao.TestExecutionFlowDAO.builNewFullFlow3", curExtrat.getName());
        assertEquals("GrChild2", curExtrat.getGroupName());
        assertEquals(1, curExtrat.getOccurenceNumber());

    }

}
