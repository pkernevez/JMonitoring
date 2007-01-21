package org.jmonitoring.console.flow.stack;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.io.IOException;
import java.text.DateFormat;

import org.apache.taglibs.standard.lang.jpath.encoding.HtmlEncoder;
import org.jmonitoring.core.configuration.Configuration;
import org.jmonitoring.core.dto.ExecutionFlowDTO;
import org.jmonitoring.core.dto.MethodCallDTO;

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
    private ExecutionFlowDTO mFlow;

    private DateFormat mFormater;

    /**
     * Private constructor. Not to use.
     * 
     * @param pFlow The flow to use for graphique.
     */
    public FlowAsStackUtil(ExecutionFlowDTO pFlow)
    {
        mFormater = Configuration.getInstance().getDateTimeFormater();
        mFlow = pFlow;
    }

    /**
     * Write a <code>Flow</code> in Html format.
     * 
     * @throws IOException
     * @todo Make an button for deploying all the MethodCallDTO or for undeploying them.
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
        long tDuration = mFlow.getEndTime().getTime() - mFlow.getBeginTime().getTime();
        tBuffer.append("[").append(tDuration).append("] ");
        tBuffer.append(mFlow.getJvmIdentifier()).append("/").append(mFlow.getThreadName());
        tBuffer.append(" : ").append(mFlow.getFirstMethodCall().getGroupName()).append(" -> ");
        tBuffer.append(mFlow.getFirstMethodCall().getClassName()).append(".");
        tBuffer.append(mFlow.getFirstMethodCall().getMethodName()).append("()");
        tBuffer.append("</a>\n");

        tBuffer.append("<ul id=\"0Menu\" class=\"menu\">");
        writeMethodCallAsHtml(mFlow.getFirstMethodCall(), tBuffer);
        tBuffer.append("</ul>\n");
        tBuffer.append("</li>");
        tBuffer.append("</ul>");
        return tBuffer;
    }

    /**
     * Write the <code>Measure</code> into writer using Html format.
     * 
     * @param pCurrentMethod The Measure to write.
     * @param pHtmlBuffer The Html buffer for this measure.
     * @todo Use background color with item.
     * @todo Mettre les titles des durées dans une feuille de style pour diminuer le volume de la page
     */
    private void writeMethodCallAsHtml(MethodCallDTO pCurrentMethod, StringBuffer pHtmlBuffer)
    {
        // Gestion de l'exception
        String tReturnImage;
        if (pCurrentMethod.isReturnCallException())
        {
            tReturnImage = "<IMG title=\"Throwable thrown\" src=\"images/warn.png\"/>";
        } else
        {
            tReturnImage = "";
        }
        // String tBgColor = getColorAsString(Configuration.getColor(pCurrentMeasure.getGroupName()));
        // Génération du lien vers les statistiques
        StringBuffer tLinkStat = new StringBuffer();
        tLinkStat.append("<A title=\"View stats...\" href=\"MethodCallStatIn.do?flowId=" + pCurrentMethod.getFlowId()
            + "&position=" + pCurrentMethod.getPosition() + "\">");
        tLinkStat.append("<IMG src=\"images/graphique.png\"/></A>");

        StringBuffer tLinkDetail = new StringBuffer();
        tLinkDetail.append("<A title=\"View details...\" href=\"MethodCallEditIn.do?flowId="
            + pCurrentMethod.getFlowId() + "&position=" + pCurrentMethod.getPosition() + "\">");
        tLinkDetail.append("<IMG src=\"images/edit.png\"/></A>");

        long tDuration = pCurrentMethod.getEndTime().getTime() - pCurrentMethod.getBeginTime().getTime();
        // Maintenant on créer le le html associé au MethodCallDTO
        if (pCurrentMethod.getChildren().length > 0)
        { // On crée un sous menu
            // style=\"BACKGROUND-COLOR: " + tBgColor + "\"
            pHtmlBuffer.append("<li>\n");
            pHtmlBuffer.append("<a href=\"#\" id=\"");
            pHtmlBuffer.append(pCurrentMethod.getPosition() + "Actuator\"");
            pHtmlBuffer.append(" class=\"actuator\" title=\"" + getMeasurePointTitle(pCurrentMethod) + "\">");
            pHtmlBuffer.append("<span class=\"prevDuration\" title=\"Since prev MethodCall\">[->");
            pHtmlBuffer.append(pCurrentMethod.getDurationFromPreviousCall() + "]</span>");
            pHtmlBuffer.append("<span class=\"curDuration\" title=\"Duration of this MethodCall\">[");
            pHtmlBuffer.append(tDuration + "]</span>");
            pHtmlBuffer.append(tReturnImage + getMeasurePointText(pCurrentMethod) + "</a>");
            pHtmlBuffer.append(tLinkStat.toString() + tLinkDetail.toString() + "\n");
            pHtmlBuffer.append("  <ul id=\"" + pCurrentMethod.getPosition() + "Menu\" class=\"submenu\">\n");
            for (int i = 0; i < pCurrentMethod.getChildren().length; i++)
            {
                writeMethodCallAsHtml(pCurrentMethod.getChild(i), pHtmlBuffer);
            }
            pHtmlBuffer.append("  </ul>\n</li>\n");
        } else
        {
            pHtmlBuffer.append("<li><span class=\"prevDuration\" title=\"Duration since the prev MethodCall\">[->");
            pHtmlBuffer.append(pCurrentMethod.getDurationFromPreviousCall() + "]</span>");
            pHtmlBuffer.append("<span class=\"curDuration\" title=\"Duration of this MethodCall\">[");
            pHtmlBuffer.append(tDuration + "]</span>");
            pHtmlBuffer.append("<span title=\"" + getMeasurePointTitle(pCurrentMethod) + "\">" + tReturnImage
                + getMeasurePointText(pCurrentMethod) + "</span>");
            pHtmlBuffer.append(tLinkStat.toString() + tLinkDetail.toString() + "</li>\n");
        }
    }

    /**
     * Get the text part of <code>MethodCallDTO</code> of a Flow.
     * 
     * @param pMeasure The measure point to use.
     * @return the Html <code>String</code> for Html rendering.
     */
    private String getMeasurePointText(MethodCallDTO pMeasure)
    {
        StringBuffer tBuffer = new StringBuffer();
        tBuffer.append(pMeasure.getGroupName()).append(" -> ").append(pMeasure.getClassName()).append(".").append(
            pMeasure.getMethodName());
        return tBuffer.toString();
    }

    /**
     * Get the title part of <code>MethodCallDTO</code> of a Flow.
     * 
     * @param pMeasure The measure point to use.
     * @return the Html <code>String</code> to use in the <code>title</code> Html attribute.
     */
    private String getMeasurePointTitle(MethodCallDTO pMeasure)
    {
        StringBuffer tBuffer = new StringBuffer();
        tBuffer.append("Start Date=");
        tBuffer.append(mFormater.format(pMeasure.getBeginTime()));
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
        return HtmlEncoder.encode(tBuffer.toString());
    }

}
