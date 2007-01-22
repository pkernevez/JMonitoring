package org.jmonitoring.core.configuration;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class ConfigurationFactory
{
    private static PropertiesConfiguration sConfiguration;

    private static Log sLog = LogFactory.getLog(ConfigurationFactory.class);

    private ConfigurationFactory()
    {
    }

    public static PropertiesConfiguration getInstance()
    {
        PropertiesConfiguration tConfig = sConfiguration;
        if (tConfig == null)
        {
            try
            {
                tConfig = new PropertiesConfiguration("jmonitoring.properties");
            } catch (org.apache.commons.configuration.ConfigurationException e)
            {
                sLog.fatal("The file [jmonitoring.properties] can't be found in classpath");
                throw new ConfigurationException("The file [jmonitoring.properties] can't be found in classpath");
            }
            sConfiguration = tConfig;
        }
        return tConfig;
    }
}
