package org.jmonitoring.console.flow.jfreechart;

import java.util.List;

import org.jfree.chart.urls.CategoryURLGenerator;
import org.jfree.chart.urls.XYURLGenerator;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class FlowDetailURLGenerator implements CategoryURLGenerator
{

    private int mCurrentMainTaskPosition = -1;

    private int mCurrentPositionForThisTask = -1;

    private static Logger sLog = LoggerFactory.getLogger(FlowDetailURLGenerator.class);

    public FlowDetailURLGenerator()
    {
        super();
    }

    /**
     * (non-Javadoc)
     * 
     * @see XYURLGenerator#generateURL(org.jfree.data.xy.XYDataset, int, int)
     */
    @SuppressWarnings("unchecked")
    public String generateURL(CategoryDataset categorydataset, int i, int j)
    {
        if (j != mCurrentMainTaskPosition)
        { // Changent de group
            mCurrentPositionForThisTask = 0;
            mCurrentMainTaskPosition = j;
        } else
        {
            mCurrentPositionForThisTask++;
        }
        TaskSeriesCollection tCollection = (TaskSeriesCollection) categorydataset;
        List<TaskSeries> tListOfTaskSeries = tCollection.getRowKeys();
        TaskSeries tTaskSeries = tListOfTaskSeries.get(0);
        Task tMainTask = tTaskSeries.get(j);
        MethodCallTask tCurrentTask = (MethodCallTask) tMainTask.getSubtask(mCurrentPositionForThisTask);

        String tUrl =
            "MethodCallEditIn.do?flowId=" + tCurrentTask.getFlowId() + "&position=" + tCurrentTask.getMethodCallId();
        sLog.debug("Generate URL:" + tUrl);
        return tUrl;
    }
}
