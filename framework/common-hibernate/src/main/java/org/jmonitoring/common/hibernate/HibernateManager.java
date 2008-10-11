package org.jmonitoring.common.hibernate;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
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

public final class HibernateManager {

    private static final String HIBERNATE_CFG_XML = "jmonitoring.hibernate.xml";

    private static final String HIBERNATE_CFG_PROPERTIES = "jmonitoring.hibernate.properties";

    private HibernateManager() {
    }

    /**
     * Logger of the class
     */
    private static Log sLogger = LogFactory.getLog(HibernateManager.class);

    private static SessionFactory sSessionFactory;

    private static Configuration sHConfig;

    private static synchronized SessionFactory getSessionFactory() {
        return getSessionFactory(HIBERNATE_CFG_XML, HIBERNATE_CFG_PROPERTIES);
    }

    static synchronized SessionFactory getSessionFactory(String pMappingFileName, String pPropertiesFileName) {
        if (sSessionFactory == null) {
            sHConfig = new Configuration();
            Properties properties = new Properties();
            try {
                checkUniquenessOfConfigurationFile(pMappingFileName);
                checkUniquenessOfConfigurationFile(pPropertiesFileName);
                properties.load(HibernateManager.class.getClassLoader().getResourceAsStream(pPropertiesFileName));
            } catch (IOException e) {
                sLogger.error("Le fichier " + pPropertiesFileName + " n'existe pas.");
            }
            sHConfig.setProperties(properties);
            sHConfig.configure(pMappingFileName);
            sSessionFactory = sHConfig.buildSessionFactory();
            sLogger.info("Hibernate SessionFactory loaded with configuration");
        }
        return sSessionFactory;
    }

    private static void checkUniquenessOfConfigurationFile(String pMappingFileName) {
        int tCount = 0;
        StringBuffer tBuffer = new StringBuffer();
        tBuffer.append("The configuration file [").append(pMappingFileName).append(
                "]has been found in multiple copy:\n");
        try {
            for (Enumeration tEnum = HibernateManager.class.getClassLoader().getResources(pMappingFileName); tEnum.hasMoreElements();) {
                tCount++;
                tBuffer.append(((URL) tEnum.nextElement()).getPath()).append("\n");
            }
            if (tCount > 1) {
                sLogger.warn(tBuffer.toString());
            }
        } catch (IOException e) {
            sLogger.fatal("Unable to find any file [" + pMappingFileName + "]");
            throw new RuntimeException("Unable to find any file [" + pMappingFileName + "]");
        }

    }

    /**
     * Accessor.
     * 
     * @return the Hibernate configuration
     */
    public static Configuration getConfig() {
        if (sSessionFactory == null) {
            getSessionFactory();
        }
        return sHConfig;
    }

    public static Statistics getStats() {
        return getSessionFactory().getStatistics();
    }

    /**
     * Get a new Hibernate Session
     * 
     * @return The new session.
     * @todo s'appuyer sur SessionFactory.getCurrentSession() et JTA
     */
    public static Session getSession() {
        // Session tSession = getSessionFactory().openSession();
        Session tSession = getSessionFactory().getCurrentSession();
        sLogger.info("Hibernate Session Opened");
        return tSession;

    }
}
