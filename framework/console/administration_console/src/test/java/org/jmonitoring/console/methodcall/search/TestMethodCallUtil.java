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

import org.jmonitoring.core.dao.ExecutionFlowDAO;
import org.jmonitoring.core.dao.PersistanceTestCase;
import org.jmonitoring.core.dto.MethodCallExtractDTO;
import org.jmonitoring.core.persistence.ExecutionFlowPO;
import org.jmonitoring.core.persistence.MethodCallPO;

public class TestMethodCallUtil extends PersistanceTestCase
{
    public void testGetListAsTree()
    {
        ExecutionFlowDAO tFlowDAO = new ExecutionFlowDAO(getSession());

        // First delete all flow, we don't use the DeleteAll Method of the
        // Dao Object because, it doesn't support transactions.
        getSession().createQuery("Delete FROM MethodCallPO").executeUpdate();
        getSession().createQuery("Delete FROM ExecutionFlowPO").executeUpdate();

        // Now insert the TestFlow
        ExecutionFlowPO tFlow = buildNewFullFlow();
        tFlowDAO.insertFullExecutionFlow(tFlow);

        List tMeasureExtracts = tFlowDAO.getListOfMethodCallExtract();
        MethodCallExtractDTO curExtrat = (MethodCallExtractDTO) tMeasureExtracts.get(0);
        assertEquals(TestMethodCallUtil.class.getName() + ".builNewFullFlow", curExtrat.getName());
        assertEquals("GrDefault", curExtrat.getGroupName());
        assertEquals(1, curExtrat.getOccurenceNumber());

        curExtrat = (MethodCallExtractDTO) tMeasureExtracts.get(1);
        assertEquals(TestMethodCallUtil.class.getName() + ".builNewFullFlow2", curExtrat.getName());
        assertEquals("GrChild1", curExtrat.getGroupName());
        assertEquals(1, curExtrat.getOccurenceNumber());

        curExtrat = (MethodCallExtractDTO) tMeasureExtracts.get(2);
        assertEquals(TestMethodCallUtil.class.getName() + ".builNewFullFlow3", curExtrat.getName());
        assertEquals("GrChild2", curExtrat.getGroupName());
        assertEquals(1, curExtrat.getOccurenceNumber());
    }

    public void testWriteMeasuresAsMenuEmpty() throws IOException
    {
        List tList = new ArrayList();
        tList.add(new MethodCallExtractDTO("Toto", "getToto", "Grp1", new Integer(2)));
        MethodCallUtil tUtil = new MethodCallUtil();
        Map tListOfAllExtractByFullName = new HashMap();
        MethodCallSearchActionIn.convertListAsTree(tListOfAllExtractByFullName, tList);
        tList = new ArrayList();
        tList.add("Toto");
        tUtil.writeMeasuresAsMenu(tListOfAllExtractByFullName, tList, new HashMap(), "getTotoGrp1", true, 0);
        assertEquals("<li><span title=\"GroupName=[Grp1]\">getToto()</span> " + "<span title=\"occurrence\">(2)</span>"
            + "<A title=\"View stats...\" href=\"MethodCallStatIn.do?className=Toto&methodName=getToto\">"
            + "<IMG src=\"images/graphique.png\"/></A></li>\n", tUtil.toString());
    }

    public void testWriteMeasuresAsMenuNotEmpty2() throws IOException
    {
        List tList = new ArrayList();
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
        Map tListOfAllExtractByFullName = new HashMap();
        MethodCallSearchActionIn.convertListAsTree(tListOfAllExtractByFullName, tList);
        tList = new ArrayList();
        tList.add("Toto");
        tUtil.writeMeasuresAsMenu(tListOfAllExtractByFullName, tList, new HashMap(), "getTotoGrp1", true, 0);
        assertTrue(tUtil.toString().length() > 20);
        assertNotNull(tListOfAllExtractByFullName.get("BE_Configuration.ctor32-DtmBusinessObjects"));

    }

    public void testWriteMeasuresAsMenuNotEmpty() throws IOException
    {
        List tList = new ArrayList();
        tList.add(new MethodCallExtractDTO("org.monitoring.toto.Toto", "getToto", "Grp1", new Integer(1)));
        tList.add(new MethodCallExtractDTO("org.monitoring.toto.Toto", "getTotoBis", "Grp1", new Integer(2)));
        MethodCallUtil tUtil = new MethodCallUtil();
        Map tListOfAllExtractByFullName = new HashMap();
        Map tMap = MethodCallSearchActionIn.convertListAsTree(tListOfAllExtractByFullName, tList);
        tUtil.writeMeasuresAsMenu(tListOfAllExtractByFullName, new ArrayList(), (Map) tMap.get("org"), "org", true, 0);
        assertTrue(tUtil.toString().length() > 20);
    }

    public static ExecutionFlowPO buildNewFullFlow()
    {
        ExecutionFlowPO tFlow;
        MethodCallPO tPoint;
        MethodCallPO tSubPoint;
        long tStartTime = System.currentTimeMillis();

        tPoint = new MethodCallPO(null, TestMethodCallUtil.class.getName(), "builNewFullFlow", "GrDefault", "[]");
        tPoint.setBeginTime(tStartTime);

        tSubPoint = new MethodCallPO(tPoint, TestMethodCallUtil.class.getName(), "builNewFullFlow2", "GrChild1", "[]");
        tSubPoint.setEndTime(System.currentTimeMillis());

        new MethodCallPO(tPoint, TestMethodCallUtil.class.getName(), "builNewFullFlow3", "GrChild2", "[]");
        tPoint.setEndTime(tStartTime + 20);
        tFlow = new ExecutionFlowPO("TEST-main", tPoint, "myJVM");
        return tFlow;
    }

}
