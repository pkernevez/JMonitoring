package org.jmonitoring.agent.store.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.jmonitoring.agent.store.IStoreWriter;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static Logger sLog = LoggerFactory.getLogger(MockWriter.class);

    private static Vector<ExecutionFlowPO> sExecutionsFlows = new Vector<ExecutionFlowPO>();

    public static synchronized void clear()
    {
        sExecutionsFlows = new Vector<ExecutionFlowPO>();
    }

    /**
     * Accessor.
     * 
     * @return The number of <code>ExecutionFlow</code> already log by the ThreadPool.
     */
    public static int getNbLog()
    {
        return sExecutionsFlows.size();
    }

    public void writeExecutionFlow(ExecutionFlowPO pExecutionFlow)
    {
        sExecutionsFlows.add(pExecutionFlow);
        sLog.info("Log ExecutionFlow, increment=[" + getNbLog() + "], Thread=" + Thread.currentThread().getName());
    }

    public static Vector<ExecutionFlowPO> getExecutionsFlows()
    {
        return sExecutionsFlows;
    }

    public static class SlowMockWriter extends MockWriter{

        @Override
        public void writeExecutionFlow(ExecutionFlowPO pExecutionFlow)
        {
            try
            {
                Thread.sleep(50);
            } catch (InterruptedException e)
            {
                sLog.error("Unable to sleep !", e);
            }
            super.writeExecutionFlow(pExecutionFlow);
        }
        
    }
}
