package org.jmonitoring.core.dto;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.io.Serializable;

/**
 * @author pke
 * 
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 * Templates
 */
public class MethodCallDTO implements Serializable
{

    private static final long serialVersionUID = -497249515537353769L;

    /** Flow Technical Id. */
    private int mFlowId;

    /** Flow Technical Id. */
    private ExecutionFlowDTO mFlow;

    /** Technical Id. */
    private int mPosition;

    /** Position of the Child. */
    private int mChildPosition;

    /** Link to the parent Node. */
    private MethodCallDTO mParent;

    /** Link to the children nodes. */
    private MethodCallDTO[] mChildren = new MethodCallDTO[0];

    /**
     * String representation of the parameters. This attribute has a value only if there is a parameter tracer defined.
     */
    private String mParams;

    /** Beginning time of the method as <code>String</code>. */
    private String mBeginTime;

    /** End time of the method as <code>String</code>. */
    private String mEndTime;

    /** Name of the class on which the Method is defined. */
    private String mClassName;

    /** Name of the class on which the Method is called. Null if it's the same as <code>mClassName</code>. */
    private String mRuntimeClassName;

    /** Name of the method. */
    private String mMethodName;

    /** Exception class name. This attribute has a value only if the method end with an exception. */
    private String mThrowableClass;

    /** Exception message. This attribute has a value only if the method end with an exception. */
    private String mThrowableMessage;

    /** Method return value. If a return value tracer is defined and if the method isn't <code>void</code> type. */
    private String mReturnValue;

    /** Group name associated to this method call. */
    private String mGroupName;

    /** Duration in milliseconds of this method call. */
    private long mDuration;

    public MethodCallDTO()
    {
    }

    /**
     * Allow to know if an exception has occured during call.
     * 
     * @return True if an exception was thrown by the call method.
     */
    public boolean isReturnCallException()
    {
        return mThrowableClass != null;

    }

    /**
     * Accessor.
     * 
     * @return The parent <code>MethodCallDTO</code> of this <code>MethodCallDTO</code>.
     */
    public MethodCallDTO getParent()
    {
        return mParent;
    }

    /**
     * Accessor.
     * 
     * @return The start time of the call to the method associated to this <code>MethodCallDTO</code>.
     */
    public String getBeginTime()
    {
        return mBeginTime;
    }

    /**
     * Accessor.
     * 
     * @return The name of the <code>Class</code> on which we call the method associated to this
     *         <code>MethodCallDTO</code>.
     */
    public String getClassName()
    {
        return mClassName;
    }

    /**
     * Accessor.
     * 
     * @return The time of the execution of the methid associated with this <code>MethodCallDTO</code>.
     */
    public long getDuration()
    {
        return mDuration;
    }

    /**
     * Accessor.
     * 
     * @return The end time of the method associated with this <code>MethodCallDTO</code>.
     */
    public String getEndTime()
    {
        return mEndTime;
    }

    /**
     * Accessor.
     * 
     * @return The name of the method associated with this <code>MethodCallDTO</code>.
     */
    public String getMethodName()
    {
        return mMethodName;
    }

    /**
     * Accessor.
     * 
     * @return The parameters of the method associated with this <code>MethodCallDTO</code> as <code>String</code>.
     */
    public String getParams()
    {
        return mParams;
    }

    /**
     * Accessor.
     * 
     * @return The return value of the method associated with this <code>MethodCallDTO</code>. Null if the method
     *         ended with an <code>Exception</code>.
     */
    public String getReturnValue()
    {
        return mReturnValue;
    }

    /**
     * Accessor.
     * 
     * @return The name of the <code>Class</code> of the <code>Exception</code> thrown by the method associated with
     *         this <code>MethodCallDTO</code>. Null if the method ended normally.
     */
    public String getThrowableClassName()
    {
        return mThrowableClass;
    }

    /**
     * Accessor.
     * 
     * @return The message of the <code>Exception</code> thrown by the method associated with this
     *         <code>MethodCallDTO</code>. Null if the method ended normally.
     */
    public String getThrowableMessage()
    {
        return mThrowableMessage;
    }

    /**
     * Accessor.
     * 
     * @return The list of the sub-method of this <code>MethodCallDTO</code>.
     */
    public MethodCallDTO[] getChildren()
    {
        return mChildren;
    }

