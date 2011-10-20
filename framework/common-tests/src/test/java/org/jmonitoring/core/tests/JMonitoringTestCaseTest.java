package org.jmonitoring.core.tests;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

@ContextConfiguration(locations = {"/default-test.xml" })
public class JMonitoringTestCaseTest extends JMonitoringTestCase
{
    @Test
    public void testApplicationContext()
    {
        assertNotNull(applicationContext);
    }
}
