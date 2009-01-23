package org.jmonitoring.core.configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class FormaterBean
{
    private SimpleDateFormat mDateTimeFormater;

    private SimpleDateFormat mDateFormater;

    private SimpleDateFormat mTimeFormater;

    private String mDateFormat;

    private String mTimeFormat;

    public Date parseTime(String tTime) throws ParseException
    {
        return mTimeFormater.parse(tTime);
    }

    public String formatTime(Date tTime)
    {
        return mTimeFormater.format(tTime);
    }

    public Date parseDateTime(String tTime)
    {
        try
        {
            return mDateTimeFormater.parse(tTime);
        } catch (ParseException e)
        {
            throw new MeasureException("Unable to parse Date", e);
        }
    }

    public Date parseDate(String tTime)
    {
        try
        {
            return mDateFormater.parse(tTime);
        } catch (ParseException e)
        {
            throw new MeasureException("Unable to parse Date", e);
        }
    }

    public String formatDateTime(Date tTime)
    {
        return mDateTimeFormater.format(tTime);
    }

    public String formatDate(Date tTime)
    {
        return mDateFormater.format(tTime);
    }

    public String formatDateTime(long tTime)
    {
        return formatDateTime(new Date(tTime));
    }

    //
    // public Object formatDate(Date pDate)
    // {
    // return mDateTimeFormater.format(pDate);
    // }

    /**
     * @param pDateFormat the mDateTimeFormater to set
     */
    public void setDateFormat(String pDateFormat)
    {
        mDateFormat = pDateFormat;
        String tFormat = (mTimeFormat == null ? pDateFormat : pDateFormat + " " + mTimeFormat);
        mDateTimeFormater = new SimpleDateFormat(tFormat);
        mDateFormater = new SimpleDateFormat(pDateFormat);
    }

    /**
     * @param pTimeFormat the mTimeFormater to set
     */
    public void setTimeFormat(String pTimeFormat)
    {
        mTimeFormat = pTimeFormat;
        if (mDateFormat != null)
        {
            mDateTimeFormater = new SimpleDateFormat(mDateFormat + " " + mTimeFormat);
        }
        mTimeFormater = new SimpleDateFormat(pTimeFormat);
    }

    /**
     * @return the dateFormater
     */
    public synchronized SimpleDateFormat getDateFormater()
    {
        return mDateFormater;
    }
}
