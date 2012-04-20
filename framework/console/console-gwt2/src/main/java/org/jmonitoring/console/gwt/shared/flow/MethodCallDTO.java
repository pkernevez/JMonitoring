package org.jmonitoring.console.gwt.shared.flow;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.io.Serializable;

/**
 * @author pke
 */
public class MethodCallDTO implements Serializable
{

    private static final long serialVersionUID = -497249515537353769L;

    /** Flow Technical Id. */
    private String flowId;

    /** Technical Id. */
    private String position;

    /** Link to the parent Node. */
    private String parentPosition;

    /** Link to the children nodes. */
    private MethodCallExtractDTO[] children = new MethodCallExtractDTO[0];

    /**
     * String representation of the parameters. This attribute has a value only if there is a parameter tracer defined.
     */
    private String params;

    /** Beginning time of the method as <code>String</code>. */
    private String beginTime;

    /** Beginning time of the method as <code>milliseconds</code>. */
    private long beginMilliSeconds;

    /** End time of the method as <code>String</code>. */
    private String endTime;

    /** End time of the method as <code>milliseconds</code>. */
    private long endMilliSeconds;

    /** Name of the class on which the Method is defined. */
    private String className;

    /** Name of the class on which the Method is called. Null if it's the same as <code>mClassName</code>. */
    private String runtimeClassName;

    /** Name of the method. */
    private String methodName;

    /** Exception class name. This attribute has a value only if the method end with an exception. */
    private String throwableClass;

    /** Exception message. This attribute has a value only if the method end with an exception. */
    private String throwableMessage;

    /** Method return value. If a return value tracer is defined and if the method isn't <code>void</code> type. */
    private String returnValue;

    /** Group name associated to this method call. */
    private String groupName;

