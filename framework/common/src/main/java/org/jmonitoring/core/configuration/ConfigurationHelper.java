package org.jmonitoring.core.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.PropertyResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public final class ConfigurationHelper
{
    public static final String DAO_STORE_KEY = "execution.dao.class";

    private static Properties sConfiguration = getInstance();

    private static Log sLog = LogFactory.getLog(ConfigurationHelper.class);

    private static final ThreadLocal sTimeFormater = new ThreadLocal();

    // private static final ThreadLocal sDateFormater = new ThreadLocal();

    private static final ThreadLocal sDateTimeFormater = new ThreadLocal();

    private static final ThreadLocal sDateFormater = new ThreadLocal();

    public static final String STORE_CLASS = "measurepoint.logger.class";

    private ConfigurationHelper()
    {
    }

    public static synchronized Properties reload()
    {
        sConfiguration = null;
        sConfiguration = getInstance();
        return sConfiguration;
    }

    static synchronized Properties getInstance()
    {
        Properties tConfig = sConfiguration;
        if (tConfig == null)
        {
            InputStream tStream = ConfigurationHelper.class.getResourceAsStream("/jmonitoring.properties");
            if (tStream == null)
            {
                sLog.fatal("The file [jmonitoring.properties] can't be found in classpath");
                throw new MeasureException("The file [jmonitoring.properties] can't be found in classpath");
            }
            try
            {
                PropertyResourceBundle tBundle = new PropertyResourceBundle(tStream);
                tConfig = new Properties();
                String tCurKey;
                for (Enumeration tEnum = tBundle.getKeys(); tEnum.hasMoreElements();)
                {
                    tCurKey = (String) tEnum.nextElement();
                    tConfig.setProperty(tCurKey, tBundle.getString(tCurKey));
                }

            } catch (IOException e)
            {
                throw new MeasureException("Unable to load properties from  [jmonitoring.properties].");
            }
            sConfiguration = tConfig;
        }
        return tConfig;
    }

    /**
     * Synchronized access to the <code>DateFormater</code> for only Date.
     * 
     * @return The <code>DateFormat</code> to use in the application.
     */
    private static SimpleDateFormat getTimeFormater()
    {
        Object tResult = sTimeFormater.get();
        if (tResult == null)
        {
            String tDateFormat = ConfigurationHelper.getInstance().getProperty("format.ihm.time");
            tResult = new SimpleDateFormat(tDateFormat);
            sTimeFormater.set(tResult);
        }
        return (SimpleDateFormat) tResult;
    }

    /**
     * Synchronized access to the <code>DateFormater</code> for only time.
     * 
     * @return The <code>DateFormat</code> to use in the application.
     */
    private static SimpleDateFormat getDateTimeFormater()
    {
        Object tResult = sDateTimeFormater.get();
        if (tResult == null)
        {
            String tDateTimeFormat = ConfigurationHelper.getInstance().getProperty("format.ihm.date") + " "
                + ConfigurationHelper.getInstance().getProperty("format.ihm.time");
            tResult = new SimpleDateFormat(tDateTimeFormat);
            sDateTimeFormater.set(tResult);
        }
        return (SimpleDateFormat) tResult;
    }

    public static Date parseDate(String pDate) throws ParseException
    {
        return getDateFormater().parse(pDate);
    }

    public static SimpleDateFormat getDateFormater()
    {
        Object tResult = sDateFormater.get();
        if (tResult == null)
        {
            String tDateFormat = ConfigurationHelper.getInstance().getProperty("format.ihm.date");
            tResult = new SimpleDateFormat(tDateFormat);
            sDateFormater.set(tResult);
        }
        return (SimpleDateFormat) tResult;
    }

    public static Date parseTime(String tTime) throws ParseException
    {
        return getTimeFormater().parse(tTime);
    }

    public static String formatTime(Date tTime)
    {
        return getTimeFormater().format(tTime);
    }

    public static Date parseDateTime(String tTime) throws ParseException
    {
        return getDateTimeFormater().parse(tTime);
    }

    public static String formatDateTime(Date tTime)
    {
        return getDateTimeFormater().format(tTime);
    }

    public static String formatDateTime(long tTime)
    {
        return formatDateTime(new Date(tTime));
    }

    public static Object formatDate(Date pDate)
    {
        return getDateFormater().format(pDate);
    }

    public static Constructor getDaoDefaultConstructor()
    {
        String tClassName = getInstance().getProperty(DAO_STORE_KEY);
        if (tClassName == null)
        {
            throw new MeasureException("Unable to find DAO classname, add a property [execution.dao.class] "
                + "to your [jmonitoring.properties] file");
        }
        try
        {
            Class tClass = Class.forName(tClassName);
            return tClass.getConstructor(new Class[0]);
        } catch (ClassNotFoundException e)
        {
            throw new MeasureException("Unable to load the class define with the property [execution.dao.class] "
                + "check your [jmonitoring.properties] file", e);
        } catch (SecurityException e)
        {
            throw new MeasureException("Unable to access to the default constructor of the class defined by the "
                + "property [execution.dao.class] check your [jmonitoring.properties] file", e);
        } catch (NoSuchMethodException e)
        {
            throw new MeasureException("Unable to access to the default constructor of the class defined by the "
                + "property [execution.dao.class] check your [jmonitoring.properties] file", e);
        }
    }

    public static int getInt(String pKey)
    {
        return Integer.parseInt(sConfiguration.getProperty(pKey));
    }

    public static boolean getBoolean(String pKey)
    {
        return getBoolean(pKey, false);
    }

    public static boolean getBoolean(String pKey, boolean pDefault)
    {
        String tString = sConfiguration.getProperty(pKey);
        return (tString == null ? pDefault : Boolean.getBoolean(tString));
    }

    public static void setProperty(String pKey, String pNewValue)
    {
        sConfiguration.setProperty(pKey, pNewValue);
    }

    public static String getString(String pKey)
    {
        return getString(pKey, null);
    }

    public static String getString(String pKey, String pDefault)
    {
        String tResult = sConfiguration.getProperty(pKey);
        return (tResult == null ? pDefault : tResult);
    }

    public static void clearProperty(String pKey)
    {
        sConfiguration.remove(pKey);
    }
}
