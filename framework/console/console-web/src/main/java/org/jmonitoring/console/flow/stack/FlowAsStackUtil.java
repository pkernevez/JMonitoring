package org.jmonitoring.console.flow.stack;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.io.IOException;

import org.apache.taglibs.standard.lang.jpath.encoding.HtmlEncoder;
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
    private final ExecutionFlowDTO mFlow;

    /**
     * Private constructor. Not to use.
     * 
     * @param pFlow The flow to use for graphique.
     */
    public FlowAsStackUtil(ExecutionFlowDTO pFlow)
    {
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
    public CharSequence writeFlowAsHtml()
    {
        StringBuilder tBuffer = new StringBuilder();

        tBuffer.append("<A onclick=\"\">Deploy All</A>");
        tBuffer.append("<ul id=\"menuList\">");
        tBuffer.append("<li class=\"menubar\">");
        tBuffer.append("<a href=\"#\" id=\"0Actuator\" class=\"actuator\">");
        long tDuration = mFlow.getFirstMethodCall().getDuration();
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
     * @todo Mettre les titles des dur�es dans une feuille de style pour diminuer le volume de la page
     */
    private void writeMethodCallAsHtml(MethodCallDTO pCurrentMethod, StringBuilder pHtmlBuffer)
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
        // G�n�ration du lien vers les statistiques
        StringBuilder tLinkStat = new StringBuilder();
        tLinkStat.append("<A title=\"View stats...\" href=\"MethodCallStatIn.do?flowId=");
        tLinkStat.append(pCurrentMethod.getFlowId()).append("&position=").append(pCurrentMethod.getPosition());
        tLinkStat.append("\">").append("<IMG src=\"images/graphique.png\"/></A>");

        StringBuilder tLinkDetail = new StringBuilder();
        tLinkDetail.append("<A title=\"View details...\" href=\"MethodCallEditIn.do?flowId=");
        tLinkDetail.append(pCurrentMethod.getFlowId()).append("&position=").append(pCurrentMethod.getPosition());
        tLinkDetail.append("\">").append("<IMG src=\"images/edit.png\"/></A>");

        long tEndTime = pCurrentMethod.getEndMilliSeconds();
        long tBeginTime = pCurrentMethod.getBeginMilliSeconds();
        long tDuration = tEndTime - tBeginTime;
        // Maintenant on cr�er le le html associ� au MethodCallDTO
        if (pCurrentMethod.getChildren().length > 0)
        { // On cr�e un sous menu
            // style=\"BACKGROUND-COLOR: " + tBgColor + "\"
            pHtmlBuffer.append("<li>\n");
            pHtmlBuffer.append("<a href=\"#\" id=\"");
            pHtmlBuffer.append(pCurrentMethod.getPosition()).append("Actuator\"");
            pHtmlBuffer.append(" class=\"actuator\" title=\"").append(getMeasurePointTitle(pCurrentMethod));
            pHtmlBuffer.append("\">");
            pHtmlBuffer.append("<span class=\"prevDuration\" title=\"Since prev MethodCall\">[->");
            pHtmlBuffer.append(pCurrentMethod.getDurationFromPreviousCall()).append("]</span>");
            pHtmlBuffer.append("<span class=\"curDuration\" title=\"Duration of this MethodCall\">[");
            pHtmlBuffer.append(tDuration).append("]</span>");
            pHtmlBuffer.append(tReturnImage).append(getMeasurePointText(pCurrentMethod)).append("</a>");
            pHtmlBuffer.append(tLinkStat).append(tLinkDetail).append("\n");
            pHtmlBuffer.append("  <ul id=\"").append(pCurrentMethod.getPosition());
            pHtmlBuffer.append("Menu\" class=\"submenu\">\n");
            for (int i = 0; i < pCurrentMethod.getChildren().length; i++)
            {
                writeMethodCallAsHtml(pCurrentMethod.getChild(i), pHtmlBuffer);
            }
            pHtmlBuffer.append("  </ul>\n</li>\n");
        } else
        {
            pHtmlBuffer.append("<li><span class=\"prevDuration\" title=\"Duration since the prev MethodCall\">[->");
            pHtmlBuffer.append(pCurrentMethod.getDurationFromPreviousCall()).append("]</span>");
            pHtmlBuffer.append("<span class=\"curDuration\" title=\"Duration of this MethodCall\">[");
            pHtmlBuffer.append(tDuration).append("]</span>");
            pHtmlBuffer.append("<span title=\"").append(getMeasurePointTitle(pCurrentMethod));
            pHtmlBuffer.append("\">").append(tReturnImage).append(getMeasurePointText(pCurrentMethod));
            pHtmlBuffer.append("</span>");
            pHtmlBuffer.append(tLinkStat).append(tLinkDetail).append("</li>\n");
        }
    }

    /**
     * Get the text part of <code>MethodCallDTO</code> of a Flow.
     * 
     * @param pMeasure The measure point to use.
     * @return the Html <code>String</code> for Html rendering.
     */
    private CharSequence getMeasurePointText(MethodCallDTO pMeasure)
    {
        StringBuilder tBuffer = new StringBuilder();
        tBuffer.append(pMeasure.getGroupName()).append(" -> ").append(pMeasure.getClassName()).append(".")
               .append(pMeasure.getMethodName());
        return tBuffer;
    }

    /**
     * Get the title part of <code>MethodCallDTO</code> of a Flow.
     * 
     * @param pMeasure The measure point to use.
     * @return the Html <code>String</code> to use in the <code>title</code> Html attribute.
     */
    private CharSequence getMeasurePointTitle(MethodCallDTO pMeasure)
    {
        StringBuilder tBuffer = new StringBuilder();
        tBuffer.append("Start Date=");
        tBuffer.append(pMeasure.getBeginTimeString());
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
