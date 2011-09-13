package org.jmonitoring.agent.store.impl;

import org.jmonitoring.agent.store.PostProcessor;
import org.jmonitoring.core.domain.ExecutionFlowPO;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class DefaultProcessor implements PostProcessor
{

    public boolean process(ExecutionFlowPO pFlow)
    {
        return true;
    }

}
