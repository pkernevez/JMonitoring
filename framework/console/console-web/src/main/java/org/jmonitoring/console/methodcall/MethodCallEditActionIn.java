package org.jmonitoring.console.methodcall;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jmonitoring.console.AbstractSpringAction;
import org.jmonitoring.core.configuration.SpringConfigurationUtil;
import org.jmonitoring.core.dto.MethodCallDTO;
import org.jmonitoring.core.process.ConsoleManager;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class MethodCallEditActionIn extends AbstractSpringAction
{
    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward executeWithSpringContext(ActionMapping pMapping, ActionForm pForm,
        HttpServletRequest pRequest, HttpServletResponse pResponse)
    {
        ConsoleManager tProcess = (ConsoleManager) SpringConfigurationUtil.getBean("consoleManager");
        // List tList = new ArrayList();
        MethodCallEditForm tForm = (MethodCallEditForm) pForm;

        MethodCallDTO tMeth = tProcess.readFullMethodCall(tForm.getFlowId(), tForm.getPosition());
        tForm.setMethodCall(tMeth);
        return pMapping.findForward("success");
    }
}