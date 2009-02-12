package org.jmonitoring.console.methodcall;

import org.hibernate.stat.Statistics;
import org.jmonitoring.console.JMonitoringMockStrustTestCase;
import org.jmonitoring.console.flow.FlowBuilderUtil;
import org.jmonitoring.core.dto.ExecutionFlowDTO;
import org.jmonitoring.core.dto.MethodCallDTO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class MethodCallStatActionInTest extends JMonitoringMockStrustTestCase
{
    @Autowired
    FlowBuilderUtil mUtil;

    @Test
    public void testOkDefaultInterval()
    {
        ExecutionFlowDTO tFlow = mUtil.buildAndSaveNewDto(5);
        MethodCallDTO tFirstMeth = tFlow.getFirstMethodCall();
        MethodCallDTO tFirstChild = tFirstMeth.getChild(0);
        int tId = tFirstChild.getPosition();
        Statistics tStats = mSessionFactory.getStatistics();
        tStats.clear();
        getSessionHib().flush();
        getSessionHib().clear();

        setRequestPathInfo("/MethodCallStatIn.do");
        MethodCallStatForm tForm = new MethodCallStatForm();
        tForm.setPosition(tId);
        tForm.setFlowId(tFlow.getId());

        setActionForm(tForm);

        actionPerform();
        verifyForward("success");

        tForm = (MethodCallStatForm) getActionForm();
        assertEquals(1, tForm.getInterval());
        assertEquals(4, tForm.getDurationAvg(), 0.0001);
        assertEquals(0, tForm.getDurationDev(), 0.0001);
        assertEquals(4, tForm.getDurationMax());
        assertEquals(4, tForm.getDurationMin());
        assertEquals("org.jmonitoring.console.flow.FlowBuilderUtil", tForm.getClassName());
        assertEquals("builNewFullFlow3", tForm.getMethodName());
        assertNotNull(tForm.getImageMap());
        assertEquals(5, tForm.getNbMeasures());
        byte[] tByte = (byte[]) getSession().getAttribute(MethodCallStatActionIn.FULL_DURATION_STAT);
        assertNotNull(tByte);
        assertTrue(tByte.length > 10);

        // On vérifie les accès en base.
        // System.out.println("Statistiques:");
        // for (StringTokenizer tToken = new StringTokenizer(tStats.toString(), ","); tToken.hasMoreTokens();)
        // {
        // System.out.println(tToken.nextElement());
        // }
        tStats = mSessionFactory.getStatistics();
        assertEquals(0, tStats.getCollectionFetchCount());
        assertEquals(0, tStats.getCollectionLoadCount());
        assertEquals(0, tStats.getEntityFetchCount());
        assertTrue("Bad load number =" + tStats.getEntityLoadCount(), tStats.getEntityLoadCount() >= 5);
        assertEquals(2, tStats.getQueryExecutionCount());
        assertEquals(2, tStats.getPrepareStatementCount());

    }

}
