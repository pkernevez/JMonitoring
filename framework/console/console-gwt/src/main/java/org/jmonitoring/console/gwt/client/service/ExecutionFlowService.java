package org.jmonitoring.console.gwt.client.service;

import java.util.List;
import java.util.Map;

import org.jmonitoring.console.gwt.client.dto.ClientUnknowFlowException;
import org.jmonitoring.console.gwt.client.dto.ExecutionFlowDTO;
import org.jmonitoring.console.gwt.client.dto.FullExecutionFlowDTO;
import org.jmonitoring.console.gwt.client.dto.MethodCallDTO;
import org.jmonitoring.console.gwt.client.dto.RootMethodCallDTO;
import org.jmonitoring.console.gwt.client.dto.StatMethodCallDTO;

import com.google.gwt.user.client.rpc.RemoteService;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public interface ExecutionFlowService extends RemoteService
{
    public static final String CHART_BAR_FLOWS = "CHART_BAR_FLOWS";

    public static final String STAT_METHOD_CALL = "STAT_METHOD_CALL";

    /** Constant used for the URL generation of the PieChart representing the number of calls. */
    public static final String NB_CALL_TO_GROUP = "NB_CALL_TO_GROUP";

    /** Constant used for the URL generation of the PieChart representing the duration of calls. */
    public static final String DURATION_IN_GROUP = "DURATION_IN_GROUP";

    public List<ExecutionFlowDTO> search(SearchCriteria pCriteria);

    public Map<Integer, MethodCallDTO> load(int pFlowId, List<Integer> pMethID);

    public void delete(int pFlowId) throws ClientUnknowFlowException;

    public FullExecutionFlowDTO load(int pFlowId);

    public RootMethodCallDTO load(int pFlowId, int pMethPosition);

    public StatMethodCallDTO loadStat(String pClassName, String pMethodName, int pAggregationScope);
}
