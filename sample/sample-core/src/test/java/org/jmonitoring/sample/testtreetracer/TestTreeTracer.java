package org.jmonitoring.sample.testtreetracer;

import java.util.StringTokenizer;

import org.jmonitoring.core.configuration.ConfigurationHelper;
import org.jmonitoring.core.dao.ConsoleDao;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.persistence.InsertionDao;
import org.jmonitoring.sample.SamplePersistenceTestcase;
import org.jmonitoring.sample.testtreetracer.ToBeCall.Child;
import org.jmonitoring.sample.testtreetracer.ToBeCall.Mother;
import org.jmonitoring.server.store.impl.SynchroneJdbcStore;
import org.jmonitoring.test.store.MemoryStoreWriter;

public class TestTreeTracer extends SamplePersistenceTestcase
{

    public void testParameterTracer()
    {
        ConfigurationHelper.getInstance().addProperty(ConfigurationHelper.STORE_CLASS, MemoryStoreWriter.class.getName());

        Mother tMother = new Mother();
        tMother.setChild1(new Child());
        tMother.setChild2(new Child());

        new ToBeCall().callWithParam(tMother, new Child());

        assertEquals(1, MemoryStoreWriter.countFlow());
        checkParameterTracer();
        assertEquals(1, MemoryStoreWriter.countFlow());

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
        assertEquals("Parameter n�1", tTok.nextToken());
        assertEquals("org.jmonitoring.sample.testtreetracer.ToBeCall$Mother", tTok.nextToken());
        assertEquals("  |-- getChild1 --> org.jmonitoring.sample.testtreetracer.ToBeCall$Child", tTok.nextToken());
        assertEquals("  |-- getChild2 --> org.jmonitoring.sample.testtreetracer.ToBeCall$Child", tTok.nextToken());
        assertEquals("Parameter n�2", tTok.nextToken());
        assertEquals("org.jmonitoring.sample.testtreetracer.ToBeCall$Child", tTok.nextToken());
        assertFalse(tTok.hasMoreTokens());
    }

    public void testReturnValueTracer()
    {
        ConfigurationHelper.getInstance().addProperty(ConfigurationHelper.STORE_CLASS, SynchroneJdbcStore.class.getName());

        new ToBeCall().callWithReturn();

        closeAndRestartSession();

        checkReturnValueTracer();
    }

    private void checkReturnValueTracer()
    {
        ConsoleDao tConsoleDao = new ConsoleDao(getSession());
        ExecutionFlowPO tFlow = tConsoleDao.readExecutionFlow(1);
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
        ConfigurationHelper.getInstance().setProperty(ConfigurationHelper.STORE_CLASS, SynchroneJdbcStore.class.getName());

        ToBeCall.callStaticMethod(new Mother());

        closeAndRestartSession();

        checkStaticCall();
    }

    private void checkStaticCall()
    {
        ConsoleDao tConsoleDao = new ConsoleDao(getSession());
        ExecutionFlowPO tFlow = tConsoleDao.readExecutionFlow(1);
        assertNotNull(tFlow);
        assertEquals(ToBeCall.class.getName(), tFlow.getFirstMethodCall().getClassName());
        assertTrue(tFlow.getFirstMethodCall().getParams().length() > 20);
    }
}
