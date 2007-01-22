package org.jmonitoring.core.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jmonitoring.core.configuration.ConfigurationException;
import org.jmonitoring.core.configuration.ConfigurationFactory;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public final class DateHelper
{
    private static final ThreadLocal sDateFormater = new ThreadLocal();

    private static final ThreadLocal sDateTimeFormater = new ThreadLocal();

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
            String tDateFormat = ConfigurationFactory.getInstance().getString("format.ihm.date");
            tResult = new SimpleDateFormat(tDateFormat);
            sDateFormater.set(tResult);
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
            String tDateTimeFormat = ConfigurationFactory.getInstance().getString("format.ihm.date") + " "
                + ConfigurationFactory.getInstance().getString("format.ihm.time");
            tResult = new SimpleDateFormat(tDateTimeFormat);
            sDateTimeFormater.set(tResult);
        }
        return (SimpleDateFormat) tResult;
    }

    public static String formatDateTime(long tTime)
    {
        DateFormat tFormat = (DateFormat) sDateTimeFormater.get();
        return tFormat.format(new Date(tTime));
    }

}
