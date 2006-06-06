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
        int tId = tFirstChild.getSequenceId();

        setRequestPathInfo("/MethodCallEditIn");
        MethodCallForm tForm = new MethodCallForm();
        tForm.setId(tId);
        setActionForm(tForm);
        assertNull(tForm.getMethodCall());
        actionPerform();
        // ok this.verifyForward("success");
        // ko verifyForwardPath("changerMotPasse");
        verifyForwardPath("/pages/layout/layout.jsp");
        MethodCallDTO tNewFirstChild = ((MethodCallForm) getActionForm()).getMethodCall();
        assertNotNull(tNewFirstChild);
        assertEquals(tFlow.getId(), tNewFirstChild.getFlowId());
        assertNotNull(tNewFirstChild.getParent());
        assertEquals(tFirstChild.getSequenceId(), tNewFirstChild.getSequenceId());

    }

    public void testKo()
    {
        FlowBuilderUtil tUtil = new FlowBuilderUtil();
        tUtil.createSchema();

        setRequestPathInfo("/MethodCallEditIn");
        MethodCallForm tForm = new MethodCallForm();
        tForm.setId(567749375);
        setActionForm(tForm);
        assertNull(tForm.getMethodCall());
        actionPerform();
        // ok this.verifyForward("success");
        // ko verifyForwardPath("changerMotPasse");
        verifyForwardPath("/Error.do");
        assertNull(((MethodCallForm) getActionForm()).getMethodCall());
    }

}
