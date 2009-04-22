package org.jmonitoring.console.gwt.client.dto;

import java.io.Serializable;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class StatMethodCallDTO implements Serializable, IsSerializable
{
    private static final long serialVersionUID = -1449222072284661892L;

    private String mClassName;

    private String mMethodName;

    private String mNbOccurence;

    private String mDurationMin;

    private String mDurationMax;

    private String mDurationAvg;

    private String mDurationDev;

    private int mAggregationScope;

    private List<StatMapAreaDTO> mImage;

    public int getAggregationScope()
    {
        return mAggregationScope;
    }

    public void setAggregationScope(int pAggregationScope)
    {
        mAggregationScope = pAggregationScope;
    }

    public String getFullMethodCallName()
    {
        return mClassName + "." + mMethodName + "(...)";
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

    public String getClassName()
    {
        return mClassName;
    }

    public void setClassName(String pClassName)
    {
        mClassName = pClassName;
    }

    public String getMethodName()
    {
        return mMethodName;
    }

    public void setMethodName(String pMethodName)
    {
        mMethodName = pMethodName;
    }

    public List<StatMapAreaDTO> getImageMap()
    {
        return mImage;
    }

    public void setImage(List<StatMapAreaDTO> pImage)
    {
        mImage = pImage;
    }
}
