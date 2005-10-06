package org.jmonitoring.console.measurepoint;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.jmonitoring.core.configuration.Configuration;
import org.jmonitoring.core.dao.ExecutionFlowMySqlDAO;
import org.jmonitoring.core.dao.MeasureExtract;
import org.jmonitoring.core.dao.StandAloneConnectionManager;
import org.jmonitoring.core.measure.MeasureException;

/**
 * Utility for image generation of the tree of measure as a menu.
 * 
 * @author pke
 *  
 */
public class MeasureMenuUtil
{

    private StringBuffer mWriter;

    private Map mMeasureExtracts;

    /**
     * Contructor for test.
     * 
     * @param pWriter The JSP writer.
     */
    public MeasureMenuUtil(StringBuffer pWriter)
    {
        mWriter = pWriter;
    }

    private Map getTreeOfMeasure()
    {
        Connection tCon = null;
        try
        {
            try
            {
                tCon = new StandAloneConnectionManager(Configuration.getInstance()).getConnection();
                ExecutionFlowMySqlDAO tDao = new ExecutionFlowMySqlDAO(tCon);
                List tListOfMeasure = tDao.getListOfMeasure();
                return convertListAsTree(tListOfMeasure);
            } finally
            {
                if (tCon != null)
                {
                    tCon.close();
                }
            }
        } catch (SQLException e)
        {
            throw new MeasureException("Unable to get List of Measure.", e);
        }
    }

    /**
     * Write the list of Measure as an Html Tree.
     */
    public void writeMeasureListAsMenu()
    {
        Map tMapAsTree = getTreeOfMeasure();
        if (tMapAsTree.size() == 0)
        {
            mWriter.append("No Measure Found");
        } else
        {
            Map.Entry curEntry;
            mWriter.append("<ul id=\"menuList\">");
            int tLastId = 0;
            for (Iterator tIt = tMapAsTree.entrySet().iterator(); tIt.hasNext();)
            {
                curEntry = (Map.Entry) tIt.next();
                mWriter.append("<li class=\"menubar\">");
                tLastId = writeMeasuresAsMenu(new ArrayList(), (Map) curEntry.getValue(), (String)curEntry.getKey(), true,
                                tLastId + 1);
                mWriter.append("</li>");
            }
            mWriter.append("</ul>");
        }
    }

    /**
     * Convert a list of String containing caracter <code>.</code> into a Tree. Each branch is composed with the
     * Token.
     * 
     * @param pListOfMeasure The list of <code>String</code>.
     * @return The Tree has a Map.
     */
    Map convertListAsTree(List pListOfMeasure)
    {
        Map tTree = new HashMap();
        mMeasureExtracts = new HashMap();
        String curString;
        Map curMap;
        MeasureExtract tExtract;
        for (Iterator tIt = pListOfMeasure.iterator(); tIt.hasNext();)
        {
            curMap = tTree;
            tExtract = (MeasureExtract) tIt.next();
            mMeasureExtracts.put(tExtract.getName() + tExtract.getGroupName(), tExtract);
            for (StringTokenizer tTokenizer = new StringTokenizer(tExtract.getName() + tExtract.getGroupName(),
                            "."); tTokenizer.hasMoreElements();)
            {
                curString = (String) tTokenizer.nextElement();
                if (curMap.get(curString) == null)
                {
                    curMap.put(curString, new HashMap());
                }
                curMap = (Map) curMap.get(curString);
            }
        }

        return tTree;
    }

    /**
     * Write the Tree as a Html Menu.
     * 
     * @param pCurrentClassName The name of the class that we are building, the <code>List</code> is compose with the
     *        package parts.
     * @param pTreeOfMeasure The Tree to write.
     * @param pCurNodeName The name of the current node.
     * @param pFirstLevel True if the current node is the first level of the menu.
     * @param pLastId The technical identifier to use for the next generation.
     * @return The last technical identifier used.
     */
    int writeMeasuresAsMenu(List pCurrentClassName, Map pTreeOfMeasure, String pCurNodeName, boolean pFirstLevel,
                    int pLastId)
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
                tLastId = writeMeasuresAsMenu(pCurrentClassName, (Map) curEntry.getValue(), (String)curEntry.getKey(), false,
                                ++tLastId);
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
            MeasureExtract tExtract = getExtractForThisNode(pCurrentClassName, pCurNodeName);
            tLinkStat.append("<li><span title=\"GroupName=[").append(tExtract.getGroupName());
            tLinkStat.append("]\">").append(tExtract.getMethodName());
            tLinkStat.append("()</span> ");
            tLinkStat.append("<span title=\"occurrence\">(").append(tExtract.getOccurenceNumber()).append(
                            ")</span>");
            tLinkStat.append("<A title=\"View stats...\" href=\"MeasurePointStat.do?className=");
            tLinkStat.append(getListAsString(pCurrentClassName)).append("&methodName=");
            tLinkStat.append(tExtract.getMethodName()).append("\">");
            tLinkStat.append("<IMG src=\"images/graphique.png\"/></A>");
            tLinkStat.append("</li>\n");
            mWriter.append(tLinkStat.toString());
        }
        return tLastId++;
    }

    private MeasureExtract getExtractForThisNode(List pClassNameAsString, String pCurrentNodeName)
    {
        //Calculation of the full name
        StringBuffer tBuffer = new StringBuffer();
        for (Iterator tIt = pClassNameAsString.iterator(); tIt.hasNext();)
        {
            tBuffer.append(tIt.next());
        }
        tBuffer.append(".").append(pCurrentNodeName);
        //Now return the extract for this name
        return (MeasureExtract) mMeasureExtracts.get(tBuffer.toString());
    }

    private String getListAsString(List pList)
    {
        StringBuffer tBuffer = new StringBuffer();
        for (Iterator tIt = pList.iterator(); tIt.hasNext();)
        {
            tBuffer.append(tIt.next());
        }
        return tBuffer.toString();
    }

}
