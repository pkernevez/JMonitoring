package org.jmonitoring.console.flow;

import org.jmonitoring.console.JMonitoringMockStrustTestCase;
import org.jmonitoring.core.dto.ExecutionFlowDTO;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestDeleteOneFlowActionIn extends JMonitoringMockStrustTestCase
{
    public void testDeleteAllFlowsInOk()
    {

        FlowBuilderUtil tUtil = new FlowBuilderUtil();
        tUtil.createSchema();
        ExecutionFlowDTO tFirstDto = tUtil.buildAndSaveNewDto(2);
        tUtil.buildAndSaveNewDto(2);

        assertEquals(2, tUtil.countFlows());

        FlowIdForm tForm = new FlowIdForm();
        tForm.setId(tFirstDto.getId());

        // Delete First Flow
        setRequestPathInfo("/DeleteOneFlowIn");
        setActionForm(tForm);
        actionPerform();
        verifyForwardPath("/pages/layout/layout.jsp");

        closeAndRestartSession();
        assertEquals(1, tUtil.countFlows());
    }

    public void testDeleteAllFlowsInBadId()
    {

        FlowBuilderUtil tUtil = new FlowBuilderUtil();
        tUtil.createSchema();
        tUtil.buildAndSaveNewDto(2);
        tUtil.buildAndSaveNewDto(2);

        closeAndRestartSession();

        assertEquals(2, tUtil.countFlows());

        FlowIdForm tForm = new FlowIdForm();
        tForm.setId(34567);

        // Delete bad flowId, it has already been deleted
        setRequestPathInfo("/DeleteOneFlowIn");
        setActionForm(tForm);
        actionPerform();
        verifyForwardPath("/pages/functionalError.jsp");
        verifyActionErrors(new String[] {"errors.executionflow.notfound" });

        closeAndRestartSession();
        assertEquals(2, tUtil.countFlows());
    }

}
