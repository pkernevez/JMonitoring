package org.jmonitoring.core.dto;

import java.util.Date;
import java.util.Iterator;

import org.jmonitoring.core.persistence.ExecutionFlowPO;
import org.jmonitoring.core.persistence.MethodCallPO;
import org.springframework.beans.BeanUtils;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public final class DtoHelper
{

    private DtoHelper()
    {
    }

    public static ExecutionFlowDTO getSimpleCopy(ExecutionFlowPO pFlowPO)
    {
        ExecutionFlowDTO tResult = new ExecutionFlowDTO();
        BeanUtils.copyProperties(pFlowPO, tResult, new String[] {"firstMethodCall","beginTime", "endTime" });
        tResult.setBeginTime(new Date(pFlowPO.getBeginTime()));
        tResult.setEndTime(new Date(pFlowPO.getEndTime()));
        return tResult;
    }

    public static ExecutionFlowDTO getDeepCopy(ExecutionFlowPO pFlowPO)
    {
        ExecutionFlowDTO tResult = getSimpleCopy(pFlowPO);
        tResult.setFirstMethodCall(getMethodCallDto(pFlowPO.getFirstMethodCall()));
        return tResult;
    }

    static MethodCallDTO getMethodCallDto(MethodCallPO pCallPO)
    {
        MethodCallDTO tResult = new MethodCallDTO();
        BeanUtils.copyProperties(pCallPO, tResult, new String[]{"beginTime", "endTime"} );
        tResult.setBeginTime(new Date(pCallPO.getBeginTime()));
        tResult.setEndTime(new Date(pCallPO.getEndTime()));
        MethodCallPO curMethod;
        for (Iterator tIt = pCallPO.getChildren().iterator(); tIt.hasNext(); )
        {
            curMethod = (MethodCallPO) tIt.next();
            tResult.addChild(getMethodCallDto(curMethod));
        }
        return tResult;
    }

}
