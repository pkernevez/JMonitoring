package org.jmonitoring.console.gwt.client.executionflow;

import java.util.List;

import org.jmonitoring.console.gwt.client.dto.ExecutionFlowDTO;

import com.google.gwt.user.client.rpc.RemoteService;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public interface ExecutionFlowService extends RemoteService
{
    public List<ExecutionFlowDTO> search(SearchCriteria pCriteria);
}
