package org.jmonitoring.sample.testtreetracer;

import java.util.StringTokenizer;

import org.jmonitoring.agent.store.StoreManager;
import org.jmonitoring.agent.store.impl.MemoryStoreWriter;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.sample.SamplePersistenceTestcase;
import org.jmonitoring.sample.testtreetracer.ToBeCall.Child;
import org.jmonitoring.sample.testtreetracer.ToBeCall.Mother;

public class TestTreeTracer extends SamplePersistenceTestcase
{

    public void testParameterTracer()
    {
        StoreManager.changeStoreManagerClass(MemoryStoreWriter.class);

        Mother tMother = new Mother();
        tMother.setChild1(new Child());
        tMother.setChild2(new Child());

        new ToBeCall().callWithParam(tMother, new Child());

        assertEquals(1, MemoryStoreWriter.countFlows());
        checkParameterTracer();
        assertEquals(1, MemoryStoreWriter.countFlows());

    }

    private void checkParameterTracer()
    {
        ExecutionFlowPO tFlow = MemoryStoreWriter.getFlow(0);
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
        StoreManager.changeStoreManagerClass(MemoryStoreWriter.class);

        new ToBeCall().callWithReturn();

        closeAndRestartSession();

        checkReturnValueTracer();
    }

    private void checkReturnValueTracer()
    {
        ExecutionFlowPO tFlow = MemoryStoreWriter.getFlow(0);
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
        StoreManager.changeStoreManagerClass(MemoryStoreWriter.class);

        ToBeCall.callStaticMethod(new Mother());

        closeAndRestartSession();

        checkStaticCall();
    }

    private void checkStaticCall()
    {
        ExecutionFlowPO tFlow = MemoryStoreWriter.getFlow(0);
        assertNotNull(tFlow);
        assertEquals(ToBeCall.class.getName(), tFlow.getFirstMethodCall().getClassName());
        assertTrue(tFlow.getFirstMethodCall().getParams().length() > 20);
    }
}
