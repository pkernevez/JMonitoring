package org.jmonitoring.console.flow.jfreechart;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.chart.renderer.category.GanttRenderer;
import org.jfree.data.gantt.GanttCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jmonitoring.core.configuration.Configuration;

/**
 * Custom Rendere for Flows.
 * 
 * @author pke
 * 
 */
public class FlowRenderer extends GanttRenderer
{

    /** Log instance. */
    private static Log sLog = LogFactory.getLog(FlowRenderer.class);

    /**
     * Pour la serialisation.
     */
    private static final long serialVersionUID = 3258413911081431864L;

    protected void drawTasks(Graphics2D pGraph2D, CategoryItemRendererState pCatState, Rectangle2D pDataArea,
                    CategoryPlot pPlot, CategoryAxis pDomainAxis, ValueAxis pRangeAxis, GanttCategoryDataset pDataset,
                    int pRow, int pColumn)
    {
        int count = pDataset.getSubIntervalCount(pRow, pColumn);
        if (count == 0)
        {
            drawTask(pGraph2D, pCatState, pDataArea, pPlot, pDomainAxis, pRangeAxis, pDataset, pRow, pColumn);
        }
        for (int subinterval = 0; subinterval < count; subinterval++)
        {
            org.jfree.ui.RectangleEdge rangeAxisLocation = pPlot.getRangeAxisEdge();
            Number value0 = pDataset.getStartValue(pRow, pColumn, subinterval);
            if (value0 == null)
            {
                return;
            }
            double translatedValue0 = pRangeAxis.valueToJava2D(value0.doubleValue(), pDataArea, rangeAxisLocation);
            Number value1 = pDataset.getEndValue(pRow, pColumn, subinterval);
            if (value1 == null)
            {
                return;
            }
            double translatedValue1 = pRangeAxis.valueToJava2D(value1.doubleValue(), pDataArea, rangeAxisLocation);
            if (translatedValue1 < translatedValue0)
            {
                double temp = translatedValue1;
                translatedValue1 = translatedValue0;
                translatedValue0 = temp;
            }
            double rectStart = calculateBarW0(pPlot, pPlot.getOrientation(), pDataArea, pDomainAxis, pCatState, pRow,
                            pColumn);
            double rectLength = Math.abs(translatedValue1 - translatedValue0);
            double rectBreadth = pCatState.getBarWidth();
            Rectangle2D bar = null;
            if (pPlot.getOrientation() == PlotOrientation.HORIZONTAL)
            {
                bar = new java.awt.geom.Rectangle2D.Double(translatedValue0, rectStart, rectLength, rectBreadth);
            } else if (pPlot.getOrientation() == PlotOrientation.VERTICAL)
            {
                bar = new java.awt.geom.Rectangle2D.Double(rectStart, translatedValue0, rectBreadth, rectLength);
            }
            Rectangle2D completeBar = null;
            Rectangle2D incompleteBar = null;
            Number percent = pDataset.getPercentComplete(pRow, pColumn, subinterval);
            double start = getStartPercent();
            double end = getEndPercent();
            if (percent != null)
            {
                double p = percent.doubleValue();
                if (pPlot.getOrientation() == PlotOrientation.HORIZONTAL)
                {
                    completeBar = new java.awt.geom.Rectangle2D.Double(translatedValue0, rectStart + start
                                    * rectBreadth, rectLength * p, rectBreadth * (end - start));
                    incompleteBar = new java.awt.geom.Rectangle2D.Double(translatedValue0 + rectLength * p, rectStart
                                    + start * rectBreadth, rectLength * (1.0D - p), rectBreadth * (end - start));
                } else if (pPlot.getOrientation() == PlotOrientation.VERTICAL)
                {
                    completeBar = new java.awt.geom.Rectangle2D.Double(rectStart + start * rectBreadth,
                                    translatedValue0 + rectLength * (1.0D - p), rectBreadth * (end - start), rectLength
                                                    * p);
                    incompleteBar = new java.awt.geom.Rectangle2D.Double(rectStart + start * rectBreadth,
                                    translatedValue0, rectBreadth * (end - start), rectLength * (1.0D - p));
                }
            }
            // Paint seriesPaint = getItemPaint(row, column);
            extractMethod(pGraph2D, pDataset, pRow, pColumn, bar, completeBar, incompleteBar);

            if (pCatState.getBarWidth() > 3D)
            {
                pGraph2D.setStroke(getItemStroke(pRow, pColumn));
                pGraph2D.setPaint(getItemOutlinePaint(pRow, pColumn));
                pGraph2D.draw(bar);
            }
            if (pCatState.getInfo() == null)
            {
                continue;
            }
            EntityCollection entities = pCatState.getInfo().getOwner().getEntityCollection();
            if (entities == null)
            {
                continue;
            }
            String tip = null;
            if (getToolTipGenerator(pRow, pColumn) != null)
            {
                tip = getToolTipGenerator(pRow, pColumn).generateToolTip(pDataset, pRow, pColumn);
            }
            String url = null;
            if (getItemURLGenerator(pRow, pColumn) != null)
            {
                url = getItemURLGenerator(pRow, pColumn).generateURL(pDataset, pRow, pColumn);
            }
            CategoryItemEntity entity = new CategoryItemEntity(bar, tip, url, pDataset, pRow, pDataset
                            .getColumnKey(pColumn), pColumn);
            entities.addEntity(entity);
        }
    }

