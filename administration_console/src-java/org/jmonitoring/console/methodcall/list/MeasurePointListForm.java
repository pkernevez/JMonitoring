package org.jmonitoring.console.methodcall.list;

import org.apache.struts.action.ActionForm;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * @author pke
 */
public class MeasurePointListForm extends ActionForm
{
    private String mClassName;
    
    private String mMethodName;
    
    private int mDurationMin;
    
    private int mDurationMax;
    
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
}
