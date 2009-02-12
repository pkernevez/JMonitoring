package org.jmonitoring.agent.store.impl;

import javax.annotation.Resource;

import org.jmonitoring.agent.store.IStoreWriter;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import EDU.oswego.cs.dl.util.concurrent.BoundedBuffer;
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
public final class AsynchroneWriter implements IStoreWriter
{
    // public static final String REAL_STORE_WRITER_NAME = "realStoreWriter";

    private static final int DEFAULT_BUFFER_SIZE = 50;

    private static final int DEFAULT_MAX_THREAD_POOL_SIZE = 5;

    private static Logger sLog = LoggerFactory.getLogger(AsynchroneWriter.class);

    private final PooledExecutor mExecutor;

    @Resource(name = "realStoreWriter")
    private IStoreWriter mRealWriter;

    public AsynchroneWriter()
    {
        this(DEFAULT_BUFFER_SIZE, DEFAULT_MAX_THREAD_POOL_SIZE);
    }

    public AsynchroneWriter(int pBufferSize, int pMaxTreadPoolSize)
    {
        BoundedBuffer tBuffer = new BoundedBuffer(pBufferSize);
        mExecutor = new PooledExecutor(tBuffer, DEFAULT_MAX_THREAD_POOL_SIZE);
        sLog.info("Start PoolExecutor of AsynchroneJdbcLogger with " + pMaxTreadPoolSize + " Threads and a buffer of ["
            + pBufferSize + "].");
    }

    /**
     * @see org.jmonitoring.core.log.IStoreWriter#writeExecutionFlow( ExecutionFlowPO)
     */
    public void writeExecutionFlow(ExecutionFlowPO pExecutionFlow)
    {
        try
        {
            mExecutor.execute(new AsynchroneTask(pExecutionFlow, mRealWriter));
            sLog.info("Added new ExecutionFlow to List " + pExecutionFlow);
        } catch (InterruptedException e)
        {
            sLog.error("Unable to add new ExecutionFlow to List " + pExecutionFlow, e);
        }
    }
}