    protected void drawTask(Graphics2D pGraph2D, CategoryItemRendererState pCatState, Rectangle2D pDataArea,
                    CategoryPlot pPlot, CategoryAxis pDomainAxis, ValueAxis pRangeAxis, GanttCategoryDataset pDataset,
                    int pRow, int pColumn)
    {
        PlotOrientation orientation = pPlot.getOrientation();
        org.jfree.ui.RectangleEdge rangeAxisLocation = pPlot.getRangeAxisEdge();
        Number value0 = pDataset.getEndValue(pRow, pColumn);
        if (value0 == null)
        {
            return;
        }
        double java2dValue0 = pRangeAxis.valueToJava2D(value0.doubleValue(), pDataArea, rangeAxisLocation);
        Number value1 = pDataset.getStartValue(pRow, pColumn);
        if (value1 == null)
        {
            return;
        }
        double java2dValue1 = pRangeAxis.valueToJava2D(value1.doubleValue(), pDataArea, rangeAxisLocation);
        if (java2dValue1 < java2dValue0)
        {
            double temp = java2dValue1;
            java2dValue1 = java2dValue0;
            java2dValue0 = temp;
            Number tempNum = value1;
            // value1 = value0;
            // value0 = tempNum;
        }
        double rectStart = calculateBarW0(pPlot, orientation, pDataArea, pDomainAxis, pCatState, pRow, pColumn);
        double rectBreadth = pCatState.getBarWidth();
        double rectLength = Math.abs(java2dValue1 - java2dValue0);
        Rectangle2D bar = null;
        if (orientation == PlotOrientation.HORIZONTAL)
        {
            bar = new java.awt.geom.Rectangle2D.Double(java2dValue0, rectStart, rectLength, rectBreadth);
        } else if (orientation == PlotOrientation.VERTICAL)
        {
            bar = new java.awt.geom.Rectangle2D.Double(rectStart, java2dValue1, rectBreadth, rectLength);
        }
        Rectangle2D completeBar = null;
        Rectangle2D incompleteBar = null;
        Number percent = pDataset.getPercentComplete(pRow, pColumn);
        double start = getStartPercent();
        double end = getEndPercent();
        if (percent != null)
        {
            double p = percent.doubleValue();
            if (pPlot.getOrientation() == PlotOrientation.HORIZONTAL)
            {
                completeBar = new java.awt.geom.Rectangle2D.Double(java2dValue0, rectStart + start * rectBreadth,
                                rectLength * p, rectBreadth * (end - start));
                incompleteBar = new java.awt.geom.Rectangle2D.Double(java2dValue0 + rectLength * p, rectStart + start
                                * rectBreadth, rectLength * (1.0D - p), rectBreadth * (end - start));
            } else if (pPlot.getOrientation() == PlotOrientation.VERTICAL)
            {
                completeBar = new java.awt.geom.Rectangle2D.Double(rectStart + start * rectBreadth, java2dValue1
                                + rectLength * (1.0D - p), rectBreadth * (end - start), rectLength * p);
                incompleteBar = new java.awt.geom.Rectangle2D.Double(rectStart + start * rectBreadth, java2dValue1,
                                rectBreadth * (end - start), rectLength * (1.0D - p));
            }
        }

        extractMethod(pGraph2D, pDataset, pRow, pColumn, bar, completeBar, incompleteBar);
        if (pCatState.getBarWidth() > 3D)
        {
            java.awt.Stroke stroke = getItemOutlineStroke(pRow, pColumn);
            Paint paint = getItemOutlinePaint(pRow, pColumn);
            if (stroke != null && paint != null)
            {
                pGraph2D.setStroke(stroke);
                pGraph2D.setPaint(paint);
                pGraph2D.draw(bar);
            }
        }
        org.jfree.chart.labels.CategoryLabelGenerator generator = getLabelGenerator(pRow, pColumn);
        if (generator != null && isItemLabelVisible(pRow, pColumn))
        {
            drawItemLabel(pGraph2D, pDataset, pRow, pColumn, pPlot, generator, bar, false);
        }
        if (pCatState.getInfo() != null)
        {
            EntityCollection entities = pCatState.getInfo().getOwner().getEntityCollection();
            if (entities != null)
            {
                String tip = null;
                CategoryToolTipGenerator tipster = getToolTipGenerator(pRow, pColumn);
                if (tipster != null)
                {
                    tip = tipster.generateToolTip(pDataset, pRow, pColumn);
                }
                String url = null;
                if (getItemURLGenerator(pRow, pColumn) != null)
                {
                    url = getItemURLGenerator(pRow, pColumn).generateURL(pDataset, pRow, pColumn);
                }
                CategoryItemEntity entity = new CategoryItemEntity(bar, tip, url, pDataset, pRow, pDataset
                                .getColumnKey(pColumn), pColumn);
                entities.addEntity(entity);
            }
        }
    }

