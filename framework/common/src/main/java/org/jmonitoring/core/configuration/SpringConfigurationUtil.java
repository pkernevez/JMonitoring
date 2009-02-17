package org.jmonitoring.core.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public final class SpringConfigurationUtil
{

    private static final String DEFAULT_SPRING_FILE_NAME = "/jmonitoring-default.xml";

    private static final String SPRING_FILE_NAME = "/jmonitoring.xml";

    private static boolean sTestMode = false;

    private static final Logger sLog = LoggerFactory.getLogger(SpringConfigurationUtil.class);

    private static ApplicationContext sContext = loadContext();

    private SpringConfigurationUtil()
    {
    }

    public static Object getBean(String pString)
    {
        return sContext.getBean(pString);
    }

    private static synchronized ApplicationContext loadContext()
    {
        ClassPathXmlApplicationContext tContext;
        try
        {
            tContext = new ClassPathXmlApplicationContext(SPRING_FILE_NAME);
        } catch (BeansException e)
        {
            sLog.info("Fail to load global specific context, use the default");
            try
            {
                tContext = new ClassPathXmlApplicationContext(DEFAULT_SPRING_FILE_NAME);
            } catch (BeansException e2)
            {
                sLog.error("Unable to find any configuration, check jmonitoring configuration");
                throw e;
            }
        }
        return tContext;
    }

    /**
     * @param pContext the context to set
     */
    public static void setContext(ApplicationContext pContext)
    {
        sContext = pContext;
        sTestMode = true;
    }

    /**
     * @return the defaultContext
     */
    public static boolean isTestMode()
    {
        return sTestMode;
    }

}
