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

    private static final String DEFAULT_GLOBAL_SPRING_FILE_NAME = "/jmonitoring-global-default.xml";

    private static final String GLOBAL_SPRING_FILE_NAME = "/jmonitoring-global.xml";

    private static final String DEFAULT_THREAD_SPRING_FILE_NAME = "/jmonitoring-thread-default.xml";

    private static final String THREAD_SPRING_FILE_NAME = "/jmonitoring-thread.xml";

    private static ApplicationContext sGlobalContext;

    private static ThreadLocal<ApplicationContext> sContext = new ThreadLocal<ApplicationContext>();

    private static boolean sTestMode = false;

    private static final Logger sLog = LoggerFactory.getLogger(SpringConfigurationUtil.class);

    private SpringConfigurationUtil()
    {
    }

    public static Object getBean(String pString)
    {
        ApplicationContext tContext = sContext.get();
        if (tContext == null)
        {
            tContext = loadContext();
            sContext.set(tContext);
        }
        return tContext.getBean(pString);
    }

    public static ApplicationContext loadContext()
    {
        ApplicationContext tGlobal = (sGlobalContext == null ? loadGlobalContext() : sGlobalContext);
        ClassPathXmlApplicationContext tThreadContext;
        try
        {
            tThreadContext = new ClassPathXmlApplicationContext(new String[] {THREAD_SPRING_FILE_NAME }, tGlobal);
        } catch (BeansException e)
        {
            sLog.info("Fail to load thread specific context, use the default");
            tThreadContext =
                new ClassPathXmlApplicationContext(new String[] {DEFAULT_THREAD_SPRING_FILE_NAME }, tGlobal);
        }
        sContext.set(tThreadContext);
        return tThreadContext;
    }

    private static synchronized ApplicationContext loadGlobalContext()
    {
        ClassPathXmlApplicationContext tContext;
        try
        {
            tContext = new ClassPathXmlApplicationContext(GLOBAL_SPRING_FILE_NAME);
        } catch (BeansException e)
        {
            sLog.info("Fail to load global specific context, use the default");
            tContext = new ClassPathXmlApplicationContext(DEFAULT_GLOBAL_SPRING_FILE_NAME);
        }
        sGlobalContext = tContext;
        return sGlobalContext;
    }

    /**
     * @param pContext the context to set
     */
    public static void setContext(ApplicationContext pContext)
    {
        sContext.set(pContext);
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
