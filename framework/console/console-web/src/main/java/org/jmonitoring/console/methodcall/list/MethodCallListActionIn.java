package org.jmonitoring.console.methodcall.list;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jmonitoring.console.AbstractSpringAction;
import org.jmonitoring.core.configuration.SpringConfigurationUtil;
import org.jmonitoring.core.process.ConsoleManager;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * @author pke
 */
public class MethodCallListActionIn extends AbstractSpringAction
{
    @Override
    public ActionForward executeWithSpringContext(ActionMapping pMapping, ActionForm pForm,
        HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception
    {
        MethodCallListForm tListForm = (MethodCallListForm) pForm;
        ConsoleManager tProcess = (ConsoleManager) SpringConfigurationUtil.getBean("consoleManager");
        tListForm.setSearchResult(tProcess.getListOfMethodCallFullExtract(tListForm.getClassName(),
                                                                          tListForm.getMethodName(),
                                                                          tListForm.getDurationMin(),
                                                                          tListForm.getDurationMax()));
        return pMapping.findForward("success");
    }

}
