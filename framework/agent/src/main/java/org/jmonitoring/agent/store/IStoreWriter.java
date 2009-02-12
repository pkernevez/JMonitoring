package org.jmonitoring.agent.store;

import org.jmonitoring.core.domain.ExecutionFlowPO;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * @author pke
 * 
 * @todo To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 *       Templates
 * @todo change the namespace of this package
 */
public interface IStoreWriter
{
    public static final String STORE_WRITER_NAME = "storeWriter";

    /**
     * Write the current MeasurePointTree in a store.
     * 
     * @param pExecutionFlow The flow to insert into the store.
     */
    void writeExecutionFlow(ExecutionFlowPO pExecutionFlow);

}
