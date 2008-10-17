package org.jmonitoring.core.configuration;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

import junit.framework.TestCase;

public class TestColorHelper extends TestCase
{

    public void testCalculColor()
    {
        assertNotNull(ColorHelper.calculColor(""));
        assertNotNull(ColorHelper.calculColor("ldkfmlkfs"));
        assertNotNull(ColorHelper.calculColor(null));
        assertTrue(ColorHelper.calculColor("") != ColorHelper.calculColor("ldkfmlkfs"));
    }

    public void testGetColor()
    {
        String tGroup = "MyGroup";
        assertEquals("#da0e3e", ColorHelper.getColor(tGroup));
        assertEquals("#da0e3e", ColorHelper.getColor(tGroup));
        ConfigurationHelper.getInstance().setProperty("group.color." + tGroup, "#456789");
        assertEquals("#456789", ColorHelper.getColor(tGroup));
        ConfigurationHelper.clearProperty("group.color." + tGroup);
        assertEquals("#da0e3e", ColorHelper.getColor(tGroup));
    }
}
