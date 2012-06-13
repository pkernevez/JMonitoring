package org.jmonitoring.console.gwt.server.common;

import it.pianetatecno.gwt.utility.client.table.Request;
import it.pianetatecno.gwt.utility.client.table.SerializableResponse;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.gwtrpcspring.RemoteServiceUtil;
import org.jmonitoring.console.gwt.client.common.GwtRemoteService;
import org.jmonitoring.console.gwt.server.common.converter.BeanConverterUtil;
import org.jmonitoring.console.gwt.server.common.converter.ExecutionFlowDeepToDtoVisitor;
import org.jmonitoring.console.gwt.server.common.converter.ExecutionFlowOnlyToDtoVisitor;
import org.jmonitoring.console.gwt.server.common.converter.MethodCallLocalToDtoVisitor;
import org.jmonitoring.console.gwt.server.flow.ConsoleDao;
import org.jmonitoring.console.gwt.server.flow.Distribution;
import org.jmonitoring.console.gwt.server.flow.Stats;
import org.jmonitoring.console.gwt.server.image.DistributionChartBarGenerator;
import org.jmonitoring.console.gwt.server.image.FlowDetailChartBarGenerator;
import org.jmonitoring.console.gwt.server.image.FlowDetailPieChartGenerator;
import org.jmonitoring.console.gwt.server.image.MappedChart;
import org.jmonitoring.console.gwt.shared.flow.ExecutionFlowDTO;
import org.jmonitoring.console.gwt.shared.flow.FlowExtractDTO;
import org.jmonitoring.console.gwt.shared.flow.MethodCallDTO;
import org.jmonitoring.console.gwt.shared.flow.MethodCallExtractDTO;
import org.jmonitoring.console.gwt.shared.flow.UnknownEntity;
import org.jmonitoring.console.gwt.shared.method.MethodCallDistributionDTO;
import org.jmonitoring.console.gwt.shared.method.MethodCallSearchCriterion;
import org.jmonitoring.console.gwt.shared.method.MethodCallSearchExtractDTO;
import org.jmonitoring.console.gwt.shared.method.MethodNavType;
import org.jmonitoring.console.gwt.shared.method.treesearch.PackageDTO;
import org.jmonitoring.core.configuration.FormaterBean;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GwtRemoteServiceImpl implements GwtRemoteService
{
    private static final int NB_DEFAULT_INTERVAL_VALUE = 50;

    private static final int INTERVAL_MULTIPLE_VALUE = 5;

    static Logger sLog = getLogger();

    @Resource(name = "consoleDao")
    protected ConsoleDao dao;

    @Resource(name = "formater")
    FormaterBean formater;

    @Autowired
    BeanConverterUtil converter;

    @Autowired
    private ColorManager color;

    private static Logger getLogger()
    {
        return LoggerFactory.getLogger(GwtRemoteServiceImpl.class);
    }

    public SerializableResponse<FlowExtractDTO> searchFlow(Request pRequest)
    {
        sLog.info("call search flow");
        SerializableResponse<FlowExtractDTO> tResponse = new SerializableResponse<FlowExtractDTO>();
        tResponse.setRequest(pRequest);
        tResponse.setTotalResults(dao.countFlows(pRequest));
        tResponse.setRows(dao.search(pRequest));
        return tResponse;
    }

    public ExecutionFlowDTO loadFull(int pFlowId) throws UnknownEntity
    {
        ExecutionFlowPO tLoadFlow = dao.loadFullFlow(pFlowId);
        return new ExecutionFlowDeepToDtoVisitor(converter).getConverted(tLoadFlow);
    }

    public ExecutionFlowDTO loadAndGenerateImage(int pFlowId) throws UnknownEntity
    {
        HttpSession tSession = RemoteServiceUtil.getThreadLocalSession();
        ExecutionFlowPO tFlowPo = dao.loadFullFlow(pFlowId);
        generateDurationInGroupChart(tSession, "DurationInGroups&" + pFlowId, tFlowPo.getFirstMethodCall());
        generateGroupsCallsChart(tSession, "GroupsCalls&" + pFlowId, tFlowPo.getFirstMethodCall());
        String tMap = generateFlowDetailChart(tSession, "FlowDetail&" + pFlowId, tFlowPo.getFirstMethodCall()).map;
        ExecutionFlowDTO tFlow = new ExecutionFlowOnlyToDtoVisitor(converter).getConverted(tFlowPo);
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
        FlowDetailPieChartGenerator tGenerator = new FlowDetailPieChartGenerator(color);
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
        FlowDetailPieChartGenerator tGenerator = new FlowDetailPieChartGenerator(color);
        byte[] tOutput = tGenerator.getGroupCalls(pFirstMeasure);
        pSession.setAttribute(pSessionId, tOutput);
        return tOutput;
    }

    public MappedChart generateFlowDetailChart(HttpSession pSession, String pSessionId, int pFlowId)
        throws UnknownEntity
    {
        ExecutionFlowPO tLoadFullFlow = dao.loadFullFlow(pFlowId);
        return generateFlowDetailChart(pSession, pSessionId, tLoadFullFlow.getFirstMethodCall());
    }

    public MappedChart generateFlowDetailChart(HttpSession pSession, String pSessionId, MethodCallPO pFirstMeasure)
    {
        FlowDetailChartBarGenerator tGenerator = new FlowDetailChartBarGenerator(color, pFirstMeasure);
        MappedChart tResult = tGenerator.getImage();
        pSession.setAttribute(pSessionId, tResult.image);
        return tResult;
    }

    public ExecutionFlowPO convertToPoDeeply(ExecutionFlowDTO pFlowDto)
    {
        ExecutionFlowPO tResult = convertToPo(pFlowDto);
        tResult.setFirstMethodCall(convertToPoDeeply(pFlowDto.getFirstMethodCall(), null, tResult));
        return tResult;
    }

    MethodCallPO convertToPoDeeply(MethodCallDTO pMethodCall, MethodCallPO pParent, ExecutionFlowPO pFlow)
    {
        MethodCallPO tResult = new MethodCallPO();
        tResult.setFlow(pFlow);
        tResult.setPosition(Integer.valueOf(pMethodCall.getPosition()));
        tResult.setParentMethodCall(pParent);
        tResult.setBeginTime(pMethodCall.getBeginMilliSeconds());
        tResult.setEndTime(pMethodCall.getEndMilliSeconds());
        tResult.setDuration(pMethodCall.getDuration());
        tResult.setGroupName(pMethodCall.getGroupName());
        tResult.setClassName(pMethodCall.getClassName());
        tResult.setRuntimeClassName(pMethodCall.getRuntimeClassName());
        tResult.setMethodName(pMethodCall.getMethodName());
        tResult.setReturnValue(pMethodCall.getReturnValue());
        tResult.setParams(pMethodCall.getParams());
        tResult.setThrowableClass(pMethodCall.getThrowableClass());
        tResult.setThrowableMessage(pMethodCall.getThrowableMessage());
        List<MethodCallPO> tChildren = new ArrayList<MethodCallPO>(pMethodCall.getChildren().length);
        for (MethodCallExtractDTO tChild : pMethodCall.getChildren())
        {
            // tChildren.add(convertToPoDeeply(tChild, tResult, pFlow));
        }
        tResult.setChildren(tChildren);
        return tResult;
    }

    ExecutionFlowPO convertToPo(ExecutionFlowDTO pFlowDto)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public MethodCallDTO loadMethodCall(int pFlowId, int pPosition)
    {
        MethodCallLocalToDtoVisitor tVisit = new MethodCallLocalToDtoVisitor(converter);
        return tVisit.getConverted(dao.loadMethodCall(pFlowId, pPosition));
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

    public MethodCallDistributionDTO getDistributionAndGenerateImage(HttpSession pSession, String pClassName,
        String pMethodName, long pInterval)
    {
        Stats tStat = dao.getDurationStats(pClassName, pMethodName);

        if (pInterval <= 0)
        {
            pInterval = getDefaultInterval(tStat.max);
        }
        List<Distribution> tDistribList = dao.getDistribution(pClassName, pMethodName, pInterval);

        MappedChart tChart =
            new DistributionChartBarGenerator().generateDistributionChart(tDistribList, tStat, pClassName, pMethodName,
                                                                          pInterval);

        MethodCallDistributionDTO tResult = new MethodCallDistributionDTO();
        tResult.setMap(tChart.map);
        tResult.setClassName(pClassName);
        tResult.setMethodName(pMethodName);
        tResult.setNbOccurences(String.valueOf(tStat.nbOccurence));
        tResult.setMinDuration(String.valueOf(tStat.min));
        tResult.setAvgDuration(round(tStat.average));
        tResult.setMaxDuration(String.valueOf(tStat.max));
        tResult.setDevianceDuration(round(tStat.stdDeviation));
        tResult.setInterval(String.valueOf(pInterval));
        pSession.setAttribute("Distribution&" + pClassName + "/" + pMethodName + "/" + pInterval, tChart.image);
        return tResult;

        // ChartBarGenerator tGenerator = new ChartBarGenerator(color, pFirstMeasure);
        // MappedChart tResult = tGenerator.getImage();
        // pSession.setAttribute(pSessionId, tResult.image);
        // return tResult;
    }

    public MethodCallDistributionDTO getDistributionAndGenerateImage(String pFullClassName, String pMethodName,
        long pInterval)
    {
        HttpSession tSession = RemoteServiceUtil.getThreadLocalSession();
        // The Interval has not be specified explicitly
        return getDistributionAndGenerateImage(tSession, pFullClassName, pMethodName, pInterval);
    }

    String round(double tValue)
    {
        return formater.get2DecimalFormatter().format(tValue);
    }

    long getDefaultInterval(long pDurationMax)
    {
        pDurationMax = pDurationMax / NB_DEFAULT_INTERVAL_VALUE;
        // We want an interval multiple of 5
        pDurationMax = pDurationMax / INTERVAL_MULTIPLE_VALUE * INTERVAL_MULTIPLE_VALUE;
        pDurationMax = Math.max(pDurationMax, 1);
        return pDurationMax;
    }

    public SerializableResponse<MethodCallSearchExtractDTO> searchMethodCall(Request pRequest,
        MethodCallSearchCriterion pCriterion)
    {
        sLog.info("call search flow");
        SerializableResponse<MethodCallSearchExtractDTO> tResponse =
            new SerializableResponse<MethodCallSearchExtractDTO>();
        tResponse.setRequest(pRequest);
        tResponse.setTotalResults(dao.countMethodCall(pCriterion));
        tResponse.setRows(dao.searchMethodCall(pRequest, pCriterion));
        return tResponse;
    }

    public PackageDTO loadMethodCallTreeSearch()
    {
        return dao.loadMethodCallTreeSearch();
    }

    public void deleteAll()
    {
        dao.deleteAllFlows();
    }

}
