package org.jmonitoring.console.flow.edit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.jmonitoring.console.AbstractSpringAction;
import org.jmonitoring.core.configuration.SpringConfigurationUtil;
import org.jmonitoring.core.dto.ExecutionFlowDTO;
import org.jmonitoring.core.process.ConsoleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImportFlowActionOut extends AbstractSpringAction
{
    private static Logger sLog = LoggerFactory.getLogger(ImportFlowActionOut.class);
    
    @Override
    public ActionForward executeWithSpringContext(ActionMapping pMapping, ActionForm pForm,
        HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception
    {

        ConsoleManager tConsoleManager = (ConsoleManager) SpringConfigurationUtil.getBean("consoleManager");

        ImportFlowForm tForm = (ImportFlowForm) pForm;

        try {
        FormFile myFile = tForm.getTheFile();
        byte[] tFileData = myFile.getFileData();

        ExecutionFlowDTO tNewFlow = tConsoleManager.insertFlowFromXml(tFileData);
        tForm.setNewExecutionFlow(tNewFlow);

        return pMapping.findForward("success");
        } catch(RuntimeException e){
            sLog.error("Unable to insertFlow",e);
            throw e;
        }
    }

}
