package org.jmonitoring.core.store.impl;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jmonitoring.core.common.MeasureException;
import org.jmonitoring.core.configuration.Configuration;
import org.jmonitoring.core.store.IStoreWriter;

/**
 * Permet d'obtenir un IMeasurePointLogger en focntion de la configuration.
 * 
 * @author pke
 */
public final class StoreFactory
{
    /** Mise en cache du constructeur du <code>IMeasurePointTreeLogger</code> à utiliser. */
    private static Constructor sConstructor;

    private static Log sLog = LogFactory.getLog(StoreFactory.class);

    private StoreFactory()
    {
    }

    /**
     * Factory.
     * 
     * @return Une instance de <code>IMeasurePointTreeLogger</code> pour les logs en fonction du paramètrage.
     */
    public static IStoreWriter getWriter()
    {
        Class tStoreClass = Configuration.getInstance().getMeasurePointStoreClass();
        if (sConstructor == null)
        {
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
        Object tResult;
        try
        {
            tResult = sConstructor.newInstance(new Object[0]);
            if (!(tResult instanceof IStoreWriter))
            {
                throw new MeasureException("The writer : [" + tStoreClass + "] is not an instance of IStoreWriter");
            }
        } catch (IllegalArgumentException e)
        {
            throw new MeasureException("Unable to create an instance of class [" + tStoreClass + "]", e);
        } catch (InstantiationException e)
        {
            throw new MeasureException("Unable to create an instance of class [" + tStoreClass + "]", e);
        } catch (IllegalAccessException e)
        {
            throw new MeasureException("Unable to create an instance of class [" + tStoreClass + "]", e);
        } catch (InvocationTargetException e)
        {
            throw new MeasureException("Unable to create an instance of class [" + tStoreClass + "]", e);
        }
        return (IStoreWriter) tResult;
    }

    /** For test purpose. */
    public static void clear()
    {
        sConstructor = null;
    }
}
