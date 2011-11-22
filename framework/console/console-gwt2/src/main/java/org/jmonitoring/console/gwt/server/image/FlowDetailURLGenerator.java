package org.jmonitoring.console.gwt.server.image;

import org.jfree.chart.urls.CategoryURLGenerator;
import org.jfree.data.category.CategoryDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class FlowDetailURLGenerator extends FlowDetailGenerator implements CategoryURLGenerator
{

    private static Logger sLog = LoggerFactory.getLogger(FlowDetailURLGenerator.class);

    public FlowDetailURLGenerator()
    {
        super();
    }

    public String generateURL(CategoryDataset categorydataset, int i, int j)
    {
        MethodCallTask tCurrentTask = getCurrentTask(categorydataset, j);

        String tUrl =
            "javascript:window.methClick(" + tCurrentTask.getFlowId() + "," + tCurrentTask.getMethodCallId() + ");";
        sLog.debug("Generate URL:{}", tUrl);
        return tUrl;
    }

}
