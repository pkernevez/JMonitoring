package org.jmonitoring.sample.persistence;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public final class SpringSampleConfigurationUtil
{
    private static final String GLOBAL_SPRING_FILE_NAME = "/jmonitoring-sample.xml";

    private static ApplicationContext sGlobalContext;

    private SpringSampleConfigurationUtil()
    {
    }

    public static Object getBean(String pString)
    {
        if (sGlobalContext == null)
        {
            sGlobalContext = loadContext();
        }
        return sGlobalContext.getBean(pString);
    }

    private static synchronized ApplicationContext loadContext()
    {
        sGlobalContext = new ClassPathXmlApplicationContext(GLOBAL_SPRING_FILE_NAME);
        return sGlobalContext;
    }

}
