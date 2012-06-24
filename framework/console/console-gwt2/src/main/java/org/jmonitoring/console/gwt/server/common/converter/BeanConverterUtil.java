package org.jmonitoring.console.gwt.server.common.converter;

import java.beans.XMLDecoder;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import javax.annotation.Resource;

import org.jmonitoring.console.gwt.server.common.ColorManager;
import org.jmonitoring.console.gwt.shared.flow.ExecutionFlowAbstractDTO;
import org.jmonitoring.console.gwt.shared.flow.ExecutionFlowDTO;
import org.jmonitoring.console.gwt.shared.flow.ExecutionFlowExportDTO;
import org.jmonitoring.console.gwt.shared.flow.MethodCallAbstractDTO;
import org.jmonitoring.console.gwt.shared.flow.MethodCallDTO;
import org.jmonitoring.console.gwt.shared.flow.MethodCallExportDTO;
import org.jmonitoring.console.gwt.shared.flow.MethodCallExtractDTO;
import org.jmonitoring.core.configuration.FormaterBean;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BeanConverterUtil
{
    @Resource(name = "formater")
    protected FormaterBean formater;

    @Resource(name = "color")
    protected ColorManager color;

    ExecutionFlowExportDTO convertExecFlowToExportDto(ExecutionFlowPO pLoadFlow)
    {
        return copyExecFlowToDto(pLoadFlow, new ExecutionFlowExportDTO());
    }

    ExecutionFlowDTO convertExecFlowToDto(ExecutionFlowPO pLoadFlow)
    {
        return copyExecFlowToDto(pLoadFlow, new ExecutionFlowDTO());
    }

    private <T extends ExecutionFlowAbstractDTO<?>> T copyExecFlowToDto(ExecutionFlowPO pLoadFlow, T pDest)
    {
        pDest.setId(pLoadFlow.getId());
        pDest.setThreadName(pLoadFlow.getThreadName());
        pDest.setJvmIdentifier(pLoadFlow.getJvmIdentifier());
        pDest.setBeginTime(formater.formatDateTime(pLoadFlow.getBeginTimeAsDate()));
        pDest.setEndTime(formater.formatDateTime(pLoadFlow.getEndTime()));
        pDest.setDuration(String.valueOf(pLoadFlow.getDuration()));
        pDest.setGroupName(pLoadFlow.getFirstMethodCall().getGroupName());
        pDest.setClassName(pLoadFlow.getFirstClassName());
        pDest.setMethodName(pLoadFlow.getFirstMethodName());
        return pDest;
    }

    MethodCallExportDTO convertMethodCallToExportDto(MethodCallPO pCallPO)
    {
        return copyMethodCallToDto(pCallPO, new MethodCallExportDTO());
    }

    MethodCallDTO convertMethodCallToDto(MethodCallPO pCallPO)
    {
        return copyMethodCallToDto(pCallPO, new MethodCallDTO());
    }

    private <T extends MethodCallAbstractDTO<?>> T copyMethodCallToDto(MethodCallPO pCallPO, T pDest)
    {
        BeanUtils.copyProperties(pCallPO, pDest, new String[] {"position", "beginTime", "endTime", "children", "flow" });
        pDest.setGroupColor(color.getColorString(pCallPO.getGroupName()));
        pDest.setPosition(String.valueOf(pCallPO.getMethId().getPosition()));
        pDest.setBeginTime(formater.formatDateTime(pCallPO.getBeginTime()), pCallPO.getBeginTime());
        pDest.setEndTime(formater.formatDateTime(pCallPO.getEndTime()), pCallPO.getEndTime());
        pDest.setFlowId(String.valueOf(pCallPO.getFlow().getId()));
        if (pCallPO.getParentMethodCall() != null)
        {
            pDest.setParentPosition(String.valueOf(pCallPO.getParentMethodCall().getMethId().getPosition()));
        }
        pDest.setPosition(String.valueOf(pCallPO.getPosition()));
        return pDest;
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

    /**
     * Concert an GZip/Xml serialized ExecutionFlow as an ExecutionFLow Object.
     * 
     * @param pFlowAsXml The GZip bytes.
     * @return The ExecutionFLow.
     */
    public ExecutionFlowPO convertFlowFromXml(InputStream pFlowAsGzipXml)
    {
        ExecutionFlowDTO tDto = readDtoFromXml(pFlowAsGzipXml);

        // Convert with visitor
        return null;
    }

    ExecutionFlowDTO readDtoFromXml(InputStream pFlowAsGzipXml)
    {
        try
        {
            GZIPInputStream tZipStream = new GZIPInputStream(pFlowAsGzipXml);
            XMLDecoder tDecoder = new XMLDecoder(tZipStream);
            Object tResult = tDecoder.readObject();
            tDecoder.close();
            if (tResult instanceof ExecutionFlowDTO)
            {
                ExecutionFlowDTO tResult2 = (ExecutionFlowDTO) tResult;
                return tResult2;
            } else
            {
                throw new RuntimeException("invalid class in gzip file: " + tResult.getClass().getName());
            }

        } catch (IOException e)
        {
            throw new RuntimeException("Unable to Unzip Xml ExecutionFlow", e);
        }
    }

}
