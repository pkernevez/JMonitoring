package org.jmonitoring.core.configuration;

import java.awt.Color;

import junit.framework.TestCase;

public class TestGroupConfigurationPO extends TestCase {

    public void testGetColorAsString() {
        GroupConfigurationPO tConf = new GroupConfigurationPO("Toto", new Color(12, 16, 17));
        assertEquals("12, 16, 17", tConf.getColorAsString());
        assertEquals(new Color(12, 16, 17), tConf.getColor());
    }

    public void testSetColorAsString() {
        GroupConfigurationPO tConf = new GroupConfigurationPO("Toto", new Color(12, 16, 17));
        tConf.setColorAsString("14, 18, 19");
        assertEquals("14, 18, 19", tConf.getColorAsString());
        assertEquals(new Color(14, 18, 19), tConf.getColor());

    }

}
