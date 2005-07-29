package net.kernevez.performance.log;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.lang.reflect.Constructor;

import net.kernevez.performance.configuration.Configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Permet d'obtenir un IMeasurePointLogger en focntion de la configuration.
 * 
 * @author pke
 */
public final class MeasurePointTreeLoggerFactory
{
    /** Mise en cache du constructeur du <code>IMeasurePointTreeLogger</code> � utiliser. */
    private static Constructor sConstructor;

    private MeasurePointTreeLoggerFactory()
    {
    }

    /**
     * Factory.
     * 
     * @return Une instance de <code>IMeasurePointTreeLogger</code> pour les logs en fonction du param�trage.
     */
    public static IMeasurePointTreeLogger getNewInstance()
    {
        Class tStoreClass = Configuration.getInstance().getMeasurePointStoreClass();
        if (sConstructor == null)
        {
            try
            {
                sConstructor = tStoreClass.getConstructor(new Class[0]);
            } catch (Throwable t)
            {
                Log tLog = LogFactory.getLog(MeasurePointTreeLoggerFactory.class);
                tLog.error("Unable to get Constructor for the class MEASURE_POINT_LOGGER_CLASS", t);
                throw new RuntimeException("Unable to find constructor without parameter for class ["
                                + tStoreClass + "]", t);
            }
        }
        Object tResult;
        try
        {
            tResult = sConstructor.newInstance(new Object[0]);
        } catch (Exception e)
        {
            throw new RuntimeException("Unable to create an instance of class [" + tStoreClass + "]", e);
        }

        return (IMeasurePointTreeLogger) tResult;
    }
}
