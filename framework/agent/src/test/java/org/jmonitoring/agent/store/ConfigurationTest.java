package org.jmonitoring.agent.store;

import junit.framework.TestCase;

import org.jmonitoring.core.configuration.SpringConfigurationUtil;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class ConfigurationTest extends TestCase
{
    public void testDefaultConfiguration()
    {
        assertNotNull("Default configuration shoul be provided !", SpringConfigurationUtil.getBean("storeManager"));
    }
}
