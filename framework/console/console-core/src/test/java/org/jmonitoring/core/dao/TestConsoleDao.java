package org.jmonitoring.core.dao;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.jmonitoring.core.dto.MethodCallExtractDTO;
import org.jmonitoring.test.dao.PersistanceTestCase;

/**
 * @author pke
 * 
 * @todo To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 *       Templates
 */
public class TestConsoleDao extends PersistanceTestCase
{

    public void testCountOk()
    {
        assertEquals(0, new ConsoleDao(getSession()).countFlows());
        assertEquals(0, countMethods());
    }

    public void testInsertSimpleFlow()
    {
        ConsoleDao tFlowDAO = new ConsoleDao(getSession());
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

        tPoint = new MethodCallPO(null, TestConsoleDao.class.getName(), "builNewFullFlow", "GrDefault", "");
        tPoint.setBeginTime(tStartTime);

        tPoint.setEndTime(tStartTime + 20);
        tFlow = new ExecutionFlowPO("TEST-main", tPoint, "myJVM");
        return tFlow;
    }

    public void testInsertNewFlows()
    {
        ConsoleDao tFlowDAO = new ConsoleDao(getSession());
        int tOldNbFlow = tFlowDAO.countFlows();
        int tOldNbMeth = countMethods();

        ExecutionFlowPO tFlow = buildNewFullFlow();
        tFlowDAO.insertFullExecutionFlow(tFlow);
        getSession().flush();
        int tNewNbFlow = tFlowDAO.countFlows();
        int tNewNbMeth = countMethods();
        assertEquals(tOldNbFlow + 1, tNewNbFlow);
        assertEquals(tOldNbMeth + 6, tNewNbMeth);

        MethodCallPO curMeth = tFlow.getFirstMethodCall();
        assertEquals(tFlow.getId(), curMeth.getFlow().getId());
        assertEquals(tFlow.getId(), curMeth.getChild(0).getFlow().getId());
        assertEquals(tFlow.getId(), curMeth.getChild(1).getFlow().getId());
    }

    public void testReadNewFlows()
    {
        ConsoleDao tFlowDAO = new ConsoleDao(getSession());

        ExecutionFlowPO tFlow = buildNewFullFlow();
        tFlowDAO.insertFullExecutionFlow(tFlow);
        getSession().flush();
        int tFlowId = tFlow.getId();
        tFlow = null;
        getSession().clear();

        tFlow = tFlowDAO.readExecutionFlow(tFlowId);
        MethodCallPO tMeth = tFlow.getFirstMethodCall();
        tMeth.getBeginTime();
        tMeth.getChild(0).getBeginTime();
        tMeth.getChild(1).getBeginTime();
        tMeth.getChild(1).getChild(0).getBeginTime();
        Statistics tStat = getStats();
        assertEquals(7, tStat.getEntityLoadCount());
        assertEquals(1, tStat.getEntityFetchCount());
        assertEquals(0, tStat.getCollectionFetchCount());
    }

    public static ExecutionFlowPO buildAndSaveNewFullFlow(Session pSession)
    {
        ExecutionFlowPO tExecFlow = buildNewFullFlow();
        ConsoleDao tDao = new ConsoleDao(pSession);
        tDao.insertFullExecutionFlow(tExecFlow);
        pSession.flush();
        return tExecFlow;
    }

    /**
     * @todo S'assurer qu'une m�thode VOID est bien logu�e comme une m�thode void.
     */
    public void testGetMethodCallOfTheFlow()
    {
        ConsoleDao tFlowDAO = new ConsoleDao(getSession());

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

        assertEquals(tOldResultM + 6, countMethods());

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
        tInitialPoint = tInitialPoint.getChildren().get(0);
        tReadPoint = tReadPoint.getChildren().get(0);
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
        tInitialPoint = tInitialPoint.getParentMethodCall().getChildren().get(1);
        tReadPoint = tReadPoint.getParentMethodCall().getChildren().get(1);
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
        ConsoleDao tFlowDAO = new ConsoleDao(getSession());
        ExecutionFlowPO tFlow = buildNewFullFlow();
        tFlowDAO.insertFullExecutionFlow(tFlow);
        getSession().flush();
        getSession().clear();

        MethodCallPO tInitialPoint = tFlow.getFirstMethodCall().getChild(0);
        MethodCallPO tReadPoint = tFlowDAO.readMethodCall(tFlow.getId(), tInitialPoint.getPosition());
        assertNotSame(tInitialPoint, tReadPoint);

        assertEquals(tInitialPoint.getClassName(), tReadPoint.getClassName());
        assertEquals("org.jmonitoring.test.dao.PersistanceTestCaseiuiu", tInitialPoint.getRuntimeClassName());
        assertEquals("org.jmonitoring.test.dao.PersistanceTestCaseiuiu", tReadPoint.getRuntimeClassName());
        assertEquals(tInitialPoint.getMethodName(), tReadPoint.getMethodName());
        assertEquals("[]", tReadPoint.getParams());
        assertEquals(tInitialPoint.getReturnValue(), tReadPoint.getReturnValue());
        assertEquals(tInitialPoint.getThrowableClass(), tReadPoint.getThrowableClass());
        assertEquals(tInitialPoint.getThrowableMessage(), tReadPoint.getThrowableMessage());
        assertEquals(tInitialPoint.getBeginTime(), tReadPoint.getBeginTime());
        assertEquals(tInitialPoint.getEndTime(), tReadPoint.getEndTime());
        assertEquals(tInitialPoint.getGroupName(), tReadPoint.getGroupName());
        try
        {
            tFlowDAO.readMethodCall(13, 34);
            fail("Should not append");
        } catch (ObjectNotFoundException e)
        {
            assertEquals(
                         "No row with the given identifier exists: [org.jmonitoring.core.domain.MethodCallPO#FlowId=13 and Position=34]",
                         e.getMessage());
        }
    }

    public void testGetListOfMethodCallExtract()
    {
        ConsoleDao tFlowDAO = new ConsoleDao(getSession());

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
        assertEquals("org.jmonitoring.test.dao.PersistanceTestCase.builNewFullFlow", curExtrat.getName());
        assertEquals("GrDefault", curExtrat.getGroupName());
        assertEquals(1, curExtrat.getOccurenceNumber());

        curExtrat = (MethodCallExtractDTO) tMeasureExtracts.get(1);
        assertEquals("org.jmonitoring.test.dao.PersistanceTestCase.builNewFullFlow2", curExtrat.getName());
        assertEquals("GrChild1", curExtrat.getGroupName());
        assertEquals(1, curExtrat.getOccurenceNumber());

        curExtrat = (MethodCallExtractDTO) tMeasureExtracts.get(2);
        assertEquals("org.jmonitoring.test.dao.PersistanceTestCase.builNewFullFlow3", curExtrat.getName());
        assertEquals("GrChild2", curExtrat.getGroupName());
        assertEquals(4, curExtrat.getOccurenceNumber());

    }

    private int countMethods()
    {
        SQLQuery tQuery = getSession().createSQLQuery("Select Count(*) as myCount From METHOD_CALL");
        Object tResult = tQuery.addScalar("myCount", Hibernate.INTEGER).list().get(0);
        return ((Integer) tResult).intValue();
    }

}
