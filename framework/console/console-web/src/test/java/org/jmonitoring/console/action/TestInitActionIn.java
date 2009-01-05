package org.jmonitoring.console.action;

import org.junit.Test;

import servletunit.struts.MockStrutsTestCase;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestInitActionIn extends MockStrutsTestCase
{
    @Test
    public void testInitActionIn()
    {
        // JMonitoringProcess tProcess = ProcessFactory.getInstance();
        // PKE Remove bad test
        // Delete First Flow
        setRequestPathInfo("/Index");
        actionPerform();
        // verifyForwardPath("/pages/layout/layout.jsp");

        // verifyTilesForward("success", "homepage");
        //
        // HibernateManager.getSession().close();
        // HibernateManager.getSession().beginTransaction();
        // assertTrue(tProcess.doDatabaseExist());

    }
}
