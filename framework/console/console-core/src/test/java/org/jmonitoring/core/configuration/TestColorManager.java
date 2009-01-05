package org.jmonitoring.core.configuration;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

import java.awt.Color;

import org.jmonitoring.core.test.JMonitoringTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = {"/formater-test.xml", "/color-test.xml" })
public class TestColorManager extends JMonitoringTestCase
{
    @Autowired
    private ColorManager mColorMgr;

    @Test
    public void testCalculColor()
    {
        assertNotNull(mColorMgr.calculColor(""));
        assertNotNull(mColorMgr.calculColor("ldkfmlkfs"));
        assertNotNull(mColorMgr.calculColor(null));
        assertTrue(mColorMgr.calculColor("") != mColorMgr.calculColor("ldkfmlkfs"));
    }

    @Test
    public void testGetColor()
    {
        String tGroup = "MyGroup";
        assertEquals(new Color(0xda, 0x0e, 0x3e), mColorMgr.getColor(tGroup));
        assertEquals(new Color(0xda, 0x0e, 0x3e), mColorMgr.getColor(tGroup));
    }
}
