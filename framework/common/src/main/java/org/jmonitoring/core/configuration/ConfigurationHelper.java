package org.jmonitoring.core.configuration;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public final class ConfigurationHelper
{
    private static PropertiesConfiguration sConfiguration;

    private static Log sLog = LogFactory.getLog(ConfigurationHelper.class);

    private static final ThreadLocal sTimeFormater = new ThreadLocal();

    private static final ThreadLocal sDateFormater = new ThreadLocal();

    private static final ThreadLocal sDateTimeFormater = new ThreadLocal();

    private ConfigurationHelper()
    {
    }

    public static void reload()
    {
        sConfiguration = null;
        sConfiguration = getInstance();
    }
    
    public static PropertiesConfiguration getInstance()
    {
        PropertiesConfiguration tConfig = sConfiguration;
        if (tConfig == null)
        {
            try
            {
                tConfig = new PropertiesConfiguration("jmonitoring.properties");
            } catch (org.apache.commons.configuration.ConfigurationException e)
            {
                sLog.fatal("The file [jmonitoring.properties] can't be found in classpath");
                throw new ConfigurationException("The file [jmonitoring.properties] can't be found in classpath");
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
    private static SimpleDateFormat getDateFormater()
    {
        Object tResult = sDateFormater.get();
        if (tResult == null)
        {
            String tDateFormat = ConfigurationHelper.getInstance().getString("format.ihm.date");
            tResult = new SimpleDateFormat(tDateFormat);
            sDateFormater.set(tResult);
        }
        return (SimpleDateFormat) tResult;
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
            String tDateFormat = ConfigurationHelper.getInstance().getString("format.ihm.time");
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
            String tDateTimeFormat = ConfigurationHelper.getInstance().getString("format.ihm.date") + " "
                + ConfigurationHelper.getInstance().getString("format.ihm.time");
            tResult = new SimpleDateFormat(tDateTimeFormat);
            sDateTimeFormater.set(tResult);
        }
        return (SimpleDateFormat) tResult;
    }

    public static Date parseTime(String tTime) throws ParseException
    {
        DateFormat tFormat = (DateFormat) sTimeFormater.get();
        return tFormat.parse(tTime);
    }

    public static String formatTime(Date tTime)
    {
        DateFormat tFormat = (DateFormat) sTimeFormater.get();
        return tFormat.format(tTime);
    }

    public static Date parseDateTime(String tTime) throws ParseException
    {
        DateFormat tFormat = (DateFormat) sDateTimeFormater.get();
        return tFormat.parse(tTime);
    }

    public static String formatDateTime(Date tTime)
    {
        DateFormat tFormat = (DateFormat) sDateTimeFormater.get();
        return tFormat.format(tTime);
    }

    public static String formatDateTime(long tTime)
    {
        return formatDateTime(new Date(tTime));
    }

}
