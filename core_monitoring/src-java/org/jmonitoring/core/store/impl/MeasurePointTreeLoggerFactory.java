package org.jmonitoring.core.store.impl;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.lang.reflect.Constructor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jmonitoring.core.configuration.Configuration;
import org.jmonitoring.core.store.IStoreWriter;

/**
 * Permet d'obtenir un IMeasurePointLogger en focntion de la configuration.
 * 
 * @author pke
 */
public final class MeasurePointTreeLoggerFactory
{
    /** Mise en cache du constructeur du <code>IMeasurePointTreeLogger</code> à utiliser. */
    private static Constructor sConstructor;

    private MeasurePointTreeLoggerFactory()
    {
    }

    /**
     * Factory.
     * 
     * @return Une instance de <code>IMeasurePointTreeLogger</code> pour les logs en fonction du paramètrage.
     */
    public static IStoreWriter getNewInstance()
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
                throw new RuntimeException("Unable to find constructor without parameter for class [" + tStoreClass
                                + "]", t);
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

        return (IStoreWriter) tResult;
    }
}