    /** Color associated to this group. */
    private String groupColor;

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
        return throwableClass != null;

    }

    /**
     * Accessor.
     * 
     * @return The start time of the call to the method associated to this <code>MethodCallDTO</code>. //TODO Change to
     *         setEndTime
     */
    public String getBeginTimeString()
    {
        return beginTime;
    }

    /**
     * @param pBeginMilliSeconds the beginMilliSeconds to set
     */
    public void setBeginMilliSeconds(long pBeginMilliSeconds)
    {
        beginMilliSeconds = pBeginMilliSeconds;
    }

    /**
     * @param pEndMilliSeconds the endMilliSeconds to set
     */
    public void setEndMilliSeconds(long pEndMilliSeconds)
    {
        endMilliSeconds = pEndMilliSeconds;
    }

    /**
     * Accessor.
     * 
     * @return The name of the <code>Class</code> on which we call the method associated to this
     *         <code>MethodCallDTO</code>.
     */
    public String getClassName()
    {
        return className;
    }

    /**
     * Accessor.
     * 
     * @return The time of the execution of the method associated with this <code>MethodCallDTO</code>.
     */
    public long getDuration()
    {
        return endMilliSeconds - beginMilliSeconds;
    }

    /**
     * Accessor.
     * 
     * @return The end time of the method associated with this <code>MethodCallDTO</code>. //TODO Change to setEndTime
     */
    public String getEndTimeString()
    {
        return endTime;
    }

    /**
     * Accessor.
     * 
     * @return The name of the method associated with this <code>MethodCallDTO</code>.
     */
    public String getMethodName()
    {
        return methodName;
    }

    /**
     * Accessor.
     * 
     * @return The parameters of the method associated with this <code>MethodCallDTO</code> as <code>String</code>.
     */
    public String getParams()
    {
        return params;
    }

    /**
     * Accessor.
     * 
     * @return The return value of the method associated with this <code>MethodCallDTO</code>. Null if the method ended
     *         with an <code>Exception</code>.
     */
    public String getReturnValue()
    {
        return returnValue;
    }

    /**
     * Accessor.
     * 
     * @return The message of the <code>Exception</code> thrown by the method associated with this
     *         <code>MethodCallDTO</code>. Null if the method ended normally.
     */
    public String getThrowableMessage()
    {
        return throwableMessage;
    }

    /**
     * Accessor.
     * 
     * @return The list of the sub-method of this <code>MethodCallDTO</code>.
     */
    public MethodCallExtractDTO[] getChildren()
    {
        return children;
    }

    /**
     * Accessor.
     * 
     * @param pPos The position of the child in the list of children.
     * @return The list of the sub-method of this <code>MethodCallDTO</code>.
     */
    public MethodCallExtractDTO getChild(int pPos)
    {
        return children[pPos];
    }

    /**
     * Accessor.
     * 
     * @return Returns the sequence identifier.
     */
    public String getPosition()
    {
        return position;
    }

    /**
     * @param pPosition The mId to set.
     */
    public void setPosition(String pPosition)
    {
        position = pPosition;
    }

    /**
     * @param pBeginTime The mBeginTime to set.
     */
    public void setBeginTime(String pBeginTime, long pBeginMs)
    {
        beginTime = pBeginTime;
        beginMilliSeconds = pBeginMs;
    }

    /**
     * @param pEndTime The mEndTime to set.
     */
    public void setEndTime(String pEndTime, long pEndMs)
    {
        endTime = pEndTime;
        endMilliSeconds = pEndMs;
    }

    /**
     * @param pReturnValue The mReturnValue to set.
     */
    public void setReturnValue(String pReturnValue)
    {
        returnValue = pReturnValue;
    }

    /**
     * @param pThrowableClass The mThrowableClass to set.
     */
    public void setThrowableClass(String pThrowableClass)
    {
        throwableClass = pThrowableClass;
    }

    /**
     * @param pThrowableMessage The mThrowableMessage to set.
     */
    public void setThrowableMessage(String pThrowableMessage)
    {
        throwableMessage = pThrowableMessage;
    }

    /**
     * @return Returns the mGroupName.
     */
    public String getGroupName()
    {
        return groupName;
    }

    /**
     * @param pGroupName The mGroupName to set.
     */
    public void setGroupName(String pGroupName)
    {
        groupName = pGroupName;
    }

    /**
     * Accessor.
     * 
     * @return The flow identifier.
     */
    public String getFlowId()
    {
        return flowId;
    }

    /**
     * Accessor.
     * 
     * @param pFlowId The flow identifier.
     */
    public void setFlowId(String pFlowId)
    {
        flowId = pFlowId;
    }

    // /**
    // * Count the number of sub <code>MethodCallDTO</code> of this measure.
    // *
    // * @return The number of measure.
    // */
    // int getSubMeasureCount()
    // {
    // int tNbMeasure = 1;
    // MethodCallDTO curChild;
    // for (int i = 0; i < children.length; i++)
    // {
    // curChild = children[i];
    // tNbMeasure += curChild.getSubMeasureCount();
    // }
    // return tNbMeasure;
    // }

    /**
     * @param pClassName The className to set.
     */
    public void setClassName(String pClassName)
    {
        className = pClassName;
    }

    /**
     * @param pMethodName The methodName to set.
     */
    public void setMethodName(String pMethodName)
    {
        methodName = pMethodName;
    }

    /**
     * @return Returns the throwableClass.
     */
    public String getThrowableClass()
    {
        return throwableClass;
    }

    /**
     * @param pChildren The children to set.
     */
    public void setChildren(MethodCallExtractDTO[] pChildren)
    {
        children = pChildren;
    }

    //
    // /**
    // * @param pIndex The index of the Child to remove.
    // */
    // public void removeChild(int pIndex)
    // {
    // MethodCallDTO[] tNewMeth = new MethodCallDTO[children.length - 1];
    // System.arraycopy(children, 0, tNewMeth, 0, pIndex);
    // System.arraycopy(children, pIndex + 1, tNewMeth, pIndex, children.length - pIndex - 1);
    // children = tNewMeth;
    // }

    /**
     * @param pParams The params to set.
     */
    public void setParams(String pParams)
    {
        params = pParams;
    }

    public String getRuntimeClassName()
    {
        return (runtimeClassName != null ? runtimeClassName : className);
    }

    public void setRuntimeClassName(String pRuntimeClassName)
    {
        runtimeClassName = pRuntimeClassName;
    }

    /**
     * @return the beginMilliSeconds
     */
    public long getBeginMilliSeconds()
    {
        return beginMilliSeconds;
    }

    /**
     * @return the endMilliSeconds
     */
    public long getEndMilliSeconds()
    {
        return endMilliSeconds;
    }

    /**
     * @param pBeginTime the beginTime to set //TODO Change to setBeginTime
     */
    public void setBeginTimeString(String pBeginTime)
    {
        beginTime = pBeginTime;
    }

    /**
     * @param pEndTime the endTime to set //TODO Change to setEndTime
     */
    public void setEndTimeString(String pEndTime)
    {
        endTime = pEndTime;
    }

    /**
     * @return the groupColor
     */
    public String getGroupColor()
    {
        return groupColor;
    }

    /**
     * @param pGroupColor the groupColor to set
     */
    public void setGroupColor(String pGroupColor)
    {
        groupColor = pGroupColor;
    }

    public String getParentPosition()
    {
        return parentPosition;
    }

    public void setParentPosition(String pParentPosition)
    {
        parentPosition = pParentPosition;
    }

    @Override
    public String toString()
    {
        return "MethodCallDTO flowId=" + flowId + " , position=" + position;
    }

}
