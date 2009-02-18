package org.jmonitoring.console.flow.jfreechart;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.jfree.data.gantt.Task;
import org.jfree.data.time.TimePeriod;
import org.jmonitoring.console.flow.jfreechart.FlowChartBarUtil.TaskForGroupName;
import org.jmonitoring.core.configuration.ColorManager;
import org.jmonitoring.core.configuration.FormaterBean;
import org.jmonitoring.core.dto.MethodCallDTO;
import org.jmonitoring.core.tests.JMonitoringTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

@ContextConfiguration(locations = {"/console.xml", "/formater-test.xml", "/color-test.xml", "/persistence-test.xml",
    "/chart-manager-test.xml" })
public class FlowChartBarUtilTest extends JMonitoringTestCase
{
    @Resource(name = "formater")
    private FormaterBean mFormater;

    @Resource(name = "color")
    private ColorManager mColor;

    @Autowired
    private FlowUtilTest mFlowUtilTest;

    @Autowired
    private ChartManager mChartManager;

    @Test
    public void testChainAllMethodCallToMainTaskOfGroup()
    {
        MethodCallDTO tFirstMethod = getVerySimpleMeasurePoint();
        FlowChartBarUtil tFlowChartBarUtil = new FlowChartBarUtil(mFormater, tFirstMethod, mColor, mChartManager);
        tFlowChartBarUtil.chainAllMethodCallToMainTaskOfGroup(tFirstMethod);

        Map<String, TaskForGroupName> tGroups = tFlowChartBarUtil.getListOfGroup();
        assertEquals(2, tGroups.size());

        TaskForGroupName tTaskEntry = tGroups.get("GrDefault");
        assertNotNull(tTaskEntry);
        Task curTask = tTaskEntry.getMainTaskOfGroup();
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

        tTaskEntry = tGroups.get("GrChild1");
        assertNotNull(tTaskEntry);
        curTask = tTaskEntry.getMainTaskOfGroup();
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

    @Test
    public void testFillListOfGroup()
    {
        MethodCallDTO tFirstMethod = mFlowUtilTest.getSampleMeasurePoint();

        FlowChartBarUtil tUtil = new FlowChartBarUtil(mFormater, tFirstMethod, mColor, mChartManager);
        tUtil.chainAllMethodCallToMainTaskOfGroup(tFirstMethod);

        Map<String, TaskForGroupName> tGroups = tUtil.getListOfGroup();
        assertEquals(3, tGroups.size());

        TaskForGroupName tTaskEntry = tGroups.get("GrDefault");
        assertNotNull(tTaskEntry);
        Task curTask = tTaskEntry.getMainTaskOfGroup();
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
        assertEquals(45, curDuration.getStart().getTime() - FlowUtilTest.START_TIME);
        assertEquals(48, curDuration.getEnd().getTime() - FlowUtilTest.START_TIME);

        curSubTask = curTask.getSubtask(3);
        curDuration = curSubTask.getDuration();
        assertEquals(75, curDuration.getStart().getTime() - START_TIME);
        assertEquals(106, curDuration.getEnd().getTime() - START_TIME);

        tTaskEntry = tGroups.get("GrChild1");
        assertNotNull(tTaskEntry);
        curTask = tTaskEntry.getMainTaskOfGroup();
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
        assertEquals(27, curDuration.getStart().getTime() - FlowUtilTest.START_TIME);
        assertEquals(45, curDuration.getEnd().getTime() - FlowUtilTest.START_TIME);

        tTaskEntry = tGroups.get("GrChild2");
        assertNotNull(tTaskEntry);
        curTask = tTaskEntry.getMainTaskOfGroup();
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

    MethodCallDTO getVerySimpleMeasurePoint()
    {
        MethodCallDTO tPoint;
        // Fri Jun 02 23:11:08 CEST 2006
        Date tRefDate = new Date(START_TIME);
        tPoint = new MethodCallDTO();
        tPoint.setPosition(1);
        tPoint.setParent(null);
        tPoint.setClassName(FlowUtilTest.class.getName());
        tPoint.setMethodName("builNewFullFlow");
        tPoint.setGroupName("GrDefault");
        tPoint.setParams("[]");
        tPoint.setBeginMilliSeconds(tRefDate.getTime());
        tPoint.setBeginTimeString(mFormater.formatDateTime(tRefDate));
        tPoint.setGroupColor("234567");
        tPoint.setEndMilliSeconds(tRefDate.getTime() + 106);
        tPoint.setEndTimeString(mFormater.formatDateTime(new Date(tRefDate.getTime() + 106)));
        MethodCallDTO[] tChildren = new MethodCallDTO[2];

        MethodCallDTO tChild1 = new MethodCallDTO();
        tChild1.setPosition(2);
        tChild1.setParent(tPoint);
        tChildren[0] = tChild1;
        tChild1.setClassName(FlowUtilTest.class.getName());
        tChild1.setMethodName("builNewFullFlow2");
        tChild1.setGroupName("GrChild1");
        tChild1.setParams("[]");
        tChild1.setBeginMilliSeconds(tRefDate.getTime() + 2);
        tChild1.setBeginTimeString(mFormater.formatDateTime(new Date(tRefDate.getTime() + 2)));
        tChild1.setGroupColor("234567");
        tChild1.setEndMilliSeconds(tRefDate.getTime() + 45);
        tChild1.setEndTimeString(mFormater.formatDateTime(new Date(tRefDate.getTime() + 45)));

        MethodCallDTO tChild2 = new MethodCallDTO();
        tChild2.setPosition(3);
        tChild2.setParent(tPoint);
        tChildren[1] = tChild2;
        tChild2.setClassName(FlowUtilTest.class.getName());
        tChild2.setMethodName("builNewFullFlow2");
        tChild2.setGroupName("GrChild1");
        tChild2.setParams("[]");
        tChild2.setBeginMilliSeconds(tRefDate.getTime() + 48);
        tChild2.setBeginTimeString(mFormater.formatDateTime(new Date(tRefDate.getTime() + 48)));
        tChild2.setEndMilliSeconds(tRefDate.getTime() + 54);
        tChild2.setEndTimeString(mFormater.formatDateTime(new Date(tRefDate.getTime() + 54)));
        tPoint.setChildren(tChildren);
        return tPoint;
    }

    @Test
    public void testComputeStatForThisFlow()
    {
        // Simple
        MethodCallDTO tFirstMethod = getVerySimpleMeasurePoint();
        FlowChartBarUtil tUtil = new FlowChartBarUtil(mFormater, tFirstMethod, mColor, mChartManager);
        assertEquals(2, tUtil.getMaxMethodPerGroup());

        // One group with couple of children
        tFirstMethod.removeChild(1);
        tFirstMethod.getChild(0).setGroupName(tFirstMethod.getGroupName());
        tUtil = new FlowChartBarUtil(mFormater, tFirstMethod, mColor, null);
        assertEquals(2, tUtil.getMaxMethodPerGroup());

        // 1 group and one child
        tFirstMethod = tFirstMethod.getChild(0);
        tUtil = new FlowChartBarUtil(mFormater, tFirstMethod, mColor, null);
        assertEquals(1, tUtil.getMaxMethodPerGroup());

        // 2 groups and one child
        tFirstMethod = getVerySimpleMeasurePoint();
        tFirstMethod.removeChild(0);
        tUtil = new FlowChartBarUtil(mFormater, tFirstMethod, mColor, null);
        assertEquals(1, tUtil.getMaxMethodPerGroup());
    }

}
