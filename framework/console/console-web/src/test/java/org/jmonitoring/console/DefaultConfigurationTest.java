package org.jmonitoring.console;

import junit.framework.TestCase;

import org.jmonitoring.core.configuration.SpringConfigurationUtil;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class DefaultConfigurationTest extends TestCase
{
    public void testAllBean()
    {
        assertNotNull(SpringConfigurationUtil.getBean("sessionFactory"));
        assertNotNull(SpringConfigurationUtil.getBean("hibernateConfiguration"));
        assertNotNull(SpringConfigurationUtil.getBean("formater"));
        assertNotNull(SpringConfigurationUtil.getBean("dao"));
        assertNotNull(SpringConfigurationUtil.getBean("color"));
        // assertNotNull(SpringConfigurationUtil.getBean("dao"));
        // assertNotNull(SpringConfigurationUtil.getBean("dao"));
    }
}
