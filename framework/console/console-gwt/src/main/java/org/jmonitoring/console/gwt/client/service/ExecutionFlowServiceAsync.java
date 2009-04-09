package org.jmonitoring.console.gwt.client.service;

import java.util.List;
import java.util.Map;

import org.jmonitoring.console.gwt.client.dto.ExecutionFlowDTO;
import org.jmonitoring.console.gwt.client.dto.FullExecutionFlowDTO;
import org.jmonitoring.console.gwt.client.dto.MethodCallDTO;
import org.jmonitoring.console.gwt.client.dto.RootMethodCallDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public interface ExecutionFlowServiceAsync
{
    public void search(SearchCriteria pCriteria, AsyncCallback<List<ExecutionFlowDTO>> pCallback);

    public void load(int pFlowId, List<Integer> pMethID, AsyncCallback<Map<Integer, MethodCallDTO>> pCallback);

    public void load(int pFlowId, AsyncCallback<FullExecutionFlowDTO> pCallback);

    public void load(int pFlowId, int pMethPosition, AsyncCallback<RootMethodCallDTO> pCallBack);
}
