package org.jmonitoring.core.configuration;


/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

import java.awt.Color;

import junit.framework.TestCase;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.jmonitoring.core.configuration.BadExecutionflowDao;
import org.jmonitoring.core.dao.ExecutionFlowDAO;

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

    public void testGetStoreClass() throws ConfigurationException
    {
        Configuration tConfig =  Configuration.getInstance();
        
        assertEquals(ExecutionFlowDAO.class, tConfig.getExecutionFlowDaoClass());
        
        PropertiesConfiguration tProperties = new PropertiesConfiguration("jmonitoring.properties");
        tProperties.clearProperty("execution.dao.class");
        tConfig.loadConfiguration(tProperties);
        assertEquals(ExecutionFlowDAO.class, tConfig.getExecutionFlowDaoClass());
        
        tProperties.setProperty("execution.dao.class", "lklk");
        tConfig.loadConfiguration(tProperties);
        assertEquals(ExecutionFlowDAO.class, tConfig.getExecutionFlowDaoClass());

        tProperties.setProperty("execution.dao.class", org.jmonitoring.core.store.impl.StoreFactory.class.getName());
        tConfig.loadConfiguration(tProperties);
        assertEquals(ExecutionFlowDAO.class, tConfig.getExecutionFlowDaoClass());

        tProperties.setProperty("execution.dao.class", ExecutionFlowDAO.class.getName());
        tConfig.loadConfiguration(tProperties);
        assertEquals(ExecutionFlowDAO.class, tConfig.getExecutionFlowDaoClass());
        
        tProperties.setProperty("execution.dao.class", BadExecutionflowDao.class.getName());
        tConfig.loadConfiguration(tProperties);
        assertEquals(BadExecutionflowDao.class, tConfig.getExecutionFlowDaoClass());

        tConfig.setExecutionFlowDaoClass(ExecutionFlowDAO.class);
        assertEquals(ExecutionFlowDAO.class, Configuration.getInstance().getExecutionFlowDaoClass());
        
    }
    
    public void testSpring() throws Throwable
    {
        try
        {
            // InputStream input = new FileInputStream(
            // "D:/Developpement/OCTO/forge/jmonitoring/trunk/core_monitoring/bin/bean.xml");
            // BeanFactory factory = new XmlBeanFactory(input);
            // IConnectionManager tManager = (IConnectionManager) factory.getBean("ConnectionManager");
            // ApplicationContext tContext = new ClassPathXmlApplicationContext("bean.xml");
            // IConnectionManager tManager = (IConnectionManager)tContext.getBean("ConnectionManager");
            // System.out.println(tManager.getClass().toString());
        } catch (Throwable t)
        {
            t.printStackTrace();
            throw t;
        }
    }
}
