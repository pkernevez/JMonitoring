package org.jmonitoring.core.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jmonitoring.core.configuration.ColorManager;
import org.jmonitoring.core.configuration.FormaterBean;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPK;
import org.jmonitoring.core.domain.MethodCallPO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/
@Service
public class DtoManager
{
    @Autowired
    private FormaterBean mFormater;

    @Autowired
    private ColorManager mColor;

    DtoManager()
    {
    }

    public ExecutionFlowDTO getSimpleCopy(ExecutionFlowPO pFlowPO)
    {
        ExecutionFlowDTO tResult = new ExecutionFlowDTO();
        BeanUtils.copyProperties(pFlowPO, tResult, new String[] {"firstMethodCall", "beginTime", "endTime" });
        tResult.setBeginTime(mFormater.formatDateTime(pFlowPO.getBeginTime()));
        tResult.setEndTime(mFormater.formatDateTime(pFlowPO.getEndTime()));
        tResult.setClassName(pFlowPO.getFirstMethodCall().getClassName());
        tResult.setMethodName(pFlowPO.getFirstMethodCall().getMethodName());
        return tResult;
    }

    public ExecutionFlowPO getSimpleCopy(ExecutionFlowDTO pFlowDto)
    {
        ExecutionFlowPO tResult = new ExecutionFlowPO();
        BeanUtils.copyProperties(pFlowDto, tResult, new String[] {"firstMethodCall", "beginTime", "endTime" });
        tResult.setBeginTime(mFormater.parseDateTime(pFlowDto.getBeginTime()).getTime());
        tResult.setEndTime(mFormater.parseDateTime(pFlowDto.getEndTime()).getTime());
        return tResult;
    }

    public ExecutionFlowDTO getDeepCopy(ExecutionFlowPO pFlowPO)
    {
        ExecutionFlowDTO tResult = getSimpleCopy(pFlowPO);
        tResult.setFirstMethodCall(getMethodCallDto(pFlowPO.getFirstMethodCall(), tResult, 0));
        return tResult;
    }

    public ExecutionFlowPO getDeepCopy(ExecutionFlowDTO pFlowDto)
    {
        ExecutionFlowPO tResult = getSimpleCopy(pFlowDto);
        tResult.setFirstMethodCall(getMethodCallPO(pFlowDto.getFirstMethodCall(), tResult));
        return tResult;
    }

