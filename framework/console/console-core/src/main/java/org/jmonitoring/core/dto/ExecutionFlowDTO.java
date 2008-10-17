package org.jmonitoring.core.dto;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

import org.jmonitoring.core.configuration.ConfigurationHelper;

/**
 * This instance is the entry of of a flow. It has a list of MeasurePoint and some properties.
 * 
 * @author pke
 */
public class ExecutionFlowDTO implements Serializable
{

    private static final long serialVersionUID = 6701199516068044627L;

    /** Thread name. */
    private String mThreadName;

    /** First Measure. */
    private MethodCallDTO mFirstMethodCall;

    /** Name of the 'JVM' or server. */
    private String mJvmIdentifier;

    /** Begin datetime. */
    private Date mBeginTime;

    /** End datetime. */
    private Date mEndTime;

    /** Technical identifier. */
    private int mId;

    /** Nom de la classe sur laquelle est fait l'appel de la m�thode. */
    private String mClassName;

    /** Nom de la m�thode associ�e � ce point de mesure. */
    private String mMethodName;

    /**
     * Constructor.
     */
    public ExecutionFlowDTO()
    {
    }

    /**
     * Constructor.
     * 
     * @param pId L'identifiant technique de ce flux.
     * @param pThreadName The name of the Thread of this flow.
     * @param pJVMIdentifier The identifier of this JVM or Server.
     * @param pBeginTime L'heure de d�but de l'appel.
     * @param pEndTime L'heure de fin de l'appel.
     */
    public ExecutionFlowDTO(int pId, String pThreadName, String pJVMIdentifier, long pBeginTime, long pEndTime)
    {
        super();
        mId = pId;
        mThreadName = pThreadName;
        mJvmIdentifier = pJVMIdentifier;
        mBeginTime = new Date(pBeginTime);
        mEndTime = new Date(pEndTime);
    }

    // /**
    // * Constructor.
    // *
    // * @param pThreadName The name of the Thread of this flow.
    // * @param pJVMIdentifier The identifier of this JVM or Server.
    // * @param pBeginTime L'heure de d�but de l'appel.
    // * @param pEndTime L'heure de fin de l'appel.
    // */
    // public ExecutionFlowDTO(String pThreadName, String pJVMIdentifier, long pBeginTime, long pEndTime)
    // {
    // this(-1, pThreadName, pJVMIdentifier, pBeginTime, pEndTime);
    // }

    // /**
    // * Constructor.
    // *
    // * @param pThreadName The name of the Thread of this flow.
    // * @param pFirstMeasure First <code>MeasurePoint</code> of this flow.
    // * @param pJVMIdentifier The identifier of this JVM or Server.
    // *
    // */
    // public ExecutionFlowDTO(String pThreadName, MethodCallDTO pFirstMeasure, String pJVMIdentifier)
    // {
    // mThreadName = pThreadName;
    // mFirstMethodCall = pFirstMeasure;
    // mJvmIdentifier = pJVMIdentifier;
    // mBeginTime = mFirstMethodCall.getBeginTime();
    // mEndTime = mFirstMethodCall.getEndTime();
    // }
    //
    /**
     * @return Returns the mFirstMethodCall.
     */
    public MethodCallDTO getFirstMethodCall()
    {
        return mFirstMethodCall;
    }

    /**
     * @param pFirstMethodCall The mFirstMethodCall to set.
     */
    public void setFirstMethodCall(MethodCallDTO pFirstMethodCall)
    {
        mFirstMethodCall = pFirstMethodCall;
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
    public Date getBeginTime()
    {
        return mBeginTime;
    }

    /**
     * Accessor.
     * 
     * @param pBeginTime The mBeginTime to set.
     */
    public void setBeginTime(Date pBeginTime)
    {
        mBeginTime = pBeginTime;
    }

    /**
     * Delegator.
     * 
     * @return The begin time of the firts measure.
     */
    public String getBeginDateAsString()
    {
        return (mBeginTime == null ? null : ConfigurationHelper.formatDateTime(mBeginTime));
    }

    /**
     * Accessor.
     * 
     * @param pBeginTime The BeginTime to set.
     * @throws ParseException If the the time is not valid.
     */
    public void setBeginDateAsString(String pBeginTime) throws ParseException
    {
        mBeginTime = ConfigurationHelper.parseDateTime(pBeginTime);
    }

    /**
     * Delegator.
     * 
     * @return The end time of the first measure.
     */
    public Date getEndTime()
    {
        return mEndTime;
    }

    /**
     * Accessor.
     * 
     * @param pEndTime The mEndTime to set.
     */
    public void setEndTime(Date pEndTime)
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
        return ConfigurationHelper.formatTime(mEndTime);
    }

    /**
     * @param pEndTime The mEndTime to set.
     * @throws ParseException If the time is not valid.
     */
    public void setEndTimeAsString(String pEndTime) throws ParseException
    {
        mEndTime = ConfigurationHelper.parseTime(pEndTime);
    }

    /**
     * @return The duration of the first measure execution in milliseconds.
     */
    public long getDuration()
    {
        return mEndTime.getTime() - mBeginTime.getTime();
    }

    /**
     * @return The duration of the first measure execution in milliseconds.
     */
    public String getDurationAsString()
    {
        return "" + (mEndTime.getTime() - mBeginTime.getTime());
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
    @Override
    public String toString()
    {
        StringBuffer tBuffer = new StringBuffer();
        tBuffer.append("FlowId=[").append(mId).append("] ");
        if (mFirstMethodCall != null)
        {
            tBuffer.append("SequenceId=[").append(mFirstMethodCall.getPosition()).append("] ");
            tBuffer.append("GroupName=[").append(mFirstMethodCall.getGroupName()).append("] ");
            tBuffer.append("ClassName=[").append(mFirstMethodCall.getClassName()).append("] ");
            tBuffer.append("MethodName=[").append(mFirstMethodCall.getMethodName()).append("] ");
        } else
        {
            tBuffer.append("SequenceId=[NULL] ");
        }
        return super.toString();
    }

    /**
     * Count the number of <code>MethodCallDTO</code> in this flow graph.
     * 
     * @return The number of measure.
     */
    public int getMeasureCount()
    {
        return mFirstMethodCall.getSubMeasureCount();
    }

    /**
     * @return Returns the className.
     */
    public String getClassName()
    {
        return mClassName;
    }

    /**
     * @param pClassName The className to set.
     */
    public void setClassName(String pClassName)
    {
        mClassName = pClassName;
    }

    /**
     * @return Returns the methodName.
     */
    public String getMethodName()
    {
        return mMethodName;
    }

    /**
     * @param pMethodName The methodName to set.
     */
    public void setMethodName(String pMethodName)
    {
        mMethodName = pMethodName;
    }

}