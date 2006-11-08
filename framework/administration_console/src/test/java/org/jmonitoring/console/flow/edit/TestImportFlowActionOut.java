package org.jmonitoring.console.flow.edit;

import org.apache.struts.upload.FormFile;
import org.jmonitoring.console.flow.FlowBuilderUtil;
import org.jmonitoring.console.flow.jfreechart.FlowUtil;
import org.jmonitoring.core.dto.ExecutionFlowDTO;
import org.jmonitoring.core.process.JMonitoringProcess;
import org.jmonitoring.core.process.ProcessFactory;

import servletunit.struts.MockStrutsTestCase;

public class TestImportFlowActionOut extends MockStrutsTestCase
{

    public void testActionIn()
    {
        FlowBuilderUtil tUtil = new FlowBuilderUtil();
        tUtil.createSchema();
        ExecutionFlowDTO tFirstDto = tUtil.buildAndSaveNewDto(10);
        setRequestPathInfo("/ImportFlowIn");
        actionPerform();
        verifyForwardPath("/pages/layout/layout.jsp");
    }
    
    public void testActionOut()
    {
        FlowBuilderUtil tUtil = new FlowBuilderUtil();
        tUtil.createSchema();
        ExecutionFlowDTO tFirstDto = tUtil.buildAndSaveNewDto(10);

        ImportFlowForm tForm = new ImportFlowForm();
        JMonitoringProcess tProcess = ProcessFactory.getInstance();

        // Delete First Flow
        setRequestPathInfo("/ImportFlowOut");
//        setActionForm(tForm);
        actionPerform();
//        verifyForwardPath("/pages/layout/layout.jsp");
        verifyForwardPath("/Error.do");
        //verifyInputTilesForward("importflow");
//        assertNotNull(getSession().getAttribute(FlowUtil.DURATION_IN_GROUP));

    }
}
