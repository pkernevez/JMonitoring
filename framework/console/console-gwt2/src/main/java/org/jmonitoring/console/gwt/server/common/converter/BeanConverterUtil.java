package org.jmonitoring.console.gwt.server.common.converter;

import org.jmonitoring.console.gwt.server.common.ColorManager;
import org.jmonitoring.console.gwt.shared.flow.ExecutionFlowDTO;
import org.jmonitoring.console.gwt.shared.flow.MethodCallDTO;
import org.jmonitoring.console.gwt.shared.flow.MethodCallExtractDTO;
import org.jmonitoring.core.configuration.FormaterBean;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BeanConverterUtil
{
    @Autowired
    protected FormaterBean formater;

    @Autowired
    protected ColorManager color;

    ExecutionFlowDTO convertExecFlowToDto(ExecutionFlowPO pLoadFlow)
    {
        ExecutionFlowDTO tResult = new ExecutionFlowDTO();
        tResult.setId(pLoadFlow.getId());
        tResult.setThreadName(pLoadFlow.getThreadName());
        tResult.setJvmIdentifier(pLoadFlow.getJvmIdentifier());
        tResult.setBeginTime(formater.formatDateTime(pLoadFlow.getBeginTimeAsDate()));
        tResult.setEndTime(formater.formatDateTime(pLoadFlow.getEndTime()));
        tResult.setDuration(String.valueOf(pLoadFlow.getDuration()));
        tResult.setGroupName(pLoadFlow.getFirstMethodCall().getGroupName());
        tResult.setClassName(pLoadFlow.getFirstClassName());
        tResult.setMethodName(pLoadFlow.getFirstMethodName());
        return tResult;
    }

    MethodCallDTO convertMethodCallToDto(MethodCallPO pCallPO)
    {
        MethodCallDTO tResult = new MethodCallDTO();
        BeanUtils.copyProperties(pCallPO, tResult,
                                 new String[] {"position", "beginTime", "endTime", "children", "flow" });
        tResult.setGroupColor(color.getColorString(pCallPO.getGroupName()));
        tResult.setPosition(String.valueOf(pCallPO.getMethId().getPosition()));
        tResult.setBeginTime(formater.formatDateTime(pCallPO.getBeginTime()), pCallPO.getBeginTime());
        tResult.setEndTime(formater.formatDateTime(pCallPO.getEndTime()), pCallPO.getEndTime());
        tResult.setFlowId(String.valueOf(pCallPO.getFlow().getId()));
        if (pCallPO.getParentMethodCall() != null)
        {
            tResult.setParentPosition(String.valueOf(pCallPO.getParentMethodCall().getMethId().getPosition()));
        }
        tResult.setPosition(String.valueOf(pCallPO.getPosition()));
        return tResult;
    }

    MethodCallExtractDTO convertToExtractDto(MethodCallPO pMethodCall)
    {
        MethodCallExtractDTO tExtract = new MethodCallExtractDTO();
        tExtract.setFullClassName(pMethodCall.getClassName());
        tExtract.setMethodName(pMethodCall.getMethodName());
        tExtract.setDuration(String.valueOf(pMethodCall.getDuration()));
        tExtract.setGroupName(pMethodCall.getGroupName());
        tExtract.setPosition(String.valueOf(pMethodCall.getPosition()));
        MethodCallPO tParent = pMethodCall.getParentMethodCall();
        if (tParent != null)
        {
            tExtract.setParentPosition(String.valueOf((tParent.getMethId().getPosition())));
        }
        long tDurationFromPrev;
        if (tParent == null)
        {
            tDurationFromPrev = 0;
        } else if (pMethodCall.isFirstChild())
        {
            tDurationFromPrev = pMethodCall.getBeginTime() - tParent.getBeginTime();
        } else
        {
            tDurationFromPrev =
                pMethodCall.getBeginTime() - tParent.getChild(pMethodCall.getPositionInSiblin() - 1).getEndTime();
        }
        tExtract.setTimeFromPrevChild(String.valueOf(tDurationFromPrev));
        tExtract.setThrownException(pMethodCall.getThrowableClass() != null);
        return tExtract;
    }

}
