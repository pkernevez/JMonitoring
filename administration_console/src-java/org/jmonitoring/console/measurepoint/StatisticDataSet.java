package org.jmonitoring.console.measurepoint;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.util.Map;

import org.jfree.data.xy.AbstractIntervalXYDataset;
import org.jfree.data.xy.IntervalXYDataset;

/**
 * Specific <code>DataSet</code> use for the statics of the durations of same methods. This Class was write because
 * the FreeChart DataSet don't manage the duration consolidation.
 * 
 * @author pke
 */
public class StatisticDataSet extends AbstractIntervalXYDataset implements IntervalXYDataset
{
    private static final long serialVersionUID = 3257008765268996401L;

    private long[] mDurations;

    private int mGroupValue;

    /**
     * Default constructor.
     * 
     * @param pMap The list of all the duration.
     * @param pGroupValue The duration to use for the consolidation.
     * @param pDurationMax The maximum duration of this list.
     */
    public StatisticDataSet(Map pMap, int pGroupValue, long pDurationMax)
    {
        mGroupValue = pGroupValue;
        int tSize = ((int) pDurationMax / pGroupValue) + 1;
        mDurations = new long[tSize];
        Long tKey;
        Integer tValue;
        for (int i = 0; i < tSize; i++)
        {
            tKey = new Long(pGroupValue * i);
            tValue = ((Integer) pMap.get(tKey));
            mDurations[i] = (tValue == null ? 0 : tValue.intValue());
        }
    }

    /**
     * @see org.jfree.data.general.AbstractSeriesDataset#getSeriesCount()
     */
    public int getSeriesCount()
    {
        return 1;
    }

    /**
     * @see org.jfree.data.general.AbstractSeriesDataset#getSeriesName(int)
     */
    public String getSeriesName(int pSeries)
    {
        return "durations (ms)";
    }

    /**
     * @see org.jfree.data.xy.IntervalXYDataset#getStartX(int, int)
     */
    public Number getStartX(int pSeries, int pItem)
    {
        return new Integer(pItem * mGroupValue);
    }

    /**
     * @see org.jfree.data.xy.IntervalXYDataset#getEndX(int, int)
     */
    public Number getEndX(int pSeries, int pItem)
    {
        return new Integer((pItem + 1) * mGroupValue);
    }

    /**
     * @see org.jfree.data.xy.IntervalXYDataset#getEndY(int, int)
     */
    public Number getStartY(int pSeries, int pItem)
    {
        return new Long(mDurations[pItem]);
    }

    /**
     * @see org.jfree.data.xy.IntervalXYDataset#getEndY(int, int)
     */
    public Number getEndY(int pSeries, int pItem)
    {
        return new Long(mDurations[pItem]);
    }

    /**
     * @see org.jfree.data.xy.XYDataset#getItemCount(int)
     */
    public int getItemCount(int pSeries)
    {
        return mDurations.length;
    }

    /**
     * @see org.jfree.data.xy.XYDataset#getX(int, int)
     */
    public Number getX(int pSeries, int pItem)
    {
        return getStartX(pSeries, pItem);
    }

    /**
     * @see org.jfree.data.xy.XYDataset#getY(int, int)
     */
    public Number getY(int pSeries, int pItem)
    {
        return getStartY(pSeries, pItem);
    }

}
