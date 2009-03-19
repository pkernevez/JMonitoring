package org.jmonitoring.console.gwt.server.executionflow.images;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
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
import org.jmonitoring.console.gwt.client.dto.MethodCallDTO;
import org.jmonitoring.console.gwt.client.executionflow.ExecutionFlowService;
import org.jmonitoring.console.gwt.client.executionflow.images.FullExecutionFlow;
import org.jmonitoring.core.configuration.ColorManager;
import org.jmonitoring.core.configuration.FormaterBean;
import org.jmonitoring.core.configuration.MeasureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlowChartBarUtil
{
    private static final float LABEL_WIDTH_RATIO = 10F;

    private static final int IMAGE_WIDTH = 930;

    private static final int BODER_TOP = 70;

    private static final int BORDER_DOWN = 60;

    private static Logger sLog = LoggerFactory.getLogger(FlowChartBarUtil.class);

    private Map<String, TaskForGroupName> mListOfGroup = new HashMap<String, TaskForGroupName>();

    private final MethodCallDTO mFirstMeasure;

    private int mMaxMethodPerGroup;

    private final ColorManager mColorManager;

    private final ChartManager mChartManager;

    public FlowChartBarUtil(FormaterBean pFormater, MethodCallDTO pFirstMeasure, ColorManager pColorManager,
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
    public static void writeImageIntoSession(FormaterBean pFormater, ColorManager pColorManager, HttpSession pSession,
        FullExecutionFlow pFullFlow, ChartManager pChartManager)
    {
        FlowChartBarUtil tUtil =
            new FlowChartBarUtil(pFormater, pFullFlow.getFlow().getFirstMethodCall(), pColorManager, pChartManager);
        tUtil.fillChart(pSession, pFullFlow);
    }

    private void fillChart(HttpSession pSession, FullExecutionFlow pFullFlow)
    {
        chainAllMethodCallToMainTaskOfGroup(mFirstMeasure);
        IntervalCategoryDataset intervalcategorydataset = createDataset();
        JFreeChart jfreechart = createGanttChart(intervalcategorydataset);
        jfreechart.getCategoryPlot().getDomainAxis().setMaxCategoryLabelWidthRatio(LABEL_WIDTH_RATIO);

        addChartToSession(jfreechart, pSession, ExecutionFlowService.CHART_BAR_FLOWS, pFullFlow);
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

    public void chainAllMethodCallToMainTaskOfGroup(MethodCallDTO pCurMeasure)
    {
        String tGroupName = pCurMeasure.getGroupName();
        TaskForGroupName tTask = getTaskForGroupName(pCurMeasure, tGroupName);

        // List tListOfClidren = pCurMeasure.getChildren();
        Date tBeginDate;
        Date tEndDate = new Date(pCurMeasure.getBeginMilliSeconds());
        MethodCallDTO curChild;
        for (int i = 0; i < pCurMeasure.getChildren().length; i++)
        {
            curChild = pCurMeasure.getChild(i);
            tBeginDate = tEndDate;
            tEndDate = new Date(curChild.getBeginMilliSeconds());
            addSubTask(tTask, pCurMeasure.getFlowId(), pCurMeasure.getPosition(), tBeginDate, tEndDate);
            chainAllMethodCallToMainTaskOfGroup(curChild);
            tEndDate = new Date(curChild.getEndMilliSeconds());
        }

        tBeginDate = tEndDate;
        tEndDate = new Date(pCurMeasure.getEndMilliSeconds());
        addSubTask(tTask, pCurMeasure.getFlowId(), pCurMeasure.getPosition(), tBeginDate, tEndDate);
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

    private TaskForGroupName getTaskForGroupName(MethodCallDTO pCurMeasure, String pGroupName)
    {
        TaskForGroupName tTaskEntry = mListOfGroup.get(pGroupName);
        if (tTaskEntry == null)
        { // We create a new entry
            tTaskEntry = new TaskForGroupName();
            tTaskEntry.mPositionOfTheGroup = mChartManager.getPosition(pGroupName);
            tTaskEntry.mMainTaskOfGroup =
                new Task(pGroupName, new SimpleTimePeriod(new Date(pCurMeasure.getBeginMilliSeconds()),
                                                          new Date(pCurMeasure.getEndMilliSeconds())));
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
    private void addChartToSession(JFreeChart pChart, HttpSession pSession, String pName, FullExecutionFlow pFullFlow)
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
            pFullFlow.setImageMap(tMapStream.toString());
        } catch (IOException e)
        {
            throw new MeasureException("Unable to write Image", e);
        }
        pSession.setAttribute(pName, tStream.toByteArray());

        sLog.debug("Image " + pName + " add to session");
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
            sLog.debug("add Task n�" + i + " for GroupName=" + curTaskEntry.mGroupName + " in position of ="
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

    private void getResursiveStatOfThisMethodCall(MethodCallDTO pCurrentMeasure, Map<String, Integer> pMapOfGroup)
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
        for (int i = 0; i < pCurrentMeasure.getChildren().length; i++)
        {
            getResursiveStatOfThisMethodCall(pCurrentMeasure.getChild(i), pMapOfGroup);
        }

    }

    int getMaxMethodPerGroup()
    {
        return mMaxMethodPerGroup;
    }
}
