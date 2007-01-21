package org.jmonitoring.console.flow.edit;

import org.jmonitoring.console.flow.FlowBuilderUtil;

import servletunit.struts.MockStrutsTestCase;

public class TestImportFlowActionOut extends MockStrutsTestCase
{

    public void testActionIn()
    {
        FlowBuilderUtil tUtil = new FlowBuilderUtil();
        tUtil.createSchema();
        tUtil.buildAndSaveNewDto(10);
        setRequestPathInfo("/ImportFlowIn");
        actionPerform();
        verifyForwardPath("/pages/layout/layout.jsp");
    }

    public void testActionOut()
    {
        FlowBuilderUtil tUtil = new FlowBuilderUtil();
        tUtil.createSchema();
        tUtil.buildAndSaveNewDto(10);

//        ImportFlowForm tForm = new ImportFlowForm();
//        JMonitoringProcess tProcess = ProcessFactory.getInstance();

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
