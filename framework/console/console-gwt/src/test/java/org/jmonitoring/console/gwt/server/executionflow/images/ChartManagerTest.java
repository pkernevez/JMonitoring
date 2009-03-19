package org.jmonitoring.console.gwt.server.executionflow.images;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class ChartManagerTest extends TestCase
{

    @Test
    public void testGetPositionSimple()
    {
        Map<String, Integer> tGroupOrder = new HashMap<String, Integer>();
        ChartManager tManager = new ChartManager(tGroupOrder);
        assertEquals(1, tManager.getPosition("group1"));
    }

    @Test
    public void testGetPositionWithAutoComplete()
    {
        Map<String, Integer> tGroupOrder = new HashMap<String, Integer>();
        tGroupOrder.put("group1", 1);
        tGroupOrder.put("group2", 2);

        ChartManager tManager = new ChartManager(tGroupOrder);
        assertEquals(1, tManager.getPosition("group1"));
        assertEquals(2, tManager.getPosition("group2"));
        assertEquals(3, tManager.getPosition("group3"));
        assertEquals(4, tManager.getPosition("group4"));
        assertEquals(3, tManager.getPosition("group3"));
    }

    @Test
    public void testGetPositionWithSpring()
    {
        ClassPathXmlApplicationContext tApplicatonContext =
            new ClassPathXmlApplicationContext(new String[] {"/chart-manager-test.xml", "/color-test.xml",
                "/formater-test.xml" });
        ChartManager tManager = (ChartManager) tApplicatonContext.getBean("chartManager");
        assertEquals(10, tManager.getPosition("SQL"));
        assertEquals(2, tManager.getPosition("SERVICE"));
    }
}
