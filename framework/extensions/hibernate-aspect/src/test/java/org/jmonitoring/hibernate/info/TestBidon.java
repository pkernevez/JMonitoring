package org.jmonitoring.hibernate.info;

import org.jmonitoring.core.tests.JMonitoringTestCase;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

@ContextConfiguration(locations = {"/memory-test.xml" })
public class TestBidon extends JMonitoringTestCase
{
    @Test
    public void testTest1()
    {
        Bidon tBid = new Bidon();
        tBid.method1();
        tBid.method2();
        tBid.method3();
    }
}
