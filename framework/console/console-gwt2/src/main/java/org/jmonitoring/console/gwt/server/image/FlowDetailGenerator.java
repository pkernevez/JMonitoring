package org.jmonitoring.console.gwt.server.image;

import java.util.List;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;

public class FlowDetailGenerator
{

    protected int currentMainTaskPosition = -1;

    protected int currentPositionForThisTask = -1;

    public FlowDetailGenerator()
    {
        super();
    }

    @SuppressWarnings("unchecked")
    protected MethodCallTask getCurrentTask(CategoryDataset categorydataset, int j)
    {
        if (j != currentMainTaskPosition)
        { // Changent de group
            currentPositionForThisTask = 0;
            currentMainTaskPosition = j;
        } else
        {
            currentPositionForThisTask++;
        }
        TaskSeriesCollection tCollection = (TaskSeriesCollection) categorydataset;
        List<TaskSeries> tListOfTaskSeries = tCollection.getRowKeys();
        TaskSeries tTaskSeries = tListOfTaskSeries.get(0);
        Task tMainTask = tTaskSeries.get(j);
        MethodCallTask tCurrentTask = (MethodCallTask) tMainTask.getSubtask(currentPositionForThisTask);
        return tCurrentTask;
    }
}