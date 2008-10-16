package org.jmonitoring.console.flow;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jmonitoring.core.process.JMonitoringProcess;
import org.jmonitoring.core.process.ProcessFactory;
import org.jmonitoring.core.process.TransactionHelper;

/**
 * @author pke
 * 
 * @todo To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 *       Templates
 */
public class DeleteAllFlowsActionIn extends Action
{

    @Override
    public ActionForward execute(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                    HttpServletResponse pResponse) throws Exception
    {
        JMonitoringProcess tProcess = ProcessFactory.getInstance();
        TransactionHelper tTx = new TransactionHelper();
        try
        {
            tProcess.deleteAllFlows();
            tTx.commit();
        } catch (Throwable t)
        {
            tTx.rollBack();
            throw new RuntimeException(t);
        }
        return pMapping.findForward("success");
    }
}
