package org.jmonitoring.console.gwt.client.dto;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class MapAreaDTO implements Serializable, IsSerializable
{
    private static final long serialVersionUID = -6507820324336233431L;

    private String mCoordinate;

    private int mPosition;

    /**
     * @return the coordinate
     */
    public String getCoordinate()
    {
        return mCoordinate;
    }

    /**
     * @param pCoordinate the coordinate to set
     */
    public void setCoordinate(String pCoordinate)
    {
        mCoordinate = pCoordinate;
    }

    /**
     * @return the position
     */
    public int getPosition()
    {
        return mPosition;
    }

    /**
     * @param pPosition the position to set
     */
    public void setPosition(int pPosition)
    {
        mPosition = pPosition;
    }
}
