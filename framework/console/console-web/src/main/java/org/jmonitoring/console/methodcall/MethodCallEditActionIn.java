package org.jmonitoring.console.methodcall;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.ObjectNotFoundException;
import org.jmonitoring.console.AbstractSpringAction;
import org.jmonitoring.core.configuration.SpringConfigurationUtil;
import org.jmonitoring.core.dto.MethodCallDTO;
import org.jmonitoring.core.process.ConsoleManager;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class MethodCallEditActionIn extends AbstractSpringAction
{
    public static final String NAV_ACTION_NEXT_IN_THREAD = "NextInThread";

    public static final String NAV_ACTION_PREV_IN_THREAD = "PrevInThread";

    public static final String NAV_ACTION_NEXT_IN_GROUP = "NextInGroup";

    public static final String NAV_ACTION_PREV_IN_GROUP = "PrevInGroup";

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
        MethodCallDTO tMeth = null;
        try
        {
            if (NAV_ACTION_PREV_IN_GROUP.equals(tForm.getNavAction()))
            {
                MethodCallDTO tCurMetho = tProcess.readMethodCall(tForm.getFlowId(), tForm.getPosition());
                tMeth = tProcess.readPrevMethodCall(tForm.getFlowId(), tForm.getPosition(), tCurMetho.getGroupName());
            } else if (NAV_ACTION_PREV_IN_THREAD.equals(tForm.getNavAction()))
            {
                tMeth = tProcess.readFullMethodCall(tForm.getFlowId(), tForm.getPosition() - 1);
            } else if (NAV_ACTION_NEXT_IN_THREAD.equals(tForm.getNavAction()))
            {
                tMeth = tProcess.readFullMethodCall(tForm.getFlowId(), tForm.getPosition() + 1);
            } else if (NAV_ACTION_NEXT_IN_GROUP.equals(tForm.getNavAction()))
            {
                MethodCallDTO tCurMetho = tProcess.readMethodCall(tForm.getFlowId(), tForm.getPosition());
                tMeth = tProcess.readNextMethodCall(tForm.getFlowId(), tForm.getPosition(), tCurMetho.getGroupName());
            }
        } catch (ObjectNotFoundException e)
        {
            tForm.setMsg("You reach the bound of the call list.");
        }
        if (tMeth == null)
        {
            tMeth = tProcess.readFullMethodCall(tForm.getFlowId(), tForm.getPosition());
        }
        tForm.setMethodCall(tMeth);
        return pMapping.findForward("success");
    }
}