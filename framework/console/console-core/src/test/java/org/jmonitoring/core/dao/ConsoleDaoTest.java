package org.jmonitoring.core.dao;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Hibernate;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.jmonitoring.core.dto.MethodCallExtractDTO;
import org.jmonitoring.test.dao.PersistanceTestCase;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author pke
 * 
 * @todo To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 *       Templates
 */
@ContextConfiguration(locations = {"/console.xml" })
public class ConsoleDaoTest extends PersistanceTestCase
{
    @Resource(name = "dao")
    private ConsoleDao mDao;

    @Test
    public void testCountOk()
    {
        assertEquals(0, mDao.countFlows());
        assertEquals(0, countMethods());
    }

    @Test
    public void testInsertSimpleFlow()
    {
        int tOldNbFlow = mDao.countFlows();
        int tOldNbMethodCall = countMethods();

        ExecutionFlowPO tFlow = buildNewSimpleFlow();
        mDao.insertFullExecutionFlow(tFlow);
        getSession().flush();

        int tNewNbFlow = mDao.countFlows();
        int tNewNbMethodCall = countMethods();
        assertEquals(tOldNbFlow + 1, tNewNbFlow);
        assertEquals(tOldNbMethodCall + 1, tNewNbMethodCall);
    }

    public static ExecutionFlowPO buildNewSimpleFlow()
    {
        ExecutionFlowPO tFlow;
        MethodCallPO tPoint;
        long tStartTime = System.currentTimeMillis();

        tPoint = new MethodCallPO(null, ConsoleDaoTest.class.getName(), "builNewFullFlow", "GrDefault", "");
        tPoint.setBeginTime(tStartTime);

        tPoint.setEndTime(tStartTime + 20);
        tFlow = new ExecutionFlowPO("TEST-main", tPoint, "myJVM");
        return tFlow;
    }

    @Test
    public void testInsertNewFlows()
    {
        int tOldNbFlow = mDao.countFlows();
        int tOldNbMeth = countMethods();

        ExecutionFlowPO tFlow = buildNewFullFlow();
        mDao.insertFullExecutionFlow(tFlow);
        getSession().flush();
        int tNewNbFlow = mDao.countFlows();
        int tNewNbMeth = countMethods();
        assertEquals(tOldNbFlow + 1, tNewNbFlow);
        assertEquals(tOldNbMeth + 6, tNewNbMeth);

        MethodCallPO curMeth = tFlow.getFirstMethodCall();
        assertEquals(tFlow.getId(), curMeth.getFlow().getId());
        assertEquals(tFlow.getId(), curMeth.getChild(0).getFlow().getId());
        assertEquals(tFlow.getId(), curMeth.getChild(1).getFlow().getId());
    }

    @Test
    public void testReadNewFlows()
    {
        ExecutionFlowPO tFlow = buildNewFullFlow();
        mDao.insertFullExecutionFlow(tFlow);
        getSession().flush();
        int tFlowId = tFlow.getId();
        tFlow = null;
        getSession().clear();

        tFlow = mDao.readExecutionFlow(tFlowId);
        MethodCallPO tMeth = tFlow.getFirstMethodCall();
        tMeth.getBeginTime();
        tMeth.getChild(0).getBeginTime();
        tMeth.getChild(1).getBeginTime();
        tMeth.getChild(1).getChild(0).getBeginTime();
        Statistics tStat = getStats();
        assertEquals(7, tStat.getEntityLoadCount());
        assertEquals(0, tStat.getEntityFetchCount());
        assertEquals(0, tStat.getCollectionFetchCount());
    }

    public ExecutionFlowPO buildAndSaveNewFullFlow(Session pSession)
    {
        ExecutionFlowPO tExecFlow = buildNewFullFlow();
        mDao.insertFullExecutionFlow(tExecFlow);
        pSession.flush();
        return tExecFlow;
    }

