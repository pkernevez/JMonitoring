package org.jmonitoring.sample.testtreetracer;

import java.util.StringTokenizer;

import org.jmonitoring.agent.store.impl.MemoryWriter;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.sample.SampleTestcase;
import org.jmonitoring.sample.testtreetracer.ToBeCall.Child;
import org.jmonitoring.sample.testtreetracer.ToBeCall.Mother;
import org.junit.Test;

public class TreeTracerTest extends SampleTestcase
{
    @Test
    public void testParameterTracer()
    {
        assertEquals(0, MemoryWriter.countFlows());
        Mother tMother = new Mother();
        tMother.setChild1(new Child());
        tMother.setChild2(new Child());

        new ToBeCall().callWithParam(tMother, new Child());

        assertEquals(1, MemoryWriter.countFlows());
        checkParameterTracer();
        assertEquals(1, MemoryWriter.countFlows());

    }

    private void checkParameterTracer()
    {
        ExecutionFlowPO tFlow = MemoryWriter.getFlow(0);
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

    @Test
    public void testReturnValueTracer()
    {
        new ToBeCall().callWithReturn();

        checkReturnValueTracer();
    }

    private void checkReturnValueTracer()
    {
        ExecutionFlowPO tFlow = MemoryWriter.getFlow(0);
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

    @Test
    public void testStaticCall()
    {
        ToBeCall.callStaticMethod(new Mother());

        // closeAndRestartSession();

        checkStaticCall();
    }

    private void checkStaticCall()
    {
        ExecutionFlowPO tFlow = MemoryWriter.getFlow(0);
        assertNotNull(tFlow);
        assertEquals(ToBeCall.class.getName(), tFlow.getFirstMethodCall().getClassName());
        assertTrue(tFlow.getFirstMethodCall().getParams().length() > 20);
    }
}
