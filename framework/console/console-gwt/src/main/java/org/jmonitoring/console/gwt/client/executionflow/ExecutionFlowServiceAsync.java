package org.jmonitoring.console.gwt.client.executionflow;

import java.util.List;

import org.jmonitoring.console.gwt.client.dto.ExecutionFlowDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public interface ExecutionFlowServiceAsync
{
    public void search(SearchCriteria pCriteria, AsyncCallback<List<ExecutionFlowDTO>> callback);

    public void load(int pFlowID, AsyncCallback<ExecutionFlowDTO> callback);
}
