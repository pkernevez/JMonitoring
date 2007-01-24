package org.jmonitoring.core.configuration;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

import java.awt.Color;

import junit.framework.TestCase;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.jmonitoring.core.dao.ConsoleDao;

public class TestColorHelper extends TestCase
{

    public void testCalculColor()
    {
        assertNotNull(ColorHelper.calculColor(""));
        assertNotNull(ColorHelper.calculColor("ldkfmlkfs"));
        assertNotNull(ColorHelper.calculColor(null));
        assertTrue(ColorHelper.calculColor("") != ColorHelper.calculColor("ldkfmlkfs"));
    }

    //    
    // public void testSpring() throws Throwable
    // {
    // try
    // {
    // // InputStream input = new FileInputStream(
    // // "D:/Developpement/OCTO/forge/jmonitoring/trunk/core_monitoring/bin/bean.xml");
    // // BeanFactory factory = new XmlBeanFactory(input);
    // // IConnectionManager tManager = (IConnectionManager) factory.getBean("ConnectionManager");
    // // ApplicationContext tContext = new ClassPathXmlApplicationContext("bean.xml");
    // // IConnectionManager tManager = (IConnectionManager)tContext.getBean("ConnectionManager");
    // // System.out.println(tManager.getClass().toString());
    // } catch (Throwable t)
    // {
    // t.printStackTrace();
    // throw t;
    // }
    // }
}
