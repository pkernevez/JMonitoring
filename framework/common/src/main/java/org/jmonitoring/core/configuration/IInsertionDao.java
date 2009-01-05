package org.jmonitoring.core.configuration;

import org.jmonitoring.core.domain.ExecutionFlowPO;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/
public interface IInsertionDao
{

    /**
     * Insert a full execution flow in database.
     * 
     * @param pExecutionFlow The <code>ExecutionFlow</code> to write into the database.
     * @return The primary key of the inserted <code>ExecutionFlow</code>.
     */
    public abstract int insertFullExecutionFlow(ExecutionFlowPO pExecutionFlow);

    /** Count the number of flow into database. */
    public abstract int countFlows();

}