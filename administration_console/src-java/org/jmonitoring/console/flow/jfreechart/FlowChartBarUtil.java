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

import org.jmonitoring.core.common.MeasureException;
import org.jmonitoring.core.dto.MethodCallDTO;

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
import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;

public class FlowChartBarUtil
{
    private static final float LABEL_WIDTH_RATIO = 10F;
    private static final int IMAGE_WIDTH = 930;
    private static final int BODER_TOP = 70;
    private static final int BORDER_DOWN = 60;
    private static Log sLog = LogFactory.getLog(FlowChartBarUtil.class);

    private static final String CHART_BAR_FLOWS = "CHART_BAR_FLOWS";

    private Map mListOfTask;

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
        mListOfTask = new HashMap();
        fillListOfGroup(pFirstMeasure);
        IntervalCategoryDataset intervalcategorydataset = createDataset();
        JFreeChart jfreechart = createChart(intervalcategorydataset);
        addChart(jfreechart, pSession, CHART_BAR_FLOWS);

    }

    private static class TaskEntry
    {
        private int mPos;

        private String mGroupName;

        private Task mTask;
    }

    private void fillListOfGroup(MethodCallDTO pCurMeasure)
    {
        String tGroupName = pCurMeasure.getGroupName();
        TaskEntry tTask = (TaskEntry) mListOfTask.get(tGroupName);
        if (tTask == null)
        { // We create a new entry
            tTask = new TaskEntry();
            tTask.mPos = mListOfTask.size() + 1;
            tTask.mTask = new Task(tGroupName, new SimpleTimePeriod(new Date(pCurMeasure.getBeginTime()),
                            new Date(pCurMeasure.getEndTime())));
            tTask.mGroupName = tGroupName;
            mListOfTask.put(tGroupName, tTask);
        }
        List tListOfClidren = pCurMeasure.getChildren();
        if (tListOfClidren.size() > 0)
        {
            long tBeginDateAsLong = pCurMeasure.getBeginTime();
            long tEndDateAsLong = ((MethodCallDTO) tListOfClidren.get(0)).getBeginTime();
            if (tEndDateAsLong < tBeginDateAsLong)
            { // Contournement du bug
                tEndDateAsLong = tBeginDateAsLong;
            }
            Date tBeginDate = new Date(tBeginDateAsLong);
            Date tEndDate = new Date(tEndDateAsLong);
            tTask.mTask.addSubtask(new Task(tGroupName, new SimpleTimePeriod(tBeginDate, tEndDate)));
            for (int i = 0; i < tListOfClidren.size(); i++)
            {
                if (i > 0)
                {
                    tBeginDateAsLong = ((MethodCallDTO) tListOfClidren.get(i - 1)).getEndTime();
                    tEndDateAsLong = ((MethodCallDTO) tListOfClidren.get(i)).getBeginTime();
                    if (tEndDateAsLong < tBeginDateAsLong)
                    { // Contournement du bug
                        tEndDateAsLong = tBeginDateAsLong;
                    }
                    tBeginDate = new Date(tBeginDateAsLong);
                    tEndDate = new Date(tEndDateAsLong);
                    tTask.mTask.addSubtask(new Task(tGroupName, new SimpleTimePeriod(tBeginDate, tEndDate)));
                }
                fillListOfGroup((MethodCallDTO) tListOfClidren.get(i));
            }

            tBeginDateAsLong = ((MethodCallDTO) tListOfClidren.get(tListOfClidren.size() - 1)).getEndTime();
            tEndDateAsLong = pCurMeasure.getEndTime();
            if (tEndDateAsLong < tBeginDateAsLong)
            { // Contournement du bug
                tEndDateAsLong = tBeginDateAsLong;
            }
            tBeginDate = new Date(tBeginDateAsLong);
            tEndDate = new Date(tEndDateAsLong);
            tTask.mTask.addSubtask(new Task(tGroupName, new SimpleTimePeriod(tBeginDate, tEndDate)));

        } else
        {
            tTask.mTask.addSubtask(new Task(tGroupName, new SimpleTimePeriod(new Date(pCurMeasure.getBeginTime()),
                            new Date(pCurMeasure.getEndTime()))));
        }
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
            int tHeight = BODER_TOP + mListOfTask.size() * BORDER_DOWN;
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
        TaskEntry[] tTaskEntries = new TaskEntry[mListOfTask.size()];
        TaskEntry curTask;
        for (Iterator tIt = mListOfTask.values().iterator(); tIt.hasNext();)
        {
            curTask = (TaskEntry) tIt.next();
            tTaskEntries[curTask.mPos - 1] = curTask;
        }
        return tTaskEntries;
    }

    private static JFreeChart createChart(IntervalCategoryDataset pIntervalcategorydataset)
    {
        JFreeChart jfreechart = createGanttChart("Flow Group Details", "Flow Groups", "Date",
                        pIntervalcategorydataset, false, false, false);
        jfreechart.getCategoryPlot().getDomainAxis().setMaxCategoryLabelWidthRatio(LABEL_WIDTH_RATIO);
        return jfreechart;
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
            renderer.setToolTipGenerator(new IntervalCategoryToolTipGenerator("{3} - {4}", DateFormat
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

}