    private void extractMethod(Graphics2D pGraph2D, GanttCategoryDataset pDataset, int pRow, int pColumn,
                    Rectangle2D pBar, Rectangle2D pCompleteBar, Rectangle2D pIncompleteBar)
    {
        List tTaskSeriesList = pDataset.getRowKeys();
        TaskSeries tSeries = (TaskSeries) tTaskSeriesList.get(pRow);
        Task tTask = tSeries.get(pColumn);
        Paint tSeriesPaint = (Paint) Configuration.getInstance().getColor(tTask.getDescription());
        if (tSeriesPaint == null)
        {
            tSeriesPaint = getItemPaint(pRow, pColumn);
            sLog.warn("Unable to find color for Group [" + tTask.getDescription() + "] in Configuration.");
        } else
        {
            sLog.debug("Use color [" + tSeriesPaint + "] for group [" + tTask.getDescription() + "].");
        }
        pGraph2D.setPaint(tSeriesPaint);
        pGraph2D.fill(pBar);
        if (pCompleteBar != null)
        {
            pGraph2D.setPaint(getCompletePaint());
            pGraph2D.fill(pCompleteBar);
        }
        if (pIncompleteBar != null)
        {
            pGraph2D.setPaint(getIncompletePaint());
            pGraph2D.fill(pIncompleteBar);
        }
    }
}
