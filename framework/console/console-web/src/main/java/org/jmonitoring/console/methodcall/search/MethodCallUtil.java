package org.jmonitoring.console.methodcall.search;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.util.ArrayList;
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

    private final StringBuffer mWriter;

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

    @Override
    public String toString()
    {
        return mWriter.toString();
    }

    /**
     * Write the list of Measure as an Html Tree.
     */
    @SuppressWarnings("unchecked")
    public void writeMeasureListAsMenu(Map<String, MethodCallExtractDTO> pListOfAllExtractByFullName,
                    Map<String, Map<String, ?>> pTreeOfExtract)
    {
        if (pTreeOfExtract.size() == 0)
        {
            mWriter.append("No Measure Found");
        } else
        {
            mWriter.append("<ul id=\"menuList\">");
            int tLastId = 0;
            for (Map.Entry<String, Map<String, ?>> curEntry : pTreeOfExtract.entrySet())
            {
                mWriter.append("<li class=\"menubar\">");
                // TODO Remove this cast with safe type ...
                tLastId = writeMeasuresAsMenu(pListOfAllExtractByFullName, new ArrayList<String>(),
                                (Map<String, Map<String, ?>>) curEntry.getValue(), curEntry.getKey(), true, tLastId + 1);
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
    int writeMeasuresAsMenu(Map<String, MethodCallExtractDTO> pListOfAllExtractByFullName,
                    List<String> pCurrentClassName, Map<String, Map<String, ?>> pTreeOfMeasure, String pCurNodeName,
                    boolean pFirstLevel, int pLastId)
    {
        int tLastId = pLastId;
        if (pTreeOfMeasure.size() > 0)
        { // Create sub menu
            String tClassName;
            if (pFirstLevel)
            { // First time
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
                            + pCurNodeName
                            + "</a>\n");
            mWriter.append("  <ul id=\"" + tLastId + "Menu\" class=\"" + tClassName + "\">\n");
            for (Map.Entry<String, Map<String, ?>> curEntry : pTreeOfMeasure.entrySet())
            {
                // TODO Remove this cast with safe type ...
                tLastId = writeMeasuresAsMenu(pListOfAllExtractByFullName, pCurrentClassName,
                                (Map<String, Map<String, ?>>) curEntry.getValue(), curEntry.getKey(), false, ++tLastId);
            }
            mWriter.append("  </ul>\n");
            if (pFirstLevel)
            { // Second time or more
                mWriter.append("</li>\n");
            }
            // Remove the current node to className
            pCurrentClassName.remove(pCurrentClassName.size() - 1);
        } else
        {
            // Generation of the hyper-link to the statistics
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

    private MethodCallExtractDTO getExtractForThisNode(Map<String, MethodCallExtractDTO> pListOfAllExtractByFullName,
                    List<String> pClassNameAsString, String pCurrentNodeName)
    {
        // Calculation of the full name
        StringBuilder tBuffer = new StringBuilder();
        for (String tClass : pClassNameAsString)
        {
            tBuffer.append(tClass);
        }
        tBuffer.append(".").append(pCurrentNodeName);
        // Now return the extract for this name
        MethodCallExtractDTO tResult = pListOfAllExtractByFullName.get(tBuffer.toString());
        if (tResult == null)
        {
            sLog.error("Unable to find Method Call with Key=[" + tBuffer.toString() + "]");
        }
        return tResult;
    }
}
