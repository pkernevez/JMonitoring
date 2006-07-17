package org.jmonitoring.console.flow.jfreechart;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.labels.IntervalCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.GanttRenderer;
import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;
import org.jmonitoring.core.common.MeasureException;
import org.jmonitoring.core.dto.MethodCallDTO;

public class FlowChartBarUtil
{
    private static final float LABEL_WIDTH_RATIO = 10F;

    private static final int IMAGE_WIDTH = 930;

    private static final int BODER_TOP = 70;

    private static final int BORDER_DOWN = 60;

    private static Log sLog = LogFactory.getLog(FlowChartBarUtil.class);

    private static final String CHART_BAR_FLOWS = "CHART_BAR_FLOWS";

    private Map mListOfGroup = new HashMap();

    /**
     * Write a PNG image into the Session. The image is the chartbar representing the statistics of the execution of the
     * same method.
     * 
     * @param pSession The <code>Session</code> where we have to write the image.
     * @param pFirstMeasure The root of the Tree of <code>MethodCallDTO</code> to use for the image generation.
     */
    public static void writeImageIntoSession(HttpSession pSession, MethodCallDTO pFirstMeasure)
    {
        FlowChartBarUtil tUtil = new FlowChartBarUtil();
        tUtil.fillChart(pSession, pFirstMeasure);
    }

    private void fillChart(HttpSession pSession, MethodCallDTO pFirstMeasure)
    {
        fillListOfGroup(pFirstMeasure);
        IntervalCategoryDataset intervalcategorydataset = createDataset();
        JFreeChart jfreechart = createGanttChart("Flow Group Details", "Flow Groups", "Date", intervalcategorydataset,
                        false, false, false);
        jfreechart.getCategoryPlot().getDomainAxis().setMaxCategoryLabelWidthRatio(LABEL_WIDTH_RATIO);
        addChart(jfreechart, pSession, CHART_BAR_FLOWS);
    }

    static class TaskEntry
    {
        private int mPos;

        private String mGroupName;

        private Task mTask;

        /**
         * @return Returns the groupName.
         */
        String getGroupName()
        {
            return mGroupName;
        }

        /**
         * @param pGroupName The groupName to set.
         */
        void setGroupName(String pGroupName)
        {
            mGroupName = pGroupName;
        }

        /**
         * @return Returns the pos.
         */
        int getPos()
        {
            return mPos;
        }

        /**
         * @param pPos The pos to set.
         */
        void setPos(int pPos)
        {
            mPos = pPos;
        }

        /**
         * @return Returns the task.
         */
        Task getTask()
        {
            return mTask;
        }

        /**
         * @param pTask The task to set.
         */
        void setTask(Task pTask)
        {
            mTask = pTask;
        }

    }

    public void fillListOfGroup(MethodCallDTO pCurMeasure)
    {
        String tGroupName = pCurMeasure.getGroupName();
        TaskEntry tTask = getTaskEntry(pCurMeasure, tGroupName);

        List tListOfClidren = pCurMeasure.getChildren();
        Date tBeginDate;
        Date tEndDate = pCurMeasure.getBeginTime();
        MethodCallDTO curChild;
        for (int i = 0; i < tListOfClidren.size(); i++)
        {
            curChild = ((MethodCallDTO) tListOfClidren.get(i));
            tBeginDate = tEndDate;
            tEndDate = curChild.getBeginTime();
            addSubTask(tGroupName, tTask, tBeginDate, tEndDate);
            fillListOfGroup(curChild);
            tEndDate = curChild.getEndTime();
        }

        tBeginDate = tEndDate;
        tEndDate = pCurMeasure.getEndTime();
        addSubTask(tGroupName, tTask, tBeginDate, tEndDate);
    }

    private void addSubTask(String pGroupName, TaskEntry pTask, Date pBeginDate, Date pEndDate)
    {
        if (pEndDate.before(pBeginDate))
        { // Contournement du bug
            pEndDate = pBeginDate;
        }
        pTask.mTask.addSubtask(new Task(pGroupName, new SimpleTimePeriod(pBeginDate, pEndDate)));
    }

