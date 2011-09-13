package org.jmonitoring.agent.store;

import org.jmonitoring.core.domain.ExecutionFlowPO;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class MockProcessor implements PostProcessor
{
    private static int sNbCall = 0;
    private static boolean sKeepFlow = false;
    
    public boolean process(ExecutionFlowPO pFlow)
    {
        sNbCall++;
        return sKeepFlow;
    }

    /**
     * @return the nbCall
     */
    public static int getNbCall()
    {
        return sNbCall;
    }

    /**
     * @param pNbCall the nbCall to set
     */
    public static void clear()
    {
        sNbCall = 0;
    }

    public static void setKeepFlow(boolean pKeepFlow)
    {
        MockProcessor.sKeepFlow = pKeepFlow;
    }

}
