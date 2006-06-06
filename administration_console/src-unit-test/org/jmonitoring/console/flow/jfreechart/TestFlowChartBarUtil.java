package org.jmonitoring.console.flow.jfreechart;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.time.TimePeriod;
import org.jmonitoring.console.flow.jfreechart.FlowChartBarUtil.TaskEntry;
import org.jmonitoring.core.common.MeasureException;
import org.jmonitoring.core.dto.MethodCallDTO;

import junit.framework.TestCase;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestFlowChartBarUtil extends TestCase
{

    public void testFillListOfGroupVerySimple()
    {
        MethodCallDTO tFirstMethod = getVerySimpleMeasurePoint();

        FlowChartBarUtil tUtil = new FlowChartBarUtil();
        tUtil.fillListOfGroup(tFirstMethod);
//        writeChart("c:/temp/toto.gif", tUtil);
        
        Map tGroups = tUtil.getListOfGroup();
        assertEquals(2, tGroups.size());

        TaskEntry tTaskEntry = (TaskEntry) tGroups.get("GrDefault");
        assertNotNull(tTaskEntry);
        Task curTask = tTaskEntry.getTask();
        assertEquals(3, curTask.getSubtaskCount());
        Task curSubTask = curTask.getSubtask(0);
        TimePeriod curDuration = curSubTask.getDuration();
        assertEquals(0, curDuration.getStart().getTime() - START_TIME);
        assertEquals(2, curDuration.getEnd().getTime() - START_TIME);

        curSubTask = curTask.getSubtask(1);
        curDuration = curSubTask.getDuration();
        assertEquals(45, curDuration.getStart().getTime() - START_TIME);
        assertEquals(48, curDuration.getEnd().getTime() - START_TIME);
        
        curSubTask = curTask.getSubtask(2);
        curDuration = curSubTask.getDuration();
        assertEquals(54, curDuration.getStart().getTime() - START_TIME);
        assertEquals(106, curDuration.getEnd().getTime() - START_TIME);
        
        tTaskEntry = (TaskEntry) tGroups.get("GrChild1");
        assertNotNull(tTaskEntry);
        curTask = tTaskEntry.getTask();
        assertEquals(2, curTask.getSubtaskCount());

        curSubTask = curTask.getSubtask(0);
        curDuration = curSubTask.getDuration();
        assertEquals(2, curDuration.getStart().getTime() - START_TIME);
        assertEquals(45, curDuration.getEnd().getTime() - START_TIME);

        curSubTask = curTask.getSubtask(1);
        curDuration = curSubTask.getDuration();
        assertEquals(48, curDuration.getStart().getTime() - START_TIME);
        assertEquals(54, curDuration.getEnd().getTime() - START_TIME);

        
    }

    private void writeChart(String pString, FlowChartBarUtil pUtil)
    {
        IntervalCategoryDataset intervalcategorydataset = pUtil.createDataset();
        JFreeChart jfreechart = pUtil.createChart(intervalcategorydataset);
        Plot tPlot = jfreechart.getPlot();
        tPlot.setNoDataMessage("No data available");
        try
        {
            OutputStream tStream = new FileOutputStream(pString);
            ChartUtilities.writeChartAsPNG(tStream, jfreechart, 600, 400);
            tStream.close();
        } catch (IOException e)
        {
            throw new MeasureException("Unable to write Image", e);
        }
    }

    public void testFillListOfGroup()
    {
        MethodCallDTO tFirstMethod = TestFlowUtils.getSampleMeasurePoint();

        FlowChartBarUtil tUtil = new FlowChartBarUtil();
        tUtil.fillListOfGroup(tFirstMethod);

//        writeChart("c:/temp/toto2.gif", tUtil);
        Map tGroups = tUtil.getListOfGroup();
        assertEquals(3, tGroups.size());

        TaskEntry tTaskEntry = (TaskEntry) tGroups.get("GrDefault");
        assertNotNull(tTaskEntry);
        Task curTask = tTaskEntry.getTask();
        assertEquals(4, curTask.getSubtaskCount());
        Task curSubTask = curTask.getSubtask(0);
        TimePeriod curDuration = curSubTask.getDuration();
        assertEquals(0, curDuration.getStart().getTime() - START_TIME);
        assertEquals(2, curDuration.getEnd().getTime() - START_TIME);

        curSubTask = curTask.getSubtask(1);
        curDuration = curSubTask.getDuration();
        assertEquals(23, curDuration.getStart().getTime() - START_TIME);
        assertEquals(27, curDuration.getEnd().getTime() - START_TIME);

         curSubTask = curTask.getSubtask(2);
         curDuration = curSubTask.getDuration();
         assertEquals(45, curDuration.getStart().getTime()-TestFlowUtils.START_TIME);
         assertEquals(48, curDuration.getEnd().getTime()-TestFlowUtils.START_TIME);

        curSubTask = curTask.getSubtask(3);
        curDuration = curSubTask.getDuration();
        assertEquals(75, curDuration.getStart().getTime() - START_TIME);
        assertEquals(106, curDuration.getEnd().getTime() - START_TIME);

        tTaskEntry = (TaskEntry) tGroups.get("GrChild1");
        assertNotNull(tTaskEntry);
        curTask = tTaskEntry.getTask();
        assertEquals(3, curTask.getSubtaskCount());

        curSubTask = curTask.getSubtask(0);
        curDuration = curSubTask.getDuration();
        assertEquals(2, curDuration.getStart().getTime() - START_TIME);
        assertEquals(5, curDuration.getEnd().getTime() - START_TIME);

        curSubTask = curTask.getSubtask(1);
        curDuration = curSubTask.getDuration();
        assertEquals(17, curDuration.getStart().getTime() - START_TIME);
        assertEquals(23, curDuration.getEnd().getTime() - START_TIME);

        curSubTask = curTask.getSubtask(2);
        curDuration = curSubTask.getDuration();
         assertEquals(27, curDuration.getStart().getTime()-TestFlowUtils.START_TIME);
         assertEquals(45, curDuration.getEnd().getTime()-TestFlowUtils.START_TIME);

        tTaskEntry = (TaskEntry) tGroups.get("GrChild2");
        assertNotNull(tTaskEntry);
        curTask = tTaskEntry.getTask();
        assertEquals(2, curTask.getSubtaskCount());

        curSubTask = curTask.getSubtask(0);
        curDuration = curSubTask.getDuration();
        assertEquals(5, curDuration.getStart().getTime() - START_TIME);
        assertEquals(17, curDuration.getEnd().getTime() - START_TIME);

        curSubTask = curTask.getSubtask(1);
        curDuration = curSubTask.getDuration();
        assertEquals(48, curDuration.getStart().getTime() - START_TIME);
        assertEquals(75, curDuration.getEnd().getTime() - START_TIME);

    }

    public static final long START_TIME = 1149282668046L;

    static MethodCallDTO getVerySimpleMeasurePoint()
    {
        long tStart = START_TIME;
        MethodCallDTO tPoint;
        // Fri Jun 02 23:11:08 CEST 2006
        Date tRefDate = new Date(START_TIME);
        tPoint = new MethodCallDTO();
        tPoint.setParent(null);
        tPoint.setClassName(TestFlowUtils.class.getName());
        tPoint.setMethodName("builNewFullFlow");
        tPoint.setGroupName("GrDefault");
        tPoint.setParams("[]");
        tPoint.setBeginTime(tRefDate);
        tPoint.setEndTime(new Date(tRefDate.getTime() + 106));

        MethodCallDTO tChild1 = new MethodCallDTO();
        tChild1.setParent(tPoint);
        tPoint.addChildren(tChild1);
        tChild1.setClassName(TestFlowUtils.class.getName());
        tChild1.setMethodName("builNewFullFlow2");
        tChild1.setGroupName("GrChild1");
        tChild1.setParams("[]");
        tChild1.setBeginTime(new Date(tRefDate.getTime() + 2));
        tChild1.setEndTime(new Date(tRefDate.getTime() + 45));

        MethodCallDTO tChild2 = new MethodCallDTO();
        tChild2.setParent(tPoint);
        tPoint.addChildren(tChild2);
        tChild2.setClassName(TestFlowUtils.class.getName());
        tChild2.setMethodName("builNewFullFlow2");
        tChild2.setGroupName("GrChild1");
        tChild2.setParams("[]");
        tChild2.setBeginTime(new Date(tRefDate.getTime() + 48));
        tChild2.setEndTime(new Date(tRefDate.getTime() + 54));
return tPoint;
    }

}
