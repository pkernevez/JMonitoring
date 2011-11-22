package org.jmonitoring.console.gwt.server.image;

import java.awt.Font;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieItemLabelGenerator;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.DrawingSupplier;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.urls.StandardPieURLGenerator;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jmonitoring.console.gwt.server.common.ColorManager;
import org.jmonitoring.core.configuration.MeasureException;
import org.jmonitoring.core.domain.MethodCallPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlowDetailPieChartGenerator
{
    /** Constant used for the URL generation of the PieChart representing the number of calls. */
    public static final String NB_CALL_TO_GROUP = "NB_CALL_TO_GROUP";

    /** Constant used for the URL generation of the PieChart representing the duration of calls. */
    public static final String DURATION_IN_GROUP = "DURATION_IN_GROUP";

    private static Logger sLog = LoggerFactory.getLogger(FlowDetailPieChartGenerator.class);

    Map<String, Integer> listOfGroup = new HashMap<String, Integer>();

    private final ColorManager colorMgr;

    public FlowDetailPieChartGenerator(ColorManager pColorMgr)
    {
        super();
        colorMgr = pColorMgr;
    }

    /**
     * Generate 2 Pie Charts for the statistics of the flow and write them in the session. The fisrt is the repartition
     * of the number of call and the other is the repertition of the duration.
     * 
     * @param pSession The session to use for the image writing as a bytes arrays.
     * @param pFirstMeasure The root of the <code>MethodCallDTO</code> tree.
     */
    public byte[] getDurationInGroup(MethodCallPO pFirstMeasure)
    {
        sLog.info("getDurationInGoup for {}", pFirstMeasure);
        addTimeWith(pFirstMeasure);
        DefaultPieDataset dataset = new DefaultPieDataset();
        Paint[] tColors = new Paint[listOfGroup.size()];
        int tPos = 0;
        for (Map.Entry<String, Integer> curEntry : listOfGroup.entrySet())
        {
            String tKey = curEntry.getKey();
            dataset.setValue(tKey, curEntry.getValue());
            tColors[tPos] = colorMgr.getColor(tKey);
            tPos++;
        }
        DefaultDrawingSupplier tSupplier =
            new DefaultDrawingSupplier(tColors, new Paint[0], new Stroke[0], new Stroke[0], new Shape[0]);

        JFreeChart chart = createPieChart("Duration in group", // chart title
                                          dataset, // data
                                          tSupplier, true, // include legend
                                          true, false);
        return getByte(chart);
    }

    private byte[] getByte(JFreeChart pChart)
    {
        PiePlot tPlot = (PiePlot) pChart.getPlot();
        tPlot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        tPlot.setNoDataMessage("No data available");
        tPlot.setCircular(false);
        tPlot.setLabelGap(0.02);

        ByteArrayOutputStream tStream = new ByteArrayOutputStream();
        try
        {
            ChartUtilities.writeChartAsPNG(tStream, pChart, 460, 360);
            return tStream.toByteArray();
        } catch (IOException e)
        {
            sLog.error("Unable to write Image", e);
            throw new MeasureException("Unable to write Image", e);
        }
    }

    /**
     * Generate 2 Pie Charts for the statistics of the flow and write them in the session. The fisrt is the repartition
     * of the number of call and the other is the repertition of the duration.
     * 
     * @param pSession The session to use for the image writing as a bytes arrays.
     * @param pFirstMeasure The root of the <code>MethodCallDTO</code> tree.
     */
    public byte[] getGroupCalls(MethodCallPO pFirstMeasure)
    {
        DefaultPieDataset dataset = new DefaultPieDataset();
        addNbCallWith(pFirstMeasure);
        Paint[] tColors = new Paint[listOfGroup.size()];
        dataset = new DefaultPieDataset();
        int tPos = 0;
        for (Map.Entry<String, Integer> curEntry : listOfGroup.entrySet())
        {
            String tKey = curEntry.getKey();
            dataset.setValue(tKey, curEntry.getValue());
            tColors[tPos] = colorMgr.getColor(tKey);
            tPos++;
        }
        DefaultDrawingSupplier tSupplier =
            new DefaultDrawingSupplier(tColors, new Paint[0], new Stroke[0], new Stroke[0], new Shape[0]);

        JFreeChart chart = createPieChart("Nb call of group", // chart title
                                          dataset, // data
                                          tSupplier, true, // include legend
                                          true, false);
        return getByte(chart);
    }

    /**
     * Append the duration passed in this <code>MethodCallDTO</code>.
     * 
     * @param pMeasure The current measure.
     */
    void addTimeWith(MethodCallPO pMeasure)
    {
        long tChildDuration = 0;

        for (MethodCallPO tChild : pMeasure.getChildren())
        {
            addTimeWith(tChild);
            long tEndTime = tChild.getEndTime();
            long tBeginTime = tChild.getBeginTime();
            tChildDuration = tChildDuration + (tEndTime - tBeginTime);
        }
        String tGroupName = pMeasure.getGroupName();
        Integer tDuration = listOfGroup.get(tGroupName);
        long tEndTime = pMeasure.getEndTime();
        long tBeginTime = pMeasure.getBeginTime();
        int tLocalDuration = (int) (tEndTime - tBeginTime - tChildDuration);
        if (tDuration != null)
        { // On ajoute la dur�e en cours
            tLocalDuration = tLocalDuration + tDuration;
        }
        listOfGroup.put(tGroupName, tLocalDuration);
    }

    /**
     * Append the number of calls passed in this <code>MethodCallDTO</code>.
     * 
     * @param pMeasure The current measure.
     */
    void addNbCallWith(MethodCallPO pMeasure)
    {
        String tGroupName = pMeasure.getGroupName();
        Integer tNbCall = listOfGroup.get(tGroupName);
        int tNbCallInt = (tNbCall == null ? 0 : tNbCall.intValue());
        listOfGroup.put(tGroupName, Integer.valueOf(tNbCallInt + 1));
        for (MethodCallPO tChild : pMeasure.getChildren())
        {
            addNbCallWith(tChild);
        }
    }

    /**
     * Creates a pie chart with default settings.
     * <P>
     * The chart object returned by this method uses a {@link PiePlot} instance as the plot.
     * 
     * @param pTitle the chart title (<code>null</code> permitted).
     * @param pDataset the dataset for the chart (<code>null</code> permitted).
     * @param pLegend a flag specifying whether or not a legend is required.
     * @param pTooltips configure chart to generate tool tips?
     * @param pSupplier The supplier.
     * @param pUrls configure chart to generate URLs?
     * 
     * @return A pie chart.
     */
    public static JFreeChart createPieChart(String pTitle, PieDataset pDataset, DrawingSupplier pSupplier,
        boolean pLegend, boolean pTooltips, boolean pUrls)
    {

        // @todo Tranformer en FlowPiePlot
        // PiePlot plot = new FlowPiePlot(dataset);
        PiePlot plot = new PiePlot(pDataset);
        plot.setLabelGenerator(new StandardPieItemLabelGenerator());
        plot.setInsets(new Insets(0, 5, 5, 5));
        plot.setDrawingSupplier(pSupplier);
        if (pTooltips)
        {
            plot.setToolTipGenerator(new StandardPieItemLabelGenerator(
                                                                       StandardPieItemLabelGenerator.DEFAULT_SECTION_LABEL_FORMAT));
        }
        if (pUrls)
        {
            plot.setURLGenerator(new StandardPieURLGenerator());
        }
        return new JFreeChart(pTitle, JFreeChart.DEFAULT_TITLE_FONT, plot, pLegend);

    }
}
