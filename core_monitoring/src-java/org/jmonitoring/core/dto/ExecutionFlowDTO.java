package org.jmonitoring.core.dto;

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
public class ExecutionFlowDTO
{
    /** Thread name. */
    private String mThreadName;

    /** First Measure. */
    private MethodCall mFirstMeasure;

    /** Name of the 'JVM' or server. */
    private String mJvmIdentifier;

    /** Begin datetime. */
    private long mBeginDate;

    /** End datetime. */
    private long mEndTime;

    /** Technical identifier. */
    private int mId;

    /**
     * Constructor.
     * 
     * @param pId L'identifiant technique de ce flux.
     * @param pThreadName The name of the Thread of this flow.
     * @param pJVMIdentifier The identifier of this JVM or Server.
     * @param pBeginTime L'heure de début de l'appel.
     * @param pEndTime L'heure de fin de l'appel.
     */
    public ExecutionFlowDTO(int pId, String pThreadName, String pJVMIdentifier, long pBeginTime, long pEndTime)
    {
        super();
        mId = pId;
        mThreadName = pThreadName;
        mJvmIdentifier = pJVMIdentifier;
        mBeginDate = pBeginTime;
        mEndTime = pEndTime;
    }

    /**
     * Constructor.
     * 
     * @param pThreadName The name of the Thread of this flow.
     * @param pJVMIdentifier The identifier of this JVM or Server.
     * @param pBeginTime L'heure de début de l'appel.
     * @param pEndTime L'heure de fin de l'appel.
     */
    public ExecutionFlowDTO(String pThreadName, String pJVMIdentifier, long pBeginTime, long pEndTime)
    {
        this(-1, pThreadName, pJVMIdentifier, pBeginTime, pEndTime);
    }

    /**
     * Constructor.
     * 
     * @param pThreadName The name of the Thread of this flow.
     * @param pFirstMeasure First <code>MeasurePoint</code> of this flow.
     * @param pJVMIdentifier The identifier of this JVM or Server.
     *  
     */
    public ExecutionFlowDTO(String pThreadName, MethodCall pFirstMeasure, String pJVMIdentifier)
    {
        mThreadName = pThreadName;
        mFirstMeasure = pFirstMeasure;
        mJvmIdentifier = pJVMIdentifier;
        mBeginDate = mFirstMeasure.getBeginTime();
        mEndTime = mFirstMeasure.getEndTime();
    }

    /**
     * @return Returns the mFirstMeasure.
     */
    public MethodCall getFirstMeasure()
    {
        return mFirstMeasure;
    }

    /**
     * @param pFirstMeasure The mFirstMeasure to set.
     */
    public void setFirstMeasure(MethodCall pFirstMeasure)
    {
        mFirstMeasure = pFirstMeasure;
    }

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
        tBuffer.append("FlowId=[").append(mId).append("] ");
        if (mFirstMeasure != null)
        {
            tBuffer.append("SequenceId=[").append(mFirstMeasure.getSequenceId()).append("] ");
            tBuffer.append("GroupName=[").append(mFirstMeasure.getGroupName()).append("] ");
            tBuffer.append("ClassName=[").append(mFirstMeasure.getClassName()).append("] ");
            tBuffer.append("MethodName=[").append(mFirstMeasure.getMethodName()).append("] ");
        } else
        {
            tBuffer.append("SequenceId=[NULL] ");
        }
        return super.toString();
    }

    /**
     * Count the number of <code>MethodCall</code> in this flow graph.
     * 
     * @return The number of measure.
     */
    public int getMeasureCount()
    {
        return mFirstMeasure.getSubMeasureCount();
    }
}
