package org.jmonitoring.console.methodcall.list;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jmonitoring.core.process.ConsoleManager;
import org.jmonitoring.core.process.ProcessFactory;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * @author pke
 */
public class MethodCallListActionIn extends Action
{
    @Override
    public ActionForward execute(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
        HttpServletResponse pResponse) throws Exception
    {
        MethodCallListForm tListForm = (MethodCallListForm) pForm;
        ConsoleManager tProcess = ProcessFactory.getInstance();
        // TransactionHelper tTx = new TransactionHelper();
        // try
        // {
        tListForm.setSearchResult(tProcess.getListOfMethodCallFullExtract(tListForm.getClassName(),
                                                                          tListForm.getMethodName(),
                                                                          tListForm.getDurationMin(),
                                                                          tListForm.getDurationMax()));
        // tTx.commit();
        return pMapping.findForward("success");
        // } catch (Throwable t)
        // {
        // tTx.rollBack();
        // throw new RuntimeException(t);
        // }
    }

}
