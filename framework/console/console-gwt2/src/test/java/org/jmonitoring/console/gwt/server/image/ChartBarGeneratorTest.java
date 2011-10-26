package org.jmonitoring.console.gwt.server.image;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.jfree.data.gantt.Task;
import org.jfree.data.time.TimePeriod;
import org.jmonitoring.console.gwt.server.common.ColorManager;
import org.jmonitoring.console.gwt.server.common.ExecutionFlowBuilder;
import org.jmonitoring.console.gwt.server.common.MethodCallBuilder;
import org.jmonitoring.console.gwt.server.common.PersistanceTestCase;
import org.jmonitoring.console.gwt.server.image.ChartBarGenerator.TaskForGroupName;
import org.jmonitoring.core.configuration.FormaterBean;
import org.jmonitoring.core.domain.MethodCallPO;
import org.junit.Test;

public class ChartBarGeneratorTest extends PersistanceTestCase
{
    @Resource(name = "formater")
    private FormaterBean mFormater;

    @Resource(name = "color")
    private ColorManager mColor;

    public ChartBarGeneratorTest()
    {
        dataInitialized = true;
    }

    @Test
    public void testChainAllMethodCallToMainTaskOfGroup()
    {
        MethodCallPO tFirstMethod = getVerySimpleMeasurePoint();
        ChartBarGenerator tChartGenerator = new ChartBarGenerator(mColor, tFirstMethod);
        tChartGenerator.chainAllMethodCallToMainTaskOfGroup();

        Map<String, TaskForGroupName> tGroups = tChartGenerator.listOfGroup;
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
        MethodCallPO tFirstMethod =
            PieChartGeneratorTest.getSampleMeasurePoint(mFormater, mColor, sessionFactory.getCurrentSession());

        ChartBarGenerator tUtil = new ChartBarGenerator(mColor, tFirstMethod);
        tUtil.chainAllMethodCallToMainTaskOfGroup();

        Map<String, TaskForGroupName> tGroups = tUtil.listOfGroup;
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
        assertEquals(45, curDuration.getStart().getTime() - START_TIME);
        assertEquals(48, curDuration.getEnd().getTime() - START_TIME);

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
        assertEquals(27, curDuration.getStart().getTime() - START_TIME);
        assertEquals(45, curDuration.getEnd().getTime() - START_TIME);

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

    MethodCallPO getVerySimpleMeasurePoint()
    {
        long tRefDate = new Date(START_TIME).getTime();

        ExecutionFlowBuilder tBuilder = ExecutionFlowBuilder.create(tRefDate);
        MethodCallBuilder tParentBuilder =
            tBuilder.createMethodCall(ChartBarGeneratorTest.class.getName(), "builNewFullFlow", "GrDefault", 106);
        tParentBuilder.addSubMethod(ChartBarGeneratorTest.class.getName(), "builNewFullFlow2", "GrChild1", 2, 43);
        tParentBuilder.addSubMethod(ChartBarGeneratorTest.class.getName(), "builNewFullFlow2", "GrChild1", 48, 6);

        return tParentBuilder.getAndSave(sessionFactory.getCurrentSession()).getFirstMethodCall();
    }

    @Test
    public void testComputeStatForThisFlow()
    {
        // Simple
        MethodCallPO tFirstMethod = getVerySimpleMeasurePoint();
        ChartBarGenerator tUtil = new ChartBarGenerator(mColor, tFirstMethod);
        assertEquals(2, tUtil.maxMethodPerGroup);

        // One group with couple of children
        tFirstMethod.getChildren().remove(1);
        tFirstMethod.getChild(0).setGroupName(tFirstMethod.getGroupName());
        tUtil = new ChartBarGenerator(mColor, tFirstMethod);
        assertEquals(2, tUtil.maxMethodPerGroup);

        // 1 group and one child
        tFirstMethod = tFirstMethod.getChild(0);
        tUtil = new ChartBarGenerator(mColor, tFirstMethod);
        assertEquals(1, tUtil.maxMethodPerGroup);

        // 2 groups and one child
        tFirstMethod = getVerySimpleMeasurePoint();
        tFirstMethod.getChildren().remove(0);
        tUtil = new ChartBarGenerator(mColor, tFirstMethod);
        assertEquals(1, tUtil.maxMethodPerGroup);
    }

}
