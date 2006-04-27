package org.jmonitoring.core.persistence;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.text.ParseException;
import java.util.Date;

import org.jmonitoring.core.configuration.Configuration;
import org.jmonitoring.core.dto.ExecutionFlowDTO;
import org.jmonitoring.core.dto.MethodCallDTO;
import org.springframework.beans.BeanUtils;

import sun.print.PSPrinterJob;

/**
 * This instance is the entry of a flow. It has a list of MethodCallDTO and some properties.
 * 
 * @todo Coder les méthodes equals et hashcode
 * @author pke
 */
public class ExecutionFlowPO
{
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

//    public ExecutionFlowPO( ExecutionFlowDTO pDto)
//    {
//        BeanUtils.copyProperties(pDto, this);
//    }
    
    /**
     * Constructor.
     * 
     * @param pId L'identifiant technique de ce flux.
     * @param pThreadName The name of the Thread of this flow.
     * @param pJVMIdentifier The identifier of this JVM or Server.
     * @param pBeginTime L'heure de début de l'appel.
     * @param pEndTime L'heure de fin de l'appel.
     */
    public ExecutionFlowPO(int pId, String pThreadName, String pJVMIdentifier, long pBeginTime, long pEndTime)
    {
        super();
        mId = pId;
        mThreadName = pThreadName;
        mJvmIdentifier = pJVMIdentifier;
        mBeginTime = pBeginTime;
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
    public ExecutionFlowPO(String pThreadName, String pJVMIdentifier, long pBeginTime, long pEndTime)
    {
        this(-1, pThreadName, pJVMIdentifier, pBeginTime, pEndTime);
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
     * @return The duration of the first measure execution in milliseconds.
     */
    public String getDurationAsString()
    {
        return "" + (mEndTime - mBeginTime);
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
