package org.jmonitoring.console.gwt.client.executionflow;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class SearchCriteria implements Serializable, IsSerializable
{
    private static final long serialVersionUID = -7461631758476639879L;

    private String mThreadName;

    private String mMinimumDuration;

    private String mGroupName;

    private String mBeginDate;

    private String mServer;

    private String mClassName;

    private String mMethodName;

    /**
     * @return the threadName
     */
    public String getThreadName()
    {
        return mThreadName;
    }

    /**
     * @param pThreadName the threadName to set
     */
    public void setThreadName(String pThreadName)
    {
        mThreadName = pThreadName;
    }

    /**
     * @return the minimumDuration
     */
    public String getMinimumDuration()
    {
        return mMinimumDuration;
    }

    /**
     * @param pMinimumDuration the minimumDuration to set
     */
    public void setMinimumDuration(String pMinimumDuration)
    {
        mMinimumDuration = pMinimumDuration;
    }

    /**
     * @return the groupName
     */
    public String getGroupName()
    {
        return mGroupName;
    }

    /**
     * @param pGroupName the groupName to set
     */
    public void setGroupName(String pGroupName)
    {
        mGroupName = pGroupName;
    }

    /**
     * @return the beginDate
     */
    public String getBeginDate()
    {
        return mBeginDate;
    }

    /**
     * @param pBeginDate the beginDate to set
     */
    public void setBeginDate(String pBeginDate)
    {
        mBeginDate = pBeginDate;
    }

    /**
     * @return the className
     */
    public String getClassName()
    {
        return mClassName;
    }

    /**
     * @param pClassName the className to set
     */
    public void setClassName(String pClassName)
    {
        mClassName = pClassName;
    }

    /**
     * @return the methodName
     */
    public String getMethodName()
    {
        return mMethodName;
    }

    /**
     * @param pMethodName the methodName to set
     */
    public void setMethodName(String pMethodName)
    {
        mMethodName = pMethodName;
    }

    /**
     * @return the server
     */
    public String getServer()
    {
        return mServer;
    }

    /**
     * @param pServer the server to set
     */
    public void setServer(String pServer)
    {
        mServer = pServer;
    }

}
