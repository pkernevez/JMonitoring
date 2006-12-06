package org.jmonitoring.sample.testtreetracer;

import java.util.StringTokenizer;

import org.jmonitoring.core.configuration.Configuration;
import org.jmonitoring.core.dao.ExecutionFlowDAO;
import org.jmonitoring.core.dao.PersistanceTestCase;
import org.jmonitoring.core.persistence.ExecutionFlowPO;
import org.jmonitoring.core.store.impl.SynchroneJdbcStore;
import org.jmonitoring.sample.testtreetracer.ToBeCall.Child;
import org.jmonitoring.sample.testtreetracer.ToBeCall.Mother;

public class TestTreeTracer extends PersistanceTestCase
{

    public void testParameterTracer()
    {
        ExecutionFlowDAO tDao = new ExecutionFlowDAO(getSession());
        Configuration.getInstance().setMeasurePointStoreClass(SynchroneJdbcStore.class);

        Mother tMother = new Mother();
        tMother.setChild1(new Child());
        tMother.setChild2(new Child());

        new ToBeCall().callWithParam(tMother, new Child());

        getSession().flush();
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
        ExecutionFlowDAO tDao = new ExecutionFlowDAO(getSession());
        Configuration.getInstance().setMeasurePointStoreClass(SynchroneJdbcStore.class);

        new ToBeCall().callWithReturn();

        getSession().flush();
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
        ExecutionFlowDAO tDao = new ExecutionFlowDAO(getSession());
        Configuration.getInstance().setMeasurePointStoreClass(SynchroneJdbcStore.class);

        ToBeCall.callStaticMethod(new Mother());

        getSession().flush();
        ExecutionFlowPO tFlow = tDao.readExecutionFlow(1);
        assertNotNull(tFlow);
        assertEquals(ToBeCall.class.getName(), tFlow.getFirstMethodCall().getClassName());
        assertTrue(tFlow.getFirstMethodCall().getParams().length() > 20);
    }
}
