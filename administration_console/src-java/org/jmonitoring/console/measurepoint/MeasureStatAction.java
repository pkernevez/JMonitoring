package org.jmonitoring.console.measurepoint;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jmonitoring.core.configuration.Configuration;
import org.jmonitoring.core.dao.ExecutionFlowMySqlDAO;
import org.jmonitoring.core.dao.StandAloneConnectionManager;
import org.jmonitoring.core.measure.MeasureException;
import org.jmonitoring.core.measure.MeasurePoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
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

/**
 * @author pke
 * @todo refactor this class into a JFreeChart / ChartBarUtil class
 */
public class MeasureStatAction extends Action
{

    private static final int NB_DEFAULT_INTERVAL_VALUE = 50;

    private static final int INTERVAL_MULTIPLE_VALUE = 5;

    /** Internal name of the full duration statistic image. */
    public static final String FULL_DURATION_STAT = "FULL_DURATION_STAT";

    /** Logger. */
    private static Log sLog = LogFactory.getLog(MeasureStatAction.class);

    /**
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                    HttpServletResponse pResponse) throws Exception
    {
        MeasurePointStatForm tForm = (MeasurePointStatForm) pForm;
        MeasurePoint[] tMeasures = readMeasure(tForm);
        tForm.setNbMeasures(tMeasures.length);
        writeFullDurationStat(pRequest.getSession(), tMeasures, tForm);
        computeStat(tMeasures, tForm);
        return pMapping.findForward("success");
    }

    private MeasurePoint[] readMeasure(MeasurePointStatForm pForm)
    {
        try
        {
            Connection tConnection = new StandAloneConnectionManager(Configuration.getInstance()).getConnection();
            ExecutionFlowMySqlDAO tDao = new ExecutionFlowMySqlDAO(tConnection);
            if (!pForm.isParametersByName())
            { // First retreive className and methodName from the given MeasurePoint
                MeasurePoint tOriginalMeasure = tDao.readMeasurePoint(pForm.getFlowId(), pForm.getSequenceId());
                pForm.setClassName(tOriginalMeasure.getClassName());
                pForm.setMethodName(tOriginalMeasure.getMethodName());
            }
            return tDao.getListOfMeasurePoint(pForm.getClassName(), pForm.getMethodName());
        } catch (SQLException e)
        {
            throw new MeasureException("Unable to read measure", e);
        }
    }

    private void computeStat(MeasurePoint[] pMeasures, MeasurePointStatForm pForm)
    {
        if (pMeasures.length != 0)
        {
            MeasurePoint curMeasure = pMeasures[0];
            long tMin = curMeasure.getDuration(), tMax = tMin, tSum = tMin;
            for (int i = 1; i < pMeasures.length; i++)
            {
                curMeasure = pMeasures[i];
                tSum += curMeasure.getDuration();
                if (curMeasure.getDuration() > tMax)
                {
                    tMax = curMeasure.getDuration();
                } else if (curMeasure.getDuration() < tMin)
                {
                    tMin = curMeasure.getDuration();
                }
            }
            double tAvg = 0;
            tAvg = (double) tSum / (double) pMeasures.length;
            // Standard Deviation
            double tDelta, tVar = 0;
            for (int i = 0; i < pMeasures.length; i++)
            {
                curMeasure = pMeasures[i];
                tDelta = tAvg - curMeasure.getDuration();
                tVar += tDelta * tDelta;
            }
            pForm.setDurationAvg(tAvg);
            pForm.setDurationMin(tMin);
            pForm.setDurationMax(tMax);
            pForm.setDurationDev(Math.sqrt(tVar / pMeasures.length));
        }
    }

    private int computeIntervalValue(MeasurePoint[] pMeasures, MeasurePointStatForm pForm)
    {
        int tIntervalValue = pForm.getInterval();
        if (tIntervalValue <= 0)
        { //The Interval has not be specified explicitly
            long tDurationMax = 0, curDuration;
            // Get duration max
            for (int i = 0; i < pMeasures.length; i++)
            {
                curDuration = pMeasures[i].getEndTime() - pMeasures[i].getBeginTime();
                if (curDuration > tDurationMax)
                {
                    tDurationMax = curDuration;
                }
            }
            int tInterval = (int) tDurationMax / NB_DEFAULT_INTERVAL_VALUE;
            // We want an interval multiple of 5
            tInterval = tInterval / INTERVAL_MULTIPLE_VALUE * INTERVAL_MULTIPLE_VALUE;
            tIntervalValue = Math.max(tInterval, 1);
            pForm.setInterval(tIntervalValue);
        }
        return tIntervalValue;
    }

    /**
     * Write the image of statistic duration image into session.
     * 
     * @param pSession The http session.
     * @param pMeasures The list of <code>MeasurePoint</code> to use for image generation.
     * @param pForm The form associated to this Action.
     */
    public void writeFullDurationStat(HttpSession pSession, MeasurePoint[] pMeasures, MeasurePointStatForm pForm)
    {
        int tInterval = computeIntervalValue(pMeasures, pForm);
        IntervalXYDataset tIntervalxydataset = createFullDurationDataset(pMeasures, tInterval);

        JFreeChart tJFreeChart = createXYBarChart(tIntervalxydataset, pForm);

        ChartRenderingInfo tChartRenderingInfo = new ChartRenderingInfo(new StandardEntityCollection());

        // ChartPanel chartpanel = new ChartPanel(jfreechart);
        ByteArrayOutputStream tImageStream = new ByteArrayOutputStream();
        ByteArrayOutputStream tMapStream = new ByteArrayOutputStream();
        try
        {
            ChartUtilities.writeChartAsPNG(tImageStream, tJFreeChart, 800, 450, tChartRenderingInfo);
            //todo regarder l'API avec FragmentURLGenerator...
            PrintWriter tWriter = new PrintWriter(tMapStream);
            ChartUtilities.writeImageMap(tWriter, "chart", tChartRenderingInfo);
            tWriter.flush();
            pForm.setImageMap(tMapStream.toString());
        } catch (IOException e)
        {
            throw new MeasureException("Unable to write Image", e);
        }
        pSession.setAttribute(FULL_DURATION_STAT, tImageStream.toByteArray());
        sLog.debug("Image " + FULL_DURATION_STAT + " add to session");
    }

