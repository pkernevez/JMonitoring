package org.jmonitoring.console.flow;

import org.jmonitoring.console.JMonitoringMockStrustTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class DeleteAllFlowsActionInTest extends JMonitoringMockStrustTestCase
{
    @Autowired
    FlowBuilderUtil mUtil;

    @Test
    public void testDeleteAllFlowsConfirmIn()
    {
        setRequestPathInfo("/DeleteAllFlowsConfirmIn");
        actionPerform();
        // ok this.verifyForward("success");
        // ko verifyForwardPath("changerMotPasse");
        verifyForwardPath("/pages/layout/layout.jsp");
    }

    @Test
    public void testDeleteAllFlowsIn()
    {

        mUtil.buildAndSaveNewDto(2);
        mUtil.buildAndSaveNewDto(2);

        assertEquals(2, mUtil.countFlows());

        setRequestPathInfo("/DeleteAllFlowsIn");
        actionPerform();
        verifyForwardPath("/pages/layout/layout.jsp");

        assertEquals(0, mUtil.countFlows());

    }

}
