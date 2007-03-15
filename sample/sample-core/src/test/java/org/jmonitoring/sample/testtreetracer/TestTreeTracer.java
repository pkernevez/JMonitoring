package org.jmonitoring.sample.testtreetracer;

import java.util.StringTokenizer;

import org.jmonitoring.core.configuration.Configuration;
import org.jmonitoring.core.dao.ExecutionFlowDAO;
import org.jmonitoring.core.dao.ExecutionFlowDaoFactory;
import org.jmonitoring.core.dao.IExecutionFlowDAO;
import org.jmonitoring.core.dao.PersistanceTestCase;
import org.jmonitoring.core.persistence.ExecutionFlowPO;
import org.jmonitoring.core.store.impl.SynchroneJdbcStore;
import org.jmonitoring.hibernate.info.SqlExecutionAspect;
import org.jmonitoring.sample.SamplePersistenceTestcase;
import org.jmonitoring.sample.testtreetracer.ToBeCall.Child;
import org.jmonitoring.sample.testtreetracer.ToBeCall.Mother;

public class TestTreeTracer extends SamplePersistenceTestcase
{

    public void testParameterTracer()
    {
        IExecutionFlowDAO tDao = ExecutionFlowDaoFactory.getExecutionFlowDao(getSession());
        assertEquals(0, tDao.countFlows());
        assertEquals(0, tDao.countFlows());
        Configuration.getInstance().setMeasurePointStoreClass(SynchroneJdbcStore.class);

        Mother tMother = new Mother();
        tMother.setChild1(new Child());
        tMother.setChild2(new Child());

        new ToBeCall().callWithParam(tMother, new Child());

        getSession().flush();
        getSession().clear();
        assertEquals(1, tDao.countFlows());
        checkParameterTracer();
        assertEquals(1, tDao.countFlows());
        
    }

    private void checkParameterTracer()
    {
        IExecutionFlowDAO tDao = ExecutionFlowDaoFactory.getExecutionFlowDao(getSession());
        ExecutionFlowPO tFlow = tDao.readExecutionFlow(1);
        assertNotNull(tFlow);
        String tParams = tFlow.getFirstMethodCall().getParams();
        StringTokenizer tTok = new StringTokenizer(tParams, "\n");

        assertTrue(tTok.nextToken().startsWith("Tracing duration = "));
        assertEquals("Max Depth = 2", tTok.nextToken());
        assertEquals("Nb Entity = 4", tTok.nextToken());
        assertEquals("Parameter n°1", tTok.nextToken());
        assertEquals("org.jmonitoring.sample.testtreetracer.ToBeCall$Mother", tTok.nextToken());
        assertEquals("  |-- getChild1 --> org.jmonitoring.sample.testtreetracer.ToBeCall$Child", tTok.nextToken());
        assertEquals("  |-- getChild2 --> org.jmonitoring.sample.testtreetracer.ToBeCall$Child", tTok.nextToken());
        assertEquals("Parameter n°2", tTok.nextToken());
        assertEquals("org.jmonitoring.sample.testtreetracer.ToBeCall$Child", tTok.nextToken());
        assertFalse(tTok.hasMoreTokens());
    }

    public void testReturnValueTracer()
    {
        Configuration.getInstance().setMeasurePointStoreClass(SynchroneJdbcStore.class);

        new ToBeCall().callWithReturn();

        getSession().flush();
        getSession().clear();
        
        checkReturnValueTracer();
    }

    private void checkReturnValueTracer()
    {
        IExecutionFlowDAO tDao = ExecutionFlowDaoFactory.getExecutionFlowDao(getSession());
        ExecutionFlowPO tFlow = tDao.readExecutionFlow(1);
        assertNotNull(tFlow);
        String tParams = tFlow.getFirstMethodCall().getReturnValue();
        StringTokenizer tTok = new StringTokenizer(tParams, "\n");

        assertTrue(tTok.nextToken().startsWith("Tracing duration = "));
        assertEquals("Max Depth = 2", tTok.nextToken());
        assertEquals("Nb Entity = 3", tTok.nextToken());
        assertEquals("Result", tTok.nextToken());
        assertEquals("org.jmonitoring.sample.testtreetracer.ToBeCall$Mother", tTok.nextToken());
        assertEquals("  |-- getChild1 --> org.jmonitoring.sample.testtreetracer.ToBeCall$Child", tTok.nextToken());
        assertEquals("  |-- getChild2 --> org.jmonitoring.sample.testtreetracer.ToBeCall$Child", tTok.nextToken());
        assertFalse(tTok.hasMoreTokens());
    }

    public void testStaticCall()
    {
        Configuration.getInstance().setMeasurePointStoreClass(SynchroneJdbcStore.class);

        ToBeCall.callStaticMethod(new Mother());

        getSession().flush();
        getSession().clear();
        
        checkStaticCall();
    }

    private void checkStaticCall()
    {
        IExecutionFlowDAO tDao = ExecutionFlowDaoFactory.getExecutionFlowDao(getSession());
        ExecutionFlowPO tFlow = tDao.readExecutionFlow(1);
        assertNotNull(tFlow);
        assertEquals(ToBeCall.class.getName(), tFlow.getFirstMethodCall().getClassName());
        assertTrue(tFlow.getFirstMethodCall().getParams().length() > 20);
    }
}
