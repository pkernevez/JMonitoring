package org.jmonitoring.console.flow.jfreechart;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.jfree.chart.JFreeChart;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jmonitoring.console.flow.FlowBuilderUtil;
import org.jmonitoring.core.dao.PersistanceTestCase;
import org.jmonitoring.core.dto.DtoHelper;
import org.jmonitoring.core.dto.ExecutionFlowDTO;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestFlowDetailURLGenerator extends PersistanceTestCase
{
    public void testUGRGeneration()
    {

        ExecutionFlowDTO tFlow = new FlowBuilderUtil().buildAndSaveNewDto(2);

        FlowChartBarUtil tUtil = new FlowChartBarUtil(tFlow.getFirstMethodCall());
        tUtil.chainAllMethodCallToMainTaskOfGroup(tFlow.getFirstMethodCall());
        IntervalCategoryDataset tIntervalcategorydataset = tUtil.createDataset();
        tUtil.createGanttChart(tIntervalcategorydataset);
        // Plot tPlot = jfreechart.getPlot();

        FlowDetailURLGenerator tGenerator = new FlowDetailURLGenerator();
        String tUrl = tGenerator.generateURL(tIntervalcategorydataset, 0, 0);
        assertEquals("MethodCallEditIn.do?id=1", tUrl);
        assertEquals("MethodCallEditIn.do?id=1", tGenerator.generateURL(tIntervalcategorydataset, 0, 0));
        assertEquals("MethodCallEditIn.do?id=1", tGenerator.generateURL(tIntervalcategorydataset, 0, 0));
        assertEquals("MethodCallEditIn.do?id=2", tGenerator.generateURL(tIntervalcategorydataset, 0, 1));
        assertEquals("MethodCallEditIn.do?id=3", tGenerator.generateURL(tIntervalcategorydataset, 0, 1));

    }

}
