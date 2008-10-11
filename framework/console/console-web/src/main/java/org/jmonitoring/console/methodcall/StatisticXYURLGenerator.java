package org.jmonitoring.console.methodcall;

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
public class StatisticXYURLGenerator implements XYURLGenerator, Serializable {
    private static final long serialVersionUID = 3478122271738115018L;

    /** The interval duration used for the char bar generation. */
    private int mInterval;

    /** The URL of the Html link. */
    private String mBaseURL;

    /** ClassName of the MethodCallDTO. */
    private String mClassName;

    /** Name of the MethodCallDTO method. */
    private String mMethodName;

    /**
     * Default constructor.
     * 
     * @param pBaseURL The URL of the Html link.
     * @param pInterval The interval duration used for the char bar generation.
     * @param pClassName ClassName of the MethodCallDTO.
     * @param pMethodName Name of the MethodCallDTO method.
     */
    public StatisticXYURLGenerator(String pBaseURL, int pInterval, String pClassName, String pMethodname) {
        mInterval = pInterval;
        mBaseURL = pBaseURL;
        mClassName = pClassName;
        mMethodName = pMethodname;
    }

    /**
     * (non-Javadoc)
     * 
     * @see XYURLGenerator#generateURL(org.jfree.data.xy.XYDataset, int, int)
     */
    public String generateURL(XYDataset pDataSet, int pSeries, int pBarChartNumber) {
        StringBuffer tBuffer = new StringBuffer();
        tBuffer.append(mBaseURL).append("?className=").append(mClassName);
        tBuffer.append("&methodName=").append(mMethodName);
        tBuffer.append("&durationMin=").append(mInterval * pBarChartNumber);
        tBuffer.append("&durationMax=").append(mInterval * (pBarChartNumber + 1));
        return tBuffer.toString();
    }

}