    /**
     * Copy a methodCall, its direct children and its parent to a DTO structure.
     * 
     * @param pCallPO The methodCall to copy.
     * @return The DTO structure.
     */
    public MethodCallDTO getFullMethodCallDto(MethodCallPO pCallPO, int pOrderInTheParentChildren)
    {
        MethodCallDTO tResult = simpleCopy(pCallPO, pOrderInTheParentChildren);
        MethodCallPO curChild;
        MethodCallDTO curChildDto;
        MethodCallDTO[] tChildren = new MethodCallDTO[pCallPO.getChildren().size()];
        for (int i = 0; i < pCallPO.getChildren().size(); i++)
        {
            curChild = pCallPO.getChildren().get(i);
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
    public MethodCallPO getFullMethodCallPO(MethodCallDTO pCallDto, ExecutionFlowPO pFlowPO)
    {
        MethodCallPO tResult = simpleCopy(pCallDto, pFlowPO);
        MethodCallDTO curChild;
        MethodCallPO curChildDto;
        List<MethodCallPO> tChildren = new ArrayList<MethodCallPO>(pCallDto.getChildren().length);
        for (int i = 0; i < pCallDto.getChildren().length; i++)
        {
            curChild = pCallDto.getChild(i);
            curChildDto = simpleCopy(curChild, pFlowPO);
            curChildDto.setParentMethodCall(tResult);
            tChildren.add(curChildDto);
        }
        tResult.setChildren(tChildren);
        return tResult;
    }

    MethodCallDTO getMethodCallDto(MethodCallPO pCallPO)
    {
        return getMethodCallDto(pCallPO, null, 0);
    }

    MethodCallDTO getMethodCallDto(MethodCallPO pCallPO, ExecutionFlowDTO pFlow, int pOrderInTheParentChildren)
    {
        MethodCallDTO tResult = simpleCopy(pCallPO, pOrderInTheParentChildren);
        tResult.setFlow(pFlow);
        MethodCallDTO curChildDto;
        MethodCallDTO[] tChildren = new MethodCallDTO[pCallPO.getChildren().size()];
        int i = 0;
        for (MethodCallPO curMethod : pCallPO.getChildren())
        {
            curChildDto = getMethodCallDto(curMethod, pFlow, i);
            curChildDto.setParent(tResult);
            tChildren[i++] = curChildDto;
        }
        tResult.setChildren(tChildren);
        return tResult;
    }

    MethodCallPO getMethodCallPO(MethodCallDTO pCallDTO, ExecutionFlowPO pFlowPO)
    {
        MethodCallPO tResult = simpleCopy(pCallDTO, pFlowPO);
        MethodCallPO curChildPo;
        List<MethodCallPO> tChildren = new ArrayList<MethodCallPO>(pCallDTO.getChildren().length);
        for (MethodCallDTO curMethod : pCallDTO.getChildren())
        {
            curChildPo = getMethodCallPO(curMethod, pFlowPO);
            curChildPo.setParentMethodCall(tResult);
            tChildren.add(curChildPo);
        }
        tResult.setChildren(tChildren);
        return tResult;
    }

    public MethodCallDTO simpleCopy(MethodCallPO pCallPO, int pOrderInTheParentChildren)
    {
        MethodCallDTO tResult = new MethodCallDTO();
        BeanUtils.copyProperties(pCallPO, tResult, new String[] {"beginTime", "endTime", "children", "flow" });
        tResult.setGroupColor(mColor.getColorString(pCallPO.getGroupName()));
        tResult.setPosition(pCallPO.getMethId().getPosition());
        tResult.setBeginTime(mFormater.formatDateTime(pCallPO.getBeginTime()), pCallPO.getBeginTime());
        tResult.setEndTime(mFormater.formatDateTime(pCallPO.getEndTime()), pCallPO.getEndTime());
        tResult.setFlowId(pCallPO.getFlow().getId());
        tResult.setChildPosition(pOrderInTheParentChildren);
        return tResult;
    }

    public MethodCallPO simpleCopy(MethodCallDTO pCallDTO, ExecutionFlowPO pFlowPO)
    {
        MethodCallPO tResult = new MethodCallPO();
        tResult.setMethId(new MethodCallPK());
        BeanUtils.copyProperties(pCallDTO, tResult, new String[] {"beginTime", "endTime", "children", "flow" });
        tResult.setPosition(pCallDTO.getPosition());
        tResult.setBeginTime(pCallDTO.getBeginMilliSeconds());
        tResult.setEndTime(pCallDTO.getEndMilliSeconds());
        tResult.setFlow(pFlowPO);
        return tResult;
    }

    public List<MethodCallDTO> copyListOfMethodPO(List<MethodCallPO> pSourceList)
    {
        List<MethodCallDTO> tResult = new ArrayList<MethodCallDTO>(pSourceList.size());
        for (MethodCallPO tMeth : pSourceList)
        {
            tResult.add(getMethodCallDto(tMeth));
        }
        return tResult;
    }

    public List<MethodCallDTO> simpleCopyListOfMethodPO(List<MethodCallPO> pSourceList)
    {
        List<MethodCallDTO> tResult = new ArrayList<MethodCallDTO>(pSourceList.size());
        int i = 0;
        for (MethodCallPO tMeth : pSourceList)
        {
            tResult.add(simpleCopy(tMeth, i++));
        }
        return tResult;
    }

    public List<MethodCallFullExtractDTO> copyListMethodCallFullExtract(List<MethodCallPO> pListOfMeth)
    {
        List<MethodCallFullExtractDTO> tResult = new ArrayList<MethodCallFullExtractDTO>(pListOfMeth.size());
        for (MethodCallPO tMeth : pListOfMeth)
        {
            tResult.add(copyMethodCallFullExtract(tMeth));
        }
        return tResult;
    }

    MethodCallFullExtractDTO copyMethodCallFullExtract(MethodCallPO pMeth)
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
