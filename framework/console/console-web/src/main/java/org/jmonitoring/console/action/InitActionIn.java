package org.jmonitoring.console.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jmonitoring.console.AbstractSpringAction;
import org.jmonitoring.core.configuration.SpringConfigurationUtil;
import org.jmonitoring.core.process.ConsoleManager;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class InitActionIn extends AbstractSpringAction
{
    @Override
    public ActionForward executeWithSpringContext(ActionMapping pMapping, ActionForm pForm,
        HttpServletRequest pRequest, HttpServletResponse pResponse)
    {
        ConsoleManager tProcess = (ConsoleManager) SpringConfigurationUtil.getBean("consoleManager");
        if (!tProcess.doDatabaseExist())
        {
            tProcess.createDataBase();
        }
        return pMapping.findForward("success");
    }

}
