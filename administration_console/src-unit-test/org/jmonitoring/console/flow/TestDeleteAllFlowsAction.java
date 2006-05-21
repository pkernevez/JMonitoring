package org.jmonitoring.console.flow;

import org.jmonitoring.core.dto.ExecutionFlowDTO;

import servletunit.struts.MockStrutsTestCase;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

public class TestDeleteAllFlowsAction extends MockStrutsTestCase
{
    public void testDeleteAllFlowsConfirmIn()
    {
        setRequestPathInfo("/DeleteAllFlowsConfirmIn");
        actionPerform();
        // ok this.verifyForward("success");
        // ko verifyForwardPath("changerMotPasse");
        verifyForwardPath("/pages/layout/layout.jsp");
    }
    
    public void testDeleteAllFlowsIn()
    {
        
        FlowBuilderUtil tUtil = new FlowBuilderUtil();
        tUtil.createSchema();
        tUtil.buildAndSaveNewDto();
        tUtil.buildAndSaveNewDto();
        
        assertEquals(2, tUtil.countFlows());
        
        setRequestPathInfo("/DeleteAllFlowsIn");
        actionPerform();
        verifyForwardPath("/pages/layout/layout.jsp");
        
        assertEquals(0, tUtil.countFlows());
        
        
    }
    
}
