package org.jmonitoring.console.measurepoint;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import org.jmonitoring.core.dao.MeasureExtract;

public class TestMeasureMenuUtil extends TestCase
{
    public void testConvertListAsTree()
    {
        List tList = new ArrayList();
        tList.add(new MeasureExtract("org.monitoring.toto.Toto.getToto", "Grp1", 1));
        tList.add(new MeasureExtract("org.monitoring.toto.Toto.getTotoBis", "Grp1", 2));
        tList.add(new MeasureExtract("org.monitoring.toto.TotoBis.getToto", "Grp1", 3));
        tList.add(new MeasureExtract("org.monitoring.tata.Tata.getTata", "Grp1", 4));
        tList.add(new MeasureExtract("org.monitoring.tata.Toto.getToto", "Grp1", 5));
        tList.add(new MeasureExtract("com.monitoring.Titi.getTiti", "Grp2", 6));

        Map tMap = new MeasureMenuUtil(null).convertListAsTree(tList);
        assertEquals(2, tMap.size());
        HashMap curMap = (HashMap) tMap.get("org");
        assertEquals(1, curMap.size());
        curMap = (HashMap) curMap.get("monitoring");
        assertEquals(2, curMap.size());
        curMap = (HashMap) curMap.get("toto");
        assertEquals(2, curMap.size());
        curMap = (HashMap) curMap.get("Toto");
        assertEquals(2, curMap.size());
        assertEquals(0, ((HashMap) curMap.get("getTotoGrp1")).size());

        assertEquals(0, ((Map) curMap.get("getTotoBisGrp1")).size());

        curMap = (HashMap) tMap.get("org");
        curMap = (HashMap) curMap.get("monitoring");
        curMap = (HashMap) curMap.get("toto");
        curMap = (HashMap) curMap.get("TotoBis");
        assertEquals(1, curMap.size());
        curMap = (HashMap) curMap.get("getTotoGrp1");
        assertEquals(0, curMap.size());

        curMap = (HashMap) tMap.get("org");
        curMap = (HashMap) curMap.get("monitoring");
        curMap = (HashMap) curMap.get("tata");
        assertEquals(2, curMap.size());
        curMap = (HashMap) curMap.get("Tata");
        assertEquals(1, curMap.size());
        curMap = (HashMap) curMap.get("getTataGrp1");
        assertEquals(0, curMap.size());

        curMap = (HashMap) tMap.get("com");
        curMap = (HashMap) curMap.get("monitoring");
        curMap = (HashMap) curMap.get("Titi");
        assertEquals(1, curMap.size());
        curMap = (HashMap) curMap.get("getTitiGrp2");
        assertEquals(0, curMap.size());
    }

    public void testWriteMeasuresAsMenuEmpty() throws IOException
    {
        List tList = new ArrayList();
        tList.add(new MeasureExtract("Toto.getToto", "Grp1", 2));
        StringBuffer tWriter = new StringBuffer();
        MeasureMenuUtil tUtil = new MeasureMenuUtil(tWriter);
        tUtil.convertListAsTree(tList);
        tList = new ArrayList();
        tList.add("Toto");
        tUtil.writeMeasuresAsMenu(tList, new HashMap(), "getTotoGrp1", true, 0);
        assertEquals("<li><span title=\"GroupName=[Grp1]\">getToto()</span> "
                        + "<span title=\"occurrence\">(2)</span>"
                        + "<A title=\"View stats...\" href=\"MeasurePointStat.do?className=Toto&methodName=getToto\">"
                        + "<IMG src=\"images/graphique.png\"/></A></li>\n", tWriter.toString());
    }

    public void testWriteMeasuresAsMenuNotEmpty() throws IOException
    {
        List tList = new ArrayList();
        tList.add(new MeasureExtract("org.monitoring.toto.Toto.getToto", "Grp1", 1));
        tList.add(new MeasureExtract("org.monitoring.toto.Toto.getTotoBis", "Grp1", 2));
        StringBuffer tWriter = new StringBuffer();
        MeasureMenuUtil tUtil = new MeasureMenuUtil(tWriter);
        Map tMap = tUtil.convertListAsTree(tList);
        tUtil.writeMeasuresAsMenu(new ArrayList(), (Map) tMap.get("org"), "org", true, 0);
        assertTrue(tWriter.toString().length() > 20);
    }
}
