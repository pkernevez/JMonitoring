package org.jmonitoring.console.methodcall;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jmonitoring.core.dto.MethodCallDTO;
import org.jmonitoring.core.process.JMonitoringProcess;
import org.jmonitoring.core.process.ProcessFactory;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class MethodCallEditActionIn extends Action {
    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
            HttpServletResponse pResponse) {
        JMonitoringProcess tProcess = ProcessFactory.getInstance();
        // List tList = new ArrayList();
        MethodCallEditForm tForm = (MethodCallEditForm) pForm;

        MethodCallDTO tMeth = tProcess.readFullMethodCall(tForm.getFlowId(), tForm.getPosition());

        tForm.setMethodCall(tMeth);
        return pMapping.findForward("success");
    }
}