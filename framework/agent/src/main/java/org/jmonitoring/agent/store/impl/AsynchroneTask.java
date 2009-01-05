package org.jmonitoring.agent.store.impl;

import org.jmonitoring.agent.store.IStoreWriter;
import org.jmonitoring.core.domain.ExecutionFlowPO;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class AsynchroneTask implements Runnable
{
    // TODO NEtoyage
    // private static ThreadLocal<IStoreWriter> sWriter = new ThreadLocal<IStoreWriter>();
    private final IStoreWriter mWriter;

    private final ExecutionFlowPO mExecutionFlowToLog;

    // private final ApplicationContext mContext;

    public AsynchroneTask(ExecutionFlowPO pExecutionFlowToLog, IStoreWriter pRealWriter)
    {
        mExecutionFlowToLog = pExecutionFlowToLog;
        // mContext = pContext;
        mWriter = pRealWriter;
    }

    public void run()
    {
        // IStoreWriter tWriter = sWriter.get();
        // if (tWriter == null)
        // {
        // tWriter = (IStoreWriter) mContext.getBean(AsynchroneWriter.REAL_STORE_WRITER_NAME);
        // sWriter.set(tWriter);
        // }
        mWriter.writeExecutionFlow(mExecutionFlowToLog);
    }
}
