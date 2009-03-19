package org.jmonitoring.console.gwt.client.executionflow.images;

import java.io.Serializable;

import org.jmonitoring.console.gwt.client.dto.ExecutionFlowDTO;

import com.google.gwt.user.client.rpc.IsSerializable;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class FullExecutionFlow implements Serializable, IsSerializable
{
    private static final long serialVersionUID = -4266334309680833179L;

    private ExecutionFlowDTO mFlow;

    private String mImageMap;

    /**
     * @return the flow
     */
    public ExecutionFlowDTO getFlow()
    {
        return mFlow;
    }

    public FullExecutionFlow(ExecutionFlowDTO pFlow)
    {
        super();
        mFlow = pFlow;
    }

    public FullExecutionFlow()
    {
    }

    /**
     * @param pFlow the flow to set
     */
    public void setFlow(ExecutionFlowDTO pFlow)
    {
        mFlow = pFlow;
    }

    public void setImageMap(String pImageMap)
    {
        mImageMap = pImageMap;
    }

    /**
     * @return the imageMap
     */
    public String getImageMap()
    {
        return mImageMap;
    }

}
