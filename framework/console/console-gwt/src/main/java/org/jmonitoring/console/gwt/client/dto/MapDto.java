package org.jmonitoring.console.gwt.client.dto;

import java.io.Serializable;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class MapDto implements Serializable, IsSerializable
{
    private static final long serialVersionUID = 4191409572568739246L;

    private int mFlowId;

    private List<MapAreaDTO> mAreas;

    /**
     * @return the flowId
     */
    public int getFlowId()
    {
        return mFlowId;
    }

    /**
     * @param pFlowId the flowId to set
     */
    public void setFlowId(int pFlowId)
    {
        mFlowId = pFlowId;
    }

    /**
     * @return the areas
     */
    public List<MapAreaDTO> getAreas()
    {
        return mAreas;
    }

    /**
     * @param pAreas the areas to set
     */
    public void setAreas(List<MapAreaDTO> pAreas)
    {
        mAreas = pAreas;
    }
}