    /**
     * Accessor.
     * 
     * @param pPos The position of the child in the list of children.
     * @return The list of the sub-method of this <code>MethodCallDTO</code>.
     */
    public MethodCallDTO getChild(int pPos)
    {
        return mChildren[pPos];
    }

    /**
     * Accessor.
     * 
     * @return Returns the sequence identifier.
     */
    public int getPosition()
    {
        return mPosition;
    }

    /**
     * @param pPosition The mId to set.
     */
    public void setPosition(int pPosition)
    {
        mPosition = pPosition;
    }

    /**
     * @param pBeginTime The mBeginTime to set.
     */
    public void setBeginTime(String pBeginTime)
    {
        mBeginTime = pBeginTime;
    }

    /**
     * @param pEndTime The mEndTime to set.
     */
    public void setEndTime(String pEndTime)
    {
        mEndTime = pEndTime;
    }

    /**
     * @param pReturnValue The mReturnValue to set.
     */
    public void setReturnValue(String pReturnValue)
    {
        mReturnValue = pReturnValue;
    }

    /**
     * @param pThrowableClass The mThrowableClass to set.
     */
    public void setThrowableClass(String pThrowableClass)
    {
        mThrowableClass = pThrowableClass;
    }

    /**
     * @param pThrowableMessage The mThrowableMessage to set.
     */
    public void setThrowableMessage(String pThrowableMessage)
    {
        mThrowableMessage = pThrowableMessage;
    }

    /**
     * @return Returns the mGroupName.
     */
    public String getGroupName()
    {
        return mGroupName;
    }

    /**
     * @param pGroupName The mGroupName to set.
     */
    public void setGroupName(String pGroupName)
    {
        mGroupName = pGroupName;
    }

    /**
     * Accessor.
     * 
     * @return The flow identifier.
     */
    public int getFlowId()
    {
        return mFlowId;
    }

    /**
     * Accessor.
     * 
     * @param pFlowId The flow identifier.
     */
    public void setFlowId(int pFlowId)
    {
        mFlowId = pFlowId;
    }

    /**
     * Count the number of sub <code>MethodCallDTO</code> of this measure.
     * 
     * @return The number of measure.
     */
    int getSubMeasureCount()
    {
        int tNbMeasure = 1;
        MethodCallDTO curChild;
        for (int i = 0; i < mChildren.length; i++)
        {
            curChild = mChildren[i];
            tNbMeasure += curChild.getSubMeasureCount();
        }
        return tNbMeasure;
    }

    /**
     * @param pClassName The className to set.
     */
    public void setClassName(String pClassName)
    {
        mClassName = pClassName;
    }

    /**
     * @param pMethodName The methodName to set.
     */
    public void setMethodName(String pMethodName)
    {
        mMethodName = pMethodName;
    }

    /**
     * @return Returns the throwableClass.
     */
    protected String getThrowableClass()
    {
        return mThrowableClass;
    }

    /**
     * @param pChildren The children to set.
     */
    public void setChildren(MethodCallDTO[] pChildren)
    {
        mChildren = pChildren;
    }

    /**
     * @param pIndex The index of the Child to remove.
     */
    public void removeChild(int pIndex)
    {
        MethodCallDTO[] tNewMeth = new MethodCallDTO[mChildren.length - 1];
        System.arraycopy(mChildren, 0, tNewMeth, 0, pIndex);
        System.arraycopy(mChildren, pIndex + 1, tNewMeth, pIndex, mChildren.length - pIndex - 1);
        mChildren = tNewMeth;
    }

    /**
     * @param pParams The params to set.
     */
    public void setParams(String pParams)
    {
        mParams = pParams;
    }

    /**
     * @param pParent The parent to set.
     */
    public void setParent(MethodCallDTO pParent)
    {
        mParent = pParent;
    }

    public void setFlow(ExecutionFlowDTO pFlow)
    {
        mFlow = pFlow;
    }

    public int getChildPosition()
    {
        return mChildPosition;
    }

    public void setChildPosition(int pChildPosition)
    {
        mChildPosition = pChildPosition;
    }

    public String getRuntimeClassName()
    {
        return (mRuntimeClassName != null ? mRuntimeClassName : mClassName);
    }

    public void setRuntimeClassName(String pRuntimeClassName)
    {
        mRuntimeClassName = pRuntimeClassName;
    }

    /**
     * @return the flow
     */
    public ExecutionFlowDTO getFlow()
    {
        return mFlow;
    }

}
