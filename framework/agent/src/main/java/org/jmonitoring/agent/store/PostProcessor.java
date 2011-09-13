package org.jmonitoring.agent.store;

import org.jmonitoring.core.domain.ExecutionFlowPO;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/
/**
 * Allow to change some value into the graph of the execution flow just before its storage.
 */
public interface PostProcessor
{
    /** Allow to change the flow.
     * @return true if we want to keep the flow and send it to the store. If return false, the flow is forgotten.
     *  */
    boolean process(ExecutionFlowPO pFlow);
}
