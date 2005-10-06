package org.jmonitoring.core.log;

import org.jmonitoring.core.measure.ExecutionFlow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * @author pke
 *  
 */
public class MockAbstractAsynchroneLogger extends AbstractAsynchroneLogger
{

    private static Log sLog = LogFactory.getLog(MockAbstractAsynchroneLogger.class);

    private static int sNbPublish;

    private static int sNbLog;

    private static synchronized void incrementNbPublish()
    {
        sNbPublish++;
    }

    /**
     * Accessor.
     * 
     * @return The number of <code>ExecutionFlow</code> already publish.
     */
    public static synchronized int getNbPublish()
    {
        return sNbPublish;
    }

    /**
     * Accessor.
     *  
     */
    public static synchronized void resetNbPublish()
    {
        sNbPublish = 0;
    }

    private static synchronized void incrementNbLog()
    {
        sNbLog++;
    }

    /**
     * Accessor.
     * 
     * @return The number of <code>ExecutionFlow</code> already log by the ThreadPool.
     */
    public static synchronized int getNbLog()
    {
        return sNbLog;
    }

    /**
     * Accessor.
     * 
     */
    public static synchronized void resetNbLog()
    {
        sNbLog = 0;
    }

    /**
     * @see AbstractAsynchroneLogger#getAsynchroneLogTask(ExecutionFlow)
     */
    protected Runnable getAsynchroneLogTask(ExecutionFlow pFlow)
    {
        incrementNbPublish();
        return new MockRunnable();
    }

    private static class MockRunnable implements Runnable
    {

        public void run()
        {
            incrementNbLog();
            sLog.info("Log ExecutionFlow, increment=[" + getNbLog() + "]");
            try
            {
                Thread.sleep(2);
            } catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        }

    }

}
