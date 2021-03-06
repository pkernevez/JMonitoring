package org.jmonitoring.console.flow;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.jmonitoring.console.AbstractSpringAction;
import org.jmonitoring.core.common.UnknownFlowException;
import org.jmonitoring.core.configuration.SpringConfigurationUtil;
import org.jmonitoring.core.process.ConsoleManager;

/**
 * @author pke
 * 
 * @todo To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 *       Templates
 */
public class DeleteOneFlowActionIn extends AbstractSpringAction
{

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse) @todo Manage Exception with a good error message...
     */
    @Override
    public ActionForward executeWithSpringContext(ActionMapping pMapping, ActionForm pForm,
        HttpServletRequest pRequest, HttpServletResponse pResponse)
    {
        FlowIdForm tForm = (FlowIdForm) pForm;
        ConsoleManager tProcess = (ConsoleManager) SpringConfigurationUtil.getBean("consoleManager");

        try
        {
            tProcess.deleteFlow(tForm.getId());
            return pMapping.findForward("success");
        } catch (UnknownFlowException e)
        {
            ActionMessages errors = new ActionMessages();
            ActionMessage error = new ActionMessage("errors.executionflow.notfound", new Integer(tForm.getId()));
            errors.add("msg1", error);
            saveErrors(pRequest, errors);
            return pMapping.findForward("invalid");
        }
    }
}
