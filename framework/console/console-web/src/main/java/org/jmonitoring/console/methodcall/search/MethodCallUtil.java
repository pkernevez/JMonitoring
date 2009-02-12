package org.jmonitoring.console.methodcall.search;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jmonitoring.core.dto.MethodCallExtractDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility for image generation of the tree of measure as a menu.
 * 
 * @author pke
 * 
 */
public class MethodCallUtil
{

    private final StringBuilder mWriter;

    private static Logger sLog = LoggerFactory.getLogger(MethodCallUtil.class);

    /**
     * Contructor for test.
     * 
     * @param pWriter The JSP writer.
     */
    public MethodCallUtil()
    {
        mWriter = new StringBuilder();
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
        MyMap pTreeOfExtract)
    {
        if (pTreeOfExtract.size() == 0)
        {
            mWriter.append("No Measure Found");
        } else
        {
            mWriter.append("<ul id=\"menuList\">");
            int tLastId = 0;
            for (Map.Entry<String, MyMap> curEntry : pTreeOfExtract.entrySet())
            {
                mWriter.append("<li class=\"menubar\">");
                tLastId =
                    writeMeasuresAsMenu(pListOfAllExtractByFullName, new ArrayList<String>(), curEntry.getValue(),
                                        curEntry.getKey(), true, tLastId + 1);
                mWriter.append("</li>");
            }
            mWriter.append("</ul>");
        }
    }

    public static interface MyMap extends Map<String, MyMap>
    {
    }

    public static class MyHashMap extends HashMap<String, MyMap> implements MyMap
    {
        private static final long serialVersionUID = 1L;
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
        List<String> pCurrentClassName, MyMap pTreeOfMeasure, String pCurNodeName, boolean pFirstLevel, int pLastId)
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
            mWriter.append("<a href=\"#\" id=\"").append(tLastId).append("Actuator\"");
            mWriter.append(" class=\"actuator\">");// + tReturnImage
            mWriter.append(pCurNodeName).append("</a>\n");
            mWriter.append("  <ul id=\"").append(tLastId).append("Menu\" class=\"").append(tClassName);
            mWriter.append("\">\n");
            for (Map.Entry<String, MyMap> curEntry : pTreeOfMeasure.entrySet())
            {
                tLastId =
                    writeMeasuresAsMenu(pListOfAllExtractByFullName, pCurrentClassName, curEntry.getValue(),
                                        curEntry.getKey(), false, ++tLastId);
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
            StringBuilder tLinkStat = new StringBuilder();
            MethodCallExtractDTO tExtract =
                getExtractForThisNode(pListOfAllExtractByFullName, pCurrentClassName, pCurNodeName);
            tLinkStat.append("<li><span title=\"GroupName=[").append(tExtract.getGroupName());
            tLinkStat.append("]\">").append(tExtract.getMethodName());
            tLinkStat.append("()</span> ");
            tLinkStat.append("<span title=\"occurrence\">(").append(tExtract.getOccurenceNumber()).append(")</span>");
            tLinkStat.append("<A title=\"View stats...\" href=\"MethodCallStatIn.do?className=");
            tLinkStat.append(tExtract.getClassName()).append("&methodName=");
            tLinkStat.append(tExtract.getMethodName()).append("\">");
            tLinkStat.append("<IMG src=\"images/graphique.png\"/></A>");
            tLinkStat.append("</li>\n");
            mWriter.append(tLinkStat);
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
