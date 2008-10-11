package org.jmonitoring.console.flow.jfreechart;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.jmonitoring.console.flow.edit.FlowEditForm;
import org.jmonitoring.core.configuration.MeasureException;
import org.jmonitoring.core.dto.MethodCallDTO;

public class FlowChartBarUtil {
    private static final float LABEL_WIDTH_RATIO = 10F;

    private static final int IMAGE_WIDTH = 930;

    private static final int BODER_TOP = 70;

    private static final int BORDER_DOWN = 60;

    private static Log sLog = LogFactory.getLog(FlowChartBarUtil.class);

    private static final String CHART_BAR_FLOWS = "CHART_BAR_FLOWS";

    private Map mListOfGroup = new HashMap();

    private MethodCallDTO mFirstMeasure;

    private int mMaxMethodPerGroup;

    public FlowChartBarUtil(MethodCallDTO pFirstMeasure) {
        super();
        mFirstMeasure = pFirstMeasure;
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
    public static void writeImageIntoSession(HttpSession pSession, MethodCallDTO pFirstMeasure, FlowEditForm pForm) {
        FlowChartBarUtil tUtil = new FlowChartBarUtil(pFirstMeasure);
        tUtil.fillChart(pSession, pForm);
    }

    private void fillChart(HttpSession pSession, FlowEditForm pForm) {
        chainAllMethodCallToMainTaskOfGroup(mFirstMeasure);
        IntervalCategoryDataset intervalcategorydataset = createDataset();
        JFreeChart jfreechart = createGanttChart(intervalcategorydataset);
        jfreechart.getCategoryPlot().getDomainAxis().setMaxCategoryLabelWidthRatio(LABEL_WIDTH_RATIO);

        addChartToSession(jfreechart, pSession, CHART_BAR_FLOWS, pForm);
    }

    /**
     * Class associated to the first Task of the group. All the MethodCall of the groups are subTask of this first Task.
     */
    static final class TaskForGroupName {
        private int mPositionOfTheGroup;

        private String mGroupName;

        private Task mMainTaskOfGroup;

        protected Task getMainTaskOfGroup() {
            return mMainTaskOfGroup;
        }

    }

    public void chainAllMethodCallToMainTaskOfGroup(MethodCallDTO pCurMeasure) {
        String tGroupName = pCurMeasure.getGroupName();
        TaskForGroupName tTask = getTaskForGroupName(pCurMeasure, tGroupName);

        // List tListOfClidren = pCurMeasure.getChildren();
        Date tBeginDate;
        Date tEndDate = pCurMeasure.getBeginTime();
        MethodCallDTO curChild;
        for (int i = 0; i < pCurMeasure.getChildren().length; i++) {
            curChild = pCurMeasure.getChild(i);
            tBeginDate = tEndDate;
            tEndDate = curChild.getBeginTime();
            addSubTask(tTask, pCurMeasure.getFlowId(), pCurMeasure.getPosition(), tBeginDate, tEndDate);
            chainAllMethodCallToMainTaskOfGroup(curChild);
            tEndDate = curChild.getEndTime();
        }

        tBeginDate = tEndDate;
        tEndDate = pCurMeasure.getEndTime();
        addSubTask(tTask, pCurMeasure.getFlowId(), pCurMeasure.getPosition(), tBeginDate, tEndDate);
    }

    private void addSubTask(TaskForGroupName pTaskForTheGroupName, int pFlowId, int pMethodCallId, Date pBeginDate,
            Date pEndDate) {
        // Contournement du bug
        Date tNewEndDate = (pEndDate.before(pBeginDate) ? pBeginDate : pEndDate);

        pTaskForTheGroupName.mMainTaskOfGroup.addSubtask(new MethodCallTask(pFlowId, pMethodCallId,
                new SimpleTimePeriod(pBeginDate, tNewEndDate)));
    }

    private TaskForGroupName getTaskForGroupName(MethodCallDTO pCurMeasure, String pGroupName) {
        TaskForGroupName tTaskEntry = (TaskForGroupName) mListOfGroup.get(pGroupName);
        if (tTaskEntry == null) { // We create a new entry
            tTaskEntry = new TaskForGroupName();
            tTaskEntry.mPositionOfTheGroup = mListOfGroup.size() + 1;
            tTaskEntry.mMainTaskOfGroup = new Task(pGroupName, new SimpleTimePeriod(pCurMeasure.getBeginTime(),
                    pCurMeasure.getEndTime()));
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
    private void addChartToSession(JFreeChart pChart, HttpSession pSession, String pName, FlowEditForm pForm) {
        Plot tPlot = pChart.getPlot();
        // tPlot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        tPlot.setNoDataMessage("No data available");
        sLog.debug("PlotClass=" + tPlot.getClass());

        ByteArrayOutputStream tStream = new ByteArrayOutputStream();
        try {
            int tHeight = BODER_TOP + mListOfGroup.size() * BORDER_DOWN;
            ChartRenderingInfo tChartRenderingInfo = new ChartRenderingInfo(new StandardEntityCollection());
            ChartUtilities.writeChartAsPNG(tStream, pChart, IMAGE_WIDTH, tHeight, tChartRenderingInfo);
            ByteArrayOutputStream tMapStream = new ByteArrayOutputStream();
            PrintWriter tWriter = new PrintWriter(tMapStream);
            ChartUtilities.writeImageMap(tWriter, "ChartBar", tChartRenderingInfo);
            tWriter.flush();
            pForm.setImageMap(tMapStream.toString());
        } catch (IOException e) {
            throw new MeasureException("Unable to write Image", e);
        }
        pSession.setAttribute(pName, tStream.toByteArray());

        sLog.debug("Image " + pName + " add to session");
    }

    public IntervalCategoryDataset createDataset() {
        sLog.debug("Start createDataset");
        TaskSeriesCollection taskseriescollection = new TaskSeriesCollection();
        TaskSeries curTaskSeries;
        TaskForGroupName curTaskEntry;
        TaskForGroupName[] tList = orderListOfTask();
        curTaskSeries = new TaskSeries("Taff");
        for (int i = 0; i < tList.length; i++) { // ForEach GroupName
            curTaskEntry = tList[i];
            sLog.debug("add Task n�" + i + " for GroupName=" + curTaskEntry.mGroupName + " in position of ="
                    + curTaskEntry.mPositionOfTheGroup);
            curTaskSeries.add(curTaskEntry.mMainTaskOfGroup);
        }
        taskseriescollection.add(curTaskSeries);
        return taskseriescollection;
    }

    private TaskForGroupName[] orderListOfTask() {
        TaskForGroupName[] tTaskEntries = new TaskForGroupName[mListOfGroup.size()];
        TaskForGroupName curTaskEntry;
        for (Iterator tIt = mListOfGroup.values().iterator(); tIt.hasNext();) {
            curTaskEntry = (TaskForGroupName) tIt.next();
            tTaskEntries[curTaskEntry.mPositionOfTheGroup - 1] = curTaskEntry;
        }
        return tTaskEntries;
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
    public JFreeChart createGanttChart(IntervalCategoryDataset pDataset) {
        CategoryAxis categoryAxis = new CategoryAxis("Flow Groups");
        DateAxis dateAxis = new DateAxis("Date");
        CategoryItemRenderer renderer = new FlowRenderer();
        renderer.setItemURLGenerator(new FlowDetailURLGenerator());
        CategoryPlot plot = new CategoryPlot(pDataset, categoryAxis, dateAxis, renderer);
        plot.setOrientation(PlotOrientation.HORIZONTAL);
        JFreeChart chart = new JFreeChart("Flow Group Details", JFreeChart.DEFAULT_TITLE_FONT, plot, false);

        return chart;

    }

    /**
     * @return Returns the listOfGroup.
     */
    protected Map getListOfGroup() {
        return mListOfGroup;
    }

    /**
     * @param pListOfGroup The listOfGroup to set.
     */
    protected void setListOfGroup(Map pListOfGroup) {
        mListOfGroup = pListOfGroup;
    }

    void computeStatForThisFlow() {
        Map tMap = new HashMap();
        getResursiveStatOfThisMethodCall(mFirstMeasure, tMap);
        int tMaxMethodPerGroup = 0;
        for (Iterator tIterator = tMap.values().iterator(); tIterator.hasNext();) {
            tMaxMethodPerGroup = Math.max(tMaxMethodPerGroup, ((Integer) tIterator.next()).intValue());
        }
        mMaxMethodPerGroup = tMaxMethodPerGroup;
    }

    private void getResursiveStatOfThisMethodCall(MethodCallDTO pCurrentMeasure, Map pMapOfGroup) {
        String tCurGroupName = pCurrentMeasure.getGroupName();
        Integer tCurrentNbOfMeth = (Integer) pMapOfGroup.get(tCurGroupName);
        if (tCurrentNbOfMeth == null) {
            pMapOfGroup.put(tCurGroupName, new Integer(1));
        } else {
            pMapOfGroup.put(tCurGroupName, new Integer(tCurrentNbOfMeth.intValue() + 1));
        }
        for (int i = 0; i < pCurrentMeasure.getChildren().length; i++) {
            getResursiveStatOfThisMethodCall(pCurrentMeasure.getChild(i), pMapOfGroup);
        }

    }

    int getMaxMethodPerGroup() {
        return mMaxMethodPerGroup;
    }
}
