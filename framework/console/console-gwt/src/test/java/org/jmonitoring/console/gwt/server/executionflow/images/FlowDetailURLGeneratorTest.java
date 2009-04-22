package org.jmonitoring.console.gwt.server.executionflow.images;

import javax.annotation.Resource;

import junit.framework.TestCase;

import org.jfree.data.category.IntervalCategoryDataset;
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
public class FlowDetailURLGeneratorTest extends TestCase
{
    @Resource(name = "formater")
    private FormaterBean mFormater;

    @Resource(name = "color")
    private ColorManager mColor;

    @Autowired
    private ChartManager mChartManager;

    @Test
    public void testUGRGeneration()
    {
        ExecutionFlowPO tFlow = buildNewDto(2);

        FlowChartBarUtil tUtil = new FlowChartBarUtil(mFormater, tFlow.getFirstMethodCall(), mColor, mChartManager);
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

    public ExecutionFlowPO buildNewDto(int pNbMethods)
    {
        ExecutionFlowPO tFlow;
        MethodCallPO tPoint;
        MethodCallPO tSubPoint;
        long currentTime = System.currentTimeMillis();

        tPoint = new MethodCallPO(null, FlowDetailURLGenerator.class.getName(), "builNewFullFlow", "GrDefault", "[]");
        tPoint.setBeginTime(currentTime);
        tFlow = new ExecutionFlowPO("TEST-main", tPoint, "myJVM");
        tFlow.setId(1);

        for (int i = 0; i < pNbMethods; i++)
        {
            tSubPoint =
                new MethodCallPO(tPoint, FlowDetailURLGenerator.class.getName(), "builNewFullFlow3", "GrChild2", "[]");
            tSubPoint.setBeginTime(currentTime + 1);
            currentTime = currentTime + 5;
            tSubPoint.setEndTime(currentTime);
            tSubPoint.setMethId(new MethodCallPK(tFlow, i + 2));
            tSubPoint.setFlow(tFlow);
        }
        tPoint.setEndTime(currentTime + 20);
        tPoint.setMethId(new MethodCallPK(tFlow, 1));
        tPoint.setFlow(tFlow);
        return tFlow;
    }
}
