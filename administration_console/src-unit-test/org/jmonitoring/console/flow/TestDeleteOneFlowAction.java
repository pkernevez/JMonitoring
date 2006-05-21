package org.jmonitoring.console.flow;

import org.jmonitoring.core.dto.ExecutionFlowDTO;

import servletunit.struts.MockStrutsTestCase;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

public class TestDeleteOneFlowAction extends MockStrutsTestCase
{
    public void testDeleteAllFlowsInOk()
    {
        
        FlowBuilderUtil tUtil = new FlowBuilderUtil();
        tUtil.createSchema();
        ExecutionFlowDTO tFirstDto = tUtil.buildAndSaveNewDto();
        ExecutionFlowDTO tSecondDto = tUtil.buildAndSaveNewDto();
        
        assertEquals(2, tUtil.countFlows());
        
        FlowIdForm tForm = new FlowIdForm();
        tForm.setId(tFirstDto.getId());
        
        // Delete First Flow
        setRequestPathInfo("/DeleteOneFlowIn");
        setActionForm(tForm);
        actionPerform();
        verifyForwardPath("/pages/layout/layout.jsp");
        
        assertEquals(1, tUtil.countFlows());
        
        //Delete bad flowId, it has already been deleted
        setRequestPathInfo("/DeleteOneFlowIn");
        setActionForm(tForm);
        actionPerform();
        verifyForwardPath("/badflowid");
        
        
    }
    
}
