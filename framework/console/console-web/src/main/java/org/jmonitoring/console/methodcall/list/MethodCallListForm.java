package org.jmonitoring.console.methodcall.list;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.jmonitoring.core.dto.MethodCallFullExtractDTO;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * @author pke
 */
public class MethodCallListForm extends ActionForm
{
    private static final long serialVersionUID = -5052343277856620291L;

    private String mClassName;

    private String mMethodName;

    private int mDurationMin;

    private int mDurationMax;

    private List<MethodCallFullExtractDTO> mSearchResult;

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
     * @return Returns the durationMax.
     */
    public int getDurationMax()
    {
        return mDurationMax;
    }

    /**
     * @param pDurationMax The durationMax to set.
     */
    public void setDurationMax(int pDurationMax)
    {
        mDurationMax = pDurationMax;
    }

    /**
     * @return Returns the durationMin.
     */
    public int getDurationMin()
    {
        return mDurationMin;
    }

    /**
     * @param pDurationMin The durationMin to set.
     */
    public void setDurationMin(int pDurationMin)
    {
        mDurationMin = pDurationMin;
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
     * @return Returns the searchResult.
     */
    public List<MethodCallFullExtractDTO> getSearchResult()
    {
        return mSearchResult;
    }

    /**
     * @param pSearchResult The searchResult to set.
     */
    public void setSearchResult(List<MethodCallFullExtractDTO> pSearchResult)
    {
        mSearchResult = pSearchResult;
    }
}
