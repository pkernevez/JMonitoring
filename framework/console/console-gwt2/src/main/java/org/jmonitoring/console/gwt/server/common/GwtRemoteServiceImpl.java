package org.jmonitoring.console.gwt.server.common;

import it.pianetatecno.gwt.utility.client.table.Request;
import it.pianetatecno.gwt.utility.client.table.SerializableResponse;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.gwtrpcspring.RemoteServiceUtil;
import org.jmonitoring.console.gwt.client.common.GwtRemoteService;
import org.jmonitoring.console.gwt.server.flow.ConsoleDao;
import org.jmonitoring.console.gwt.server.image.ChartBarGenerator;
import org.jmonitoring.console.gwt.server.image.ChartBarGenerator.FlowDetailChart;
import org.jmonitoring.console.gwt.server.image.PieChartGenerator;
import org.jmonitoring.console.gwt.shared.flow.ExecutionFlowDTO;
import org.jmonitoring.console.gwt.shared.flow.FlowExtractDTO;
import org.jmonitoring.console.gwt.shared.flow.MethodCallDTO;
import org.jmonitoring.console.gwt.shared.flow.MethodCallExtractDTO;
import org.jmonitoring.console.gwt.shared.flow.UnknownEntity;
import org.jmonitoring.console.gwt.shared.method.MethodNavType;
import org.jmonitoring.core.configuration.FormaterBean;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GwtRemoteServiceImpl implements GwtRemoteService
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
        return LoggerFactory.getLogger(GwtRemoteServiceImpl.class);
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

    public ExecutionFlowDTO loadFull(int pFlowId) throws UnknownEntity
    {
        return convertToDtoDeeply(dao.loadFullFlow(pFlowId));
    }

    public ExecutionFlowDTO loadAndGenerateImage(int pFlowId) throws UnknownEntity
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
        throws UnknownEntity
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

    public byte[] generateGroupsCallsChart(HttpSession pSession, String pSessionId, int pFlowId) throws UnknownEntity
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
        throws UnknownEntity
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

    ExecutionFlowDTO convertToDtoDeeply(ExecutionFlowPO pFlowPO)
    {
        ExecutionFlowDTO tResult = convertToDto(pFlowPO);
        tResult.setFirstMethodCall(convertToDtoDeeply(pFlowPO.getFirstMethodCall(), -1));
        return tResult;
    }

    /**
     * Depth to recurse is the children tree.
     * 
     * @param pCallPO The MethodCall root
     * @param pDepth The pDepth, stop when pDepth is 0. Passe negative value to recurse without limit.
     * @return The converted value.
     */
    private MethodCallDTO convertToDtoDeeply(MethodCallPO pCallPO, int pDepth)
    {
        MethodCallDTO tResult = convertToDto(pCallPO);
        tResult.setFlowId(String.valueOf(pCallPO.getFlow().getId()));
        // MethodCallExtractDTO tRootDuplication = convertToExtract(pCallPO, 0);
        tResult.setChildren(convertToDtoDeeply(pCallPO.getChildren(), pDepth - 1));
        return tResult;
    }

    private MethodCallExtractDTO convertToExtract(MethodCallPO pMethodCall, int pChildPosition)
    {
        MethodCallExtractDTO tExtract = new MethodCallExtractDTO();
        tExtract.setFullMethodName(pMethodCall.getClassName() + "." + pMethodCall.getMethodName() + "()");
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
        } else if (pChildPosition == 0)
        {
            tDurationFromPrev = pMethodCall.getBeginTime() - tParent.getBeginTime();
        } else
        {
            tDurationFromPrev = pMethodCall.getBeginTime() - tParent.getChild(pChildPosition - 1).getEndTime();
        }
        tExtract.setTimeFromPrevChild(String.valueOf(tDurationFromPrev));
        tExtract.setThrownException(pMethodCall.getThrowableClass() != null);
        return tExtract;
    }

    private MethodCallExtractDTO[] convertToDtoDeeply(List<MethodCallPO> pCallPOs, int pDepth)
    {
        MethodCallExtractDTO[] tResult = new MethodCallExtractDTO[pCallPOs.size()];
        int i = 0;
        for (MethodCallPO tMethodCallPO : pCallPOs)
        {
            MethodCallExtractDTO tExtract = convertToExtract(tMethodCallPO, i);
            if (pDepth != 0)
            {
                tExtract.setChildren(convertToDtoDeeply(tMethodCallPO.getChildren(), pDepth - 1));
            }
            tResult[i] = tExtract;
            i++;
        }
        return tResult;
    }

    private MethodCallDTO convertToDto(MethodCallPO pCallPO)
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

    public MethodCallDTO loadMethodCall(int pFlowId, int pPosition)
    {
        MethodCallDTO tResult = convertToDtoDeeply(dao.loadMethodCall(pFlowId, pPosition), 1);
        return tResult;
    }

    public int getMethodPositionWhenNavigate(int pFlowId, int pCurrentPosition, String pGroupName, MethodNavType pType)
    {
        int tResult = -1;
        if (MethodNavType.NEXT_IN_THREAD == pType)
        {
            ExecutionFlowPO tFlow = dao.loadFlow(pFlowId);
            if (pCurrentPosition < tFlow.getFirstMethodCall().getChildren().size())
            {
                tResult = pCurrentPosition + 1;
            }
        } else if (MethodNavType.PREV_IN_GROUP == pType)
        {
            tResult = dao.getPrevInGroup(pFlowId, pCurrentPosition, pGroupName);
        } else if (MethodNavType.NEXT_IN_GROUP == pType)
        {
            tResult = dao.getNextInGroup(pFlowId, pCurrentPosition, pGroupName);
        } else
        {
            throw new RuntimeException("Unsupported MethodNavType");
        }
        return tResult;
    }

    public void delete(int pFlowId)
    {
        dao.deleteFlow(pFlowId);

    }
}
