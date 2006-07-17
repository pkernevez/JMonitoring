package org.jmonitoring.console.methodcall;

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

public class TestMeasureMenuUtil extends PersistanceTestCase
{
    public void testGetListAsTree()
    {
        ExecutionFlowDAO tFlowDAO = new ExecutionFlowDAO(mSession);

        // First delete all flow, we don't use the DeleteAll Method of the
        // Dao Object because, it doesn't support transactions.
        mSession.createQuery("Delete FROM MethodCallPO").executeUpdate();
        mSession.createQuery("Delete FROM ExecutionFlowPO").executeUpdate();

        // Now insert the TestFlow
        ExecutionFlowPO tFlow = buildNewFullFlow();
        int tFlowId = tFlowDAO.insertFullExecutionFlow(tFlow);

        List tMeasureExtracts = tFlowDAO.getListOfMethodCallExtract();
        MethodCallExtractDTO curExtrat = (MethodCallExtractDTO) tMeasureExtracts.get(0);
        assertEquals("org.jmonitoring.console.methodcall.TestMeasureMenuUtil.builNewFullFlow", curExtrat.getName());
        assertEquals("GrDefault", curExtrat.getGroupName());
        assertEquals(1, curExtrat.getOccurenceNumber());

        curExtrat = (MethodCallExtractDTO) tMeasureExtracts.get(1);
        assertEquals("org.jmonitoring.console.methodcall.TestMeasureMenuUtil.builNewFullFlow2", curExtrat.getName());
        assertEquals("GrChild1", curExtrat.getGroupName());
        assertEquals(1, curExtrat.getOccurenceNumber());

        curExtrat = (MethodCallExtractDTO) tMeasureExtracts.get(2);
        assertEquals("org.jmonitoring.console.methodcall.TestMeasureMenuUtil.builNewFullFlow3", curExtrat.getName());
        assertEquals("GrChild2", curExtrat.getGroupName());
        assertEquals(1, curExtrat.getOccurenceNumber());
    }

    public void testConvertListAsTree()
    {
        List tList = new ArrayList();
        tList.add(new MethodCallExtractDTO("org.monitoring.toto.Toto.getToto", "Grp1", new Integer(1)));
        tList.add(new MethodCallExtractDTO("org.monitoring.toto.Toto.getTotoBis", "Grp1", new Integer(2)));
        tList.add(new MethodCallExtractDTO("org.monitoring.toto.TotoBis.getToto", "Grp1", new Integer(3)));
        tList.add(new MethodCallExtractDTO("org.monitoring.tata.Tata.getTata", "Grp1", new Integer(4)));
        tList.add(new MethodCallExtractDTO("org.monitoring.tata.Toto.getToto", "Grp1", new Integer(5)));
        tList.add(new MethodCallExtractDTO("com.monitoring.Titi.getTiti", "Grp2", new Integer(6)));

        Map tMap = new MethodCallUtil(null).convertListAsTree(tList);
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
        tList.add(new MethodCallExtractDTO("Toto.getToto", "Grp1", new Integer(2)));
        StringBuffer tWriter = new StringBuffer();
        MethodCallUtil tUtil = new MethodCallUtil(tWriter);
        tUtil.convertListAsTree(tList);
        tList = new ArrayList();
        tList.add("Toto");
        tUtil.writeMeasuresAsMenu(tList, new HashMap(), "getTotoGrp1", true, 0);
        assertEquals("<li><span title=\"GroupName=[Grp1]\">getToto()</span> " + "<span title=\"occurrence\">(2)</span>"
                        + "<A title=\"View stats...\" href=\"MethodCallStatIn.do?className=Toto&methodName=getToto\">"
                        + "<IMG src=\"images/graphique.png\"/></A></li>\n", tWriter.toString());
    }

    public void testWriteMeasuresAsMenuNotEmpty() throws IOException
    {
        List tList = new ArrayList();
        tList.add(new MethodCallExtractDTO("org.monitoring.toto.Toto.getToto", "Grp1", new Integer(1)));
        tList.add(new MethodCallExtractDTO("org.monitoring.toto.Toto.getTotoBis", "Grp1", new Integer(2)));
        StringBuffer tWriter = new StringBuffer();
        MethodCallUtil tUtil = new MethodCallUtil(tWriter);
        Map tMap = tUtil.convertListAsTree(tList);
        tUtil.writeMeasuresAsMenu(new ArrayList(), (Map) tMap.get("org"), "org", true, 0);
        assertTrue(tWriter.toString().length() > 20);
    }

    public static ExecutionFlowPO buildNewFullFlow()
    {
        ExecutionFlowPO tFlow;
        MethodCallPO tPoint;
        MethodCallPO tSubPoint;
        long tStartTime = System.currentTimeMillis();

        tPoint = new MethodCallPO(null, TestMeasureMenuUtil.class.getName(), "builNewFullFlow", "GrDefault",
                        new Object[0]);
        tPoint.setBeginTime(tStartTime);

        tSubPoint = new MethodCallPO(tPoint, TestMeasureMenuUtil.class.getName(), "builNewFullFlow2", "GrChild1",
                        new Object[0]);
        tSubPoint.setEndTime(System.currentTimeMillis());

        tSubPoint = new MethodCallPO(tPoint, TestMeasureMenuUtil.class.getName(), "builNewFullFlow3", "GrChild2",
                        new Object[0]);
        tPoint.setEndTime(tStartTime + 20);
        tFlow = new ExecutionFlowPO("TEST-main", tPoint, "myJVM");
        return tFlow;
    }

}
