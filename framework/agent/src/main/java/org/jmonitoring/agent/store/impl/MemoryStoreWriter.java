package org.jmonitoring.agent.store.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jmonitoring.agent.store.IStoreWriter;
import org.jmonitoring.core.domain.ExecutionFlowPO;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class MemoryStoreWriter implements IStoreWriter {
    private static List sListOfExecutionFlow = new ArrayList();

    private static Log sLog = LogFactory.getLog(MemoryStoreWriter.class);

    public void writeExecutionFlow(ExecutionFlowPO pExecutionFlow) {
        sLog.info("Add new ExecutionFlow to memory");
        sListOfExecutionFlow.add(pExecutionFlow);
    }

    public static void clear() {
        sListOfExecutionFlow.clear();
    }

    public static int countFlows() {
        return sListOfExecutionFlow.size();
    }

    public static ExecutionFlowPO getFlow(int pPosition) {
        return (ExecutionFlowPO) sListOfExecutionFlow.get(pPosition);
    }

}
