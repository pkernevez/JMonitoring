package org.jmonitoring.console.gwt.server.common;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

import java.awt.Color;

import org.jmonitoring.core.tests.JMonitoringTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = {"/color-test.xml" })
public class ColorManagerTest extends JMonitoringTestCase
{
    @Autowired
    private ColorManager colorMgr;

    @Test
    public void testCalculColor()
    {
        assertNotNull(colorMgr.calculColor(""));
        assertNotNull(colorMgr.calculColor("ldkfmlkfs"));
        assertNotNull(colorMgr.calculColor(null));
        assertTrue(colorMgr.calculColor("") != colorMgr.calculColor("ldkfmlkfs"));
    }

    @Test
    public void testGetColor()
    {
        String tGroup = "MyGroup";
        assertEquals(new Color(0xda, 0x0e, 0x3e), colorMgr.getColor(tGroup));
        assertEquals(new Color(0xda, 0x0e, 0x3e), colorMgr.getColor(tGroup));
    }
}
