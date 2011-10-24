package org.jmonitoring.console.gwt.server.flow;

import it.pianetatecno.gwt.utility.client.table.Request;
import it.pianetatecno.gwt.utility.client.table.SerializableResponse;

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
        ExecutionFlowDTO tFlow = convertToDtoDeeply(tFlowPo);
        generateDurationInGroupChart(tSession, "DurationInGroups&" + pFlowId, tFlow.getFirstMethodCall());
        generateGroupsCallsChart(tSession, "GroupsCalls&" + pFlowId, tFlow.getFirstMethodCall());
        String tMap = generateFlowDetailChart(tSession, "FlowDetail&" + pFlowId, tFlow.getFirstMethodCall()).map;
        tFlow = convertToDto(tFlowPo);
        tFlow.setDetailMap(tMap);
        return tFlow;
    }

    public byte[] generateDurationInGroupChart(HttpSession pSession, String pSessionId, int pFlowId)
    {
        MethodCallDTO tFirstMeasure = convertToDtoDeeply(dao.loadFullFlow(pFlowId)).getFirstMethodCall();
        return generateDurationInGroupChart(pSession, pSessionId, tFirstMeasure);
    }

    public byte[] generateDurationInGroupChart(HttpSession pSession, String pSessionId, MethodCallDTO pFirstMeasure)
    {
        PieChartGenerator tGenerator = new PieChartGenerator(color);
        byte[] tOutput = tGenerator.getDurationInGroup(pFirstMeasure);
        pSession.setAttribute(pSessionId, tOutput);
        return tOutput;
    }

    public byte[] generateGroupsCallsChart(HttpSession pSession, String pSessionId, int pFlowId)
    {
        ExecutionFlowPO tLoadFullFlow = dao.loadFullFlow(pFlowId);

        ExecutionFlowDTO tConvertToDtoDeeply = convertToDtoDeeply(tLoadFullFlow);
        MethodCallDTO tFirstMeasure = tConvertToDtoDeeply.getFirstMethodCall();
        return generateGroupsCallsChart(pSession, pSessionId, tFirstMeasure);
    }

    public byte[] generateGroupsCallsChart(HttpSession pSession, String pSessionId, MethodCallDTO pFirstMeasure)
    {
        PieChartGenerator tGenerator = new PieChartGenerator(color);
        byte[] tOutput = tGenerator.getGroupCalls(pFirstMeasure);
        pSession.setAttribute(pSessionId, tOutput);
        return tOutput;
    }

    public FlowDetailChart generateFlowDetailChart(HttpSession pSession, String pSessionId, int pFlowId)
    {
        ExecutionFlowPO tLoadFullFlow = dao.loadFullFlow(pFlowId);
        ExecutionFlowDTO tConvertToDtoDeeply = convertToDtoDeeply(tLoadFullFlow);
        MethodCallDTO tFirstMeasure = tConvertToDtoDeeply.getFirstMethodCall();
        return generateFlowDetailChart(pSession, pSessionId, tFirstMeasure);
    }

    public FlowDetailChart generateFlowDetailChart(HttpSession pSession, String pSessionId, MethodCallDTO pFirstMeasure)
    {
        ChartBarGenerator tGenerator = new ChartBarGenerator(color, pFirstMeasure);
        FlowDetailChart tResult = tGenerator.getImage();
        pSession.setAttribute(pSessionId, tResult.image);
        return tResult;
    }

    public ExecutionFlowDTO convertToDtoDeeply(ExecutionFlowPO pFlowPO)
    {
        ExecutionFlowDTO tResult = convertToDto(pFlowPO);
        tResult.setFirstMethodCall(convertToDtoDeeply(pFlowPO.getFirstMethodCall(), tResult, 0));
        return tResult;
    }

    MethodCallDTO convertToDtoDeeply(MethodCallPO pCallPO, ExecutionFlowDTO pFlow, int pOrderInTheParentChildren)
    {
        MethodCallDTO tResult = convertToDto(pCallPO, pOrderInTheParentChildren);
        tResult.setFlow(pFlow);
        MethodCallDTO curChildDto;
        MethodCallDTO[] tChildren = new MethodCallDTO[pCallPO.getChildren().size()];
        int i = 0;
        for (MethodCallPO curMethod : pCallPO.getChildren())
        {
            curChildDto = convertToDtoDeeply(curMethod, pFlow, i);
            curChildDto.setParent(tResult);
            tChildren[i++] = curChildDto;
        }
        tResult.setChildren(tChildren);
        return tResult;
    }

    public MethodCallDTO convertToDto(MethodCallPO pCallPO, int pOrderInTheParentChildren)
    {
        MethodCallDTO tResult = new MethodCallDTO();
        BeanUtils.copyProperties(pCallPO, tResult, new String[] {"beginTime", "endTime", "children", "flow" });
        tResult.setGroupColor(color.getColorString(pCallPO.getGroupName()));
        tResult.setPosition(pCallPO.getMethId().getPosition());
        tResult.setBeginTime(formater.formatDateTime(pCallPO.getBeginTime()), pCallPO.getBeginTime());
        tResult.setEndTime(formater.formatDateTime(pCallPO.getEndTime()), pCallPO.getEndTime());
        tResult.setFlowId(pCallPO.getFlow().getId());
        tResult.setChildPosition(pOrderInTheParentChildren);
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
        return convertToDto(dao.loadMethodCall(pMethodCallId, pPosition), -1);
    }

}
