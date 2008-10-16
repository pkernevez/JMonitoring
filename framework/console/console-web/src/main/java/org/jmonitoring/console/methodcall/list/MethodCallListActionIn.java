package org.jmonitoring.console.methodcall.list;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jmonitoring.core.process.JMonitoringProcess;
import org.jmonitoring.core.process.ProcessFactory;
import org.jmonitoring.core.process.TransactionHelper;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * @author pke
 */
public class MethodCallListActionIn extends Action
{
    /**
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward execute(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                    HttpServletResponse pResponse) throws Exception
    {
        MethodCallListForm tListForm = (MethodCallListForm) pForm;
        JMonitoringProcess tProcess = ProcessFactory.getInstance();
        TransactionHelper tTx = new TransactionHelper();
        try
        {
            List tResult = tProcess.getListOfMethodCallFullExtract(tListForm.getClassName(), tListForm.getMethodName(),
                                                                   tListForm.getDurationMin(),
                                                                   tListForm.getDurationMax());
            tListForm.setSearchResult(tResult);
            tTx.commit();
            return pMapping.findForward("success");
        } catch (Throwable t)
        {
            tTx.rollBack();
            throw new RuntimeException(t);
        }
    }

}
