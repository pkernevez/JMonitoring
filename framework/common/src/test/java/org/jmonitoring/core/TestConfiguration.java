package org.jmonitoring.core;
/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

import junit.framework.TestCase;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.jmonitoring.core.configuration.ConfigurationFactory;
import org.jmonitoring.core.store.impl.XmlFileLogger;

public class TestConfiguration extends TestCase
{

    public void testGetParam() throws ConfigurationException
    {
        PropertiesConfiguration tConf = ConfigurationFactory.getInstance();
        assertEquals(XmlFileLogger.class.getName(), tConf.getString("measurepoint.logger.class"));
        assertTrue(tConf.getInt("asynchronelogger.threadpool.size") >= 1);
        assertNotNull(tConf.getString("format.ihm.date"));
        assertNotNull(tConf.getString("format.ihm.time"));
        assertNotNull(tConf.getString("xml.logger.dir"));
        assertFalse(tConf.getBoolean("xml.file.per.thread"));
    }
}
