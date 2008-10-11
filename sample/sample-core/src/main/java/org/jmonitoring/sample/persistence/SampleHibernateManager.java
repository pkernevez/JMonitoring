package org.jmonitoring.sample.persistence;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.stat.Statistics;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.jmonitoring.common.hibernate.HibernateManager;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public final class SampleHibernateManager {

    private static final String HIBERNATE_CFG_XML = "jmonitoring-sample.hibernate.xml";

    private static final String HIBERNATE_CFG_PROPERTIES = "jmonitoring-sample.hibernate.properties";

    private SampleHibernateManager() {
    }

    /**
     * Logger of the class
     */
    private static Log sLogger = LogFactory.getLog(SampleHibernateManager.class);

    private static SessionFactory sSessionFactory;

    private static Configuration sHConfig;

    private static synchronized SessionFactory getSessionFactory() {
        if (sSessionFactory == null) {
            sHConfig = new Configuration();
            Properties properties = new Properties();
            try {
                properties.load(SampleHibernateManager.class.getClassLoader().getResourceAsStream(
                        HIBERNATE_CFG_PROPERTIES));
                sLogger.info("Properties file [" + HIBERNATE_CFG_PROPERTIES + "] Loaded");
            } catch (IOException e) {
                sLogger.error("Le fichier " + HIBERNATE_CFG_PROPERTIES + " n'existe pas.");
            }
            sHConfig.setProperties(properties);
            sHConfig.configure(HIBERNATE_CFG_XML);
            sSessionFactory = sHConfig.buildSessionFactory();
            sLogger.info("Hibernate SessionFactory loaded with configuration [" + HIBERNATE_CFG_XML + "]");
        }
        return sSessionFactory;
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
        Session tSession = getSessionFactory().openSession();
        // getSessionFactory().getCurrentSession();
        sLogger.info("Hibernate Session Opened");
        return tSession;

    }

    public static void createSchema() {
        Configuration tConfig = SampleHibernateManager.getConfig();
        SchemaExport tDdlexport = new SchemaExport(tConfig);

        tDdlexport.create(true, true);
    }

    public static void dropSchema() {
        Configuration tConfig = HibernateManager.getConfig();
        SchemaExport tDdlexport = new SchemaExport(tConfig);

        tDdlexport.drop(true, true);
    }

    public static void flush() {
        getSession().flush();
    }

    public static void clear() {
        getSession().clear();
    }
}