    private TaskEntry getTaskEntry(MethodCallDTO pCurMeasure, String tGroupName)
    {
        TaskEntry tTask = (TaskEntry) mListOfGroup.get(tGroupName);
        if (tTask == null)
        { // We create a new entry
            tTask = new TaskEntry();
            tTask.mPos = mListOfGroup.size() + 1;
            tTask.mTask = new Task(tGroupName, new SimpleTimePeriod(pCurMeasure.getBeginTime(), pCurMeasure
                            .getEndTime()));
            tTask.mGroupName = tGroupName;
            mListOfGroup.put(tGroupName, tTask);
        }
        return tTask;
    }

    /**
     * Add a chart into an attribute name of the user session.
     * 
     * @param pChart The Chart to add to the UserSession.
     * @param pSession The user HttSession.
     * @param pName Name of the session attribute.
     */
    private void addChart(JFreeChart pChart, HttpSession pSession, String pName)
    {
        Plot tPlot = pChart.getPlot();
        // tPlot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        tPlot.setNoDataMessage("No data available");
        sLog.debug("PlotClass=" + tPlot.getClass());

        ByteArrayOutputStream tStream = new ByteArrayOutputStream();
        try
        {
            int tHeight = BODER_TOP + mListOfGroup.size() * BORDER_DOWN;
            ChartUtilities.writeChartAsPNG(tStream, pChart, IMAGE_WIDTH, tHeight);
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
        TaskEntry curTaskEntry;
        TaskEntry[] tList = orderListOfTask();
        // curTaskSeries = new TaskSeries(curTaskEntry.mGroupName);
        curTaskSeries = new TaskSeries("Taff");
        for (int i = 0; i < tList.length; i++)
        { // ForEach GroupName
            curTaskEntry = tList[i];
            sLog.debug("add Task n°" + i + " for GroupName=" + curTaskEntry.mGroupName + " in position of ="
                            + curTaskEntry.mPos);
            curTaskSeries.add(curTaskEntry.mTask);
        }
        taskseriescollection.add(curTaskSeries);
        return taskseriescollection;
    }

    private TaskEntry[] orderListOfTask()
    {
        TaskEntry[] tTaskEntries = new TaskEntry[mListOfGroup.size()];
        TaskEntry curTask;
        for (Iterator tIt = mListOfGroup.values().iterator(); tIt.hasNext();)
        {
            curTask = (TaskEntry) tIt.next();
            tTaskEntries[curTask.mPos - 1] = curTask;
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
     * @param pTitle the chart title (<code>null</code> permitted).
     * @param pCategoryAxisLabel the label for the category axis (<code>null</code> permitted).
     * @param pDateAxisLabel the label for the date axis (<code>null</code> permitted).
     * @param pDataset the dataset for the chart (<code>null</code> permitted).
     * @param pLegend a flag specifying whether or not a legend is required.
     * @param pTooltips configure chart to generate tool tips?
     * @param pUrls configure chart to generate URLs?
     * @return A Gantt chart.
     */
    public static JFreeChart createGanttChart(String pTitle, String pCategoryAxisLabel, String pDateAxisLabel,
                    IntervalCategoryDataset pDataset, boolean pLegend, boolean pTooltips, boolean pUrls)
    {

        CategoryAxis categoryAxis = new CategoryAxis(pCategoryAxisLabel);
        DateAxis dateAxis = new DateAxis(pDateAxisLabel);

        CategoryItemRenderer renderer = new FlowRenderer();
        if (pTooltips)
        {
            renderer
                            .setToolTipGenerator(new IntervalCategoryToolTipGenerator("{3} - {4}", DateFormat
                                            .getDateInstance()));
        }
        if (pUrls)
        {
            renderer.setItemURLGenerator(new StandardCategoryURLGenerator());
        }

        CategoryPlot plot = new CategoryPlot(pDataset, categoryAxis, dateAxis, renderer);
        plot.setOrientation(PlotOrientation.HORIZONTAL);
        JFreeChart chart = new JFreeChart(pTitle, JFreeChart.DEFAULT_TITLE_FONT, plot, pLegend);

        return chart;

    }

    /**
     * @return Returns the listOfGroup.
     */
    protected Map getListOfGroup()
    {
        return mListOfGroup;
    }

    /**
     * @param pListOfGroup The listOfGroup to set.
     */
    protected void setListOfGroup(Map pListOfGroup)
    {
        mListOfGroup = pListOfGroup;
    }

}
