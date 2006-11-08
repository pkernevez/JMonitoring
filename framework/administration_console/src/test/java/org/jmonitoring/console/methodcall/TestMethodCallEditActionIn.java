package org.jmonitoring.console.methodcall;

import org.jmonitoring.console.flow.FlowBuilderUtil;
import org.jmonitoring.core.dto.ExecutionFlowDTO;
import org.jmonitoring.core.dto.MethodCallDTO;

import servletunit.struts.MockStrutsTestCase;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestMethodCallEditActionIn extends MockStrutsTestCase
{

    public void testOk()
    {
        FlowBuilderUtil tUtil = new FlowBuilderUtil();
        tUtil.createSchema();
        ExecutionFlowDTO tFlow = tUtil.buildAndSaveNewDto(2);
        MethodCallDTO tFirstMeth = tFlow.getFirstMethodCall();
        MethodCallDTO tFirstChild = tFirstMeth.getChild(0);
        int tId = tFirstChild.getPosition();

        setRequestPathInfo("/MethodCallEditIn");
        MethodCallEditForm tForm = new MethodCallEditForm();
        tForm.setPosition(tId);
        tForm.setFlowId(tFlow.getId());
        setActionForm(tForm);
        assertNull(tForm.getMethodCall());

        actionPerform();
        verifyForward("success");

        MethodCallDTO tNewFirstChild = ((MethodCallEditForm) getActionForm()).getMethodCall();
        assertNotNull(tNewFirstChild);
        assertEquals(tFlow.getId(), tNewFirstChild.getFlowId());
        assertNotNull(tNewFirstChild.getParent());
        assertEquals(tFirstChild.getPosition(), tNewFirstChild.getPosition());

    }

    public void testKo()
    {
        FlowBuilderUtil tUtil = new FlowBuilderUtil();
        tUtil.createSchema();

        setRequestPathInfo("/MethodCallEditIn");
        MethodCallEditForm tForm = new MethodCallEditForm();
        tForm.setPosition(567749375);
        setActionForm(tForm);
        assertNull(tForm.getMethodCall());
        actionPerform();
        // ok this.verifyForward("success");
        // ko verifyForwardPath("changerMotPasse");
        verifyForwardPath("/Error.do");
        assertNull(((MethodCallEditForm) getActionForm()).getMethodCall());
    }

}
