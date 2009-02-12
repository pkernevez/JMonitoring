package org.jmonitoring.core.tests;

import org.junit.Test;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class JMonitoringTestCaseTest extends JMonitoringTestCase
{
    @Test
    public void testApplicationContext()
    {
        assertNotNull(applicationContext);
    }
}
