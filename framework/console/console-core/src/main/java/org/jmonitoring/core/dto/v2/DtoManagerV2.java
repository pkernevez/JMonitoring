package org.jmonitoring.core.dto.v2;

import org.jmonitoring.core.configuration.FormaterBean;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPK;
import org.jmonitoring.core.domain.MethodCallPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DtoManagerV2 {

    Logger sLog = LoggerFactory.getLogger(DtoManagerV2.class);

    private FormaterBean mFormater;

    public ExecutionFlowPO loadV2Export(byte[] msg) {
        ExecutionFlowDTO dto = readDTO(msg);
        return (dto == null ? null : deepCopy(dto));
    }

    ExecutionFlowDTO readDTO(byte[] msg) {
        try {
            String raw = new String(msg, "UTF-8");
            raw = raw.replaceAll("class=\"org\\.jmonitoring\\.core\\.dto\\.", "class=\"org.jmonitoring.core.dto.v2.");

            XMLDecoder tDecoder = new XMLDecoder(new ByteArrayInputStream(raw.getBytes()));
            Object tResult = tDecoder.readObject();
            tDecoder.close();
            return (ExecutionFlowDTO) tResult;

        } catch (UnsupportedEncodingException | ArrayIndexOutOfBoundsException e) {
            sLog.info("Loading v2 export failed... Should not be a v2 export format");
            return null;
        }
    }

    ExecutionFlowPO deepCopy(ExecutionFlowDTO execFlow) {
        ExecutionFlowPO tResult = copy(execFlow);
        tResult.setFirstMethodCall(deepCopy(execFlow.getFirstMethodCall(), tResult));
        return tResult;
    }

    MethodCallPO deepCopy(MethodCallDTO methodCallDto, ExecutionFlowPO execFlow) {
        MethodCallPO tResult = copy(methodCallDto, execFlow);
        MethodCallPO curChildPo;
        List<MethodCallPO> tChildren = new ArrayList<MethodCallPO>(methodCallDto.getChildren().length);
        for (MethodCallDTO curMethod : methodCallDto.getChildren()) {
            curChildPo = deepCopy(curMethod, execFlow);
            curChildPo.setParentMethodCall(tResult);
            tChildren.add(curChildPo);
        }
        tResult.setChildren(tChildren);
        return tResult;
    }

    ExecutionFlowPO copy(ExecutionFlowDTO execFlow) {
        ExecutionFlowPO tResult = new ExecutionFlowPO();
        BeanUtils.copyProperties(execFlow, tResult, new String[]{"firstMethodCall", "beginTime", "endTime"});
        tResult.setBeginTime(execFlow.getBeginTime().getTime());
        tResult.setEndTime(execFlow.getEndTime().getTime());
        tResult.setFirstClassName(execFlow.getClassName());
        tResult.setFirstMethodName(execFlow.getMethodName());
        return tResult;

    }

    MethodCallPO copy(MethodCallDTO methodCallDto, ExecutionFlowPO execFlow) {
        MethodCallPO tResult = new MethodCallPO();
        tResult.setMethId(new MethodCallPK());
        BeanUtils.copyProperties(methodCallDto, tResult, new String[]{"beginTime", "endTime", "children", "flow"});
//        tResult.setPosition(methodCallDto.getPosition());
        tResult.setBeginTime(methodCallDto.getBeginTime().getTime());
        tResult.setEndTime(methodCallDto.getEndTime().getTime());
        tResult.setThrowableClass(methodCallDto.getThrowableClass());
        tResult.setFlow(execFlow);
        return tResult;
    }


}
