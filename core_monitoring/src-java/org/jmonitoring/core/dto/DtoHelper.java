package org.jmonitoring.core.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.jmonitoring.core.persistence.ExecutionFlowPO;
import org.jmonitoring.core.persistence.MethodCallPO;
import org.springframework.beans.BeanUtils;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public final class DtoHelper
{

    private int mSequence = 1;

    DtoHelper()
    {
    }

    public static ExecutionFlowDTO getSimpleCopy(ExecutionFlowPO pFlowPO)
    {
        ExecutionFlowDTO tResult = new ExecutionFlowDTO();
        BeanUtils.copyProperties(pFlowPO, tResult, new String[] {"firstMethodCall", "beginTime", "endTime" });
        tResult.setBeginTime(new Date(pFlowPO.getBeginTime()));
        tResult.setEndTime(new Date(pFlowPO.getEndTime()));
        tResult.setClassName(pFlowPO.getFirstMethodCall().getClassName());
        tResult.setMethodName(pFlowPO.getFirstMethodCall().getMethodName());
        return tResult;
    }

    public static ExecutionFlowDTO getDeepCopy(ExecutionFlowPO pFlowPO)
    {
        ExecutionFlowDTO tResult = getSimpleCopy(pFlowPO);
        DtoHelper tHelper = new DtoHelper();
        tResult.setFirstMethodCall(tHelper.getMethodCallDto(pFlowPO.getFirstMethodCall()));
        return tResult;
    }

    /**
     * Copy a methodCall, its direct children and its parent to a DTO structure.
     * 
     * @param pCallPO The methodCall to copy.
     * @return The DTO structure.
     */
    public static MethodCallDTO getFullMethodCallDto(MethodCallPO pCallPO)
    {
        DtoHelper tHelper = new DtoHelper();
        MethodCallDTO tResult = tHelper.getSimpleCopy(pCallPO);
        MethodCallPO curChild;
        MethodCallDTO curChildDto;
        for (int i = 0; i < pCallPO.getChildren().size(); i++)
        {
            curChild = (MethodCallPO) pCallPO.getChildren().get(i);
            curChildDto = tHelper.getSimpleCopy(curChild);
            tResult.addChild(curChildDto);
            curChildDto.setParent(tResult);
        }
        if (pCallPO.getParentMethodCall() != null)
        {
            tResult.setParent(tHelper.getSimpleCopy(pCallPO.getParentMethodCall()));
        }
        return tResult;
    }

    MethodCallDTO getMethodCallDto(MethodCallPO pCallPO)
    {
        MethodCallDTO tResult = getSimpleCopy(pCallPO);
        MethodCallPO curMethod;
        MethodCallDTO curChildDto;
        for (Iterator tIt = pCallPO.getChildren().iterator(); tIt.hasNext();)
        {
            curMethod = (MethodCallPO) tIt.next();
            curChildDto = getMethodCallDto(curMethod);
            tResult.addChild(curChildDto);
            curChildDto.setParent(tResult);
        }
        return tResult;
    }

    private MethodCallDTO getSimpleCopy(MethodCallPO pCallPO)
    {
        MethodCallDTO tResult = new MethodCallDTO();
        BeanUtils.copyProperties(pCallPO, tResult, new String[] {"beginTime", "endTime" });
        tResult.setBeginTime(new Date(pCallPO.getBeginTime()));
        tResult.setEndTime(new Date(pCallPO.getEndTime()));
        tResult.setFlowId(pCallPO.getFlowId());
        tResult.setSequenceId(mSequence++);
        return tResult;
    }

    public static List copyListOfMethodPO(List pResult)
    {
        List tResult = new ArrayList(pResult.size());
        DtoHelper tHelper = new DtoHelper();
        for (Iterator tIt = pResult.iterator(); tIt.hasNext();)
        {
            tResult.add(tHelper.getMethodCallDto((MethodCallPO) tIt.next()));
        }
        return tResult;
    }

}
