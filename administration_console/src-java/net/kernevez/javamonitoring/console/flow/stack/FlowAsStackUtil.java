package net.kernevez.javamonitoring.console.flow.stack;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;

import net.kernevez.performance.configuration.Configuration;
import net.kernevez.performance.measure.ExecutionFlow;
import net.kernevez.performance.measure.MeasurePoint;

/**
 * Util class for render a flow as an Satck in an Html page.
 * 
 * @todo refactor this class as taglib.
 * @author pke
 */
public class FlowAsStackUtil
{

    /**
     * The execution flow to work with.
     */
    private ExecutionFlow mFlow;

    private DateFormat mFormater;

    /**
     * Private constructor. Not to use.
     * 
     * @param pFlow The flow to use for graphique.
     */
    public FlowAsStackUtil(ExecutionFlow pFlow)
    {
        mFormater = Configuration.getInstance().getDateTimeFormater();
        mFlow = pFlow;
    }

    /**
     * Write a <code>Flow</code> in Html format.
     * 
     * @param pWriter Text Writer.
     * @throws IOException
     * @todo Make an button for deploying all the MeasurePoint or for undeploying them.
     * @todo Option undeply only those with exception or duration upper XXX.
     * @return The Html code of this flow.
     */
    public StringBuffer writeFlowAsHtml()
    {
        StringBuffer tBuffer = new StringBuffer();

        tBuffer.append("<A onclick=\"\">Deploy All</A>");
        tBuffer.append("<ul id=\"menuList\">");
        tBuffer.append("<li class=\"menubar\">");
        tBuffer.append("<a href=\"#\" id=\"0Actuator\" class=\"actuator\">");
        long tDuration = mFlow.getEndTime() - mFlow.getBeginDate();
        tBuffer.append("[").append(tDuration).append("] ");
        tBuffer.append(mFlow.getJvmIdentifier()).append("/").append(mFlow.getThreadName());
        tBuffer.append(" : ").append(mFlow.getFirstMeasure().getGroupName()).append(" -> ");
        tBuffer.append(mFlow.getFirstMeasure().getClassName()).append(".");
        tBuffer.append(mFlow.getFirstMeasure().getMethodName()).append("()");
        tBuffer.append("</a>\n");

        tBuffer.append("<ul id=\"0Menu\" class=\"menu\">");
        writeMeasureAsHtml(mFlow.getFirstMeasure(), tBuffer);
        tBuffer.append("</ul>\n");
        tBuffer.append("</li>");
        tBuffer.append("</ul>");
        return tBuffer;
    }

    /**
     * Write the <code>Measure</code> into writer using Html format.
     * 
     * @param pCurrentMeasure The Measure to write.
     * @param pHtmlBuffer The Html buffer for this measure.
     * @todo Use background color with item.
     */
    private void writeMeasureAsHtml(MeasurePoint pCurrentMeasure, StringBuffer pHtmlBuffer)
    {
        // Gestion de l'exception
        String tReturnImage;
        if (pCurrentMeasure.isReturnCallException())
        {
            tReturnImage = "<IMG title=\"Throwable thrown\" src=\"images/warn.png\"/>";
        } else
        {
            tReturnImage = "";
        }
        // String tBgColor = getColorAsString(Configuration.getColor(pCurrentMeasure.getGroupName()));
        // G�n�ration du lien vers les statistiques
        StringBuffer tLinkStat = new StringBuffer();
        tLinkStat.append("<A title=\"View stats...\" href=\"MeasurePointStat.do?flowId="
                        + pCurrentMeasure.getFlowId() + "&sequenceId=" + pCurrentMeasure.getSequenceId() + "\">");
        tLinkStat.append("<IMG src=\"images/graphique.png\"/></A>");

        StringBuffer tLinkDetail = new StringBuffer();
        tLinkDetail.append("<A title=\"View details...\" href=\"MeasurePointEdit.do?flowId="
                        + pCurrentMeasure.getFlowId() + "&sequenceId=" + pCurrentMeasure.getSequenceId() + "\">");
        tLinkDetail.append("<IMG src=\"images/edit.png\"/></A>");

        // Maintenant on cr�er le le html associ� au MeasurePoint
        if (pCurrentMeasure.getChildren().size() > 0)
        { // On cr�e un sous menu
            // style=\"BACKGROUND-COLOR: " + tBgColor + "\"
            pHtmlBuffer.append("<li>\n<a href=\"#\" id=\"" + pCurrentMeasure.getSequenceId() + "Actuator\"");
            pHtmlBuffer.append(" class=\"actuator\" title=\"" + getMeasurePointTitle(pCurrentMeasure) + "\">");
            pHtmlBuffer.append(tReturnImage + getMeasurePointText(pCurrentMeasure) + "</a>");
            pHtmlBuffer.append(tLinkStat.toString() + tLinkDetail.toString() + "\n");
            pHtmlBuffer.append("  <ul id=\"" + pCurrentMeasure.getSequenceId() + "Menu\" class=\"submenu\">\n");
            for (Iterator tIterator = pCurrentMeasure.getChildren().iterator(); tIterator.hasNext();)
            {
                writeMeasureAsHtml((MeasurePoint) tIterator.next(), pHtmlBuffer);
            }
            pHtmlBuffer.append("  </ul>\n</li>\n");
        } else
        {
            pHtmlBuffer.append("<li><span title=\"" + getMeasurePointTitle(pCurrentMeasure) + "\">" + tReturnImage
                            + getMeasurePointText(pCurrentMeasure) + "</span>");
            pHtmlBuffer.append(tLinkStat.toString() + tLinkDetail.toString() + "</li>\n");
        }
    }

    /**
     * Get the text part of <code>MeasurePoint</code> of a Flow.
     * 
     * @param pMeasure The measure point to use.
     * @return the Html <code>String</code> for Html rendering.
     */
    private String getMeasurePointText(MeasurePoint pMeasure)
    {
        StringBuffer tBuffer = new StringBuffer();
        long tDuration = pMeasure.getEndTime() - pMeasure.getBeginTime();
        tBuffer.append("[").append(tDuration).append("] ");
        tBuffer.append(pMeasure.getGroupName()).append(" -> ").append(pMeasure.getClassName()).append(".").append(
                        pMeasure.getMethodName());
        return tBuffer.toString();
    }

    /**
     * Get the title part of <code>MeasurePoint</code> of a Flow.
     * 
     * @param pMeasure The measure point to use.
     * @return the Html <code>String</code> to use in the <code>title</code> Html attribute.
     */
    private String getMeasurePointTitle(MeasurePoint pMeasure)
    {
        StringBuffer tBuffer = new StringBuffer();
        tBuffer.append("Start Date=");
        tBuffer.append(mFormater.format(new Date(pMeasure.getBeginTime())));
        if (pMeasure.getParams() != null)
        {
            tBuffer.append("\n PARAM=[").append(pMeasure.getParams()).append("]");
        }
        if (pMeasure.getReturnValue() != null)
        {
            tBuffer.append("\n ReturnValue=[").append(pMeasure.getReturnValue()).append("]");
        }
        if (pMeasure.getThrowableClassName() != null)
        {
            tBuffer.append("\n ThrowableClassName=[").append(pMeasure.getThrowableClassName()).append("]");
        }
        if (pMeasure.getThrowableMessage() != null)
        {
            tBuffer.append("\n ThrowableMesssage=[").append(pMeasure.getThrowableMessage()).append("]");
        }
        return tBuffer.toString();
    }

}