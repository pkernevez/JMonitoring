package org.jmonitoring.console.gwt.server.flow;

import it.pianetatecno.gwt.utility.client.table.Request;
import it.pianetatecno.gwt.utility.client.table.SerializableResponse;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.gwtrpcspring.RemoteServiceUtil;
import org.jmonitoring.console.gwt.client.flow.FlowService;
import org.jmonitoring.console.gwt.server.common.ColorManager;
import org.jmonitoring.console.gwt.server.image.ChartBarGenerator;
import org.jmonitoring.console.gwt.server.image.ChartBarGenerator.FlowDetailChart;
import org.jmonitoring.console.gwt.server.image.PieChartGenerator;
import org.jmonitoring.console.gwt.shared.flow.ExecutionFlowDTO;
import org.jmonitoring.console.gwt.shared.flow.FlowExtractDTO;
import org.jmonitoring.console.gwt.shared.flow.MethodCallDTO;
import org.jmonitoring.console.gwt.shared.flow.MethodCallExtractDTO;
import org.jmonitoring.core.configuration.FormaterBean;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlowServiceImpl implements FlowService
{

    static Logger sLog = getLogger();

    @Resource(name = "consoleDao")
    protected ConsoleDao dao;

    @Resource(name = "formater")
    private FormaterBean formater;

    @Autowired
    private ColorManager color;

    private static Logger getLogger()
    {
        return LoggerFactory.getLogger(FlowServiceImpl.class);
    }

    public SerializableResponse<FlowExtractDTO> search(Request pRequest)
    {
        sLog.info("call search");
        SerializableResponse<FlowExtractDTO> tResponse = new SerializableResponse<FlowExtractDTO>();
        tResponse.setRequest(pRequest);
        tResponse.setTotalResults(dao.countFlows(pRequest));
        tResponse.setRows(dao.search(pRequest));
        return tResponse;
    }

    public ExecutionFlowDTO loadFull(int pFlowId)
    {
        return convertToDtoDeeply(dao.loadFullFlow(pFlowId));
    }

    public ExecutionFlowDTO loadAndGenerateImage(int pFlowId)
    {
        HttpSession tSession = RemoteServiceUtil.getThreadLocalSession();
        ExecutionFlowPO tFlowPo = dao.loadFullFlow(pFlowId);
        generateDurationInGroupChart(tSession, "DurationInGroups&" + pFlowId, tFlowPo.getFirstMethodCall());
        generateGroupsCallsChart(tSession, "GroupsCalls&" + pFlowId, tFlowPo.getFirstMethodCall());
        String tMap = generateFlowDetailChart(tSession, "FlowDetail&" + pFlowId, tFlowPo.getFirstMethodCall()).map;
        ExecutionFlowDTO tFlow = convertToDto(tFlowPo);
        tFlow.setDetailMap(tMap);
        return tFlow;
    }

    public byte[] generateDurationInGroupChart(HttpSession pSession, String pSessionId, int pFlowId)
    {
        MethodCallPO tFirstMeasure = dao.loadFullFlow(pFlowId).getFirstMethodCall();
        return generateDurationInGroupChart(pSession, pSessionId, tFirstMeasure);
    }

    public byte[] generateDurationInGroupChart(HttpSession pSession, String pSessionId, MethodCallPO pFirstMeasure)
    {
        PieChartGenerator tGenerator = new PieChartGenerator(color);
        byte[] tOutput = tGenerator.getDurationInGroup(pFirstMeasure);
        pSession.setAttribute(pSessionId, tOutput);
        return tOutput;
    }

    public byte[] generateGroupsCallsChart(HttpSession pSession, String pSessionId, int pFlowId)
    {
        ExecutionFlowPO tLoadFullFlow = dao.loadFullFlow(pFlowId);

        return generateGroupsCallsChart(pSession, pSessionId, tLoadFullFlow.getFirstMethodCall());
    }

    public byte[] generateGroupsCallsChart(HttpSession pSession, String pSessionId, MethodCallPO pFirstMeasure)
    {
        PieChartGenerator tGenerator = new PieChartGenerator(color);
        byte[] tOutput = tGenerator.getGroupCalls(pFirstMeasure);
        pSession.setAttribute(pSessionId, tOutput);
        return tOutput;
    }

    public FlowDetailChart generateFlowDetailChart(HttpSession pSession, String pSessionId, int pFlowId)
    {
        ExecutionFlowPO tLoadFullFlow = dao.loadFullFlow(pFlowId);
        return generateFlowDetailChart(pSession, pSessionId, tLoadFullFlow.getFirstMethodCall());
    }

    public FlowDetailChart generateFlowDetailChart(HttpSession pSession, String pSessionId, MethodCallPO pFirstMeasure)
    {
        ChartBarGenerator tGenerator = new ChartBarGenerator(color, pFirstMeasure);
        FlowDetailChart tResult = tGenerator.getImage();
        pSession.setAttribute(pSessionId, tResult.image);
        return tResult;
    }

    public ExecutionFlowDTO convertToDtoDeeply(ExecutionFlowPO pFlowPO)
    {
        ExecutionFlowDTO tResult = convertToDto(pFlowPO);
        tResult.setFirstMethodCall(convertToDtoDeeply(pFlowPO.getFirstMethodCall(), pFlowPO.getId()));
        return tResult;
    }

    MethodCallDTO convertToDtoDeeply(MethodCallPO pCallPO, int pFlowId)
    {
        MethodCallDTO tResult = convertToDto(pCallPO, null, 0);
        tResult.setFlowId(String.valueOf(pFlowId));
        MethodCallExtractDTO tRootDuplication = convertToExtract(pCallPO, 0);
        tResult.setChildren(convertToDtoDeeply(pCallPO.getChildren(), tRootDuplication));
        return tResult;
    }

    MethodCallExtractDTO convertToExtract(MethodCallPO pMethodCall, int pChildPosition)
    {
        MethodCallExtractDTO tExtract = new MethodCallExtractDTO();
        tExtract.setFullMethodName(pMethodCall.getClassName() + "." + pMethodCall.getMethodName() + "()");
        tExtract.setChildPosition(String.valueOf(pChildPosition));
        tExtract.setDuration(String.valueOf(pMethodCall.getDuration()));
        tExtract.setGroupName(pMethodCall.getGroupName());
        tExtract.setPosition(String.valueOf(pMethodCall.getPosition()));
        if (pMethodCall.getParentMethodCall() != null)
        {
            tExtract.setParentPosition(String.valueOf((pMethodCall.getParentMethodCall().getPosition())));
        }
        long tDurationFromPrev;
        MethodCallPO tParent = pMethodCall.getParentMethodCall();
        if (tParent == null)
        {
            tDurationFromPrev = 0;
        } else if (pChildPosition == 0)
        {
            tDurationFromPrev = pMethodCall.getBeginTime() - tParent.getBeginTime();
        } else
        {
            tDurationFromPrev = pMethodCall.getBeginTime() - tParent.getChild(pChildPosition - 1).getEndTime();
        }
        tExtract.setTimeFromPrevChild(String.valueOf(tDurationFromPrev));
        return tExtract;
    }

    MethodCallExtractDTO[] convertToDtoDeeply(List<MethodCallPO> pCallPO, MethodCallExtractDTO pParent)
    {
        MethodCallExtractDTO[] tResult = new MethodCallExtractDTO[pCallPO.size()];
        int i = 0;
        for (MethodCallPO tMethodCallPO : pCallPO)
        {
            MethodCallExtractDTO tExtract = convertToExtract(tMethodCallPO, i);
            tExtract.setChildren(convertToDtoDeeply(tMethodCallPO.getChildren(), tExtract));
            tResult[i] = tExtract;
            i++;
        }
        return tResult;
    }

    public MethodCallDTO convertToDto(MethodCallPO pCallPO, MethodCallDTO pParent, int pOrderInTheParentChildren)
    {
        MethodCallDTO tResult = new MethodCallDTO();
        BeanUtils.copyProperties(pCallPO, tResult,
                                 new String[] {"position", "beginTime", "endTime", "children", "flow" });
        tResult.setGroupColor(color.getColorString(pCallPO.getGroupName()));
        tResult.setPosition(String.valueOf(pCallPO.getMethId().getPosition()));
        tResult.setBeginTime(formater.formatDateTime(pCallPO.getBeginTime()), pCallPO.getBeginTime());
        tResult.setEndTime(formater.formatDateTime(pCallPO.getEndTime()), pCallPO.getEndTime());
        tResult.setFlowId(String.valueOf(pCallPO.getFlow().getId()));
        tResult.setChildPosition(pOrderInTheParentChildren);
        tResult.setPosition(String.valueOf(pCallPO.getPosition()));
        if (pParent != null)
        {
            tResult.setParentPosition(pParent.getParentPosition());
        }
        return tResult;
    }

    private ExecutionFlowDTO convertToDto(ExecutionFlowPO pLoadFlow)
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

    public MethodCallDTO loadMethodCall(int pMethodCallId, int pPosition)
    {
        return convertToDtoDeeply(dao.loadMethodCall(pMethodCallId, pPosition), -1);
    }

}
