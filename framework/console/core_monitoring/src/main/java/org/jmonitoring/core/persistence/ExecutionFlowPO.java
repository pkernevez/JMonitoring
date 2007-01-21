package org.jmonitoring.core.persistence;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

/**
 * This instance is the entry of a flow. It has a list of MethodCallDTO and some properties.
 * 
 * @todo Coder les méthodes equals et hashcode
 * @author pke
 */
public class ExecutionFlowPO implements Serializable
{
    private static final long serialVersionUID = -5320683232717386139L;

    /** Thread name. */
    private String mThreadName;

    /** Name of the 'JVM' or server. */
    private String mJvmIdentifier;

    /** Begin datetime. */
    private long mBeginTime;

    /** End datetime. */
    private long mEndTime;

    /** Technical identifier. */
    private int mId = -1;

    /** First method call of this flow. */
    private MethodCallPO mFirstMethodCall;

    /**
     * Default constructor for Hibernate.
     */
    public ExecutionFlowPO()
    {
    }

    /**
     * Constructor.
     * 
     * @param pThreadName The name of the Thread of this flow.
     * @param pFirstMeasure First <code>MeasurePoint</code> of this flow.
     * @param pJVMIdentifier The identifier of this JVM or Server.
     * @todo remove mBeginTime
     */
    public ExecutionFlowPO(String pThreadName, MethodCallPO pFirstMeasure, String pJVMIdentifier)
    {
        mThreadName = pThreadName;
        mFirstMethodCall = pFirstMeasure;
        mJvmIdentifier = pJVMIdentifier;
        mBeginTime = mFirstMethodCall.getBeginTime();
        mEndTime = mFirstMethodCall.getEndTime();
        mFirstMethodCall.setFlowRecusivly(this);
    }

    /**
     * @return Returns the firstMeasure.
     */
    public MethodCallPO getFirstMethodCall()
    {
        return mFirstMethodCall;
    }

    /**
     * @param pFirstMeasure The firstMeasure to set.
     */
    public void setFirstMethodCall(MethodCallPO pFirstMeasure)
    {
        mFirstMethodCall = pFirstMeasure;
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
    public long getBeginTime()
    {
        return mBeginTime;
    }

    /**
     * Accessor.
     * 
     * @param pBeginTime The mBeginTime to set.
     */
    public void setBeginTime(long pBeginTime)
    {
        mBeginTime = pBeginTime;
    }

    /**
     * Delegator.
     * 
     * @return The begin time of the firts measure.
     */
    public Date getBeginTimeAsDate()
    {
        return new Date(mBeginTime);
    }

    /**
     * Accessor.
     * 
     * @param pBeginTime The BeginTime to set.
     * @throws ParseException If the the time is not valid.
     */
    public void setBeginTimeAsDate(Date pBeginTime) throws ParseException
    {
        // Nothing to do, this field is only for information purpose into DBase
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
     * @return The duration of the first measure execution in milliseconds.
     */
    public long getDuration()
    {
        return mEndTime - mBeginTime;
    }

    /**
     * @param pDuration The duration of the first measure execution in milliseconds.
     * @todo Remove this method it's a hook for hibernate
     */
    public void setDuration(long pDuration)
    {

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
        tBuffer.append("ExecutionFlowPO FlowId=[").append(mId).append("]");
        return tBuffer.toString();
    }

}
