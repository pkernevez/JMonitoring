package org.jmonitoring.console.gwt.client.dto;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class StatMapAreaDTO implements Serializable, IsSerializable
{
    private static final long serialVersionUID = -4608543580195947785L;

    private String mCoordinate;

    private int mDurationMin;

    private int mDurationMax;

    public String getCoordinate()
    {
        return mCoordinate;
    }

    public void setCoordinate(String pCoordinate)
    {
        mCoordinate = pCoordinate;
    }

    public int getDurationMin()
    {
        return mDurationMin;
    }

    public void setDurationMin(int pDurationMin)
    {
        mDurationMin = pDurationMin;
    }

    public int getDurationMax()
    {
        return mDurationMax;
    }

    public void setDurationMax(int pDurationMax)
    {
        mDurationMax = pDurationMax;
    }

}
