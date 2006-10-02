package org.jmonitoring.core.dao;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.SessionStatistics;
import org.hibernate.stat.Statistics;
import org.jmonitoring.core.dto.MethodCallExtractDTO;
import org.jmonitoring.core.persistence.ExecutionFlowPO;
import org.jmonitoring.core.persistence.HibernateManager;
import org.jmonitoring.core.persistence.MethodCallPK;
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
        assertEquals(0, new ExecutionFlowDAO(getSession()).countFlows());
        assertEquals(0, countMethods());
    }

    public void testInsertSimpleFlow()
    {
        ExecutionFlowDAO tFlowDAO = new ExecutionFlowDAO(getSession());
        int tOldNbFlow = tFlowDAO.countFlows();
        int tOldNbMethodCall = countMethods();

        ExecutionFlowPO tFlow = buildNewSimpleFlow();
        tFlowDAO.insertFullExecutionFlow(tFlow);
        getSession().flush();

        int tNewNbFlow = tFlowDAO.countFlows();
        int tNewNbMethodCall = countMethods();
        assertEquals(tOldNbFlow + 1, tNewNbFlow);
        assertEquals(tOldNbMethodCall + 1, tNewNbMethodCall);
    }

    public static ExecutionFlowPO buildNewSimpleFlow()
    {
        ExecutionFlowPO tFlow;
        MethodCallPO tPoint;
        long tStartTime = System.currentTimeMillis();

        tPoint = new MethodCallPO(null, TestExecutionFlowDAO.class.getName(), "builNewFullFlow", "GrDefault",
            new Object[0]);
        tPoint.setBeginTime(tStartTime);

        tPoint.setEndTime(tStartTime + 20);
        tFlow = new ExecutionFlowPO("TEST-main", tPoint, "myJVM");
        return tFlow;
    }

    public void testInsertNewFlows()
    {
        ExecutionFlowDAO tFlowDAO = new ExecutionFlowDAO(getSession());
        int tOldNbFlow = tFlowDAO.countFlows();
        int tOldNbMeth = countMethods();

        ExecutionFlowPO tFlow = buildNewFullFlow();
        tFlowDAO.insertFullExecutionFlow(tFlow);
        getSession().flush();
        int tNewNbFlow = tFlowDAO.countFlows();
        int tNewNbMeth = countMethods();
        assertEquals(tOldNbFlow + 1, tNewNbFlow);
        assertEquals(tOldNbMeth + 3, tNewNbMeth);

        MethodCallPO curMeth = tFlow.getFirstMethodCall();
        assertEquals(tFlow.getId(), curMeth.getFlow().getId());
        assertEquals(tFlow.getId(), curMeth.getChild(0).getFlow().getId());
        assertEquals(tFlow.getId(), curMeth.getChild(1).getFlow().getId());
    }

    public void testInsertMethodCall()
    {
        int tOldResult = countMethods();

        ExecutionFlowPO tFlow = new ExecutionFlowPO();
        getSession().save(tFlow);
        MethodCallPO tMethodCall = new MethodCallPO(null, TestExecutionFlowDAO.class.getName(), "builNewFullFlow",
            "GrDefault", new Object[0]);
        tMethodCall.setMethId(new MethodCallPK(tFlow, 0));
        getSession().save(tMethodCall);
        getSession().flush();

        int tNewResult = countMethods();
        assertEquals(tOldResult + 1, tNewResult);
        assertStatistics(MethodCallPO.class, 1, 0, 0, 0);
    }

    public void testInsertMethodCalls()
    {
        int tOldResult = countMethods();

        ExecutionFlowPO tFlow = new ExecutionFlowPO();
        getSession().save(tFlow);
        MethodCallPO tMethodCall1 = new MethodCallPO(null, TestExecutionFlowDAO.class.getName(), "builNewFullFlow",
            "GrDefault", new Object[0]);
        tMethodCall1.setMethId(new MethodCallPK(tFlow, 1));
        MethodCallPO tMethodCall2 = new MethodCallPO(null, TestExecutionFlowDAO.class.getName(), "builNewFullFlow",
            "GrDefault", new Object[0]);
        tMethodCall2.setMethId(new MethodCallPK(tFlow, 2));
        MethodCallPO tMethodCall3 = new MethodCallPO(null, TestExecutionFlowDAO.class.getName(), "builNewFullFlow",
            "GrDefault", new Object[0]);
        tMethodCall3.setMethId(new MethodCallPK(tFlow, 3));
        tMethodCall1.addChildren(tMethodCall2);
        tMethodCall1.addChildren(tMethodCall3);

        getSession().save(tMethodCall1);
        getSession().save(tMethodCall2);
        getSession().save(tMethodCall3);
        getSession().flush();

        int tNewResult = countMethods();
        assertEquals(tOldResult + 3, tNewResult);
        assertEquals(1, tMethodCall1.getPosition());
        assertEquals(2, tMethodCall2.getPosition());
        assertEquals(3, tMethodCall3.getPosition());
        assertStatistics(MethodCallPO.class, 3, 0, 0, 0);
    }

    public void testLinkedMethodCalls()
    {
        ExecutionFlowPO tFlow = new ExecutionFlowPO();
        getSession().save(tFlow);
        MethodCallPO tMethodCall1 = new MethodCallPO(null, TestExecutionFlowDAO.class.getName(), "builNewFullFlow",
            "GrDefault", new Object[0]);
        tMethodCall1.setMethId(new MethodCallPK(tFlow, 0));
        getSession().save(tMethodCall1);
        getSession().flush();

        MethodCallPO tMethodCall2 = new MethodCallPO(null, TestExecutionFlowDAO.class.getName(), "builNewFullFlow",
            "GrDefault", new Object[0]);
        tMethodCall2.setMethId(new MethodCallPK(tFlow, 1));
        getSession().save(tMethodCall2);
        getSession().flush();

        assertNull(tMethodCall1.getParentMethodCall());
        assertNull(tMethodCall2.getParentMethodCall());

        assertEquals(0, tMethodCall1.getPosition());
        assertEquals(1, tMethodCall2.getPosition());
        assertStatistics(MethodCallPO.class, 2, 0, 0, 0);

        assertEquals(0, countMethodsWithParent());
        tMethodCall1.addChildren(tMethodCall2);
        getSession().save(tMethodCall1);
        getSession().save(tMethodCall2);
        getSession().flush();
        // assertStatistics(MethodCallPO.class, 2, 2, 0, 0);

        assertEquals(0, tMethodCall1.getPosition());
        assertEquals(1, tMethodCall2.getPosition());

        assertEquals(1, countMethodsWithParent());

        getSession().clear();
        MethodCallPO tTempMeth = (MethodCallPO) getSession().load(MethodCallPO.class,
            new MethodCallPK(tFlow, tMethodCall1.getPosition()));
        assertNotSame(tMethodCall1, tTempMeth);
        tMethodCall1 = tTempMeth;
        tTempMeth = (MethodCallPO) getSession().load(MethodCallPO.class,
            new MethodCallPK(tFlow, tMethodCall2.getPosition()));
        assertNotSame(tMethodCall2, tTempMeth);
        tMethodCall2 = tTempMeth;
        assertNull(tMethodCall1.getParentMethodCall());
        assertNotNull(tMethodCall2.getParentMethodCall());
        assertSame(tMethodCall1, tMethodCall2.getParentMethodCall());
        assertEquals(1, tMethodCall1.getChildren().size());
        assertSame(tMethodCall2, tMethodCall1.getChild(0));
    }

    private int countMethods()
    {
        SQLQuery tQuery = getSession().createSQLQuery("Select Count(*) as myCount From METHOD_CALL");
        Object tResult = tQuery.addScalar("myCount", Hibernate.INTEGER).list().get(0);
        return ((Integer) tResult).intValue();
    }

    /** @todo Passer les requêtes en Query externe */
    private int countMethodsWithParent()
    {
        SQLQuery tQuery = getSession().createSQLQuery(
            "Select Count(*) as myCount From METHOD_CALL Where PARENT_INDEX_IN_FLOW IS NOT NULL");
        Object tResult = tQuery.addScalar("myCount", Hibernate.INTEGER).list().get(0);
        return ((Integer) tResult).intValue();
    }

    public static ExecutionFlowPO buildAndSaveNewFullFlow(Session pSession)
    {
        ExecutionFlowPO tExecFlow = buildNewFullFlow();
        ExecutionFlowDAO tDao = new ExecutionFlowDAO(pSession);
        tDao.insertFullExecutionFlow(tExecFlow);
        pSession.flush();
        return tExecFlow;
    }

    public static ExecutionFlowPO buildNewFullFlow()
    {
        MethodCallPO tPoint;
        MethodCallPO tSubPoint,tSubPoint2;
        long tStartTime = System.currentTimeMillis();

        tPoint = new MethodCallPO(null, TestExecutionFlowDAO.class.getName(), "builNewFullFlow", "GrDefault",
            new Object[0]);
        tPoint.setBeginTime(tStartTime);
        tSubPoint = new MethodCallPO(tPoint, TestExecutionFlowDAO.class.getName(), "builNewFullFlow2", "GrChild1",
            new Object[0]);
        tSubPoint.setBeginTime(tStartTime + 2);
        tSubPoint.setEndTime(tStartTime + 5);

        tSubPoint2 = new MethodCallPO(tPoint, TestExecutionFlowDAO.class.getName(), "builNewFullFlow3", "GrChild2",
            new Object[0]);
        tSubPoint2.setBeginTime(tStartTime + 8);
        tSubPoint2.setEndTime(tStartTime + 13);

        tPoint.setEndTime(tStartTime + 20);
        ExecutionFlowPO   tFlow = new ExecutionFlowPO("TEST-main", tPoint, "myJVM");
        tPoint.setMethId(new MethodCallPK(tFlow, 0));
        tSubPoint.setMethId(new MethodCallPK(tFlow, 1));
        tSubPoint2.setMethId(new MethodCallPK(tFlow, 2));
        return tFlow;
    }

    /**
     * @todo S'assurer qu'une méthode VOID est bien loguée comme une méthode void.
     */
    public void testGetMethodCallOfTheFlow()
    {
        ExecutionFlowDAO tFlowDAO = new ExecutionFlowDAO(getSession());

        int tOldResult = tFlowDAO.countFlows();
        int tOldResultM = countMethods();

        // First insert the new Flow in DB.
        ExecutionFlowPO tInitialFlow = buildNewFullFlow();
        int tId = tFlowDAO.insertFullExecutionFlow(tInitialFlow);
        // Check the insertion
        int tNewResult = tFlowDAO.countFlows();
        assertEquals(tOldResult + 1, tNewResult);

        getSession().flush();
        getSession().clear();

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
        assertNotNull(tInitialPoint);
        MethodCallPO tReadPoint = tReadFlow.getFirstMethodCall();
        assertNotNull(tReadPoint);
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
        ExecutionFlowDAO tFlowDAO = new ExecutionFlowDAO(getSession());
        ExecutionFlowPO tFlow = buildNewFullFlow();
        tFlowDAO.insertFullExecutionFlow(tFlow);
        getSession().flush();
        getSession().clear();

        MethodCallPO tInitialPoint = (MethodCallPO) tFlow.getFirstMethodCall().getChildren().get(0);
        MethodCallPO tReadPoint = tFlowDAO.readMethodCall(tFlow.getId(), tInitialPoint.getPosition());
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
        ExecutionFlowDAO tFlowDAO = new ExecutionFlowDAO(getSession());

        // First delete all flow, we don't use the DeleteAll Method of the
        // Dao Object because, it doesn't support transactions.
        getSession().createQuery("Delete FROM MethodCallPO").executeUpdate();
        getSession().createQuery("Delete FROM ExecutionFlowPO").executeUpdate();

        // Now insert the TestFlow
        ExecutionFlowPO tFlow = buildNewFullFlow();
        tFlowDAO.insertFullExecutionFlow(tFlow);
        getSession().flush();

        List tMeasureExtracts = tFlowDAO.getListOfMethodCallExtract();
        MethodCallExtractDTO curExtrat = (MethodCallExtractDTO) tMeasureExtracts.get(0);
        assertEquals("org.jmonitoring.core.dao.TestExecutionFlowDAO.builNewFullFlow", curExtrat.getName());
        assertEquals("GrDefault", curExtrat.getGroupName());
        assertEquals(1, curExtrat.getOccurenceNumber());

        curExtrat = (MethodCallExtractDTO) tMeasureExtracts.get(1);
        assertEquals("org.jmonitoring.core.dao.TestExecutionFlowDAO.builNewFullFlow2", curExtrat.getName());
        assertEquals("GrChild1", curExtrat.getGroupName());
        assertEquals(1, curExtrat.getOccurenceNumber());

        curExtrat = (MethodCallExtractDTO) tMeasureExtracts.get(2);
        assertEquals("org.jmonitoring.core.dao.TestExecutionFlowDAO.builNewFullFlow3", curExtrat.getName());
        assertEquals("GrChild2", curExtrat.getGroupName());
        assertEquals(1, curExtrat.getOccurenceNumber());

    }

}
