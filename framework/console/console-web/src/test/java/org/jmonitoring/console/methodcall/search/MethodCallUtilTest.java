package org.jmonitoring.console.methodcall.search;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jmonitoring.console.JMonitoringMockStrustTestCase;
import org.jmonitoring.console.methodcall.search.MethodCallUtil.MyHashMap;
import org.jmonitoring.console.methodcall.search.MethodCallUtil.MyMap;
import org.jmonitoring.core.dao.ConsoleDao;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.jmonitoring.core.dto.MethodCallExtractDTO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MethodCallUtilTest extends JMonitoringMockStrustTestCase
{
    @Autowired
    ConsoleDao mFlowDao;

    @Test
    public void testGetListAsTree()
    {
        // Now insert the TestFlow
        ExecutionFlowPO tFlow = buildNewFullFlow();
        mFlowDao.insertFullExecutionFlow(tFlow);

        List<MethodCallExtractDTO> tMeasureExtracts = mFlowDao.getListOfMethodCallExtractOld();
        MethodCallExtractDTO curExtrat = tMeasureExtracts.get(0);
        assertEquals(MethodCallUtilTest.class.getName() + ".builNewFullFlow", curExtrat.getName());
        assertEquals("GrDefault", curExtrat.getGroupName());
        assertEquals(1, curExtrat.getOccurenceNumber());

        curExtrat = tMeasureExtracts.get(1);
        assertEquals(MethodCallUtilTest.class.getName() + ".builNewFullFlow2", curExtrat.getName());
        assertEquals("GrChild1", curExtrat.getGroupName());
        assertEquals(1, curExtrat.getOccurenceNumber());

        curExtrat = tMeasureExtracts.get(2);
        assertEquals(MethodCallUtilTest.class.getName() + ".builNewFullFlow3", curExtrat.getName());
        assertEquals("GrChild2", curExtrat.getGroupName());
        assertEquals(1, curExtrat.getOccurenceNumber());
    }

    @Test
    public void testWriteMeasuresAsMenuEmpty() throws IOException
    {
        List<MethodCallExtractDTO> tList = new ArrayList<MethodCallExtractDTO>();
        tList.add(new MethodCallExtractDTO("Toto", "getToto", "Grp1", new Integer(2)));
        MethodCallUtil tUtil = new MethodCallUtil();
        Map<String, MethodCallExtractDTO> tListOfAllExtractByFullName = new HashMap<String, MethodCallExtractDTO>();
        MethodCallSearchActionIn.convertListAsTree(tListOfAllExtractByFullName, tList);
        List<String> tList2 = new ArrayList<String>();
        tList2.add("Toto");
        tUtil.writeMeasuresAsMenu(tListOfAllExtractByFullName, tList2, new MyHashMap(), "getTotoGrp1", true, 0);
        assertEquals("<li><span title=\"GroupName=[Grp1]\">getToto()</span> " + "<span title=\"occurrence\">(2)</span>"
            + "<A title=\"View stats...\" href=\"MethodCallStatIn.do?className=Toto&methodName=getToto\">"
            + "<IMG src=\"images/graphique.png\"/></A></li>\n", tUtil.toString());
    }

    @Test
    public void testWriteMeasuresAsMenuNotEmpty2() throws IOException
    {
        List<MethodCallExtractDTO> tList = new ArrayList<MethodCallExtractDTO>();
        tList.add(new MethodCallExtractDTO("Toto", "getToto", "Grp1", new Integer(2)));
        tList.add(new MethodCallExtractDTO("_Default", "InitializeComponent", "1-Philae Metier", new Integer(1)));
        tList.add(new MethodCallExtractDTO("BE_Configuration", ".ctor", "32-Dtm.BusinessObjects", new Integer(2)));
        tList.add(new MethodCallExtractDTO("BP_Administration", "RechercherUneConfigurationParCode", "2-Process",
                                           new Integer(2)));
        tList.add(new MethodCallExtractDTO("BP_Administration", "RechercherUneDateCouranteParModule", "2-Process",
                                           new Integer(2)));
        tList.add(new MethodCallExtractDTO("Connexion", "InitializeComponent", "1-Philae Metier", new Integer(2)));
        tList.add(new MethodCallExtractDTO("Constantes", ".cctor", "2-Process", new Integer(1)));
        tList.add(new MethodCallExtractDTO("Database", "Connect", "2-Process", new Integer(4)));
        tList.add(new MethodCallExtractDTO("Global", "InitializeComponent", "1-Philae Metier", new Integer(2)));
        tList.add(new MethodCallExtractDTO("Global", "Request : /Philae_Audit_3/Connexion.aspx", "Request",
                                           new Integer(2)));
        tList.add(new MethodCallExtractDTO("SqlCommand", "ExecuteReader", "4 - System.Data", new Integer(18)));
        tList.add(new MethodCallExtractDTO("Tools", "FillDDL", "1-Philae Metier", new Integer(2)));

        MethodCallUtil tUtil = new MethodCallUtil();
        Map<String, MethodCallExtractDTO> tListOfAllExtractByFullName = new HashMap<String, MethodCallExtractDTO>();
        MethodCallSearchActionIn.convertListAsTree(tListOfAllExtractByFullName, tList);
        List<String> tList2 = new ArrayList<String>();
        tList2.add("Toto");
        tUtil.writeMeasuresAsMenu(tListOfAllExtractByFullName, tList2, new MyHashMap(), "getTotoGrp1", true, 0);
        assertTrue(tUtil.toString().length() > 20);
        assertNotNull(tListOfAllExtractByFullName.get("BE_Configuration.ctor32-DtmBusinessObjects"));

    }

    @Test
    public void testWriteMeasuresAsMenuNotEmpty() throws IOException
    {
        List<MethodCallExtractDTO> tList = new ArrayList<MethodCallExtractDTO>();
        tList.add(new MethodCallExtractDTO("org.monitoring.toto.Toto", "getToto", "Grp1", new Integer(1)));
        tList.add(new MethodCallExtractDTO("org.monitoring.toto.Toto", "getTotoBis", "Grp1", new Integer(2)));
        MethodCallUtil tUtil = new MethodCallUtil();
        Map<String, MethodCallExtractDTO> tListOfAllExtractByFullName = new HashMap<String, MethodCallExtractDTO>();
        MyMap tMap = MethodCallSearchActionIn.convertListAsTree(tListOfAllExtractByFullName, tList);
        tUtil
             .writeMeasuresAsMenu(tListOfAllExtractByFullName, new ArrayList<String>(), tMap.get("org"), "org", true, 0);
        assertTrue(tUtil.toString().length() > 20);
    }

    public static ExecutionFlowPO buildNewFullFlow()
    {
        ExecutionFlowPO tFlow;
        MethodCallPO tPoint;
        MethodCallPO tSubPoint;
        long tStartTime = System.currentTimeMillis();

        tPoint = new MethodCallPO(null, MethodCallUtilTest.class.getName(), "builNewFullFlow", "GrDefault", "[]");
        tPoint.setBeginTime(tStartTime);

        tSubPoint = new MethodCallPO(tPoint, MethodCallUtilTest.class.getName(), "builNewFullFlow2", "GrChild1", "[]");
        tSubPoint.setEndTime(System.currentTimeMillis());

        new MethodCallPO(tPoint, MethodCallUtilTest.class.getName(), "builNewFullFlow3", "GrChild2", "[]");
        tPoint.setEndTime(tStartTime + 20);
        tFlow = new ExecutionFlowPO("TEST-main", tPoint, "myJVM");
        return tFlow;
    }

}
