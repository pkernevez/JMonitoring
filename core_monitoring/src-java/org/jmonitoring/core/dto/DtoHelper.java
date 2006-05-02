package org.jmonitoring.core.dto;

import org.jmonitoring.core.persistence.ExecutionFlowPO;
import org.jmonitoring.core.persistence.MethodCallPO;
import org.springframework.beans.BeanUtils;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

public final class DtoHelper
{

    private DtoHelper()
    {
    }

    public static ExecutionFlowDTO getExecutionFlowDto(ExecutionFlowPO pFlowPO)
    {
        ExecutionFlowDTO tResult = new ExecutionFlowDTO();
        BeanUtils.copyProperties(pFlowPO, tResult);
        tResult.setFirstMeasure( getMethodCallDto(pFlowPO.getFirstMethodCall()));
        return tResult;
    }

    private static MethodCallDTO getMethodCallDto(MethodCallPO pCallPO)
    {
        MethodCallDTO tResult = new MethodCallDTO();
        BeanUtils.copyProperties(pCallPO, tResult);
        
        return tResult;
    }

}
