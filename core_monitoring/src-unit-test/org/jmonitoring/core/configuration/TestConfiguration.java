package org.jmonitoring.core.configuration;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.awt.Color;

import junit.framework.TestCase;

public class TestConfiguration extends TestCase
{

    public void testGetColor()
    {
        String tGroupName = "TestColor";
        Color tColor = Configuration.getInstance().getColor(tGroupName);
        assertEquals(162, tColor.getRed());
        assertEquals(150, tColor.getGreen());
        assertEquals(112, tColor.getBlue());
        tGroupName = "TestColors";
        tColor = Configuration.getInstance().getColor(tGroupName);
        assertEquals(124, tColor.getRed());
        assertEquals(172, tColor.getGreen());
        assertEquals(74, tColor.getBlue());
    }
}
