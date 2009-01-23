package org.jmonitoring.console.flow.jfreechart;

import javax.annotation.Resource;

import org.jfree.data.category.IntervalCategoryDataset;
import org.jmonitoring.console.JMonitoringMockStrustTestCase;
import org.jmonitoring.console.flow.FlowBuilderUtil;
import org.jmonitoring.core.configuration.ColorManager;
import org.jmonitoring.core.configuration.FormaterBean;
import org.jmonitoring.core.dto.ExecutionFlowDTO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class FlowDetailURLGeneratorTest extends JMonitoringMockStrustTestCase
{
    @Resource(name = "formater")
    private FormaterBean mFormater;

    @Resource(name = "color")
    private ColorManager mColor;

    @Autowired
    private FlowBuilderUtil mFlowBuilder;

    @Test
    public void testUGRGeneration()
    {
        dropCreate();
        ExecutionFlowDTO tFlow = mFlowBuilder.buildAndSaveNewDto(2);

        FlowChartBarUtil tUtil = new FlowChartBarUtil(mFormater, tFlow.getFirstMethodCall(), mColor);
        tUtil.chainAllMethodCallToMainTaskOfGroup(tFlow.getFirstMethodCall());
        IntervalCategoryDataset tIntervalcategorydataset = tUtil.createDataset();
        tUtil.createGanttChart(tIntervalcategorydataset);
        // Plot tPlot = jfreechart.getPlot();

        FlowDetailURLGenerator tGenerator = new FlowDetailURLGenerator();
        String tUrl = tGenerator.generateURL(tIntervalcategorydataset, 0, 0);
        assertEquals("MethodCallEditIn.do?flowId=1&position=1", tUrl);
        assertEquals("MethodCallEditIn.do?flowId=1&position=1", tGenerator.generateURL(tIntervalcategorydataset, 0, 0));
        assertEquals("MethodCallEditIn.do?flowId=1&position=1", tGenerator.generateURL(tIntervalcategorydataset, 0, 0));
        assertEquals("MethodCallEditIn.do?flowId=1&position=2", tGenerator.generateURL(tIntervalcategorydataset, 0, 1));
        assertEquals("MethodCallEditIn.do?flowId=1&position=3", tGenerator.generateURL(tIntervalcategorydataset, 0, 1));

    }

}
