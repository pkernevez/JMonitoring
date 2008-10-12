package org.jmonitoring.agent.store.impl;

import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jmonitoring.agent.store.IStoreWriter;
import org.jmonitoring.core.configuration.ConfigurationHelper;
import org.jmonitoring.core.domain.ExecutionFlowPO;

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
public abstract class AbstractAsynchroneWriter implements IStoreWriter {
    private static final String THREAD_POOL_KEY = "asynchronelogger.threadpool.size";

    private static Log sLog = LogFactory.getLog(AbstractAsynchroneWriter.class);

    private static PooledExecutor sExecutor;

    static {
        int tAsynchroneLoggerThreadPoolSize = ConfigurationHelper.getInt(THREAD_POOL_KEY);
        sExecutor = new PooledExecutor(tAsynchroneLoggerThreadPoolSize);
        sLog.info("Start PoolExecutor of AsynchroneJdbcLogger with " + tAsynchroneLoggerThreadPoolSize + " Threads.");
    }

    /**
     * Defautl constructor.
     */
    public AbstractAsynchroneWriter() {
    }

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
    public void writeExecutionFlow(ExecutionFlowPO pExecutionFlow) {
        try {
            sExecutor.execute(getAsynchroneLogTask(pExecutionFlow));
            sLog.info("Added new ExecutionFlow to List " + pExecutionFlow);
        } catch (InterruptedException e) {
            sLog.error("Unable to add new ExecutionFlow to List " + pExecutionFlow, e);
        }
    }

}
