package org.jmonitoring.console.methodcall;

import org.jmonitoring.console.flow.FlowBuilderUtil;
import org.jmonitoring.core.dto.ExecutionFlowDTO;
import org.jmonitoring.core.dto.MethodCallDTO;

import servletunit.struts.MockStrutsTestCase;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestMethodCallStatActionIn extends MockStrutsTestCase
{
    public void testOkDefaultInterval()
    {
        FlowBuilderUtil tUtil = new FlowBuilderUtil();
        tUtil.createSchema();
        ExecutionFlowDTO tFlow = tUtil.buildAndSaveNewDto(2);
        MethodCallDTO tFirstMeth = tFlow.getFirstMethodCall();
        MethodCallDTO tFirstChild = tFirstMeth.getChild(0);
        int tId = tFirstChild.getId();

        setRequestPathInfo("/MethodCallStatIn.do");
        MethodCallStatForm tForm = new MethodCallStatForm();
        tForm.setId(tId);
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
        assertEquals(2, tForm.getNbMeasures());
        byte[] tByte = (byte[]) getSession().getAttribute(MethodCallStatActionIn.FULL_DURATION_STAT);
        assertNotNull(tByte);
        assertTrue(tByte.length > 10);
    }

}
