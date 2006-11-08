package org.jmonitoring.console.flow.edit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

public class ImportFlowActionOut extends Action
{
    public ActionForward execute(ActionMapping pMpping, ActionForm pForm, HttpServletRequest pRequest,
                    HttpServletResponse pResponse) throws Exception
    {
        ImportFlowForm tForm = (ImportFlowForm) pForm;

        // Process the FormFile
        FormFile myFile = tForm.getTheFile();
        String contentType = myFile.getContentType();
        String fileName = myFile.getFileName();
        int fileSize = myFile.getFileSize();
        byte[] fileData = myFile.getFileData();
        System.out.println("contentType: " + contentType);
        System.out.println("File Name: " + fileName);
        System.out.println("File Size: " + fileSize);

        return pMpping.findForward("success");
    }

}
