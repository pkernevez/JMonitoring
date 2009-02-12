package org.jmonitoring.console.flow.edit;

import org.jmonitoring.console.JMonitoringMockStrustTestCase;
import org.jmonitoring.console.flow.FlowBuilderUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ImportFlowActionOutTest extends JMonitoringMockStrustTestCase
{
    @Autowired
    FlowBuilderUtil mUtil;

    @Test
    public void testActionIn()
    {
        mUtil.buildAndSaveNewDto(10);

        setRequestPathInfo("/ImportFlowIn");
        actionPerform();
        verifyForwardPath("/pages/layout/layout.jsp");
    }

    @Test
    public void testActionOut()
    {
        mUtil.buildAndSaveNewDto(10);

        // Delete First Flow
        setRequestPathInfo("/ImportFlowOut");
        // setActionForm(tForm);
        actionPerform();
        // verifyForwardPath("/pages/layout/layout.jsp");
        verifyForwardPath("/Error.do");
        // verifyInputTilesForward("importflow");
        // assertNotNull(getSession().getAttribute(FlowUtil.DURATION_IN_GROUP));

    }

}