    private static JFreeChart createXYBarChart(IntervalXYDataset dataset, MeasurePointStatForm pForm)
    {
        NumberAxis tAxis = new NumberAxis("duration (ms)");
        tAxis.setAutoRangeIncludesZero(false);
        ValueAxis tValueAxis = new NumberAxis("number of occurences");

        XYBarRenderer renderer = new XYBarRenderer();
        renderer.setToolTipGenerator(new StandardXYToolTipGenerator());

        renderer.setURLGenerator(new StatisticXYURLGenerator("MeasurePointList.do", pForm.getInterval(), pForm
                        .getClassName(), pForm.getMethodName()));

        XYPlot plot = new XYPlot(dataset, tAxis, tValueAxis, renderer);
        plot.setOrientation(PlotOrientation.VERTICAL);

        JFreeChart chart = new JFreeChart("Full duration statistics", JFreeChart.DEFAULT_TITLE_FONT, plot, false);

        return chart;
    }

    private IntervalXYDataset createFullDurationDataset(MeasurePoint[] pMeasures, int pInterval)
    {
        Map tMap = new HashMap();
        Integer tCurNb;
        Long tCurDurationAsLong;
        long tCurDuration;
        long tDurationMax = 0;
        for (int i = 0; i < pMeasures.length; i++)
        {
            tCurDuration = pMeasures[i].getEndTime() - pMeasures[i].getBeginTime();
            if (tCurDuration > tDurationMax)
            {
                tDurationMax = tCurDuration;
            }
            // Around the duration with duration groupvalue
            tCurDuration = ((long) tCurDuration / pInterval) * pInterval;
            tCurDurationAsLong = new Long(tCurDuration);
            tCurNb = (Integer) tMap.get(tCurDurationAsLong);
            if (tCurNb != null)
            {
                tCurNb = new Integer(tCurNb.intValue() + 1);
            } else
            {
                tCurNb = new Integer(1);
            }
            tMap.put(tCurDurationAsLong, tCurNb);
        }
        return new StatisticDataSet(tMap, pInterval, tDurationMax);
    }

}
