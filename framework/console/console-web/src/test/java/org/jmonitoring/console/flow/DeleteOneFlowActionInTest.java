package org.jmonitoring.console.flow;

import org.jmonitoring.console.JMonitoringMockStrustTestCase;
import org.jmonitoring.core.dto.ExecutionFlowDTO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class DeleteOneFlowActionInTest extends JMonitoringMockStrustTestCase
{
    @Autowired
    FlowBuilderUtil mUtil;

    @Test
    public void testDeleteAllFlowsInOk()
    {

        ExecutionFlowDTO tFirstDto = mUtil.buildAndSaveNewDto(2);
        mUtil.buildAndSaveNewDto(2);

        assertEquals(2, mUtil.countFlows());

        FlowIdForm tForm = new FlowIdForm();
        tForm.setId(tFirstDto.getId());

        // Delete First Flow
        setRequestPathInfo("/DeleteOneFlowIn");
        setActionForm(tForm);
        actionPerform();
        verifyForwardPath("/pages/layout/layout.jsp");

        assertEquals(1, mUtil.countFlows());
    }

    @Test
    public void testDeleteAllFlowsInBadId()
    {

        mUtil.buildAndSaveNewDto(2);
        mUtil.buildAndSaveNewDto(2);

        assertEquals(2, mUtil.countFlows());

        FlowIdForm tForm = new FlowIdForm();
        tForm.setId(34567);

        // Delete bad flowId, it has already been deleted
        setRequestPathInfo("/DeleteOneFlowIn");
        setActionForm(tForm);
        actionPerform();
        verifyForwardPath("/pages/functionalError.jsp");
        verifyActionErrors(new String[] {"errors.executionflow.notfound" });

        assertEquals(2, mUtil.countFlows());
    }

}
