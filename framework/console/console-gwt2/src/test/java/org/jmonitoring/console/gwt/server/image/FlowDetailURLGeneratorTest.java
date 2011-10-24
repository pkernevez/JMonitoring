package org.jmonitoring.console.gwt.server.image;

import javax.annotation.Resource;

import org.jfree.data.category.IntervalCategoryDataset;
import org.jmonitoring.console.gwt.server.common.ColorManager;
import org.jmonitoring.console.gwt.server.common.PersistanceTestCase;
import org.jmonitoring.console.gwt.server.flow.FlowBuilderUtil;
import org.jmonitoring.console.gwt.shared.flow.ExecutionFlowDTO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class FlowDetailURLGeneratorTest extends PersistanceTestCase
{
    @Resource(name = "color")
    private ColorManager mColor;

    @Autowired
    private FlowBuilderUtil mFlowBuilder;

    public FlowDetailURLGeneratorTest()
    {
        dataInitialized = true;// Don't insert data before those test
    }

    @Test
    public void testUGRGeneration()
    {
        // dropCreate();
        ExecutionFlowDTO tFlow = mFlowBuilder.buildAndSaveNewDto(2);

        ChartBarGenerator tUtil = new ChartBarGenerator(mColor, tFlow.getFirstMethodCall());
        tUtil.chainAllMethodCallToMainTaskOfGroup();
        IntervalCategoryDataset tIntervalcategorydataset = tUtil.createDataset();
        tUtil.createGanttChart(tIntervalcategorydataset);

        FlowDetailURLGenerator tGenerator = new FlowDetailURLGenerator();
        String tUrl = tGenerator.generateURL(tIntervalcategorydataset, 0, 0);
        assertEquals("MethodCallEditIn.do?flowId=1&position=1", tUrl);
        assertEquals("MethodCallEditIn.do?flowId=1&position=1", tGenerator.generateURL(tIntervalcategorydataset, 0, 0));
        assertEquals("MethodCallEditIn.do?flowId=1&position=1", tGenerator.generateURL(tIntervalcategorydataset, 0, 0));
        assertEquals("MethodCallEditIn.do?flowId=1&position=2", tGenerator.generateURL(tIntervalcategorydataset, 0, 1));
        assertEquals("MethodCallEditIn.do?flowId=1&position=3", tGenerator.generateURL(tIntervalcategorydataset, 0, 1));

    }

}
