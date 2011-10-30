package org.jmonitoring.console.gwt.shared.flow;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.io.Serializable;

public class MethodCallExtractDTO implements Serializable
{
    private static final long serialVersionUID = -497249515537353769L;

    /** Technical Id. */
    private String position;

    private String parentPosition;

    private boolean thrownException;

    /** Link to the children nodes. */
    private MethodCallExtractDTO[] children;

    /**
     * Time since the prev brother method call had finished. It's the time spend in the execution of the parent since
     * the end of the prev child.
     */
    private String timeFromPrevChild;

    /** Duration. */
    private String duration;

    /** Group name associated to this method call. */
    private String groupName;

    /** The name as "{fullClassName}.{methodName}()" */
    private String fullMethodName;

    public MethodCallExtractDTO()
    {
    }

    public String getPosition()
    {
        return position;
    }

    public void setPosition(String pPosition)
    {
        position = pPosition;
    }

    public String getParentPosition()
    {
        return parentPosition;
    }

    public void setParentPosition(String pParentPosition)
    {
        parentPosition = pParentPosition;
    }

    public MethodCallExtractDTO[] getChildren()
    {
        return children;
    }

    public void setChildren(MethodCallExtractDTO[] pChildren)
    {
        children = pChildren;
    }

    public String getTimeFromPrevChild()
    {
        return timeFromPrevChild;
    }

    public void setTimeFromPrevChild(String pTimeFromPrevChild)
    {
        timeFromPrevChild = pTimeFromPrevChild;
    }

    public String getDuration()
    {
        return duration;
    }

    public void setDuration(String pDuration)
    {
        duration = pDuration;
    }

    public String getGroupName()
    {
        return groupName;
    }

    public void setGroupName(String pGroupName)
    {
        groupName = pGroupName;
    }

    public String getFullMethodName()
    {
        return fullMethodName;
    }

    public void setFullMethodName(String pFullMethodName)
    {
        fullMethodName = pFullMethodName;
    }

    @Override
    public String toString()
    {
        return "MethodCallExctractDTO position=" + position;
    }

    public boolean hasThrownException()
    {
        return thrownException;
    }

    public void setThrownException(boolean pHasThrownException)
    {
        thrownException = pHasThrownException;
    }

}
