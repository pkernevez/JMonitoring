package org.jmonitoring.console.gwt.server.image;

import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.data.category.CategoryDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlowDetailToolTipGenerator extends FlowDetailGenerator implements CategoryToolTipGenerator
{
    private static Logger sLog = LoggerFactory.getLogger(FlowDetailToolTipGenerator.class);

    /** {@inheritDoc} */
    public String generateToolTip(CategoryDataset categorydataset, int i, int j)
    {
        MethodCallTask tCurrentTask = getCurrentTask(categorydataset, j);

        String tTitle =
            "goto (flowId=" + tCurrentTask.getFlowId() + ", position=" + tCurrentTask.getMethodCallId() + ")";
        sLog.debug("Generate URL:{}", tTitle);
        return tTitle;
    }

}
