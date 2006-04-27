package org.jmonitoring.core.persistence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class HibernateManager
{

    private static final String HIBERNATE_CFG_XML = "hibernate.cfg.xml";

    /**
     * Logger of the class
     */
    private static Log logger = LogFactory.getLog(HibernateManager.class);

    private static SessionFactory sSessionFactory;

    private static Configuration sHConfig;

    private static synchronized SessionFactory getSessionFactory()
    {
        if (sSessionFactory == null)
        {
            sHConfig = new Configuration();
            sHConfig.configure(HIBERNATE_CFG_XML);
            sSessionFactory = sHConfig.buildSessionFactory();
            logger.info("Hibernate SessionFactory loaded with configuration");
        }
        return sSessionFactory;
    }

    /**
     * Accessor.
     * 
     * @return the Hibernate configuration
     */
    public static Configuration getConfig()
    {
        if (sSessionFactory == null)
        {
            getSessionFactory();
        }
        return sHConfig;
    }

    /**
     * Get a new Hibernate Session
     * 
     * @return The new session.
     * @todo s'appuyer sur SessionFactory.getCurrentSession() et JTA
     */
    public static Session getSession()
    {
        Session tSession = getSessionFactory().openSession();
        //getSessionFactory().getCurrentSession();
        logger.info("Hibernate Session Opened");
        return tSession;

    }
}
