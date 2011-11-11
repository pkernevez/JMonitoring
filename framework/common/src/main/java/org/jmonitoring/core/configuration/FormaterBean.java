package org.jmonitoring.core.configuration;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class FormaterBean
{
    public static final long ONE_DAY = 24 * 60 * 60 * 1000L;

    private ThreadLocal<SimpleDateFormat> mDateTimeFormater;

    private ThreadLocal<SimpleDateFormat> mDateFormater;

    private ThreadLocal<SimpleDateFormat> mTimeFormater;

    private ThreadLocal<DecimalFormat> mDecimalFormater = new ThreadLocal<DecimalFormat>();

    private String mDateFormat;

    private String mTimeFormat;

    public Date parseTime(String tTime) throws ParseException
    {
        return getTimeFormatter().parse(tTime);
    }

    public String formatTime(Date tTime)
    {
        return getTimeFormatter().format(tTime);
    }

    public Date parseDateTime(String tTime)
    {
        try
        {
            return getDateTimeFormatter().parse(tTime);
        } catch (ParseException e)
        {
            throw new MeasureException("Unable to parse Date", e);
        }
    }

    public Date parseDate(String pDate)
    {
        try
        {
            return (pDate == null || pDate.length() == 0 ? null : getDateFormatter().parse(pDate));
        } catch (ParseException e)
        {
            throw new MeasureException("Unable to parse Date", e);
        }
    }

    public String formatDateTime(Date tTime)
    {
        return getDateTimeFormatter().format(tTime);
    }

    public String formatDate(Date tTime)
    {
        return getDateFormatter().format(tTime);
    }

    public String formatDateTime(long tTime)
    {
        return formatDateTime(new Date(tTime));
    }

    /**
     * @param pDateFormat the mDateTimeFormater to set
     */
    public void setDateFormat(String pDateFormat)
    {
        mDateFormat = pDateFormat;
        mDateFormater = new ThreadLocal<SimpleDateFormat>();
        mDateTimeFormater = new ThreadLocal<SimpleDateFormat>();
        mTimeFormater = new ThreadLocal<SimpleDateFormat>();
    }

    /**
     * @param pTimeFormat the mTimeFormater to set
     */
    public void setTimeFormat(String pTimeFormat)
    {
        mTimeFormat = pTimeFormat;
        mDateFormater = new ThreadLocal<SimpleDateFormat>();
        mDateTimeFormater = new ThreadLocal<SimpleDateFormat>();
        mTimeFormater = new ThreadLocal<SimpleDateFormat>();
    }

    private SimpleDateFormat getTimeFormatter()
    {
        SimpleDateFormat tResult = mTimeFormater.get();
        if (tResult == null)
        {
            tResult = new SimpleDateFormat(mTimeFormat);
            mTimeFormater.set(tResult);
        }
        return tResult;
    }

    private SimpleDateFormat getDateTimeFormatter()
    {
        SimpleDateFormat tResult = mDateTimeFormater.get();
        if (tResult == null)
        {
            String tFormat = (mDateFormat == null ? "" : mDateFormat + " ") + (mTimeFormat == null ? "" : mTimeFormat);
            tResult = new SimpleDateFormat(tFormat);
            mDateTimeFormater.set(tResult);
        }
        return tResult;
    }

    /**
     * @return the dateFormater
     */
    public synchronized SimpleDateFormat getDateFormatter()
    {
        SimpleDateFormat tResult = mDateFormater.get();
        if (tResult == null)
        {
            tResult = new SimpleDateFormat(mDateFormat);
            mDateFormater.set(tResult);
        }
        return tResult;
    }

    /**
     * @return the dateFormater
     */
    public synchronized DecimalFormat get2DecimalFormatter()
    {
        DecimalFormat tResult = mDecimalFormater.get();
        if (tResult == null)
        {
            tResult = new DecimalFormat("#.##");
            mDecimalFormater.set(tResult);
        }
        return tResult;
    }
}
