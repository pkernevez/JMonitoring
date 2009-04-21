package org.jmonitoring.console.gwt.client.dto;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class StatMethodCallDTO
{
    private String mFullMethodCallName;

    private String mNbOccurence;

    private String mDurationMin;

    private String mDurationMax;

    private String mDurationAvg;

    private String mDurationDev;

    private String mAggregationScope;

    public String getAggregationScope()
    {
        return mAggregationScope;
    }

    public void setAggregationScope(String pAggregationScope)
    {
        mAggregationScope = pAggregationScope;
    }

    public String getFullMethodCallName()
    {
        return mFullMethodCallName;
    }

    public void setFullMethodCallName(String pFullMethodCallName)
    {
        mFullMethodCallName = pFullMethodCallName;
    }

    public String getNbOccurence()
    {
        return mNbOccurence;
    }

    public void setNbOccurence(String pNbOccurence)
    {
        mNbOccurence = pNbOccurence;
    }

    public String getDurationMin()
    {
        return mDurationMin;
    }

    public void setDurationMin(String pDurationMin)
    {
        mDurationMin = pDurationMin;
    }

    public String getDurationMax()
    {
        return mDurationMax;
    }

    public void setDurationMax(String pDurationMax)
    {
        mDurationMax = pDurationMax;
    }

    public String getDurationAvg()
    {
        return mDurationAvg;
    }

    public void setDurationAvg(String pDurationAvg)
    {
        mDurationAvg = pDurationAvg;
    }

    public String getDurationDev()
    {
        return mDurationDev;
    }

    public void setDurationDev(String pDurationDev)
    {
        mDurationDev = pDurationDev;
    }
}
