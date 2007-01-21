package org.jmonitoring.core.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.jmonitoring.core.persistence.ExecutionFlowPO;
import org.jmonitoring.core.persistence.MethodCallPK;
import org.jmonitoring.core.persistence.MethodCallPO;
import org.springframework.beans.BeanUtils;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public final class DtoHelper
{

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

    public static ExecutionFlowPO getSimpleCopy(ExecutionFlowDTO pFlowDto)
    {
        ExecutionFlowPO tResult = new ExecutionFlowPO();
        BeanUtils.copyProperties(pFlowDto, tResult, new String[] {"firstMethodCall", "beginTime", "endTime" });
        tResult.setBeginTime(pFlowDto.getBeginTime().getTime());
        tResult.setEndTime(pFlowDto.getEndTime().getTime());
        // tResult.getFirstMethodCall().setClassName(pFlowDto.getFirstMethodCall().getClassName());
        // tResult.setMethodName(pFlowDto.getFirstMethodCall().getMethodName());
        return tResult;
    }

    public static ExecutionFlowDTO getDeepCopy(ExecutionFlowPO pFlowPO)
    {
        ExecutionFlowDTO tResult = getSimpleCopy(pFlowPO);
        tResult.setFirstMethodCall(DtoHelper.getMethodCallDto(pFlowPO.getFirstMethodCall(), tResult, 0));
        return tResult;
    }

    public static ExecutionFlowPO getDeepCopy(ExecutionFlowDTO pFlowDto)
    {
        ExecutionFlowPO tResult = getSimpleCopy(pFlowDto);
        tResult.setFirstMethodCall(DtoHelper.getMethodCallPO(pFlowDto.getFirstMethodCall(), tResult));
        return tResult;
    }

    /**
     * Copy a methodCall, its direct children and its parent to a DTO structure.
     * 
     * @param pCallPO The methodCall to copy.
     * @return The DTO structure.
     */
    public static MethodCallDTO getFullMethodCallDto(MethodCallPO pCallPO, int pOrderInTheParentChildren)
    {
        MethodCallDTO tResult = simpleCopy(pCallPO, pOrderInTheParentChildren);
        MethodCallPO curChild;
        MethodCallDTO curChildDto;
        MethodCallDTO[] tChildren = new MethodCallDTO[pCallPO.getChildren().size()];
        for (int i = 0; i < pCallPO.getChildren().size(); i++)
        {
            curChild = (MethodCallPO) pCallPO.getChildren().get(i);
            curChildDto = simpleCopy(curChild, i);
            curChildDto.setParent(tResult);
            tChildren[i] = curChildDto;
        }
        tResult.setChildren(tChildren);
        if (pCallPO.getParentMethodCall() != null)
        {
            tResult.setParent(simpleCopy(pCallPO.getParentMethodCall(), -1));
        }
        return tResult;
    }

    /**
     * Copy a methodCall, its direct children and its parent to a DTO structure.
     * 
     * @param pCallDto The methodCall to copy.
     * @return The DTO structure.
     */
    public static MethodCallPO getFullMethodCallPO(MethodCallDTO pCallDto, ExecutionFlowPO pFlowPO)
    {
        MethodCallPO tResult = simpleCopy(pCallDto, pFlowPO);
        MethodCallDTO curChild;
        MethodCallPO curChildDto;
        List tChildren = new ArrayList(pCallDto.getChildren().length);
        for (int i = 0; i < pCallDto.getChildren().length; i++)
        {
            curChild = (MethodCallDTO) pCallDto.getChild(i);
            curChildDto = simpleCopy(curChild, pFlowPO);
            curChildDto.setParentMethodCall(tResult);
            tChildren.add(curChildDto);
        }
        tResult.setChildren(tChildren);
        // if (pCallDto.getParent() != null)
        // {
        // tResult.setParentMethodCall(simpleCopy(pCallDto.getParent()));
        // }
        return tResult;
    }

    static MethodCallDTO getMethodCallDto(MethodCallPO pCallPO)
    {
        return getMethodCallDto(pCallPO, null, 0);
    }

    static MethodCallDTO getMethodCallDto(MethodCallPO pCallPO, ExecutionFlowDTO pFlow, int pOrderInTheParentChildren)
    {
        MethodCallDTO tResult = simpleCopy(pCallPO, pOrderInTheParentChildren);
        tResult.setFlow(pFlow);
        MethodCallPO curMethod;
        MethodCallDTO curChildDto;
        MethodCallDTO[] tChildren = new MethodCallDTO[pCallPO.getChildren().size()];
        int i = 0;
        for (Iterator tIt = pCallPO.getChildren().iterator(); tIt.hasNext();)
        {
            curMethod = (MethodCallPO) tIt.next();
            curChildDto = getMethodCallDto(curMethod, pFlow, i);
            curChildDto.setParent(tResult);
            tChildren[i++] = curChildDto;
        }
        tResult.setChildren(tChildren);
        return tResult;
    }

    static MethodCallPO getMethodCallPO(MethodCallDTO pCallDTO, ExecutionFlowPO pFlowPO)
    {
        MethodCallPO tResult = simpleCopy(pCallDTO, pFlowPO);
        MethodCallDTO curMethod;
        MethodCallPO curChildPo;
        List tChildren = new ArrayList(pCallDTO.getChildren().length);
        for (int i = 0; i < pCallDTO.getChildren().length; i++)
        {
            curMethod = pCallDTO.getChild(i);
            curChildPo = getMethodCallPO(curMethod, pFlowPO);
            curChildPo.setParentMethodCall(tResult);
            tChildren.add(curChildPo);
        }
        tResult.setChildren(tChildren);
        return tResult;
    }

    public static MethodCallDTO simpleCopy(MethodCallPO pCallPO, int pOrderInTheParentChildren)
    {
        MethodCallDTO tResult = new MethodCallDTO();
        BeanUtils.copyProperties(pCallPO, tResult, new String[] {"beginTime", "endTime", "children", "flow" });
        tResult.setPosition(pCallPO.getMethId().getPosition());
        tResult.setBeginTime(new Date(pCallPO.getBeginTime()));
        tResult.setEndTime(new Date(pCallPO.getEndTime()));
        tResult.setFlowId(pCallPO.getFlow().getId());
        tResult.setChildPosition(pOrderInTheParentChildren);
        return tResult;
    }

    public static MethodCallPO simpleCopy(MethodCallDTO pCallDTO, ExecutionFlowPO pFlowPO)
    {
        MethodCallPO tResult = new MethodCallPO();
        tResult.setMethId(new MethodCallPK());
        BeanUtils.copyProperties(pCallDTO, tResult, new String[] {"beginTime", "endTime", "children", "flow" });
        tResult.setPosition(pCallDTO.getPosition());
        tResult.setBeginTime(pCallDTO.getBeginTime().getTime());
        tResult.setEndTime(pCallDTO.getEndTime().getTime());
        tResult.setFlow(pFlowPO);
        return tResult;
    }

    public static List copyListOfMethodPO(List pResult)
    {
        List tResult = new ArrayList(pResult.size());
        for (Iterator tIt = pResult.iterator(); tIt.hasNext();)
        {
            tResult.add(DtoHelper.getMethodCallDto((MethodCallPO) tIt.next()));
        }
        return tResult;
    }

    public static List simpleCopyListOfMethodPO(List pResult)
    {
        List tResult = new ArrayList(pResult.size());
        int i = 0;
        for (Iterator tIt = pResult.iterator(); tIt.hasNext();)
        {
            tResult.add(simpleCopy((MethodCallPO) tIt.next(), i++));
        }
        return tResult;
    }

    public static List copyListMethodCallFullExtract(List pListOfMeth)
    {
        MethodCallPO tMeth;
        List tResult = new ArrayList(pListOfMeth.size());
        for (Iterator tIt = pListOfMeth.iterator(); tIt.hasNext();)
        {
            tMeth = (MethodCallPO) tIt.next();
            tResult.add(copyMethodCallFullExtract(tMeth));
        }
        return tResult;
    }

    static MethodCallFullExtractDTO copyMethodCallFullExtract(MethodCallPO pMeth)
    {
        MethodCallFullExtractDTO tResult = new MethodCallFullExtractDTO();
        ExecutionFlowPO tFlow = pMeth.getFlow();
        tResult.setBeginDate(new Date(tFlow.getBeginTime()));
        tResult.setDuration(pMeth.getDuration());
        tResult.setFlowDuration(tFlow.getDuration());
        tResult.setFlowId(tFlow.getId());
        tResult.setJvmName(tFlow.getJvmIdentifier());
        tResult.setThreadName(tFlow.getThreadName());
        tResult.setPosition(pMeth.getPosition());
        return tResult;
    }

}
