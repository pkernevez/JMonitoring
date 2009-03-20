package org.jmonitoring.console.gwt.client.dto;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class FullExecutionFlowDTO implements Serializable, IsSerializable
{
    private static final long serialVersionUID = -4266334309680833179L;

    private ExecutionFlowDTO mFlow;

    private MapDto mImageMap;

    /**
     * @return the flow
     */
    public ExecutionFlowDTO getFlow()
    {
        return mFlow;
    }

    public FullExecutionFlowDTO(ExecutionFlowDTO pFlow)
    {
        super();
        mFlow = pFlow;
    }

    public FullExecutionFlowDTO()
    {
    }

    /**
     * @param pFlow the flow to set
     */
    public void setFlow(ExecutionFlowDTO pFlow)
    {
        mFlow = pFlow;
    }

    public void setImageMap(MapDto pImageMap)
    {
        mImageMap = pImageMap;
    }

    /**
     * @return the imageMap
     */
    public MapDto getImageMap()
    {
        return mImageMap;
    }

}
