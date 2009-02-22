package org.jmonitoring.core.dto;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.io.Serializable;

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
    private String mBeginTime;

    /** End datetime. */
    private String mEndTime;

    /** Technical identifier. */
    private int mId;

    /** Name of the class on which is done the first method call. */
    private String mClassName;

    /** Name of the method on which is done the first method call. */
    private String mMethodName;

    private long mDuration;

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
     * @param pDuration Duration
     */
    public ExecutionFlowDTO(int pId, String pThreadName, String pJVMIdentifier, String pBeginTime, String pEndTime,
        long pDuration)
    {
        super();
        mId = pId;
        mThreadName = pThreadName;
        mJvmIdentifier = pJVMIdentifier;
        mBeginTime = pBeginTime;
        mEndTime = pEndTime;
        mDuration = pDuration;
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
     * @return The duration of the first measure execution in milliseconds.
     */
    public long getDuration()
    {
        return mDuration;
    }

    /**
     * @return The duration of the first measure execution in milliseconds.
     */
    public String getDurationAsString()
    {
        return "" + mDuration;
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

    @Override
    public String toString()
    {
        StringBuilder tBuffer = new StringBuilder();
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
        return tBuffer.toString();
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

    /**
     * @return the beginTime
     */
    public String getBeginTime()
    {
        return mBeginTime;
    }

    /**
     * @param pBeginTime the beginTime to set
     */
    public void setBeginTime(String pBeginTime)
    {
        mBeginTime = pBeginTime;
    }

    /**
     * @return the endTime
     */
    public String getEndTime()
    {
        return mEndTime;
    }

    /**
     * @param pEndTime the endTime to set
     */
    public void setEndTime(String pEndTime)
    {
        mEndTime = pEndTime;
    }

    /**
     * @param pDuration the duration to set
     */
    public void setDuration(long pDuration)
    {
        mDuration = pDuration;
    }

}