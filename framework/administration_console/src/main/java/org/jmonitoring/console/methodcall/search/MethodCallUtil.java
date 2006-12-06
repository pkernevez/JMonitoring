package org.jmonitoring.console.methodcall.search;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jmonitoring.core.dto.MethodCallExtractDTO;

/**
 * Utility for image generation of the tree of measure as a menu.
 * 
 * @author pke
 * 
 */
public class MethodCallUtil
{

    private StringBuffer mWriter;

    private static Log sLog = LogFactory.getLog(MethodCallUtil.class);

    /**
     * Contructor for test.
     * 
     * @param pWriter The JSP writer.
     */
    public MethodCallUtil()
    {
        mWriter = new StringBuffer();
    }

    public String toString()
    {
        return mWriter.toString();
    }

    /**
     * Write the list of Measure as an Html Tree.
     */
    public void writeMeasureListAsMenu(Map pListOfAllExtractByFullName, Map pTreeOfExtract)
    {
        if (pTreeOfExtract.size() == 0)
        {
            mWriter.append("No Measure Found");
        } else
        {
            Map.Entry curEntry;
            mWriter.append("<ul id=\"menuList\">");
            int tLastId = 0;
            for (Iterator tIt = pTreeOfExtract.entrySet().iterator(); tIt.hasNext();)
            {
                curEntry = (Map.Entry) tIt.next();
                mWriter.append("<li class=\"menubar\">");
                tLastId = writeMeasuresAsMenu(pListOfAllExtractByFullName, new ArrayList(), (Map) curEntry.getValue(),
                    (String) curEntry.getKey(), true, tLastId + 1);
                mWriter.append("</li>");
            }
            mWriter.append("</ul>");
        }
    }

    /**
     * Write the Tree as a Html Menu.
     * 
     * @param pListOfAllExtractByFullName List of all the Extract, the key is <code>FullClassName.methodName</code>.
     * @param pCurrentClassName The name of the class that we are building, the <code>List</code> is compose with the
     *        package parts.
     * @param pTreeOfMeasure The Tree to write.
     * @param pCurNodeName The name of the current node.
     * @param pFirstLevel True if the current node is the first level of the menu.
     * @param pLastId The technical identifier to use for the next generation.
     * @return The last technical identifier used.
     */
    int writeMeasuresAsMenu(Map pListOfAllExtractByFullName, List pCurrentClassName, Map pTreeOfMeasure,
                    String pCurNodeName, boolean pFirstLevel, int pLastId)
    {
        int tLastId = pLastId;
        if (pTreeOfMeasure.size() > 0)
        { // On crée un sous menu
            String tClassName;
            if (pFirstLevel)
            { // Firsttime
                tClassName = "menu";
                // We add the current node to class name
                pCurrentClassName.add(pCurNodeName);
            } else
            {
                mWriter.append("<li>\n");
                tClassName = "submenu";
                // We add the current node to class name
                pCurrentClassName.add("." + pCurNodeName);
            }
            mWriter.append("<a href=\"#\" id=\"" + tLastId + "Actuator\"");
            mWriter.append(" class=\"actuator\">" // + tReturnImage
                + pCurNodeName + "</a>\n");
            mWriter.append("  <ul id=\"" + tLastId + "Menu\" class=\"" + tClassName + "\">\n");
            Map.Entry curEntry;
            for (Iterator tIterator = pTreeOfMeasure.entrySet().iterator(); tIterator.hasNext();)
            {
                curEntry = (Map.Entry) tIterator.next();
                tLastId = writeMeasuresAsMenu(pListOfAllExtractByFullName, pCurrentClassName,
                    (Map) curEntry.getValue(), (String) curEntry.getKey(), false, ++tLastId);
            }
            mWriter.append("  </ul>\n");
            if (pFirstLevel)
            { // Secondtime or more
                mWriter.append("</li>\n");
            }
            // Remove the current node to className
            pCurrentClassName.remove(pCurrentClassName.size() - 1);
        } else
        {
            // Génération du lien vers les statistiques
            StringBuffer tLinkStat = new StringBuffer();
            MethodCallExtractDTO tExtract = getExtractForThisNode(pListOfAllExtractByFullName, pCurrentClassName,
                pCurNodeName);
            tLinkStat.append("<li><span title=\"GroupName=[").append(tExtract.getGroupName());
            tLinkStat.append("]\">").append(tExtract.getMethodName());
            tLinkStat.append("()</span> ");
            tLinkStat.append("<span title=\"occurrence\">(").append(tExtract.getOccurenceNumber()).append(")</span>");
            tLinkStat.append("<A title=\"View stats...\" href=\"MethodCallStatIn.do?className=");
            tLinkStat.append(tExtract.getClassName()).append("&methodName=");
            tLinkStat.append(tExtract.getMethodName()).append("\">");
            tLinkStat.append("<IMG src=\"images/graphique.png\"/></A>");
            tLinkStat.append("</li>\n");
            mWriter.append(tLinkStat.toString());
        }
        return tLastId++;
    }

    private MethodCallExtractDTO getExtractForThisNode(Map pListOfAllExtractByFullName, List pClassNameAsString,
                    String pCurrentNodeName)
    {
        // Calculation of the full name
        StringBuffer tBuffer = new StringBuffer();
        for (Iterator tIt = pClassNameAsString.iterator(); tIt.hasNext();)
        {
            tBuffer.append(tIt.next());
        }
        tBuffer.append(".").append(pCurrentNodeName);
        // Now return the extract for this name
        MethodCallExtractDTO tResult = (MethodCallExtractDTO) pListOfAllExtractByFullName.get(tBuffer.toString());
        if (tResult == null)
        {
            sLog.error("Unable to find Method Call with Key=[" + tBuffer.toString() + "]");
        }
        return tResult;
    }


}
