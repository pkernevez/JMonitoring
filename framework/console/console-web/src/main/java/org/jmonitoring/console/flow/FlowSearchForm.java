package org.jmonitoring.console.flow;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.jmonitoring.core.dto.ExecutionFlowDTO;

/**
 * Formbean associated to the search criterion.
 * 
 * @author pke
 */
public class FlowSearchForm extends ActionForm
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3760560910175122742L;

    /** The name of the Thread. */
    private String mThreadName;

    /** Begin date/time. */
    private String mBeginDate;

    /** End date/time. */
    private String mBeginTimeMin;

    /** Duration. */
    private String mDurationMin;

    /** JVM identifier. */
    private String mJVM;

    /** Liste de r�sultat en cours. */
    private List<ExecutionFlowDTO> mListOfFlows;

    /** Name of the class of the first measure. */
    private String mFirstMeasureClassName;

    /** Name of the method of the first measure. */
    private String mFirstMeasureMethodName;

    /** Name of the Group of the first measure. */
    private String mFirstMeasureGroupName;

    /**
     * @return Returns the mListOfFlows.
     */
    public List<ExecutionFlowDTO> getListOfFlows()
    {
        return mListOfFlows;
    }

    /**
     * @param pListOfFlows The mListOfFlows to set.
     */
    public void setListOfFlows(List<ExecutionFlowDTO> pListOfFlows)
    {
        mListOfFlows = pListOfFlows;
    }

    /**
     * @return Returns the mBeginDate.
     */
    public String getBeginDate()
    {
        return mBeginDate;
    }

    /**
     * @param pBeginDate The mBeginDate to set.
     */
    public void setBeginDate(String pBeginDate)
    {
        mBeginDate = pBeginDate;
    }

    /**
     * @return Returns the mBeginTimeMin.
     */
    public String getBeginTimeMin()
    {
        return mBeginTimeMin;
    }

    /**
     * @param pBeginTimeMin The mBeginTimeMin to set.
     */
    public void setBeginTimeMin(String pBeginTimeMin)
    {
        mBeginTimeMin = pBeginTimeMin;
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
     * @return Returns the mDurationMin.
     */
    public String getDurationMin()
    {
        return mDurationMin;
    }

    /**
     * @param pDuration The mDurationMin to set.
     */
    public void setDurationMin(String pDuration)
    {
        mDurationMin = pDuration;
    }

    /**
     * @return Returns the mJVM.
     */
    public String getJVM()
    {
        return mJVM;
    }

    /**
     * @param pJvm The mJVM to set.
     */
    public void setJVM(String pJvm)
    {
        mJVM = pJvm;
    }

    /**
     * @return Returns the firstMeasureClassName.
     */
    public String getFirstMeasureClassName()
    {
        return mFirstMeasureClassName;
    }

    /**
     * @param pFirstMeasureClassName The firstMeasureClassName to set.
     */
    public void setFirstMeasureClassName(String pFirstMeasureClassName)
    {
        mFirstMeasureClassName = pFirstMeasureClassName;
    }

    /**
     * @return Returns the firstMeasureGroupName.
     */
    public String getFirstMeasureGroupName()
    {
        return mFirstMeasureGroupName;
    }

    /**
     * @param pFirstMeasureGroupName The firstMeasureGroupName to set.
     */
    public void setFirstMeasureGroupName(String pFirstMeasureGroupName)
    {
        mFirstMeasureGroupName = pFirstMeasureGroupName;
    }

    /**
     * @return Returns the firstMeasureMethodName.
     */
    public String getFirstMeasureMethodName()
    {
        return mFirstMeasureMethodName;
    }

    /**
     * @param pFirstMeasureMethodName The firstMeasureMethodName to set.
     */
    public void setFirstMeasureMethodName(String pFirstMeasureMethodName)
    {
        mFirstMeasureMethodName = pFirstMeasureMethodName;
    }
}
