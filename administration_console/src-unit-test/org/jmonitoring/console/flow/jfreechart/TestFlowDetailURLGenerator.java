package org.jmonitoring.console.flow.jfreechart;

import junit.framework.TestCase;

import org.jfree.data.category.IntervalCategoryDataset;
import org.jmonitoring.console.flow.FlowBuilderUtil;
import org.jmonitoring.core.dto.DtoHelper;
import org.jmonitoring.core.dto.ExecutionFlowDTO;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestFlowDetailURLGenerator extends TestCase
{
    public void testUGRGeneration()
    {

        ExecutionFlowDTO tFlow = DtoHelper.getDeepCopy(new FlowBuilderUtil().buildNewFullFlow(2));

        FlowChartBarUtil tUtil = new FlowChartBarUtil();
        tUtil.fillListOfGroup(tFlow.getFirstMethodCall());
        IntervalCategoryDataset tIntervalcategorydataset = tUtil.createDataset();
        // JFreeChart jfreechart = tUtil.createChart(intervalcategorydataset);
        // Plot tPlot = jfreechart.getPlot();

        FlowDetailURLGenerator tGenerator = new FlowDetailURLGenerator();
        // String tUrl = tGenerator.generateURL(tIntervalcategorydataset, 0, 0);
        // assertEquals("/MethodCallEditIn.do?flowId=4&id=100", tUrl);
    }
}
