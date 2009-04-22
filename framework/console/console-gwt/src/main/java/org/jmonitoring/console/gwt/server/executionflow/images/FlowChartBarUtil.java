package org.jmonitoring.console.gwt.server.executionflow.images;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpSession;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.GanttRenderer;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;
import org.jmonitoring.console.gwt.client.dto.MapAreaDTO;
import org.jmonitoring.console.gwt.client.dto.MapDto;
import org.jmonitoring.console.gwt.client.service.ExecutionFlowService;
import org.jmonitoring.core.configuration.ColorManager;
import org.jmonitoring.core.configuration.FormaterBean;
import org.jmonitoring.core.configuration.MeasureException;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlowChartBarUtil
{
    private static final String POSITION = "&position=";

    private static final String COORDS = "COORDS=\"";

    private static final float LABEL_WIDTH_RATIO = 10F;

    private static final int IMAGE_WIDTH = 930;

    private static final int BODER_TOP = 70;

    private static final int BORDER_DOWN = 60;

    private static Logger sLog = LoggerFactory.getLogger(FlowChartBarUtil.class);

    private Map<String, TaskForGroupName> mListOfGroup = new HashMap<String, TaskForGroupName>();

    private final MethodCallPO mFirstMeasure;

    private int mMaxMethodPerGroup;

    private final ColorManager mColorManager;

    private final ChartManager mChartManager;

    public FlowChartBarUtil(FormaterBean pFormater, MethodCallPO pFirstMeasure, ColorManager pColorManager,
        ChartManager pChartManager)
    {
        super();
        mColorManager = pColorManager;
        mFirstMeasure = pFirstMeasure;
        mChartManager = pChartManager;
        computeStatForThisFlow();
    }

    /**
     * Write a PNG image into the Session. The image is the chartbar representing the statistics of the execution of the
     * same method.
     * 
     * @param pSession The <code>Session</code> where we have to write the image.
     * @param pFirstMeasure The root of the Tree of <code>MethodCallDTO</code> to use for the image generation.
     * @param pForm TODO
     */
    public static MapDto writeImageIntoSession(FormaterBean pFormater, ColorManager pColorManager,
        HttpSession pSession, ExecutionFlowPO pFlow, ChartManager pChartManager)
    {
        FlowChartBarUtil tUtil =
            new FlowChartBarUtil(pFormater, pFlow.getFirstMethodCall(), pColorManager, pChartManager);
        return tUtil.fillChart(pSession, pFlow);
    }

    private MapDto fillChart(HttpSession pSession, ExecutionFlowPO pFullFlow)
    {
        chainAllMethodCallToMainTaskOfGroup(mFirstMeasure);
        IntervalCategoryDataset intervalcategorydataset = createDataset();
        JFreeChart jfreechart = createGanttChart(intervalcategorydataset);
        jfreechart.getCategoryPlot().getDomainAxis().setMaxCategoryLabelWidthRatio(LABEL_WIDTH_RATIO);

        return addChartToSession(jfreechart, pSession, ExecutionFlowService.CHART_BAR_FLOWS, pFullFlow);
    }

    /**
     * Class associated to the first Task of the group. All the MethodCall of the groups are subTask of this first Task.
     */
    static final class TaskForGroupName
    {
        private int mPositionOfTheGroup;

        private String mGroupName;

        private Task mMainTaskOfGroup;

        protected Task getMainTaskOfGroup()
        {
            return mMainTaskOfGroup;
        }

    }

    public void chainAllMethodCallToMainTaskOfGroup(MethodCallPO pCurMeasure)
    {
        String tGroupName = pCurMeasure.getGroupName();
        TaskForGroupName tTask = getTaskForGroupName(pCurMeasure, tGroupName);

        // List tListOfClidren = pCurMeasure.getChildren();
        Date tBeginDate;
        Date tEndDate = new Date(pCurMeasure.getBeginTime());
        for (MethodCallPO curChild : pCurMeasure.getChildren())
        {
            tBeginDate = tEndDate;
            tEndDate = new Date(curChild.getBeginTime());
            addSubTask(tTask, pCurMeasure.getFlow().getId(), pCurMeasure.getPosition(), tBeginDate, tEndDate);
            chainAllMethodCallToMainTaskOfGroup(curChild);
            tEndDate = new Date(curChild.getEndTime());
        }

        tBeginDate = tEndDate;
        tEndDate = new Date(pCurMeasure.getEndTime());
        addSubTask(tTask, pCurMeasure.getFlow().getId(), pCurMeasure.getPosition(), tBeginDate, tEndDate);
    }

    private void addSubTask(TaskForGroupName pTaskForTheGroupName, int pFlowId, int pMethodCallId, Date pBeginDate,
        Date pEndDate)
    {
        // Contournement du bug
        Date tNewEndDate = (pEndDate.before(pBeginDate) ? pBeginDate : pEndDate);

        pTaskForTheGroupName.mMainTaskOfGroup.addSubtask(new MethodCallTask(pFlowId, pMethodCallId,
                                                                            new SimpleTimePeriod(pBeginDate,
                                                                                                 tNewEndDate)));
    }

    private TaskForGroupName getTaskForGroupName(MethodCallPO pCurMeasure, String pGroupName)
    {
        TaskForGroupName tTaskEntry = mListOfGroup.get(pGroupName);
        if (tTaskEntry == null)
        { // We create a new entry
            tTaskEntry = new TaskForGroupName();
            tTaskEntry.mPositionOfTheGroup = mChartManager.getPosition(pGroupName);
            tTaskEntry.mMainTaskOfGroup =
                new Task(pGroupName, new SimpleTimePeriod(new Date(pCurMeasure.getBeginTime()),
                                                          new Date(pCurMeasure.getEndTime())));
            tTaskEntry.mGroupName = pGroupName;
            mListOfGroup.put(pGroupName, tTaskEntry);
        }
        return tTaskEntry;
    }

    /**
     * Add a chart into an attribute name of the user session.
     * 
     * @param pChart The Chart to add to the UserSession.
     * @param pSession The user HttSession.
     * @param pName Name of the session attribute.
     * @param pForm TODO
     */
    private MapDto addChartToSession(JFreeChart pChart, HttpSession pSession, String pName, ExecutionFlowPO pFullFlow)
    {
        Plot tPlot = pChart.getPlot();
        // tPlot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        tPlot.setNoDataMessage("No data available");
        sLog.debug("PlotClass=" + tPlot.getClass());

        ByteArrayOutputStream tStream = new ByteArrayOutputStream();
        try
        {
            int tHeight = BODER_TOP + mListOfGroup.size() * BORDER_DOWN;
            ChartRenderingInfo tChartRenderingInfo = new ChartRenderingInfo(new StandardEntityCollection());
            ChartUtilities.writeChartAsPNG(tStream, pChart, IMAGE_WIDTH, tHeight, tChartRenderingInfo);
            ByteArrayOutputStream tMapStream = new ByteArrayOutputStream();
            PrintWriter tWriter = new PrintWriter(tMapStream);
            ChartUtilities.writeImageMap(tWriter, "ChartBar", tChartRenderingInfo);
            tWriter.flush();
            pSession.setAttribute(pName, tStream.toByteArray());

            sLog.debug("Image " + pName + " add to session");
            return parseMap(tMapStream.toString());
        } catch (IOException e)
        {
            throw new MeasureException("Unable to write Image", e);
        }
    }

    public static MapDto parseMap(String pMap)
    {
        int tStart = pMap.indexOf("flowId=") + 7;
        int tEnd = pMap.indexOf("&");
        int tFlowId = Integer.parseInt(pMap.substring(tStart, tEnd));
        MapDto tResult = new MapDto();
        tResult.setFlowId(tFlowId);

        List<MapAreaDTO> tList = new ArrayList<MapAreaDTO>();
        int tStartCoord, tEndCoord, tStartPosition, tEndPosition = 0;
        while (pMap.indexOf(COORDS, tEndPosition) != -1)
        {
            tStartCoord = pMap.indexOf(COORDS, tEndPosition) + COORDS.length();
            tEndCoord = pMap.indexOf("\" ", tStartCoord);
            tStartPosition = pMap.indexOf(POSITION, tEndCoord) + POSITION.length();
            tEndPosition = pMap.indexOf("\">", tStartPosition);
            MapAreaDTO tArea = new MapAreaDTO();
            tArea.setCoordinate(pMap.substring(tStartCoord, tEndCoord));
            tArea.setPosition(Integer.parseInt(pMap.substring(tStartPosition, tEndPosition)));
            tList.add(tArea);
        }
        tResult.setAreas(tList);
        return tResult;
    }

    public IntervalCategoryDataset createDataset()
    {
        sLog.debug("Start createDataset");
        TaskSeriesCollection taskseriescollection = new TaskSeriesCollection();
        TaskSeries curTaskSeries;
        TaskForGroupName curTaskEntry;
        TaskForGroupName[] tList = orderListOfTask();
        curTaskSeries = new TaskSeries("Taff");
        for (int i = 0; i < tList.length; i++)
        { // ForEach GroupName
            curTaskEntry = tList[i];
            sLog.debug("add Task n°" + i + " for GroupName=" + curTaskEntry.mGroupName + " in position of ="
                + curTaskEntry.mPositionOfTheGroup);
            curTaskSeries.add(curTaskEntry.mMainTaskOfGroup);
        }
        taskseriescollection.add(curTaskSeries);
        return taskseriescollection;
    }

    private TaskForGroupName[] orderListOfTask()
    {
        TaskForGroupName[] tTaskEntries = new TaskForGroupName[mListOfGroup.size()];;
        SortedSet<TaskForGroupName> tOrderedTask = new TreeSet<TaskForGroupName>(new TaskComparator());
        tOrderedTask.addAll(mListOfGroup.values());
        int i = 0;
        for (TaskForGroupName curTaskEntry : tOrderedTask)
        {
            tTaskEntries[i++] = curTaskEntry;
        }
        return tTaskEntries;
    }

    private static class TaskComparator implements Comparator<TaskForGroupName>
    {
        public int compare(TaskForGroupName pTask1, TaskForGroupName pTask2)
        {
            return (pTask1.mPositionOfTheGroup - pTask2.mPositionOfTheGroup);
        }
    }

    /**
     * Creates a Gantt chart using the supplied attributes plus default values where required.
     * <P>
     * The chart object returned by this method uses a {@link CategoryPlot}instance as the plot, with a
     * {@link CategoryAxis}for the domain axis, a {@link DateAxis}as the range axis, and a {@link GanttRenderer}as
     * the renderer.
     * 
     * @param pDataset the dataset for the chart (<code>null</code> permitted).
     * @return A Gantt chart.
     */
    public JFreeChart createGanttChart(IntervalCategoryDataset pDataset)
    {
        CategoryAxis categoryAxis = new CategoryAxis("Flow Groups");
        DateAxis dateAxis = new DateAxis("Date");
        CategoryItemRenderer renderer = new FlowRenderer(mColorManager);
        renderer.setItemURLGenerator(new FlowDetailURLGenerator());
        CategoryPlot plot = new CategoryPlot(pDataset, categoryAxis, dateAxis, renderer);
        plot.setOrientation(PlotOrientation.HORIZONTAL);
        JFreeChart chart = new JFreeChart("Flow Group Details", JFreeChart.DEFAULT_TITLE_FONT, plot, false);

        return chart;

    }

    /**
     * @return Returns the listOfGroup.
     */
    protected Map<String, TaskForGroupName> getListOfGroup()
    {
        return mListOfGroup;
    }

    /**
     * @param pListOfGroup The listOfGroup to set.
     */
    protected void setListOfGroup(Map<String, TaskForGroupName> pListOfGroup)
    {
        mListOfGroup = pListOfGroup;
    }

    void computeStatForThisFlow()
    {
        Map<String, Integer> tMap = new HashMap<String, Integer>();
        getResursiveStatOfThisMethodCall(mFirstMeasure, tMap);
        int tMaxMethodPerGroup = 0;
        for (int tCurValue : tMap.values())
        {
            tMaxMethodPerGroup = Math.max(tMaxMethodPerGroup, tCurValue);
        }
        mMaxMethodPerGroup = tMaxMethodPerGroup;
    }

    private void getResursiveStatOfThisMethodCall(MethodCallPO pCurrentMeasure, Map<String, Integer> pMapOfGroup)
    {
        String tCurGroupName = pCurrentMeasure.getGroupName();
        Integer tCurrentNbOfMeth = pMapOfGroup.get(tCurGroupName);
        if (tCurrentNbOfMeth == null)
        {
            pMapOfGroup.put(tCurGroupName, 1);
        } else
        {
            pMapOfGroup.put(tCurGroupName, tCurrentNbOfMeth + 1);
        }
        for (int i = 0; i < pCurrentMeasure.getChildren().size(); i++)
        {
            getResursiveStatOfThisMethodCall(pCurrentMeasure.getChild(i), pMapOfGroup);
        }

    }

    int getMaxMethodPerGroup()
    {
        return mMaxMethodPerGroup;
    }
}
