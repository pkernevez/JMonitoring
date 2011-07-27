package org.jmonitoring.console.gwt.server;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.hibernate.exception.SQLGrammarException;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.xy.IntervalXYDataset;
import org.jmonitoring.console.gwt.client.dto.ExecutionFlowDTO;
import org.jmonitoring.console.gwt.client.dto.MethodCallDTO;
import org.jmonitoring.console.gwt.client.dto.MethodCallExtractDTO;
import org.jmonitoring.console.gwt.client.dto.MethodCallFullExtractDTO;
import org.jmonitoring.console.gwt.client.dto.RootMethodCallDTO;
import org.jmonitoring.console.gwt.client.dto.StatMapAreaDTO;
import org.jmonitoring.console.gwt.client.dto.StatMethodCallDTO;
import org.jmonitoring.console.gwt.client.service.ExecutionFlowService;
import org.jmonitoring.console.gwt.client.service.SearchCriteria;
import org.jmonitoring.console.gwt.server.dto.DtoManager;
import org.jmonitoring.console.gwt.server.jfreechart.StatisticDataSet;
import org.jmonitoring.console.gwt.server.jfreechart.StatisticXYURLGenerator;
import org.jmonitoring.core.common.UnknownFlowException;
import org.jmonitoring.core.configuration.FormaterBean;
import org.jmonitoring.core.configuration.MeasureException;
import org.jmonitoring.core.dao.ConsoleDao;
import org.jmonitoring.core.dao.FlowSearchCriterion;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class ConsoleManager
{
    private static final int NB_DEFAULT_INTERVAL_VALUE = 50;

    private static final int INTERVAL_MULTIPLE_VALUE = 5;

    private static Logger sLog = LoggerFactory.getLogger(ConsoleManager.class);

    @Resource(name = "dtoManager")
    private DtoManager dtoManager;

    @Resource(name = "dao")
    private ConsoleDao mDao;

    @Resource(name = "formater")
    private FormaterBean mFormater;

    ConsoleManager()
    {
    }

    public boolean doDatabaseExist()
    {
        try
        {
            mDao.countFlows();
            return true;
        } catch (SQLGrammarException t)
        {
            return false;
        }

    }

    public void deleteFlow(int pId) throws UnknownFlowException
    {
        try
        {
            mDao.deleteFlow(pId);
        } catch (RuntimeException t)
        {
            sLog.error("Unable to Execute Action" + t);
            throw t;
        }
    }

    public void deleteAllFlows()
    {
        try
        {
            mDao.deleteAllFlows();
        } catch (Throwable t)
        {
            sLog.error("Unable to Execute Action" + t);
        }
    }

    public RootMethodCallDTO readFullMethodCall(int pFlowId, int pId)
    {
        sLog.debug("Read method call from database, Id=[" + pId + "]");
        MethodCallPO tMethodCallPo = mDao.readMethodCall(pFlowId, pId);
        RootMethodCallDTO tRoot = new RootMethodCallDTO();
        BeanUtils.copyProperties(dtoManager.getFullMethodCallDto(tMethodCallPo, -1), tRoot);
        tRoot.setPrevInGroup(getPrevMethodCallInGroup(pFlowId, pId, tMethodCallPo.getGroupName()));
        tRoot.setNextInGroup(getNextMethodCallInGroup(pFlowId, pId, tMethodCallPo.getGroupName()));
        if (tMethodCallPo.getPosition() > 1)
        {
            tRoot.setPrev(String.valueOf(tMethodCallPo.getPosition() - 1));
        }
        if (mDao.existMethodCall(pFlowId, pId + 1))
        {
            tRoot.setNext(String.valueOf(tMethodCallPo.getPosition() + 1));
        }
        return tRoot;
    }

    public ExecutionFlowPO readExecutionFlow(int pId)
    {
        return mDao.readExecutionFlow(pId);
    }

    public List<ExecutionFlowDTO> getListOfExecutionFlowDto(SearchCriteria pCriterion)
    {
        FlowSearchCriterion tCrit = new FlowSearchCriterion();
        tCrit.setBeginDate(mFormater.parseDate(pCriterion.getBeginDate()));
        tCrit.setClassName(pCriterion.getClassName());
        String tMinimumDuration = pCriterion.getMinimumDuration();
        if (tMinimumDuration != null && tMinimumDuration.length() > 0)
        {
            tCrit.setDurationMin(Long.parseLong(tMinimumDuration));
        }
        tCrit.setGroupName(pCriterion.getGroupName());
        tCrit.setMethodName(pCriterion.getMethodName());
        tCrit.setJVM(pCriterion.getServer());
        tCrit.setThreadName(pCriterion.getThreadName());
        List<ExecutionFlowDTO> tList = new ArrayList<ExecutionFlowDTO>();
        for (ExecutionFlowPO tFlow : mDao.getListOfExecutionFlowPO(tCrit))
        {
            tList.add(dtoManager.getSimpleCopy(tFlow));
        }
        return tList;
    }

    public MethodCallDTO readMethodCall(int pFlowId, int pMethodCallId)
    {
        MethodCallPO tMethod = mDao.readMethodCall(pFlowId, pMethodCallId);
        return dtoManager.simpleCopy(tMethod, -1);
    }

    public List<MethodCallDTO> getListOfMethodCallFromClassAndMethodName(String pClassName, String pMethodName)
    {
        List<MethodCallPO> tResult = mDao.getListOfMethodCall(pClassName, pMethodName);
        return dtoManager.simpleCopyListOfMethodPO(tResult);
    }

    public List<MethodCallExtractDTO> getListOfMethodCallExtract()
    {
        List<MethodCallExtractDTO> tResult = new ArrayList<MethodCallExtractDTO>();
        for (Object[] tExtract : mDao.getListOfMethodCallExtract())
        {
            tResult.add(new MethodCallExtractDTO((String) tExtract[ConsoleDao.EXTRACT_CLASSNAME_POS],
                                                 (String) tExtract[ConsoleDao.EXTRACT_METHODNAME_POS],
                                                 (String) tExtract[ConsoleDao.EXTRACT_GROUPNAME_POS],
                                                 ((Long) tExtract[ConsoleDao.EXTRACT_NB_POS]).intValue()));
        }
        return tResult;
    }

    public Map<Integer, MethodCallDTO> getListOfMethodCall(int pFlowId, List<Integer> pIds)
    {
        Map<Integer, MethodCallDTO> tResult = new HashMap<Integer, MethodCallDTO>();
        for (MethodCallPO tMeth : mDao.getMethodCallList(pFlowId, pIds))
        {
            tResult.put(tMeth.getPosition(), dtoManager.getMethodCallDto(tMeth, 2));
        }
        return tResult;
    }

    public List<MethodCallDTO> getListOfMethodCall(int pFlowId)
    {
        return dtoManager.copyListOfMethodPO(mDao.readExecutionFlow(pFlowId).getFirstMethodCall().getChildren(), 2);
    }

    public List<MethodCallFullExtractDTO> getListOfMethodCallFullExtract(String pClassName, String pMethodName,
        long pDurationMin, long pDurationMax)
    {
        List<MethodCallPO> tListOfMethodCall =
            mDao.getMethodCallList(pClassName, pMethodName, pDurationMin, pDurationMax);
        return dtoManager.copyListMethodCallFullExtract(tListOfMethodCall);
    }

    public void createDataBase()
    {
        mDao.createDataBase();
    }

    /**
     * Serialize a full ExecutionFlow as Xml/GZip bytes.
     * 
     * @param pFlow The flow to serialize.
     * @return The bytes of a GZip.
     */
    public byte[] convertFlowToXml(ExecutionFlowPO pFlow)
    {
        ByteArrayOutputStream tOutput = new ByteArrayOutputStream(10000);
        ExecutionFlowDTO tFlow = dtoManager.getFullCopy(pFlow);
        GZIPOutputStream tZipStream;
        try
        {
            tZipStream = new GZIPOutputStream(tOutput);
            XMLEncoder tEncoder = new XMLEncoder(tZipStream);
            tEncoder.writeObject(tFlow);
            tEncoder.close();
            return tOutput.toByteArray();
        } catch (IOException e)
        {
            throw new MeasureException("Unable to Zip Xml ExecutionFlow", e);
        }
    }

    /**
     * Concert an GZip/Xml serialized ExecutionFlow as an ExecutionFLow Object.
     * 
     * @param pFlowAsXml The GZip bytes.
     * @return The ExecutionFLow.
     */
    public ExecutionFlowDTO convertFlowFromXml(InputStream pInput)
    {
        try
        {
            GZIPInputStream tZipStream = new GZIPInputStream(pInput);
            XMLDecoder tDecoder = new XMLDecoder(tZipStream);
            Object tResult = tDecoder.readObject();
            tDecoder.close();
            return (ExecutionFlowDTO) tResult;
        } catch (IOException e)
        {
            throw new MeasureException("Unable to Zip Xml ExecutionFlow", e);
        }
    }

    public ExecutionFlowDTO insertFlowFromXml(InputStream pInput)
    {
        ExecutionFlowDTO tFlowDto = convertFlowFromXml(pInput);
        ExecutionFlowPO tFlowPO = dtoManager.getDeepCopy(tFlowDto);
        tFlowPO.setId(-1);
        mDao.insertFullExecutionFlow(tFlowPO);
        return dtoManager.getLimitedCopy(tFlowPO);
    }

    public String getPrevMethodCallInGroup(int pFlowId, int pPosition, String pGroupName)
    {
        MethodCallPO tMethPo = mDao.getPrevInGroup(pFlowId, pPosition, pGroupName);
        if (tMethPo == null)
        {
            return null;
        } else
        {
            return String.valueOf(tMethPo.getPosition());
        }
    }

    public String getNextMethodCallInGroup(int pFlowId, int pPosition, String pGroupName)
    {
        MethodCallPO tMethPo = mDao.getNextInGroup(pFlowId, pPosition, pGroupName);
        if (tMethPo == null)
        {
            return null;
        } else
        {
            return String.valueOf(tMethPo.getPosition());
        }
    }

    public StatMethodCallDTO readStatMethodCall(HttpSession pSession, String pClassName, String pMethodName,
        int pAggregationScope)
    {
        StatMethodCallDTO tResult = new StatMethodCallDTO();
        List<MethodCallPO> tMethods = mDao.getListOfMethodCall(pClassName, pMethodName);
        tResult.setNbOccurence("" + tMethods.size());
        tResult.setAggregationScope(pAggregationScope);
        tResult.setClassName(pClassName);
        tResult.setMethodName(pMethodName);
        computeStat(tMethods, tResult);
        writeFullDurationStat(pSession, tMethods, tResult);
        return tResult;
    }

    /**
     * Write the image of statistic duration image into session.
     * 
     * @param pSession The http session.
     * @param pMeasures The list of <code>MethodCallDTO</code> to use for image generation.
     * @param pForm The form associated to this Action.
     */
    private void writeFullDurationStat(HttpSession pSession, List<MethodCallPO> pMeasures, StatMethodCallDTO pStat)
    {
        int tInterval = computeIntervalValue(pMeasures, pStat.getAggregationScope());
        IntervalXYDataset tIntervalxydataset = createFullDurationDataset(pMeasures, tInterval);

        JFreeChart tJFreeChart = createXYBarChart(tIntervalxydataset, tInterval, pStat);

        ChartRenderingInfo tChartRenderingInfo = new ChartRenderingInfo(new StandardEntityCollection());

        ByteArrayOutputStream tImageStream = new ByteArrayOutputStream();
        ByteArrayOutputStream tMapStream = new ByteArrayOutputStream();
        try
        {
            ChartUtilities.writeChartAsPNG(tImageStream, tJFreeChart, 800, 450, tChartRenderingInfo);
            // todo regarder l'API avec FragmentURLGenerator...
            PrintWriter tWriter = new PrintWriter(tMapStream);
            ChartUtilities.writeImageMap(tWriter, "chart", tChartRenderingInfo);
            tWriter.flush();
            pStat.setImage(parseMap(tMapStream.toString()));
        } catch (IOException e)
        {
            throw new MeasureException("Unable to write Image", e);
        }
        pSession.setAttribute(ExecutionFlowService.STAT_METHOD_CALL, tImageStream.toByteArray());
        sLog.debug("Image " + ExecutionFlowService.STAT_METHOD_CALL + " add to session");
    }

    private static final String DURATION_MIN = "&durationMin=";

    private static final String DURATION_MAX = "&durationMax=";

    private static final String COORDS = "COORDS=\"";

    static List<StatMapAreaDTO> parseMap(String pMap)
    {
        List<StatMapAreaDTO> tResult = new ArrayList<StatMapAreaDTO>();

        int tStartCoord, tEndCoord, tStartPosition, tEndPosition = 0;
        while (pMap.indexOf(COORDS, tEndPosition) != -1)
        {
            tStartCoord = pMap.indexOf(COORDS, tEndPosition) + COORDS.length();
            tEndCoord = pMap.indexOf("\" ", tStartCoord);
            StatMapAreaDTO tArea = new StatMapAreaDTO();
            tArea.setCoordinate(pMap.substring(tStartCoord, tEndCoord));
            tStartPosition = pMap.indexOf(DURATION_MIN, tEndCoord) + DURATION_MIN.length();
            tEndPosition = pMap.indexOf("&", tStartPosition);
            tArea.setDurationMin(Integer.parseInt(pMap.substring(tStartPosition, tEndPosition)));
            tStartPosition = pMap.indexOf(DURATION_MAX, tEndCoord) + DURATION_MAX.length();
            tEndPosition = pMap.indexOf("\">", tStartPosition);
            tArea.setDurationMax(Integer.parseInt(pMap.substring(tStartPosition, tEndPosition)));
            tResult.add(tArea);
        }
        return tResult;
    }

    private IntervalXYDataset createFullDurationDataset(List<MethodCallPO> pMeasures, int pInterval)
    {
        Map<Long, Integer> tMap = new HashMap<Long, Integer>();
        Integer tCurNb;
        long tCurDuration;
        long tDurationMax = 0;
        for (MethodCallPO curMEasure : pMeasures)
        {
            tCurDuration = curMEasure.getDuration();
            tDurationMax = Math.max(tCurDuration, tDurationMax);
            // Around the duration with duration groupvalue
            tCurDuration = (tCurDuration / pInterval) * pInterval;
            tCurNb = tMap.get(tCurDuration);
            tCurNb = (tCurNb == null ? 1 : tCurNb.intValue() + 1);
            tMap.put(tCurDuration, tCurNb);
        }
        return new StatisticDataSet(tMap, pInterval, tDurationMax);
    }

    private static JFreeChart createXYBarChart(IntervalXYDataset dataset, int pInterval, StatMethodCallDTO pStat)
    {
        NumberAxis tAxis = new NumberAxis("duration (ms)");
        tAxis.setAutoRangeIncludesZero(false);
        ValueAxis tValueAxis = new NumberAxis("number of occurences");

        XYBarRenderer renderer = new XYBarRenderer();
        renderer.setToolTipGenerator(new StandardXYToolTipGenerator());

        renderer.setURLGenerator(new StatisticXYURLGenerator("MethodCallListIn.do", pInterval, pStat.getClassName(),
                                                             pStat.getMethodName()));

        XYPlot plot = new XYPlot(dataset, tAxis, tValueAxis, renderer);
        plot.setOrientation(PlotOrientation.VERTICAL);

        JFreeChart chart = new JFreeChart("Full duration statistics", JFreeChart.DEFAULT_TITLE_FONT, plot, false);

        return chart;
    }

    private int computeIntervalValue(List<MethodCallPO> pMeasures, int pIntervalValue)
    {
        if (pIntervalValue <= 0)
        { // The Interval has not be specified explicitly
            long tDurationMax = 0;
            // Get duration max
            for (MethodCallPO curMeasure : pMeasures)
            {
                tDurationMax = Math.max(tDurationMax, curMeasure.getDuration());
            }
            int tInterval = (int) tDurationMax / NB_DEFAULT_INTERVAL_VALUE;
            // We want an interval multiple of 5
            tInterval = tInterval / INTERVAL_MULTIPLE_VALUE * INTERVAL_MULTIPLE_VALUE;
            return Math.max(tInterval, 1);
        } else
        {
            return pIntervalValue;
        }
    }

    private void computeStat(List<MethodCallPO> pMeasures, StatMethodCallDTO pStat)
    {
        if (pMeasures.size() != 0)
        {
            long tMin = Long.MAX_VALUE, tMax = Long.MIN_VALUE, tSum = 0;
            for (MethodCallPO curMeasure : pMeasures)
            {
                long curDuration = curMeasure.getDuration();
                tSum += curDuration;
                tMax = Math.max(tMax, curDuration);
                tMin = Math.min(tMin, curDuration);
            }
            double tAvg = (double) tSum / (double) pMeasures.size();
            // Standard Deviation
            double tDelta, tVar = 0;
            for (MethodCallPO curMeasure : pMeasures)
            {
                tDelta = tAvg - curMeasure.getDuration();
                tVar += tDelta * tDelta;
            }
            pStat.setDurationAvg(String.valueOf(Math.round(tAvg)));
            pStat.setDurationMin(String.valueOf(tMin));
            pStat.setDurationMax(String.valueOf(tMax));
            pStat.setDurationDev(String.valueOf(Math.round(Math.sqrt(tVar / pMeasures.size()))));
        }
    }
}
