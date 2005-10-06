package org.jmonitoring.core.log;

import junit.framework.TestCase;
import org.jmonitoring.core.utils.AspectLoggerEmulator;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * @author pke
 *  
 */
public class TestAbstractAsynchroneLogger extends TestCase
{

    private static final int TIME_TO_WAIT = 5000;

    private static final int NB_FLOW_TO_LOG = 100;

    protected void setUp() throws Exception
    {
        MockAbstractAsynchroneLogger.resetNbLog();
        MockAbstractAsynchroneLogger.resetNbPublish();
    }

    /**
     * Test asynhrone publication.
     * 
     * @throws InterruptedException ff
     */
    public void testAsynchronePublication() throws InterruptedException
    {
        //Check the count
        assertEquals(0, MockAbstractAsynchroneLogger.getNbPublish());
        assertEquals(0, MockAbstractAsynchroneLogger.getNbLog());

        AspectLoggerEmulator tEmulator = new AspectLoggerEmulator(new MockAbstractAsynchroneLogger());
        //Log NB_FLOW_TO_LOG
        for (int i = 0; i < NB_FLOW_TO_LOG; i++)
        {
            tEmulator.simulateExecutionFlow();
        }
        assertEquals(NB_FLOW_TO_LOG, MockAbstractAsynchroneLogger.getNbPublish());
        Thread.sleep(TIME_TO_WAIT);
        assertEquals(NB_FLOW_TO_LOG, MockAbstractAsynchroneLogger.getNbLog());
    }
}
