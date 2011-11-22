package org.jmonitoring.console.gwt.server.image;

import java.io.Serializable;

import org.jfree.chart.urls.XYURLGenerator;
import org.jfree.data.xy.XYDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * @author pke
 * 
 *         Generate the URL link for the statistics char bar. Each link reference the list of the measures that have the
 *         same duration.
 */
public class DistributionXYURLGenerator implements XYURLGenerator, Serializable
{
    Logger sLog = LoggerFactory.getLogger(DistributionXYURLGenerator.class);

    private static final long serialVersionUID = 3478122271738115018L;

    /** The interval duration used for the char bar generation. */
    private final long interval;

    /** ClassName of the MethodCallDTO. */
    private final String className;

    /** Name of the MethodCallDTO method. */
    private final String methodName;

    /**
     * Default constructor.
     * 
     * @param pClassName ClassName of the MethodCallDTO.
     * @param pMethodName Name of the MethodCallDTO method.
     * @param pInterval The interval duration used for the char bar generation.
     */
    public DistributionXYURLGenerator(String pClassName, String pMethodname, long pInterval)
    {
        interval = pInterval;
        className = pClassName;
        methodName = pMethodname;
    }

    public String generateURL(XYDataset pDataSet, int pSeries, int pBarChartNumber)
    {
        StringBuilder tBuffer = new StringBuilder();
        tBuffer.append("javascript:window.methClick('");
        tBuffer.append(className).append("', '");
        tBuffer.append(methodName).append("', ");
        tBuffer.append(interval * pBarChartNumber).append(", ");
        tBuffer.append(interval * (pBarChartNumber + 1)).append(");");
        String tUrl = tBuffer.toString();
        sLog.debug("Generate URL:{}", tUrl);
        return tUrl;
    }

}
