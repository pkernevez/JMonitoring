package org.jmonitoring.agent.aspect;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class PerfomanceConfig
{
    private static String[] sSpringFileNames = new String[] {"/default-jmonitoring-context.xml" };

    public static synchronized void setSpringFileNames(String[] pFileNames)
    {
        sSpringFileNames = pFileNames;
        loadContext();
    }

    static ApplicationContext sApplicationContext;

    static void loadContext()
    {
        // TODO Manage default and custom configuration
        ClassPathXmlApplicationContext tContext = new ClassPathXmlApplicationContext(sSpringFileNames);
        sApplicationContext = tContext;
    }

}
