package org.jmonitoring.agent.store;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jmonitoring.core.configuration.ConfigurationException;
import org.jmonitoring.core.configuration.ConfigurationHelper;
import org.jmonitoring.core.configuration.MeasureException;

/**
 * Permet d'obtenir un IMeasurePointLogger en focntion de la configuration.
 * 
 * @author pke
 */
public final class StoreFactory
{
    /** Mise en cache du constructeur du <code>IMeasurePointTreeLogger</code> � utiliser. */
    private static Constructor<IStoreWriter> sConstructor;

    private static Log sLog = LogFactory.getLog(StoreFactory.class);

    private StoreFactory()
    {
    }

    /**
     * Factory.
     * 
     * @return Une instance de <code>IMeasurePointTreeLogger</code> pour les logs en fonction du param�trage.
     */
    public static IStoreWriter getWriter()
    {
        if (sConstructor == null)
        {
            Class<IStoreWriter> tStoreClass = getMeasurePointStoreClass();
            try
            {
                sConstructor = tStoreClass.getConstructor(new Class[0]);
            } catch (SecurityException e)
            {
                sLog.error("Unable to get Constructor for the class MEASURE_POINT_LOGGER_CLASS", e);
                throw new MeasureException("Unable to find constructor without parameter for class [" + tStoreClass
                                + "]", e);
            } catch (NoSuchMethodException e)
            {
                sLog.error("Unable to get Constructor for the class MEASURE_POINT_LOGGER_CLASS", e);
                throw new MeasureException("Unable to find constructor without parameter for class [" + tStoreClass
                                + "]", e);
            }
        }
        try
        {
            return sConstructor.newInstance(new Object[0]);
        } catch (IllegalArgumentException e)
        {
            throw new MeasureException("Unable to create an instance of class [" + sConstructor.getDeclaringClass()
                            + "]", e);
        } catch (InstantiationException e)
        {
            throw new MeasureException("Unable to create an instance of class [" + sConstructor.getDeclaringClass()
                            + "]", e);
        } catch (IllegalAccessException e)
        {
            throw new MeasureException("Unable to create an instance of class [" + sConstructor.getDeclaringClass()
                            + "]", e);
        } catch (InvocationTargetException e)
        {
            throw new MeasureException("Unable to create an instance of class [" + sConstructor.getDeclaringClass()
                            + "]", e);
        }
    }

    @SuppressWarnings("unchecked")
    private static Class<IStoreWriter> getMeasurePointStoreClass()
    {
        String tLoggerClassName = ConfigurationHelper.getString(ConfigurationHelper.STORE_CLASS);
        try
        {
            return (Class<IStoreWriter>) Class.forName(tLoggerClassName);
        } catch (ClassNotFoundException e)
        {
            sLog.error("Unable to create LogClass [" + tLoggerClassName + "]", e);
            throw new ConfigurationException("Unable to create LogClass [" + tLoggerClassName + "]", e);
        } catch (NoClassDefFoundError e)
        {
            sLog.error("Unable to create LogClass [" + tLoggerClassName + "]", e);
            throw new ConfigurationException("Unable to create LogClass [" + tLoggerClassName + "]", e);
        }
    }

    /** For test purpose. */
    static synchronized void clear()
    {
        sConstructor = null;
    }
}
