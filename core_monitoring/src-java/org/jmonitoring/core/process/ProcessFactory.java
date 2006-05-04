package org.jmonitoring.core.process;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class ProcessFactory
{

    /**
     * Use for test.
     */
    private static JMonitoringProcess sMockProcess;

    static void setMockProcess(JMonitoringProcess pProcess)
    {
        sMockProcess = pProcess;
    }
    
    public static JMonitoringProcess getInstance()
    {
        return (sMockProcess == null ? new JMonitoringProcess() : sMockProcess);
    }
    
}
