package org.jmonitoring.console.methodcall.list;

import org.jmonitoring.console.flow.FlowBuilderUtil;
import org.jmonitoring.core.dto.ExecutionFlowDTO;
import org.jmonitoring.core.dto.MethodCallDTO;
import org.jmonitoring.core.dto.MethodCallFullExtractDTO;

import servletunit.struts.MockStrutsTestCase;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestMethodCallListActionIn extends MockStrutsTestCase
{
    public void testOk()
    {
        FlowBuilderUtil tUtil = new FlowBuilderUtil();
        tUtil.createSchema();
        ExecutionFlowDTO tFlow = tUtil.buildAndSaveNewDto(2);
        MethodCallDTO tFirstMeth = tFlow.getFirstMethodCall();
        MethodCallDTO tFirstChild = tFirstMeth.getChild(0);
        int tId = tFirstChild.getId();

        setRequestPathInfo("/MethodCallListIn");
        MethodCallListForm tForm = new MethodCallListForm();
        tForm.setClassName(FlowBuilderUtil.class.getName());
        tForm.setMethodName("builNewFullFlow3");
        tForm.setDurationMin(3);
        tForm.setDurationMax(5);
        setActionForm(tForm);

        actionPerform();
        verifyForward("success");
        verifyForwardPath("/pages/layout/layout.jsp");

        tForm = (MethodCallListForm) getActionForm();
        assertEquals(2, tForm.getSearchResult().size());
        assertEquals(MethodCallFullExtractDTO.class.getName(), tForm.getSearchResult().get(0).getClass().getName());
        MethodCallFullExtractDTO tMeth = (MethodCallFullExtractDTO) tForm.getSearchResult().get(0);
        assertEquals(1, tMeth.getFlowId());
        assertEquals("TEST-main", tMeth.getThreadName());
        assertEquals(30, tMeth.getFlowDuration());
        assertNotNull(tMeth.getBeginDate());
        assertEquals(4, tMeth.getDuration());
        assertEquals("myJVM", tMeth.getJvmName());

    }
}