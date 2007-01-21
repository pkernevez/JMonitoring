package org.jmonitoring.console.flow.jfreechart;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.urls.CategoryURLGenerator;
import org.jfree.chart.urls.XYURLGenerator;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class FlowDetailURLGenerator implements CategoryURLGenerator
{

    private int mCurrentMainTaskPosition = -1;

    private int mCurrentPositionForThisTask = -1;

    private static Log sLog = LogFactory.getLog(FlowDetailURLGenerator.class);

    public FlowDetailURLGenerator()
    {
        super();
    }

    /**
     * (non-Javadoc)
     * 
     * @see XYURLGenerator#generateURL(org.jfree.data.xy.XYDataset, int, int)
     */
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
        List tListOfTaskSeries = tCollection.getRowKeys();
        TaskSeries tTaskSeries = (TaskSeries) tListOfTaskSeries.get(0);
        Task tMainTask = (Task) tTaskSeries.get(j);
        MethodCallTask tCurrentTask = (MethodCallTask) tMainTask.getSubtask(mCurrentPositionForThisTask);

        String tUrl = "MethodCallEditIn.do?flowId=" + tCurrentTask.getFlowId() + "&position="
            + tCurrentTask.getMethodCallId();
        sLog.debug("Generate URL:" + tUrl);
        return tUrl;
    }
}
