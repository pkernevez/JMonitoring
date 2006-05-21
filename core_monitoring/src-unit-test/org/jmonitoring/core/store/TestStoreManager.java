package org.jmonitoring.core.store;

import java.util.List;

import junit.framework.TestCase;

import org.jmonitoring.core.configuration.Configuration;
import org.jmonitoring.core.dao.TestExecutionFlowDAO;
import org.jmonitoring.core.persistence.ExecutionFlowPO;
import org.jmonitoring.core.store.AspectLoggerEmulator.ErrorLogTracer;
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
        //Check the count
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
        //Check the count
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
     * Check the number of toString() method called on the object associated to MethodCallDTO. This is important because of
     * the cost of a toString method on complexe objects.
     * 
     * @throws InterruptedException no doc
     */
    public void testNbToStringMethodCallWithLogOfParameter() throws InterruptedException
    {
        callOneExecutionFlow(true);

        //Now check the number of toString called
        assertEquals(3, AspectLoggerEmulator.Param.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Parent.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Child1.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Child2.getNbToString());
        assertEquals(2, AspectLoggerEmulator.getNbReturnValueToString());
        List tErrors = ((ErrorLogTracer)StoreManager.sLog).mErrors;
        assertEquals(1, tErrors.size());
        assertEquals(String.class.getName(), tErrors.get(0).getClass().getName());
        assertEquals("Unable to trace return value of call.Pour faire planter un appelMain", tErrors.get(0));
    }

    /**
     * Check the number of toString() method called on the object associated to MethodCallDTO. This is important because of
     * the cost of a toString method on complexe objects.
     * 
     * @throws InterruptedException no doc
     */
    public void testNbToStringMethodCallWithoutLogOfParameter() throws InterruptedException
    {
        callOneExecutionFlow(false);

        Configuration.getInstance().setLogMethodParameter(false);
        //Now check the number of toString called
        assertEquals(3, AspectLoggerEmulator.Param.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Parent.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Child1.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Child2.getNbToString());
        assertEquals(2, AspectLoggerEmulator.getNbReturnValueToString());
        List tErrors = ((ErrorLogTracer)StoreManager.sLog).mErrors;
        assertEquals(1, tErrors.size());
        assertEquals(String.class.getName(), tErrors.get(0).getClass().getName());
        assertEquals("Unable to trace return value of call.Pour faire planter un appelMain", tErrors.get(0));
    }

    /**
     * Check the number of toString() method called on the object associated to MethodCallDTO. This is important because of
     * the cost of a toString method on complexe objects.
     * 
     * @throws InterruptedException no doc
     */
    public void testNbToStringMethodCallWithLogAndExceptionInMain() throws InterruptedException
    {
        callOneExecutionFlowWithExceptionInMain(true);

        //Now check the number of toString called
        assertEquals(5, AspectLoggerEmulator.Param.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Parent.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Child1.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Child2.getNbToString());
        assertEquals(1, AspectLoggerEmulator.getNbReturnValueToString());
        List tErrors = ((ErrorLogTracer)StoreManager.sLog).mErrors;
        assertEquals(0, tErrors.size());
    }

    /**
     * Check the number of toString() method called on the object associated to MethodCallDTO. This is important because of
     * the cost of a toString method on complexe objects.
     * 
     * @throws InterruptedException no doc
     */
    public void testNbToStringMethodCallWithoutLogAndExceptionInMain() throws InterruptedException
    {
        callOneExecutionFlowWithExceptionInMain(false);

        //Now check the number of toString called
        assertEquals(5, AspectLoggerEmulator.Param.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Parent.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Child1.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Child2.getNbToString());
        assertEquals(1, AspectLoggerEmulator.getNbReturnValueToString());
        List tErrors = ((ErrorLogTracer)StoreManager.sLog).mErrors;
        assertEquals(0, tErrors.size());
    }

    public void testFactory()
    {
        StoreManager tManager = new StoreManager();
        tManager = new StoreManager(null, null);
    }
    
}
