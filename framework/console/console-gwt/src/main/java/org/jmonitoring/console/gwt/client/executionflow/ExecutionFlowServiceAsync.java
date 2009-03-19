package org.jmonitoring.console.gwt.client.executionflow;

import java.util.List;
import java.util.Map;

import org.jmonitoring.console.gwt.client.dto.ExecutionFlowDTO;
import org.jmonitoring.console.gwt.client.dto.MethodCallDTO;
import org.jmonitoring.console.gwt.client.executionflow.images.FullExecutionFlow;

import com.google.gwt.user.client.rpc.AsyncCallback;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public interface ExecutionFlowServiceAsync
{
    public void search(SearchCriteria pCriteria, AsyncCallback<List<ExecutionFlowDTO>> callback);

    public void load(int pFlowId, List<Integer> pMethID, AsyncCallback<Map<Integer, MethodCallDTO>> callback);

    public void load(int pFlowId, AsyncCallback<FullExecutionFlow> callback);

}
