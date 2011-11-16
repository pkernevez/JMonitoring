package org.jmonitoring.console.gwt.server.image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.xy.IntervalXYDataset;
import org.jmonitoring.console.gwt.server.flow.Distribution;
import org.jmonitoring.console.gwt.server.flow.Stats;
import org.jmonitoring.core.configuration.MeasureException;

public class DistributionChartGenerator
{
    public MappedChart generateDistributionChart(List<Distribution> tDistribList, Stats pStats, long pInterval)
    {
        IntervalXYDataset tIntervalxydataset = new DistributionDataSet(tDistribList, pInterval, pStats.max);

        JFreeChart tJFreeChart = createXYBarChart(tIntervalxydataset, pInterval);

        ChartRenderingInfo tChartRenderingInfo = new ChartRenderingInfo(new StandardEntityCollection());

        ByteArrayOutputStream tImageStream = new ByteArrayOutputStream();
        ByteArrayOutputStream tMapStream = new ByteArrayOutputStream();
        try
        {
            ChartUtilities.writeChartAsPNG(tImageStream, tJFreeChart, 800, 450, tChartRenderingInfo);
            // TODO regarder l'API avec FragmentURLGenerator...
            PrintWriter tWriter = new PrintWriter(tMapStream);
            ChartUtilities.writeImageMap(tWriter, "chart", tChartRenderingInfo);
            tWriter.flush();
            MappedChart tResult = new MappedChart();
            tResult.image = tImageStream.toByteArray();
            tResult.map = tMapStream.toString();
            return tResult;
        } catch (IOException e)
        {
            throw new MeasureException("Unable to write Image", e);
        }
        // pSession.setAttribute(FULL_DURATION_STAT, tImageStream.toByteArray());
        // sLog.debug("Image " + FULL_DURATION_STAT + " add to session");
    }

    private static JFreeChart createXYBarChart(IntervalXYDataset dataset, long pInterval)
    {
        NumberAxis tAxis = new NumberAxis("duration (ms)");
        tAxis.setAutoRangeIncludesZero(false);
        ValueAxis tValueAxis = new NumberAxis("number of occurences");

        XYBarRenderer renderer = new XYBarRenderer();
        renderer.setToolTipGenerator(new StandardXYToolTipGenerator());
        // TODO
        // renderer.setURLGenerator(new DistributionXYURLGenerator("MethodCallListIn.do", pForm.getInterval(),
        // pForm.getClassName(), pForm.getMethodName()));

        XYPlot plot = new XYPlot(dataset, tAxis, tValueAxis, renderer);
        plot.setOrientation(PlotOrientation.VERTICAL);

        JFreeChart chart = new JFreeChart("Full duration statistics", JFreeChart.DEFAULT_TITLE_FONT, plot, false);

        return chart;
    }

    // IntervalXYDataset createFullDurationDataset(List<Distribution> pDistribList, Stats pStats, int pInterval)
    // {
    // Map<Long, Integer> tMap = new HashMap<Long, Integer>();
    // Integer tCurNb;
    // Long tCurDurationAsLong;
    // long tCurDuration;
    // long tDurationMax = 0;
    // for (int i = 0; i < pMeasures.size(); i++)
    // {
    // tCurDuration = pMeasures.get(i).getDuration();
    // if (tCurDuration > tDurationMax)
    // {
    // tDurationMax = tCurDuration;
    // }
    // // Around the duration with duration groupvalue
    // tCurDuration = (tCurDuration / pInterval) * pInterval;
    // tCurDurationAsLong = new Long(tCurDuration);
    // tCurNb = tMap.get(tCurDurationAsLong);
    // if (tCurNb != null)
    // {
    // tCurNb = tCurNb.intValue() + 1;
    // } else
    // {
    // tCurNb = 1;
    // }
    // tMap.put(tCurDurationAsLong, tCurNb);
    // }
    // return new DistributionDataSet(tMap, pInterval, tDurationMax);
    // }

}
