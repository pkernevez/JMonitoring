package org.jmonitoring.agent.store.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jmonitoring.agent.store.IStoreWriter;
import org.jmonitoring.core.domain.ExecutionFlowPO;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * @author pke
 * 
 */
public class MockWriter implements IStoreWriter
{
    private static Log sLog = LogFactory.getLog(MockWriter.class);

    private static int sNbLog;

    public static void clear()
    {
        sNbLog = 0;
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

    public void writeExecutionFlow(ExecutionFlowPO pExecutionFlow)
    {
        incrementNbLog();
        try
        {
            Thread.sleep(1);
        } catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
        sLog.info("Log ExecutionFlow, increment=[" + getNbLog() + "], Thread=" + Thread.currentThread().getName());
    }

}
