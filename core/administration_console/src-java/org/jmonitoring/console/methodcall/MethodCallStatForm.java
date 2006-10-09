package org.jmonitoring.console.methodcall;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * Struts form associated with the generation of the statistics of the MethodCallDTO.
 * 
 * @author pke
 */
public class MethodCallStatForm extends AbstractMethodCallForm
{
    private long mDurationMin;

    private long mDurationMax;

    private double mDurationAvg;

    private double mDurationDev;

    private String mImageMap;

    /** The value to use to group duration values for statistics. */
    private int mInterval = -1;

    private int mNbMeasures;

    /**
     * @return Returns the nbMeasure.
     */
    public int getNbMeasures()
    {
        return mNbMeasures;
    }

    /**
     * @param pNbMeasures The nbMeasure to set.
     */
    public void setNbMeasures(int pNbMeasures)
    {
        mNbMeasures = pNbMeasures;
    }

    /**
     * @return Returns the durationAvg.
     */
    public double getDurationAvg()
    {
        return mDurationAvg;
    }

    /**
     * @param pDurationAvg The durationAvg to set.
     */
    public void setDurationAvg(double pDurationAvg)
    {
        mDurationAvg = pDurationAvg;
    }

    /**
     * @return Returns the durationDev.
     */
    public double getDurationDev()
    {
        return mDurationDev;
    }

    /**
     * @param pDurationDev The durationDev to set.
     */
    public void setDurationDev(double pDurationDev)
    {
        mDurationDev = pDurationDev;
    }

    /**
     * @return Returns the durationMax.
     */
    public long getDurationMax()
    {
        return mDurationMax;
    }

    /**
     * @param pDurationMax The durationMax to set.
     */
    public void setDurationMax(long pDurationMax)
    {
        mDurationMax = pDurationMax;
    }

    /**
     * @return Returns the durationMin.
     */
    public long getDurationMin()
    {
        return mDurationMin;
    }

    /**
     * @param pDurationMin The durationMin to set.
     */
    public void setDurationMin(long pDurationMin)
    {
        mDurationMin = pDurationMin;
    }

    /**
     * Accessor.
     * 
     * @return The number of milliseconds to use for the statistics consolidation.
     */
    public int getInterval()
    {
        return mInterval;
    }

    /**
     * Accessor.
     * 
     * @param pInterval The number of milliseconds to use for the statistics consolidation.
     */
    public void setInterval(int pInterval)
    {
        mInterval = pInterval;
    }

    /**
     * @return Returns the imageMap.
     */
    public String getImageMap()
    {
        return mImageMap;
    }

    /**
     * @param pImageMap The imageMap to set.
     */
    public void setImageMap(String pImageMap)
    {
        mImageMap = pImageMap;
    }
}
