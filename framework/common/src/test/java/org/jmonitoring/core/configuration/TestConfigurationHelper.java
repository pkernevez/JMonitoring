package org.jmonitoring.core.configuration;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import junit.framework.TestCase;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestConfigurationHelper extends TestCase
{

    public void testReload()
    {
        PropertiesConfiguration tConf = ConfigurationHelper.getInstance();
        assertEquals("1", tConf.getString("asynchronelogger.threadpool.size"));
        tConf.setProperty("asynchronelogger.threadpool.size", "2");
        tConf.setProperty("asynchronelogger", "3");
        assertEquals("2", tConf.getString("asynchronelogger.threadpool.size"));
        assertEquals("3", tConf.getString("asynchronelogger"));
        tConf = ConfigurationHelper.reload();
        assertEquals("1", tConf.getString("asynchronelogger.threadpool.size"));
        assertNull(tConf.getString("asynchronelogger"));
        
    }

    public void testGetParam() throws ConfigurationException
    {
        ConfigurationHelper.reload();
        PropertiesConfiguration tConf = ConfigurationHelper.getInstance();
        assertEquals("1", tConf.getString("asynchronelogger.threadpool.size"));
        assertTrue(tConf.getInt("asynchronelogger.threadpool.size") >= 1);
        assertNotNull(tConf.getString("format.ihm.date"));
        assertNotNull(tConf.getString("format.ihm.time"));
        assertNotNull(tConf.getString("xml.logger.dir"));
        assertFalse(tConf.getBoolean("xml.file.per.thread"));
    }

}
