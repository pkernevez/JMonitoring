package org.jmonitoring.agent.store;

import java.sql.SQLException;
import java.util.List;

import junit.framework.TestCase;

import org.jmonitoring.agent.store.AspectLoggerEmulator.ErrorLogTracer;
import org.jmonitoring.agent.store.impl.MockWriter;
import org.jmonitoring.core.configuration.ConfigurationHelper;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * @author pke
 */
public class StoreManagerTest extends TestCase
{
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        ConfigurationHelper.resetConfigFile("JMonitoringMemoryWriterApplicationContext.xml");
    }

    private void callOneExecutionFlow(boolean pLogDebugEnabled) throws InterruptedException
    {
        // Check the count
        MockWriter.clear();
        AspectLoggerEmulator.clear();
        assertEquals(0, MockWriter.getNbLog());

        AspectLoggerEmulator tEmulator = new AspectLoggerEmulator(new MockWriter());
        tEmulator.simulateExecutionFlow(pLogDebugEnabled);
        assertEquals(1, MockWriter.getNbLog());
    }

    private void callOneExecutionFlowWithExceptionInMain(boolean pLogDebugEnabled) throws InterruptedException
    {
        // Check the count
        MockWriter.clear();
        AspectLoggerEmulator.clear();
        assertEquals(0, MockWriter.getNbLog());

        AspectLoggerEmulator tEmulator = new AspectLoggerEmulator(new MockWriter());
        tEmulator.simulateExecutionFlowWithExceptioninMain(pLogDebugEnabled);

        assertEquals(1, MockWriter.getNbLog());
    }

    /**
     * Check the number of toString() method called on the object associated to MethodCallDTO. This is important because
     * of the cost of a toString method on complexe objects.
     * 
     * @throws InterruptedException no doc
     */
    public void testNbToStringMethodCallWithLogOfParameter() throws InterruptedException
    {
        callOneExecutionFlow(true);

        // Now check the number of toString called
        assertEquals(3, AspectLoggerEmulator.Param.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Parent.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Child1.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Child2.getNbToString());
        assertEquals(2, AspectLoggerEmulator.getNbReturnValueToString());
        List<Object> tErrors = ((ErrorLogTracer) StoreManager.getLog()).mErrors;
        assertEquals(1, tErrors.size());
        assertEquals(String.class.getName(), tErrors.get(0).getClass().getName());
        assertEquals("Unable to trace class=[org.jmonitoring.agent.store.AspectLoggerEmulator$ExceptionResult] "
            + "with tracer=[org.jmonitoring.agent.info.impl.ToStringResultTracer]Pour faire planter un appelMain",
                     (String) tErrors.get(0));
    }

    /**
     * Check the number of toString() method called on the object associated to MethodCallDTO. This is important because
     * of the cost of a toString method on complexe objects.
     * 
     * @throws InterruptedException no doc
     */
    public void testNbToStringMethodCallWithoutLogOfParameter() throws InterruptedException
    {
        callOneExecutionFlow(false);

        // Now check the number of toString called
        assertEquals(3, AspectLoggerEmulator.Param.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Parent.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Child1.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Child2.getNbToString());
        assertEquals(2, AspectLoggerEmulator.getNbReturnValueToString());
        List<Object> tErrors = ((ErrorLogTracer) StoreManager.getLog()).mErrors;
        assertEquals(1, tErrors.size());
        assertEquals(String.class.getName(), tErrors.get(0).getClass().getName());
        assertEquals("Unable to trace class=[org.jmonitoring.agent.store.AspectLoggerEmulator$ExceptionResult]"
            + " with tracer=[org.jmonitoring.agent.info.impl.ToStringResultTracer]Pour faire planter un appelMain",
                     (String) tErrors.get(0));
    }

    /**
     * Check the number of toString() method called on the object associated to MethodCallDTO. This is important because
     * of the cost of a toString method on complexe objects.
     * 
     * @throws InterruptedException no doc
     */
    public void testNbToStringMethodCallWithLogAndExceptionInMain() throws InterruptedException
    {
        callOneExecutionFlowWithExceptionInMain(true);

        // Now check the number of toString called
        assertEquals(5, AspectLoggerEmulator.Param.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Parent.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Child1.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Child2.getNbToString());
        assertEquals(1, AspectLoggerEmulator.getNbReturnValueToString());
        List<Object> tErrors = ((ErrorLogTracer) StoreManager.getLog()).mErrors;
        assertEquals(0, tErrors.size());
    }

    /**
     * Check the number of toString() method called on the object associated to MethodCallDTO. This is important because
     * of the cost of a toString method on complexe objects.
     * 
     * @throws InterruptedException no doc
     */
    public void testNbToStringMethodCallWithoutLogAndExceptionInMain() throws InterruptedException
    {
        callOneExecutionFlowWithExceptionInMain(false);

        // Now check the number of toString called
        assertEquals(5, AspectLoggerEmulator.Param.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Parent.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Child1.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Child2.getNbToString());
        assertEquals(1, AspectLoggerEmulator.getNbReturnValueToString());
        List<Object> tErrors = ((ErrorLogTracer) StoreManager.getLog()).mErrors;
        assertEquals(0, tErrors.size());
    }

    private static final int NB_FLOW_TO_LOG = 100;

    /**
     * Test asynhrone publication.
     * 
     * @throws InterruptedException ff
     */
    public void testAsynchronePublication() throws InterruptedException
    {
        MockWriter.resetNbLog();
        // Check the count
        assertEquals(0, MockWriter.getNbLog());

        AspectLoggerEmulator tEmulator = new AspectLoggerEmulator(new MockWriter());
        // Log NB_FLOW_TO_LOG
        for (int i = 0; i < NB_FLOW_TO_LOG; i++)
        {
            tEmulator.simulateExecutionFlow(true);
        }
        assertEquals(NB_FLOW_TO_LOG, MockWriter.getNbLog());
    }

    /**
     * Check the number of toString() method called on the object associated to MethodCallDTO. This is important because
     * of the cost of a toString method on complexe objects.
     * 
     * @throws InterruptedException no doc
     * @throws SQLException no doc
     */
    public void testNbToStringMethodCall() throws InterruptedException
    {

        MockWriter.resetNbLog();

        AspectLoggerEmulator tEmulator = new AspectLoggerEmulator(new MockWriter());
        tEmulator.simulateExecutionFlow(true);

        // Now check the number of toString called
        assertEquals(3, AspectLoggerEmulator.Param.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Parent.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Child1.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Child2.getNbToString());

        assertEquals(1, MockWriter.getNbLog());
    }

    public void testClearChangeManager()
    {
        StoreManager tManager =
            (StoreManager) ConfigurationHelper.getContext().getBean(StoreManager.STORE_MANAGER_NAME);
        assertEquals("TestMachine", tManager.getServerName());
        assertNotNull(tManager.getStoreWriter());
        StoreManager tManager2 =
            (StoreManager) ConfigurationHelper.getContext().getBean(StoreManager.STORE_MANAGER_NAME);
        assertNotSame(tManager, tManager2);
    }
}
