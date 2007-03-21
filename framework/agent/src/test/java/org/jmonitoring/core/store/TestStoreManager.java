package org.jmonitoring.core.store;

import java.sql.SQLException;
import java.util.List;

import junit.framework.TestCase;
import net.sf.ehcache.store.MemoryStore;

import org.jmonitoring.core.configuration.ConfigurationHelper;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.store.IStoreWriter;
import org.jmonitoring.core.store.StoreManager;
import org.jmonitoring.core.store.AspectLoggerEmulator.ErrorLogTracer;
import org.jmonitoring.core.store.impl.MemoryStoreWriter;
import org.jmonitoring.core.store.impl.MockAbstractAsynchroneLogger;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * @author pke
 */
public class TestStoreManager extends TestCase
{

    private static final int TIME_TO_WAIT = 1000;

    private void callOneExecutionFlow(boolean pLogDebugEnabled) throws InterruptedException
    {
        // Check the count
        MockAbstractAsynchroneLogger.clear();
        AspectLoggerEmulator.clear();
        assertEquals(0, MockAbstractAsynchroneLogger.getNbPublish());
        assertEquals(0, MockAbstractAsynchroneLogger.getNbLog());

        AspectLoggerEmulator tEmulator = new AspectLoggerEmulator(new MockAbstractAsynchroneLogger());
        tEmulator.simulateExecutionFlow(pLogDebugEnabled);

        assertEquals(1, MockAbstractAsynchroneLogger.getNbPublish());
        Thread.sleep(TIME_TO_WAIT);
        assertEquals(1, MockAbstractAsynchroneLogger.getNbLog());
    }

    private void callOneExecutionFlowWithExceptionInMain(boolean pLogDebugEnabled) throws InterruptedException
    {
        // Check the count
        MockAbstractAsynchroneLogger.clear();
        AspectLoggerEmulator.clear();
        assertEquals(0, MockAbstractAsynchroneLogger.getNbPublish());
        assertEquals(0, MockAbstractAsynchroneLogger.getNbLog());

        AspectLoggerEmulator tEmulator = new AspectLoggerEmulator(new MockAbstractAsynchroneLogger());
        tEmulator.simulateExecutionFlowWithExceptioninMain(pLogDebugEnabled);

        assertEquals(1, MockAbstractAsynchroneLogger.getNbPublish());
        Thread.sleep(TIME_TO_WAIT);
        assertEquals(1, MockAbstractAsynchroneLogger.getNbLog());
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
        List tErrors = ((ErrorLogTracer) StoreManager.getLog()).mErrors;
        assertEquals(1, tErrors.size());
        assertEquals(String.class.getName(), tErrors.get(0).getClass().getName());
        assertEquals("Unable to trace class=[org.jmonitoring.core.store.AspectLoggerEmulator$ExceptionResult] "
            + "with tracer=[org.jmonitoring.core.info.impl.ToStringResultTracer]Pour faire planter un appelMain",
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
        List tErrors = ((ErrorLogTracer) StoreManager.getLog()).mErrors;
        assertEquals(1, tErrors.size());
        assertEquals(String.class.getName(), tErrors.get(0).getClass().getName());
        assertEquals("Unable to trace class=[org.jmonitoring.core.store.AspectLoggerEmulator$ExceptionResult]"
            + " with tracer=[org.jmonitoring.core.info.impl.ToStringResultTracer]Pour faire planter un appelMain",
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
        List tErrors = ((ErrorLogTracer) StoreManager.getLog()).mErrors;
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
        List tErrors = ((ErrorLogTracer) StoreManager.getLog()).mErrors;
        assertEquals(0, tErrors.size());
    }

    private static final int TIME_TO_WAIT2 = 5000;

    private static final int NB_FLOW_TO_LOG = 100;

    /**
     * Test asynhrone publication.
     * 
     * @throws InterruptedException ff
     */
    public void testAsynchronePublication() throws InterruptedException
    {
        MockAbstractAsynchroneLogger.resetNbLog();
        MockAbstractAsynchroneLogger.resetNbPublish();
        // Check the count
        assertEquals(0, MockAbstractAsynchroneLogger.getNbPublish());
        assertEquals(0, MockAbstractAsynchroneLogger.getNbLog());

        AspectLoggerEmulator tEmulator = new AspectLoggerEmulator(new MockAbstractAsynchroneLogger());
        // Log NB_FLOW_TO_LOG
        for (int i = 0; i < NB_FLOW_TO_LOG; i++)
        {
            tEmulator.simulateExecutionFlow(true);
        }
        assertEquals(NB_FLOW_TO_LOG, MockAbstractAsynchroneLogger.getNbPublish());
        Thread.sleep(TIME_TO_WAIT2);
        assertEquals(NB_FLOW_TO_LOG, MockAbstractAsynchroneLogger.getNbLog());
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

        MockAbstractAsynchroneLogger.resetNbLog();
        MockAbstractAsynchroneLogger.resetNbPublish();

        AspectLoggerEmulator tEmulator = new AspectLoggerEmulator(new MockAbstractAsynchroneLogger());
        tEmulator.simulateExecutionFlow(true);
        Thread.sleep(TIME_TO_WAIT);

        // Now check the number of toString called
        assertEquals(3, AspectLoggerEmulator.Param.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Parent.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Child1.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Child2.getNbToString());

        assertEquals(1, MockAbstractAsynchroneLogger.getNbPublish());
    }

    public void testClearChangeManager()
    {
        StoreManager tManager = StoreManager.getManager();
        assertSame(tManager, StoreManager.getManager());
        StoreManager.changeStoreManagerClass(MemoryStore.class);
        assertNotSame(tManager, StoreManager.getManager());
    }

    public void testClearChangeManagerConfiguration()
    {
        
        StoreManager.changeStoreManagerClass(MemoryStoreWriter.class);
        assertNotNull(StoreManager.getManager());
        assertNotNull(StoreManager.getManager().getStoreWriter());
        
        assertEquals(MemoryStoreWriter.class.getName(), StoreManager.getManager().getStoreWriter().getClass().getName());
        
        StoreManager.changeStoreManagerClass(MyWriter.class);
        assertEquals(MyWriter.class.getName(), StoreManager.getManager().getStoreWriter().getClass().getName());
    }
    
    public static class MyWriter implements IStoreWriter {

        public void writeExecutionFlow(ExecutionFlowPO pExecutionFlow)
        {
        }
        
    }

}
