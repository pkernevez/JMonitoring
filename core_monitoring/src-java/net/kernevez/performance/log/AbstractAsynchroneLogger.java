package net.kernevez.performance.log;

import net.kernevez.performance.configuration.Configuration;
import net.kernevez.performance.measure.ExecutionFlow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
public abstract class AbstractAsynchroneLogger implements IMeasurePointTreeLogger
{
    private static Log sLog;

    private static PooledExecutor sExecutor;

    /**
     * Defautl constructor.
     */
    public AbstractAsynchroneLogger()
    {
        if (sLog == null)
        {
            initStatic();
        }
    }

    /**
     * Initialisation of all threads shared ressources.
     */
    private static synchronized void initStatic()
    {
        sLog = LogFactory.getLog(AbstractAsynchroneLogger.class);
        int tAsynchroneLoggerThreadPoolSize = Configuration.getInstance().getAsynchroneStoreThreadPoolSize();
        sExecutor = new PooledExecutor(tAsynchroneLoggerThreadPoolSize);
        sLog.info("Start PoolExecutor of AsynchroneJdbcLogger with " + tAsynchroneLoggerThreadPoolSize
                        + " Threads.");
    }

    /**
     * Get the log Task that will be executed by the asynchrone logger with the ThreadPool.
     * 
     * @param pFlow The flow to log.
     * @return The Task that will be pulled for upcoming logging.
     */
    protected abstract Runnable getAsynchroneLogTask(ExecutionFlow pFlow);

    /**
     * @see org.jmonitoring.core.log.IMeasurePointTreeLogger#logMeasurePointTree(
     *      org.jmonitoring.core.measure.ExecutionFlow)
     */
    public void logMeasurePointTree(ExecutionFlow pExecutionFlow)
    {
        //Test because of ClassLoader and hotdeploy in some container...
        if (sExecutor == null)
        {
            sLog.error("No more actif thread for this logger, restart another one...");
            initStatic();
        }
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
