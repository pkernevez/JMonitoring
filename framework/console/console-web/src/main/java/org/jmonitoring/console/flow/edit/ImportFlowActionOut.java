package org.jmonitoring.console.flow.edit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.jmonitoring.core.dto.ExecutionFlowDTO;
import org.jmonitoring.core.process.ConsoleManager;
import org.jmonitoring.core.process.ProcessFactory;

public class ImportFlowActionOut extends Action {
    public ActionForward execute(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
            HttpServletResponse pResponse) throws Exception {
        ImportFlowForm tForm = (ImportFlowForm) pForm;

        FormFile myFile = tForm.getTheFile();
        byte[] tFileData = myFile.getFileData();

        ConsoleManager tProc = ProcessFactory.getInstance();
        ExecutionFlowDTO tNewFlow = tProc.insertFlowFromXml(tFileData);
        tForm.setNewExecutionFlow(tNewFlow);

        return pMapping.findForward("success");
    }

}
