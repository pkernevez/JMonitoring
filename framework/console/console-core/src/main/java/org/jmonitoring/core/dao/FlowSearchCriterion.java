package org.jmonitoring.core.dao;

import java.util.Date;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * Criterion for the database search of the <code>ExecutionFlow</code>.
 * 
 * @author pke
 */
public class FlowSearchCriterion
{
    /** The name of the Thread. */
    private String mThreadName;

    /** Begin date/time. */
    private Date mBeginDate;

    /** @todo menage End date/time. */
    private Date mBeginTimeMin;

    /** Duration. */
    private Long mDurationMin;

    /** JVM identifier. */
    private String mJVM;

    /** Name of the class of the first measure. */
    private String mFirstMeasureClassName;

    /** Name of the method of the first measure. */
    private String mFirstMeasureMethodName;

    /** Name of the Group of the first measure. */
    private String mFirstMeasureGroupName;

    /**
     * @return Returns the firstMeasureClassName.
     */
    public String getClassName()
    {
        return mFirstMeasureClassName;
    }

    /**
     * @param pFirstMeasureClassName The firstMeasureClassName to set.
     */
    public void setClassName(String pFirstMeasureClassName)
    {
        mFirstMeasureClassName = pFirstMeasureClassName;
    }

    /**
     * @return Returns the firstMeasureGroupName.
     */
    public String getGroupName()
    {
        return mFirstMeasureGroupName;
    }

    /**
     * @param pFirstMeasureGroupName The firstMeasureGroupName to set.
     */
    public void setGroupName(String pFirstMeasureGroupName)
    {
        mFirstMeasureGroupName = pFirstMeasureGroupName;
    }

    /**
     * @return Returns the firstMeasureMethodName.
     */
    public String getMethodName()
    {
        return mFirstMeasureMethodName;
    }

    /**
     * @param pFirstMeasureMethodName The firstMeasureMethodName to set.
     */
    public void setMethodName(String pFirstMeasureMethodName)
    {
        mFirstMeasureMethodName = pFirstMeasureMethodName;
    }

    /**
     * @return Returns the jVM.
     */
    public String getJVM()
    {
        return mJVM;
    }

    /**
     * @param pJvm The jVM to set. TODO Rename to Server
     */
    public void setJVM(String pJvm)
    {
        mJVM = pJvm;
    }

    /**
     * @return Returns the threadName.
     */
    public String getThreadName()
    {
        return mThreadName;
    }

    /**
     * @param pThreadName The threadName to set.
     */
    public void setThreadName(String pThreadName)
    {
        mThreadName = pThreadName;
    }

    /**
     * @return Returns the beginDate.
     */
    public Date getBeginDate()
    {
        return mBeginDate;
    }

    /**
     * @param pBeginDate The beginDate to set.
     */
    public void setBeginDate(Date pBeginDate)
    {
        mBeginDate = (pBeginDate == null ? null : new Date(pBeginDate.getTime()));
    }

    /**
     * @todo menage
     * @return Returns the beginTime.
     */
    public Date getBeginTimeMin()
    {
        return mBeginTimeMin;
    }

    /**
     * @todo menage
     * @param pBeginTimeMin The beginTime to set.
     */
    public void setBeginTimeMin(Date pBeginTimeMin)
    {
        mBeginTimeMin = new Date(pBeginTimeMin.getTime());
    }

    /**
     * @return Returns the duration.
     */
    public Long getDurationMin()
    {
        return mDurationMin;
    }

    /**
     * @param pDuration The duration to set.
     */
    public void setDurationMin(Long pDuration)
    {
        mDurationMin = pDuration;
    }
}
