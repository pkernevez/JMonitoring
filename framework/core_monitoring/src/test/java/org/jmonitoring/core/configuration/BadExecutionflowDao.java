package org.jmonitoring.core.configuration;

import java.util.List;

import org.jmonitoring.core.common.UnknownFlowException;
import org.jmonitoring.core.dao.FlowSearchCriterion;
import org.jmonitoring.core.dao.IExecutionFlowDAO;
import org.jmonitoring.core.persistence.ExecutionFlowPO;
import org.jmonitoring.core.persistence.MethodCallPO;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

public class BadExecutionflowDao implements IExecutionFlowDAO
{

    public int countFlows()
    {
        return 0;
    }

    public void createDataBase()
    {
    }

    public void deleteAllFlows()
    {
    }

    public void deleteFlow(int pId) throws UnknownFlowException
    {
    }

    public List getListOfExecutionFlowPO(FlowSearchCriterion pCriterion)
    {
        return null;
    }

    public List getListOfMethodCall(String pClassName, String pMethodName)
    {
        return null;
    }

    public List getListOfMethodCallExtract()
    {
        return null;
    }

    public List getMethodCallList(String pClassName, String pMethodName, long pDurationMin, long pDurationMax)
    {
        return null;
    }

    public int insertFullExecutionFlow(ExecutionFlowPO pExecutionFlow)
    {
        return 0;
    }

    public ExecutionFlowPO readExecutionFlow(int pFlowId)
    {
        return null;
    }

    public MethodCallPO readMethodCall(int pFlowId, int pMethodId)
    {
        return null;
    }

}
