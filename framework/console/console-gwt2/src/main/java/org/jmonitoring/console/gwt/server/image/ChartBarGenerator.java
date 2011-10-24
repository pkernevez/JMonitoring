package org.jmonitoring.console.gwt.server.image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

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
import org.jmonitoring.console.gwt.server.common.ColorManager;
import org.jmonitoring.console.gwt.shared.flow.MethodCallDTO;
import org.jmonitoring.core.configuration.MeasureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChartBarGenerator
{
    private static final Logger sLog = LoggerFactory.getLogger(ChartBarGenerator.class);

    private static final String CHART_BAR_FLOWS = "CHART_BAR_FLOWS";

    private static final float LABEL_WIDTH_RATIO = 10F;

    private static final int IMAGE_WIDTH = 930;

    private static final int BODER_TOP = 70;

    private static final int BORDER_DOWN = 60;

    private final ColorManager colorManager;

    private final MethodCallDTO firstMeasure;

    Map<String, TaskForGroupName> listOfGroup = new HashMap<String, TaskForGroupName>();

    int maxMethodPerGroup;

    public ChartBarGenerator(ColorManager pColorManager, MethodCallDTO pFirstMeasure)
    {
        super();
        colorManager = pColorManager;
        firstMeasure = pFirstMeasure;
        computeStatForThisFlow();
    }

    void computeStatForThisFlow()
    {
        Map<String, Integer> tMap = new HashMap<String, Integer>();
        getResursiveStatOfThisMethodCall(firstMeasure, tMap);
        int tMaxMethodPerGroup = 0;
        for (int tCurValue : tMap.values())
        {
            tMaxMethodPerGroup = Math.max(tMaxMethodPerGroup, tCurValue);
        }
        maxMethodPerGroup = tMaxMethodPerGroup;
    }

    private void getResursiveStatOfThisMethodCall(MethodCallDTO pCurrentMeasure, Map<String, Integer> pMapOfGroup)
    {
        String tCurGroupName = pCurrentMeasure.getGroupName();
        Integer tCurrentNbOfMeth = pMapOfGroup.get(tCurGroupName);
        tCurrentNbOfMeth = (tCurrentNbOfMeth == null ? 0 : tCurrentNbOfMeth);
        pMapOfGroup.put(tCurGroupName, tCurrentNbOfMeth + 1);
        for (int i = 0; i < pCurrentMeasure.getChildren().length; i++)
        {
            getResursiveStatOfThisMethodCall(pCurrentMeasure.getChild(i), pMapOfGroup);
        }

    }

    private ChartManager chartManager = new ChartManager();

    private static class TaskComparator implements Comparator<TaskForGroupName>
    {
        public int compare(TaskForGroupName pTask1, TaskForGroupName pTask2)
        {
            return (pTask1.positionOfTheGroup - pTask2.positionOfTheGroup);
        }
    }

    /**
     * Class associated to the first Task of the group. All the MethodCall of the groups are subTask of this first Task.
     */
    static final class TaskForGroupName
    {
        private int positionOfTheGroup;

        private String groupName;

        private Task mainTaskOfGroup;

        protected Task getMainTaskOfGroup()
        {
            return mainTaskOfGroup;
        }

    }

    public void chainAllMethodCallToMainTaskOfGroup()
    {
        chainAllMethodCallToMainTaskOfGroup(firstMeasure);
    }

    private void chainAllMethodCallToMainTaskOfGroup(MethodCallDTO pCurMeasure)
    {
        String tGroupName = pCurMeasure.getGroupName();
        TaskForGroupName tTask = getTaskForGroupName(pCurMeasure, tGroupName);

        // List tListOfClidren = pCurMeasure.getChildren();
        Date tBeginDate;
        Date tEndDate = new Date(pCurMeasure.getBeginMilliSeconds());
        for (MethodCallDTO curChild : pCurMeasure.getChildren())
        {
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

        pTaskForTheGroupName.mainTaskOfGroup.addSubtask(new MethodCallTask(
                                                                           pFlowId,
                                                                           pMethodCallId,
                                                                           new SimpleTimePeriod(pBeginDate, tNewEndDate)));
    }

    private TaskForGroupName getTaskForGroupName(MethodCallDTO pCurMeasure, String pGroupName)
    {
        TaskForGroupName tTaskEntry = listOfGroup.get(pGroupName);
        if (tTaskEntry == null)
        { // We create a new entry
            tTaskEntry = new TaskForGroupName();
            tTaskEntry.positionOfTheGroup = chartManager.getPosition(pGroupName);
            tTaskEntry.mainTaskOfGroup =
                new Task(pGroupName, new SimpleTimePeriod(new Date(pCurMeasure.getBeginMilliSeconds()),
                                                          new Date(pCurMeasure.getEndMilliSeconds())));
            tTaskEntry.groupName = pGroupName;
            listOfGroup.put(pGroupName, tTaskEntry);
        }
        return tTaskEntry;
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
            sLog.debug("add Task n˚" + i + " for GroupName=" + curTaskEntry.groupName + " in position of ="
                + curTaskEntry.positionOfTheGroup);
            curTaskSeries.add(curTaskEntry.mainTaskOfGroup);
        }
        taskseriescollection.add(curTaskSeries);
        return taskseriescollection;
    }

    private TaskForGroupName[] orderListOfTask()
    {
        TaskForGroupName[] tTaskEntries = new TaskForGroupName[listOfGroup.size()];;
        SortedSet<TaskForGroupName> tOrderedTask = new TreeSet<TaskForGroupName>(new TaskComparator());
        tOrderedTask.addAll(listOfGroup.values());
        int i = 0;
        for (TaskForGroupName curTaskEntry : tOrderedTask)
        {
            tTaskEntries[i++] = curTaskEntry;
        }
        return tTaskEntries;
    }

    /**
     * Creates a Gantt chart using the supplied attributes plus default values where required.
     * <P>
     * The chart object returned by this method uses a {@link CategoryPlot}instance as the plot, with a
     * {@link CategoryAxis}for the domain axis, a {@link DateAxis}as the range axis, and a {@link GanttRenderer}as the
     * renderer.
     * 
     * @param pDataset the dataset for the chart (<code>null</code> permitted).
     * @return A Gantt chart.
     */
    public JFreeChart createGanttChart(IntervalCategoryDataset pDataset)
    {
        CategoryAxis categoryAxis = new CategoryAxis("Flow Groups");
        DateAxis dateAxis = new DateAxis("Date");
        CategoryItemRenderer renderer = new FlowRenderer(colorManager);
        renderer.setItemURLGenerator(new FlowDetailURLGenerator());
        CategoryPlot plot = new CategoryPlot(pDataset, categoryAxis, dateAxis, renderer);
        plot.setOrientation(PlotOrientation.HORIZONTAL);
        JFreeChart chart = new JFreeChart("Flow Group Details", JFreeChart.DEFAULT_TITLE_FONT, plot, false);

        return chart;

    }

    public byte[] getDurationInGroup(MethodCallDTO pFirstMeasure)
    {
        chainAllMethodCallToMainTaskOfGroup(pFirstMeasure);
        IntervalCategoryDataset intervalcategorydataset = createDataset();
        JFreeChart tChart = createGanttChart(intervalcategorydataset);
        tChart.getCategoryPlot().getDomainAxis().setMaxCategoryLabelWidthRatio(LABEL_WIDTH_RATIO);

        // addChartToSession(jfreechart, null, CHART_BAR_FLOWS, null);
        Plot tPlot = tChart.getPlot();
        // tPlot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        tPlot.setNoDataMessage("No data available");
        sLog.debug("PlotClass=" + tPlot.getClass());

        ByteArrayOutputStream tStream = new ByteArrayOutputStream();
        try
        {
            int tHeight = BODER_TOP + listOfGroup.size() * BORDER_DOWN;
            ChartRenderingInfo tChartRenderingInfo = new ChartRenderingInfo(new StandardEntityCollection());
            ChartUtilities.writeChartAsPNG(tStream, tChart, IMAGE_WIDTH, tHeight, tChartRenderingInfo);
            ByteArrayOutputStream tMapStream = new ByteArrayOutputStream();
            PrintWriter tWriter = new PrintWriter(tMapStream);
            ChartUtilities.writeImageMap(tWriter, "ChartBar", tChartRenderingInfo);
            tWriter.flush();
            // pForm.setImageMap(tMapStream.toString());
            return null;
        } catch (IOException e)
        {
            throw new MeasureException("Unable to write Image", e);
        }
    }
}