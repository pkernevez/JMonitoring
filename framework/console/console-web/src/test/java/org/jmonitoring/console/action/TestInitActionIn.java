package org.jmonitoring.console.action;

import org.jmonitoring.core.process.JMonitoringProcess;
import org.jmonitoring.core.process.ProcessFactory;

import servletunit.struts.MockStrutsTestCase;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestInitActionIn extends MockStrutsTestCase
{

    public void testInitActionIn()
    {
        JMonitoringProcess tProcess = ProcessFactory.getInstance();

        // Delete First Flow
        setRequestPathInfo("/Index");
        actionPerform();
        verifyForwardPath("/pages/layout/layout.jsp");

        verifyTilesForward("success", "homepage");

        assertTrue(tProcess.doDatabaseExist());

    }
}