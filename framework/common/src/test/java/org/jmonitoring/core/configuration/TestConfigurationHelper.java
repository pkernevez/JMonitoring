package org.jmonitoring.core.configuration;

import java.util.Properties;

import junit.framework.TestCase;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestConfigurationHelper extends TestCase {

    public void testReload() {
        Properties tConf = ConfigurationHelper.getInstance();
        assertEquals("1", tConf.getProperty("asynchronelogger.threadpool.size"));
        tConf.setProperty("asynchronelogger.threadpool.size", "2");
        tConf.setProperty("asynchronelogger", "3");
        assertEquals("2", tConf.getProperty("asynchronelogger.threadpool.size"));
        assertEquals("3", tConf.getProperty("asynchronelogger"));
        tConf = ConfigurationHelper.reload();
        assertEquals("1", tConf.getProperty("asynchronelogger.threadpool.size"));
        assertNull(tConf.getProperty("asynchronelogger"));

    }

    public void testGetParam() throws MeasureException {
        ConfigurationHelper.reload();
        assertEquals("1", ConfigurationHelper.getString("asynchronelogger.threadpool.size"));
        assertTrue(ConfigurationHelper.getInt("asynchronelogger.threadpool.size") >= 1);
        assertNotNull(ConfigurationHelper.getString("format.ihm.date"));
        assertNotNull(ConfigurationHelper.getString("format.ihm.time"));
        assertNotNull(ConfigurationHelper.getString("xml.logger.dir"));
        assertFalse(ConfigurationHelper.getBoolean("xml.file.per.thread"));
    }

}
