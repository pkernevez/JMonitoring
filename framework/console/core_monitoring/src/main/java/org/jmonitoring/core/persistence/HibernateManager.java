package org.jmonitoring.core.persistence;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.stat.Statistics;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public final class HibernateManager
{

    private static final String HIBERNATE_CFG_XML = "hibernate.cfg.xml";

    private static final String HIBERNATE_CFG_PROPERTIES = "hibernate.cfg.properties";

    private HibernateManager()
    {
    }

    /**
     * Logger of the class
     */
    private static Log sLogger = LogFactory.getLog(HibernateManager.class);

    private static SessionFactory sSessionFactory;

    private static Configuration sHConfig;

    private static synchronized SessionFactory getSessionFactory()
    {
        if (sSessionFactory == null)
        {
            sHConfig = new Configuration();
            Properties properties = new Properties();
            try
            {
                properties.load(HibernateManager.class.getClassLoader().getResourceAsStream(HIBERNATE_CFG_PROPERTIES));
            } catch (IOException e)
            {
                sLogger.error("Le fichier " + HIBERNATE_CFG_PROPERTIES + " n'existe pas.");
            }
            sHConfig.setProperties(properties);
            sHConfig.configure(HIBERNATE_CFG_XML);
            sSessionFactory = sHConfig.buildSessionFactory();
            sLogger.info("Hibernate SessionFactory loaded with configuration");
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

    public static Statistics getStats()
    {
        return getSessionFactory().getStatistics();
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
        // getSessionFactory().getCurrentSession();
        sLogger.info("Hibernate Session Opened");
        return tSession;

    }
}