    /**
     * @todo S'assurer qu'une m�thode VOID est bien logu�e comme une m�thode void.
     */
    @Test
    public void testGetMethodCallOfTheFlow()
    {
        int tOldResult = mDao.countFlows();
        int tOldResultM = countMethods();

        // First insert the new Flow in DB.
        ExecutionFlowPO tInitialFlow = buildNewFullFlow();
        int tId = mDao.insertFullExecutionFlow(tInitialFlow);
        // Check the insertion
        int tNewResult = mDao.countFlows();
        assertEquals(tOldResult + 1, tNewResult);

        getSession().flush();
        getSession().clear();

        assertEquals(tOldResultM + 6, countMethods());

        ExecutionFlowPO tReadFlow = mDao.readExecutionFlow(tId);
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

    @Test
    public void testReadMethodCall()
    {
        ExecutionFlowPO tFlow = buildNewFullFlow();
        mDao.insertFullExecutionFlow(tFlow);
        getSession().flush();
        getSession().clear();

        MethodCallPO tInitialPoint = tFlow.getFirstMethodCall().getChild(0);
        MethodCallPO tReadPoint = mDao.readMethodCall(tFlow.getId(), tInitialPoint.getPosition());
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
            mDao.readMethodCall(13, 34);
            fail("Should not append");
        } catch (ObjectNotFoundException e)
        {
            assertEquals(
                         "No row with the given identifier exists: [org.jmonitoring.core.domain.MethodCallPO#FlowId=13 and Position=34]",
                         e.getMessage());
        }
    }

    @Test
        public void testGetListOfMethodCallExtractOld()
        {
            // First delete all flow, we don't use the DeleteAll Method of the
            // Dao Object because, it doesn't support transactions.
            getSession().createQuery("Delete FROM MethodCallPO").executeUpdate();
            getSession().createQuery("Delete FROM ExecutionFlowPO").executeUpdate();
    
            // Now insert the TestFlow
            ExecutionFlowPO tFlow = buildNewFullFlow();
            mDao.insertFullExecutionFlow(tFlow);
            getSession().flush();
    
            List<MethodCallExtractDTO> tMeasureExtracts = mDao.getListOfMethodCallExtractOld();
            MethodCallExtractDTO curExtrat = tMeasureExtracts.get(0);
            assertEquals("org.jmonitoring.test.dao.PersistanceTestCase.builNewFullFlow", curExtrat.getName());
            assertEquals("GrDefault", curExtrat.getGroupName());
            assertEquals(1, curExtrat.getOccurenceNumber());
    
            curExtrat = tMeasureExtracts.get(1);
            assertEquals("org.jmonitoring.test.dao.PersistanceTestCase.builNewFullFlow2", curExtrat.getName());
            assertEquals("GrChild1", curExtrat.getGroupName());
            assertEquals(1, curExtrat.getOccurenceNumber());
    
            curExtrat = tMeasureExtracts.get(2);
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

    @Test
    public void testGetNextInGroup()
    {
        ExecutionFlowPO tFlow = buildNewFullFlow();
        mDao.insertFullExecutionFlow(tFlow);
        getSession().flush();

        int tFlowId = tFlow.getId();
        assertEquals(1, mDao.getNextInGroup(tFlowId, 0, "GrDefault").getPosition());
        assertNull(mDao.getNextInGroup(tFlowId, 1, "GrDefault"));

        assertEquals(2, mDao.getNextInGroup(tFlowId, 0, "GrChild1").getPosition());
        assertNull(mDao.getNextInGroup(tFlowId, 2, "GrChild1"));

        assertEquals(3, mDao.getNextInGroup(tFlowId, 0, "GrChild2").getPosition());
        assertEquals(4, mDao.getNextInGroup(tFlowId, 3, "GrChild2").getPosition());
        assertEquals(5, mDao.getNextInGroup(tFlowId, 4, "GrChild2").getPosition());
        assertEquals(6, mDao.getNextInGroup(tFlowId, 5, "GrChild2").getPosition());
        assertNull(mDao.getNextInGroup(tFlowId, 6, "GrChild2"));
    }

    @Test
    public void testGetPrevInGroup()
    {
        ExecutionFlowPO tFlow = buildNewFullFlow();
        mDao.insertFullExecutionFlow(tFlow);
        getSession().flush();

        int tFlowId = tFlow.getId();

        assertEquals(6, mDao.getPrevInGroup(tFlowId, 7, "GrChild2").getPosition());
        assertEquals(5, mDao.getPrevInGroup(tFlowId, 6, "GrChild2").getPosition());
        assertEquals(4, mDao.getPrevInGroup(tFlowId, 5, "GrChild2").getPosition());
        assertEquals(3, mDao.getPrevInGroup(tFlowId, 4, "GrChild2").getPosition());
        assertNull(mDao.getPrevInGroup(tFlowId, 3, "GrChild2"));

        assertEquals(2, mDao.getPrevInGroup(tFlowId, 7, "GrChild1").getPosition());
        assertNull(mDao.getPrevInGroup(tFlowId, 2, "GrChild1"));

        assertEquals(1, mDao.getPrevInGroup(tFlowId, 7, "GrDefault").getPosition());
        assertNull(mDao.getPrevInGroup(tFlowId, 1, "GrDefault"));
    }
}
