package org.jmonitoring.core.store.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jmonitoring.core.configuration.Configuration;
import org.jmonitoring.core.persistence.ExecutionFlowPO;
import org.jmonitoring.core.store.IStoreWriter;

import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * Default implementation for all Asynchrone Logger.
 * 
 * @author pke
 */
public abstract class AbstractAsynchroneLogger implements IStoreWriter
{
    private static Log sLog = LogFactory.getLog(AbstractAsynchroneLogger.class);

    private static PooledExecutor sExecutor;

    {
        int tAsynchroneLoggerThreadPoolSize = Configuration.getInstance().getAsynchroneStoreThreadPoolSize();
        sExecutor = new PooledExecutor(tAsynchroneLoggerThreadPoolSize);
        sLog.info("Start PoolExecutor of AsynchroneJdbcLogger with " + tAsynchroneLoggerThreadPoolSize + " Threads.");
    }

    /**
     * Defautl constructor.
     */
    public AbstractAsynchroneLogger()
    {
        // if (sLog == null)
        // {
        // initStatic();
        // }
    }

    // /**
    // * Initialisation of all threads shared ressources.
    // */
    // private static synchronized void initStatic()
    // {
    // int tAsynchroneLoggerThreadPoolSize = Configuration.getInstance().getAsynchroneStoreThreadPoolSize();
    // sExecutor = new PooledExecutor(tAsynchroneLoggerThreadPoolSize);
    // sLog.info("Start PoolExecutor of AsynchroneJdbcLogger with " + tAsynchroneLoggerThreadPoolSize + " Threads.");
    // sLog =
    // }

    /**
     * Get the log Task that will be executed by the asynchrone logger with the ThreadPool.
     * 
     * @param pFlow The flow to log.
     * @return The Task that will be pulled for upcoming logging.
     */
    protected abstract Runnable getAsynchroneLogTask(ExecutionFlowPO pFlow);

    /**
     * @see org.jmonitoring.core.log.IStoreWriter#writeExecutionFlow( ExecutionFlowPO)
     */
    public void writeExecutionFlow(ExecutionFlowPO pExecutionFlow)
    {
        // Test because of ClassLoader and hotdeploy in some container...
        // if (sExecutor == null)
        // {
        // sLog.error("No more actif thread for this logger, restart another one...");
        // initStatic();
        // }
        try
        {
            sExecutor.execute(getAsynchroneLogTask(pExecutionFlow));
            sLog.info("Added new ExecutionFlow to List " + pExecutionFlow);
        } catch (InterruptedException e)
        {
            sLog.error("Unable to add new ExecutionFlow to List " + pExecutionFlow, e);
        }
    }

}
