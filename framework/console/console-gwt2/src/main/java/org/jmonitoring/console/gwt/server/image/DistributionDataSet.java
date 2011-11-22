package org.jmonitoring.console.gwt.server.image;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.util.List;

import org.jfree.data.xy.AbstractIntervalXYDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jmonitoring.console.gwt.server.flow.Distribution;

/**
 * Specific <code>DataSet</code> use for the statics of the durations of same methods. This Class was write because the
 * FreeChart DataSet don't manage the duration consolidation.
 * 
 * @author pke
 */
public class DistributionDataSet extends AbstractIntervalXYDataset implements IntervalXYDataset
{
    private static final long serialVersionUID = 3257008765268996401L;

    List<Distribution> distribList;

    private final long interval;

    public DistributionDataSet(List<Distribution> pDistribList, long pInterval)
    { // TODO Cleaning
      // Map<Long, Integer> pMap
        distribList = pDistribList;
        interval = pInterval;
        // int tSize = pDistribList.size() + 1; // +1 ??
        // mDurations = new long[tSize];
        // Long tKey;
        // Integer tValue;
        // for (int i = 0; i < tSize; i++)
        // {
        // tKey = new Long(pGroupValue * i);
        // tValue = pMap.get(tKey);
        // mDurations[i] = (tValue == null ? 0 : tValue.intValue());
        // }
    }

    /**
     * @see org.jfree.data.general.AbstractSeriesDataset#getSeriesCount()
     */
    @Override
    public int getSeriesCount()
    {
        return 1;
    }

    /**
     * @see org.jfree.data.general.AbstractSeriesDataset#getSeriesName(int)
     */
    @Override
    public String getSeriesName(int pSeries)
    {
        return "durations (ms)";
    }

    /**
     * @see org.jfree.data.xy.IntervalXYDataset#getStartX(int, int)
     */
    public Number getStartX(int pSeries, int pItem)
    {
        return pItem * interval;
    }

    /**
     * @see org.jfree.data.xy.IntervalXYDataset#getEndX(int, int)
     */
    public Number getEndX(int pSeries, int pItem)
    {
        return (pItem + 1) * interval;
    }

    /**
     * @see org.jfree.data.xy.IntervalXYDataset#getEndY(int, int)
     */
    public Number getStartY(int pSeries, int pItem)
    {
        return distribList.get(pItem).numberOfOccurence;
    }

    /**
     * @see org.jfree.data.xy.IntervalXYDataset#getEndY(int, int)
     */
    public Number getEndY(int pSeries, int pItem)
    {
        return distribList.get(pItem).numberOfOccurence;
    }

    /**
     * @see org.jfree.data.xy.XYDataset#getItemCount(int)
     */
    public int getItemCount(int pSeries)
    {
        return distribList.size();
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
