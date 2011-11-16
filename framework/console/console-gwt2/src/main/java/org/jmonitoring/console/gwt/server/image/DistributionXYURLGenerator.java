package org.jmonitoring.console.gwt.server.image;

import java.io.Serializable;

import org.jfree.chart.urls.XYURLGenerator;
import org.jfree.data.xy.XYDataset;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * @author pke
 * 
 * Generate the URL link for the statistics char bar. Each link reference the list of the measures that have the same
 * duration.
 */
public class DistributionXYURLGenerator implements XYURLGenerator, Serializable
{
    private static final long serialVersionUID = 3478122271738115018L;

    /** The interval duration used for the char bar generation. */
    private final int mInterval;

    /** The URL of the Html link. */
    private final String mBaseURL;

    /** ClassName of the MethodCallDTO. */
    private final String mClassName;

    /** Name of the MethodCallDTO method. */
    private final String mMethodName;

    /**
     * Default constructor.
     * 
     * @param pBaseURL The URL of the Html link.
     * @param pInterval The interval duration used for the char bar generation.
     * @param pClassName ClassName of the MethodCallDTO.
     * @param pMethodName Name of the MethodCallDTO method.
     */
    public DistributionXYURLGenerator(String pBaseURL, int pInterval, String pClassName, String pMethodname)
    {
        mInterval = pInterval;
        mBaseURL = pBaseURL;
        mClassName = pClassName;
        mMethodName = pMethodname;
    }

    public String generateURL(XYDataset pDataSet, int pSeries, int pBarChartNumber)
    {
        StringBuilder tBuffer = new StringBuilder();
        tBuffer.append(mBaseURL).append("?className=").append(mClassName);
        tBuffer.append("&methodName=").append(mMethodName);
        tBuffer.append("&durationMin=").append(mInterval * pBarChartNumber);
        tBuffer.append("&durationMax=").append(mInterval * (pBarChartNumber + 1));
        return tBuffer.toString();
    }

}
