package org.jmonitoring.console.gwt.server.image;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.jfree.data.xy.IntervalXYDataset;
import org.jmonitoring.console.gwt.server.flow.Distribution;
import org.junit.Test;

public class DistributionXYURLGeneratorTest extends TestCase
{

    @Test
    public void testGenerateURL()
    {

        // DistributionChartBarGenerator tChartGenerator = new DistributionChartBarGenerator();
        // Stats tStat = new Stats(2, 234, 50.4567, 1256).setStdDeviation(12.678809);
        List<Distribution> tDistribList = new ArrayList<Distribution>();
        tDistribList.add(new Distribution(56, 0));
        tDistribList.add(new Distribution(100, 50));
        tDistribList.add(new Distribution(200, 100));
        tDistribList.add(new Distribution(400, 150));
        tDistribList.add(new Distribution(500, 200));

        // MappedChart tMappedChart = tChartGenerator.generateDistributionChart(tDistribList, tStat, 50L);
        IntervalXYDataset tIntervalxydataset = new DistributionDataSet(tDistribList, 50L);

        DistributionXYURLGenerator tGenerator = new DistributionXYURLGenerator("theClass", "theMeth", 50L);
        assertEquals("javascript:window.methClick('theClass', 'theMeth', 0, 50);",
                     tGenerator.generateURL(tIntervalxydataset, 0, 0));
        assertEquals("javascript:window.methClick('theClass', 'theMeth', 50, 100);",
                     tGenerator.generateURL(tIntervalxydataset, 0, 1));
        assertEquals("javascript:window.methClick('theClass', 'theMeth', 100, 150);",
                     tGenerator.generateURL(tIntervalxydataset, 0, 2));
        assertEquals("javascript:window.methClick('theClass', 'theMeth', 150, 200);",
                     tGenerator.generateURL(tIntervalxydataset, 0, 3));
        assertEquals("javascript:window.methClick('theClass', 'theMeth', 200, 250);",
                     tGenerator.generateURL(tIntervalxydataset, 0, 4));

    }
}
