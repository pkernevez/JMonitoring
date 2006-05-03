package org.jmonitoring.core.store;

import junit.framework.TestCase;

import org.jmonitoring.core.dao.TestExecutionFlowDAO;
import org.jmonitoring.core.persistence.ExecutionFlowPO;
import org.jmonitoring.core.store.impl.MockAbstractAsynchroneLogger;
import org.jmonitoring.core.utils.AspectLoggerEmulator;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * @author pke
 */
public class TestMeasurePointManager extends TestCase
{

    private static final int TIME_TO_WAIT = 1000;


    private void callOneExecutionFlow() throws InterruptedException
    {
        //Check the count
        assertEquals(0, MockAbstractAsynchroneLogger.getNbPublish());
        assertEquals(0, MockAbstractAsynchroneLogger.getNbLog());

        AspectLoggerEmulator tEmulator = new AspectLoggerEmulator(new MockAbstractAsynchroneLogger());
        tEmulator.simulateExecutionFlow();

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
    public void testNbToStringMethodCall() throws InterruptedException
    {
        callOneExecutionFlow();

        //Now check the number of toString dalled
        assertEquals(4, AspectLoggerEmulator.Param.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Parent.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Child1.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Child2.getNbToString());

    }

}
