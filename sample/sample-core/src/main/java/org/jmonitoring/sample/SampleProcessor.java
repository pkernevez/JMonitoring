package org.jmonitoring.sample;

import org.jmonitoring.agent.store.PostProcessor;
import org.jmonitoring.core.domain.ExecutionFlowPO;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class SampleProcessor implements PostProcessor
{

    public boolean process(ExecutionFlowPO pFlow)
    {
        if (pFlow.getFirstMethodCall().getClassName()
                 .equals("org.jmonitoring.sample.console.SampleAlreadyWeavedActionIn"))
        {
            pFlow.setFirstClassName(pFlow.getFirstMethodCall().getClassName() + " Modified");
            pFlow.setFirstMethodName(pFlow.getFirstMethodCall().getMethodName() + " Modified");
        } else
        {
            pFlow.setFirstClassName(pFlow.getFirstMethodCall().getClassName());
            pFlow.setFirstMethodName(pFlow.getFirstMethodCall().getMethodName());
        }
        return true;
    }

}
