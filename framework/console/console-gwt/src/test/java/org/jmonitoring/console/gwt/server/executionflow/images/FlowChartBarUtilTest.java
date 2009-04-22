package org.jmonitoring.console.gwt.server.executionflow.images;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import junit.framework.TestCase;

import org.jfree.data.gantt.Task;
import org.jfree.data.time.TimePeriod;
import org.jmonitoring.console.gwt.client.dto.MapDto;
import org.jmonitoring.console.gwt.server.executionflow.images.FlowChartBarUtil.TaskForGroupName;
import org.jmonitoring.core.configuration.ColorManager;
import org.jmonitoring.core.configuration.FormaterBean;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPK;
import org.jmonitoring.core.domain.MethodCallPO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners( {DependencyInjectionTestExecutionListener.class })
@ContextConfiguration(locations = {"/formater-test.xml", "/color-test.xml", "/chart-manager-test.xml" })
public class FlowChartBarUtilTest extends TestCase
{
    @Resource(name = "formater")
    private FormaterBean mFormater;

    @Resource(name = "color")
    private ColorManager mColor;

    @Autowired
    private ChartManager mChartManager;

    @Test
    public void testChainAllMethodCallToMainTaskOfGroup()
    {
        MethodCallPO tFirstMethod = getVerySimpleMeasurePoint();
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
        MethodCallPO tFirstMethod = getSampleMeasurePoint();

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

    private MethodCallPO getSampleMeasurePoint()
    {
        ExecutionFlowPO tFlow = new ExecutionFlowPO();
        tFlow.setId(0);

        MethodCallPO tPoint;
        // Fri Jun 02 23:11:08 CEST 2006
        Date tRefDate = new Date(START_TIME);
        tPoint = new MethodCallPO();
        tPoint.setMethId(new MethodCallPK(tFlow, 0));
        tPoint.setFlow(tFlow);
        tPoint.setClassName(FlowUtilTest.class.getName());
        tPoint.setMethodName("builNewFullFlow");
        tPoint.setGroupName("GrDefault");
        tPoint.setParams("[]");
        tPoint.setBeginTime(tRefDate.getTime());
        tPoint.setEndTime(tRefDate.getTime() + 106);

        MethodCallPO tChild1 = new MethodCallPO();
        tChild1.setMethId(new MethodCallPK(tFlow, 1));
        tChild1.setFlow(tFlow);
        tChild1.setParentMethodCall(tPoint);
        tChild1.setClassName(FlowUtilTest.class.getName());
        tChild1.setMethodName("builNewFullFlow2");
        tChild1.setGroupName("GrChild1");
        tChild1.setParams("[]");
        tChild1.setBeginTime(tRefDate.getTime() + 2);
        tChild1.setEndTime(tRefDate.getTime() + 45);

        MethodCallPO tChild3 = new MethodCallPO();
        tChild3.setMethId(new MethodCallPK(tFlow, 3));
        tChild3.setFlow(tFlow);
        tChild3.setParentMethodCall(tChild1);
        tChild3.setClassName(FlowUtilTest.class.getName());
        tChild3.setMethodName("builNewFullFlow4");
        tChild3.setGroupName("GrChild2");
        tChild3.setParams("[]");
        tChild3.setBeginTime(tRefDate.getTime() + 5);
        tChild3.setEndTime(tRefDate.getTime() + 17);

        MethodCallPO tChild4 = new MethodCallPO();
        tChild4.setMethId(new MethodCallPK(tFlow, 4));
        tChild4.setFlow(tFlow);
        tChild4.setParentMethodCall(tChild1);
        tChild4.setClassName(FlowUtilTest.class.getName());
        tChild4.setMethodName("builNewFullFlow4");
        tChild4.setGroupName("GrDefault");
        tChild4.setParams("[]");
        tChild4.setBeginTime(tRefDate.getTime() + 23);
        tChild4.setEndTime(tRefDate.getTime() + 27);

        MethodCallPO tChild2 = new MethodCallPO();
        tChild2.setMethId(new MethodCallPK(tFlow, 2));
        tChild2.setFlow(tFlow);
        tChild2.setParentMethodCall(tPoint);
        tChild2.setClassName(FlowUtilTest.class.getName());
        tChild2.setMethodName("builNewFullFlow3");
        tChild2.setGroupName("GrChild2");
        tChild2.setParams("[]");
        tChild2.setBeginTime(tRefDate.getTime() + 48);
        tChild2.setEndTime(tRefDate.getTime() + 75);
        return tPoint;
    }

    public static final long START_TIME = 1149282668046L;

    MethodCallPO getVerySimpleMeasurePoint()
    {
        ExecutionFlowPO tFlow = new ExecutionFlowPO();
        tFlow.setId(0);

        MethodCallPO tPoint;
        // Fri Jun 02 23:11:08 CEST 2006
        Date tRefDate = new Date(START_TIME);
        tPoint = new MethodCallPO();
        tPoint.setMethId(new MethodCallPK(tFlow, 1));
        tPoint.setFlow(tFlow);
        tPoint.setParentMethodCall(null);
        tPoint.setClassName(FlowUtilTest.class.getName());
        tPoint.setMethodName("builNewFullFlow");
        tPoint.setGroupName("GrDefault");
        tPoint.setParams("[]");
        tPoint.setBeginTime(tRefDate.getTime());
        tPoint.setEndTime(tRefDate.getTime() + 106);

        MethodCallPO tChild1 = new MethodCallPO();
        tChild1.setMethId(new MethodCallPK(tFlow, 2));
        tChild1.setFlow(tFlow);
        tChild1.setParentMethodCall(tPoint);
        tChild1.setClassName(FlowUtilTest.class.getName());
        tChild1.setMethodName("builNewFullFlow2");
        tChild1.setGroupName("GrChild1");
        tChild1.setParams("[]");
        tChild1.setBeginTime(tRefDate.getTime() + 2);
        tChild1.setEndTime(tRefDate.getTime() + 45);

        MethodCallPO tChild2 = new MethodCallPO();
        tChild2.setMethId(new MethodCallPK(tFlow, 3));
        tChild2.setFlow(tFlow);
        tChild2.setParentMethodCall(tPoint);
        tChild2.setClassName(FlowUtilTest.class.getName());
        tChild2.setMethodName("builNewFullFlow2");
        tChild2.setGroupName("GrChild1");
        tChild2.setParams("[]");
        tChild2.setBeginTime(tRefDate.getTime() + 48);
        tChild2.setEndTime(tRefDate.getTime() + 54);

        return tPoint;
    }

    @Test
    public void testComputeStatForThisFlow()
    {
        // Simple
        MethodCallPO tFirstMethod = getVerySimpleMeasurePoint();
        FlowChartBarUtil tUtil = new FlowChartBarUtil(mFormater, tFirstMethod, mColor, mChartManager);
        assertEquals(2, tUtil.getMaxMethodPerGroup());

        // One group with couple of children
        tFirstMethod.getChildren().remove(1);
        tFirstMethod.getChild(0).setGroupName(tFirstMethod.getGroupName());
        tUtil = new FlowChartBarUtil(mFormater, tFirstMethod, mColor, null);
        assertEquals(2, tUtil.getMaxMethodPerGroup());

        // 1 group and one child
        tFirstMethod = tFirstMethod.getChild(0);
        tUtil = new FlowChartBarUtil(mFormater, tFirstMethod, mColor, null);
        assertEquals(1, tUtil.getMaxMethodPerGroup());

        // 2 groups and one child
        tFirstMethod = getVerySimpleMeasurePoint();
        tFirstMethod.getChildren().remove(0);
        tUtil = new FlowChartBarUtil(mFormater, tFirstMethod, mColor, null);
        assertEquals(1, tUtil.getMaxMethodPerGroup());
    }

    @Test
    public void testParseMap()
    {
        String tMap =
            "<MAP NAME=\"ChartBar\">\n"
                + "<AREA SHAPE=\"RECT\" COORDS=\"122,85,123,125\" href=\"MethodCallEditIn.do?flowId=14&position=1\">"
                + "<AREA SHAPE=\"RECT\" COORDS=\"134,85,135,125\" href=\"MethodCallEditIn.do?flowId=14&position=2\">"
                + "<AREA SHAPE=\"RECT\" COORDS=\"134,85,138,125\" href=\"MethodCallEditIn.do?flowId=14&position=4\">"
                + "</MAP>";
        MapDto tMapDto = FlowChartBarUtil.parseMap(tMap);
        assertEquals(14, tMapDto.getFlowId());
        assertEquals(3, tMapDto.getAreas().size());
        assertEquals("122,85,123,125", tMapDto.getAreas().get(0).getCoordinate());
        assertEquals(1, tMapDto.getAreas().get(0).getPosition());
        assertEquals("134,85,135,125", tMapDto.getAreas().get(1).getCoordinate());
        assertEquals(2, tMapDto.getAreas().get(1).getPosition());
        assertEquals("134,85,138,125", tMapDto.getAreas().get(2).getCoordinate());
        assertEquals(4, tMapDto.getAreas().get(2).getPosition());
    }
}
