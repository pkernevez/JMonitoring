package org.jmonitoring.core.persistence;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.text.ParseException;
import java.util.Date;

import org.jmonitoring.core.configuration.Configuration;

/**
 * This instance is the entry of of a flow. It has a list of MeasurePoint and some properties.
 * 
 * @author pke
 */
public class ExecutionFlowPO
{
    /** Thread name. */
    private String mThreadName;

    /** Name of the 'JVM' or server. */
    private String mJvmIdentifier;

    /** Begin datetime. */
    private long mBeginDate;

    /** End datetime. */
    private long mEndTime;

    /** Technical identifier. */
    private int mId;

    /**
     * @return Returns the mJVMIdentifier.
     */
    public String getJvmIdentifier()
    {
        return mJvmIdentifier;
    }

    /**
     * @param pIdentifier The mJVMIdentifier to set.
     */
    public void setJvmIdentifier(String pIdentifier)
    {
        mJvmIdentifier = pIdentifier;
    }

    /**
     * @return Returns the mThreadName.
     */
    public String getThreadName()
    {
        return mThreadName;
    }

    /**
     * @param pThreadName The mThreadName to set.
     */
    public void setThreadName(String pThreadName)
    {
        mThreadName = pThreadName;
    }

    /**
     * Delegator.
     * 
     * @return The begin time of the firts measure.
     */
    public long getBeginDate()
    {
        return mBeginDate;
    }

    /**
     * Accessor.
     * 
     * @param pBeginTime The mBeginTime to set.
     */
    public void setBeginDate(long pBeginTime)
    {
        mBeginDate = pBeginTime;
    }

    /**
     * Delegator.
     * 
     * @return The begin time of the firts measure.
     */
    public String getBeginDateAsString()
    {
        return Configuration.getInstance().getDateTimeFormater().format(new Date(mBeginDate));
    }

    /**
     * Accessor.
     * 
     * @param pBeginTime The BeginTime to set.
     * @throws ParseException If the the time is not valid.
     */
    public void setBeginDateAsString(String pBeginTime) throws ParseException
    {
        mBeginDate = Configuration.getInstance().getDateTimeFormater().parse(pBeginTime).getTime();
    }

    /**
     * Delegator.
     * 
     * @return The end time of the first measure.
     */
    public long getEndTime()
    {
        return mEndTime;
    }

    /**
     * Accessor.
     * 
     * @param pEndTime The mEndTime to set.
     */
    public void setEndTime(long pEndTime)
    {
        mEndTime = pEndTime;
    }

    /**
     * Delegator.
     * 
     * @return The end time of the first measure.
     */
    public String getEndTimeAsString()
    {
        return Configuration.getInstance().getTimeFormater().format(new Date(mEndTime));
    }

    /**
     * @param pEndTime The mEndTime to set.
     * @throws ParseException If the time is not valid.
     */
    public void setEndTimeAsString(String pEndTime) throws ParseException
    {
        mEndTime = Configuration.getInstance().getTimeFormater().parse(pEndTime).getTime();
    }

    /**
     * @return The duration of the first measure execution in milliseconds.
     */
    public long getDuration()
    {
        return mEndTime - mBeginDate;
    }

    /**
     * @param pDuration The duration of the first measure execution in milliseconds.
     * @todo Remove this method it's a hook for hibernate
     */
    public void setDuration(long pDuration)
    {
        
    }

    /**
     * @return The duration of the first measure execution in milliseconds.
     */
    public String getDurationAsString()
    {
        return "" + (mEndTime - mBeginDate);
    }

    /**
     * @return Returns the mId.
     */
    public int getId()
    {
        return mId;
    }

    /**
     * @param pId The mId to set.
     */
    public void setId(int pId)
    {
        mId = pId;
    }

    /**
     * @see java.lang.Object.toString()
     */
    public String toString()
    {
        StringBuffer tBuffer = new StringBuffer();
        tBuffer.append("ExecutionFlowPO FlowId=[").append(mId).append("] ");
        return super.toString();
    }

}
