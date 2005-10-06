package org.jmonitoring.console.flow.jfreechart;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.awt.Font;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.jmonitoring.core.configuration.Configuration;
import org.jmonitoring.core.measure.MeasureException;
import org.jmonitoring.core.measure.MeasurePoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieItemLabelGenerator;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.DrawingSupplier;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.urls.StandardPieURLGenerator;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

/**
 * @author pke
 */
public class FlowUtil
{
    
    /** Constant used for the URL generation of the PieChart representing the number of calls.*/
    public static final String NB_CALL_TO_GROUP = "NB_CALL_TO_GROUP";

    /** Constant used for the URL generation of the PieChart representing the duration of calls.*/
    public static final String DURATION_IN_GROUP = "DURATION_IN_GROUP";

    private static Log sLog = LogFactory.getLog(FlowUtil.class);

    private Map mListOfGroup = new HashMap();

    /**
     * Generate 2 Pie Charts for the statistics of the flow and write them in the session.
     * The fisrt is the repartition of the number of call and the other is the repertition of 
     * the duration.
     * @param pSession The session to use for the image writing as a bytes arrays.
     * @param pFirstMeasure The root of the <code>MeasurePoint</code> tree.
     */
    public static void writeImageIntoSession(HttpSession pSession, MeasurePoint pFirstMeasure)
    {
        FlowUtil tFlow = new FlowUtil();
        tFlow.addTimeWith(pFirstMeasure);

        DefaultPieDataset dataset = new DefaultPieDataset();
        String curValue;
        Paint[] tColors = new Paint[tFlow.mListOfGroup.size()];
        int tPos = 0;
        for (Iterator tIter = tFlow.mListOfGroup.keySet().iterator(); tIter.hasNext();)
        {
            curValue = (String) tIter.next();
            dataset.setValue(curValue, ((Long) tFlow.mListOfGroup.get(curValue)).longValue());
            tColors[tPos] = (Paint) Configuration.getInstance().getColor(curValue);
            tPos++;
        }
        DefaultDrawingSupplier tSupplier = new DefaultDrawingSupplier(tColors, new Paint[0], new Stroke[0],
                        new Stroke[0], new Shape[0]);

        JFreeChart chart = createPieChart("Duration in group", // chart title
                        dataset, // data
                        tSupplier, true, // include legend
                        true, false);
        addChart(chart, pSession, DURATION_IN_GROUP);

        // Maintenant image par nb d'appel
        tFlow.mListOfGroup = new HashMap();
        tFlow.addNbCallWith(pFirstMeasure);
        dataset = new DefaultPieDataset();
        for (Iterator tIter = tFlow.mListOfGroup.keySet().iterator(); tIter.hasNext();)
        {
            curValue = (String) tIter.next();
            dataset.setValue(curValue, ((Integer) tFlow.mListOfGroup.get(curValue)).intValue());
        }
        chart = createPieChart("Nb call of group", // chart title
                        dataset, // data
                        tSupplier, true, // include legend
                        true, false);
        addChart(chart, pSession, NB_CALL_TO_GROUP);

    }

    /**
     * Return the while list of group managed by this Util class.
     * @return The list of the group name (<code>Map</code> of <code>String</code>.
     */
    Map getListOfGroup()
    {
        return mListOfGroup;
    }

     /**
      * Write a chart into a session with a name.
      * @param pChart The chart to write in the session.
      * @param pSession The user Session. 
      * @param pName The name of the attributes to use for the image.
      */
    private static void addChart(JFreeChart pChart, HttpSession pSession, String pName)
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
        } catch (IOException e)
        {
            throw new MeasureException("Unable to write Image", e);
        }
        pSession.setAttribute(pName, tStream.toByteArray());
        sLog.debug("Image " + pName + " add to session");
    }

    /**
     * Append the duration passed in this <code>MeasurePoint</code>. 
     * @param pMeasure The current measure.
     */
    void addTimeWith(MeasurePoint pMeasure)
    {
        long tChildDuration = 0;
        MeasurePoint curPoint;
        // On itère sur les noeuds fils
        for (int i = 0; i < pMeasure.getChildren().size(); i++)
        {
            curPoint = (MeasurePoint) pMeasure.getChildren().get(i);
            addTimeWith(curPoint);
            tChildDuration = tChildDuration + (curPoint.getEndTime() - curPoint.getBeginTime());
        }
        String tGroupName = pMeasure.getGroupName();
        Long tDuration = (Long) mListOfGroup.get(tGroupName);
        long tLocalDuration = pMeasure.getEndTime() - pMeasure.getBeginTime() - tChildDuration;
        if (tDuration != null)
        { // On ajoute la durée en cours
            long tLong = tDuration.longValue();
            tLocalDuration = tLocalDuration + tLong;
        }
        mListOfGroup.put(tGroupName, new Long(tLocalDuration));
    }

    /**
     * Append the number of calls passed in this <code>MeasurePoint</code>. 
     * @param pMeasure The current measure.
     */
    void addNbCallWith(MeasurePoint pMeasure)
    {
        String tGroupName = pMeasure.getGroupName();
        Integer tNbCall = (Integer) mListOfGroup.get(tGroupName);
        if (tNbCall == null)
        { // Nouveau groupe ou l'ajoute
            mListOfGroup.put(tGroupName, new Integer(1));
        } else
        { // On ajoute la durée en cours
            mListOfGroup.put(tGroupName, new Integer(tNbCall.intValue() + 1));
        }
        // On itère sur les noeuds fils
        for (int i = 0; i < pMeasure.getChildren().size(); i++)
        {
            addNbCallWith((MeasurePoint) pMeasure.getChildren().get(i));
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
