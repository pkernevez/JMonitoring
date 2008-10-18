package org.jmonitoring.console.flow;

import org.jmonitoring.common.hibernate.HibernateManager;
import org.jmonitoring.console.JMonitoringMockStrustTestCase;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestDeleteAllFlowsActionIn extends JMonitoringMockStrustTestCase {
    public void testDeleteAllFlowsConfirmIn() {
        setRequestPathInfo("/DeleteAllFlowsConfirmIn");
        actionPerform();
        // ok this.verifyForward("success");
        // ko verifyForwardPath("changerMotPasse");
        verifyForwardPath("/pages/layout/layout.jsp");
    }

    public void testDeleteAllFlowsIn() {

        FlowBuilderUtil tUtil = new FlowBuilderUtil();
        tUtil.createSchema();
        tUtil.buildAndSaveNewDto(2);
        tUtil.buildAndSaveNewDto(2);

        assertEquals(2, tUtil.countFlows());

        setRequestPathInfo("/DeleteAllFlowsIn");
        actionPerform();
        verifyForwardPath("/pages/layout/layout.jsp");

        HibernateManager.getSession().close();
        HibernateManager.getSession().beginTransaction();
        tUtil = new FlowBuilderUtil();
        assertEquals(0, tUtil.countFlows());

    }

}
