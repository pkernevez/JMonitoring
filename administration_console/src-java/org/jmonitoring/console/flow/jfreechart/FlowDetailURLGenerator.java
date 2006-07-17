package org.jmonitoring.console.flow.jfreechart;

import org.jfree.chart.urls.XYURLGenerator;
import org.jfree.data.xy.XYDataset;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class FlowDetailURLGenerator implements XYURLGenerator
{

    /** The URL of the Html link. */
    private String mBaseURL;

    /**
     * (non-Javadoc)
     * 
     * @see XYURLGenerator#generateURL(org.jfree.data.xy.XYDataset, int, int)
     */
    public String generateURL(XYDataset pDataSet, int pSeries, int pBarChartNumber)
    {
        StringBuffer tBuffer = new StringBuffer();
        // tBuffer.append(mBaseURL).append("?className=").append(mClassName);
        // tBuffer.append("&methodName=").append(mMethodName);
        // tBuffer.append("&durationMin=").append(mInterval * pBarChartNumber);
        // tBuffer.append("&durationMax=").append(mInterval * (pBarChartNumber + 1));
        return tBuffer.toString();
    }

}
