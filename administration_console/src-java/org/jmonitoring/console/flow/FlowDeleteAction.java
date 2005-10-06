package org.jmonitoring.console.flow;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jmonitoring.core.configuration.Configuration;
import org.jmonitoring.core.dao.ExecutionFlowMySqlDAO;
import org.jmonitoring.core.dao.StandAloneConnectionManager;

import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author pke
 * 
 * @todo To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 *       Templates
 */
public class FlowDeleteAction extends Action
{

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                    HttpServletResponse pResponse) throws Exception
    {
        FlowEditForm tForm = (FlowEditForm) pForm;
        Connection tConnection = null;
        try
        {
            tConnection = new StandAloneConnectionManager(Configuration.getInstance()).getConnection();
            ExecutionFlowMySqlDAO tDao = new ExecutionFlowMySqlDAO(tConnection);
            if (tForm != null && tForm.getId() != -1)
            {
                tDao.deleteFlow(tForm.getId());
            } else
            {
                tDao.deleteAllFlows();
            }
            tConnection.commit();
            return pMapping.findForward("success");

        } catch (Throwable t)
        {
            LogFactory.getLog(this.getClass()).error("Unable to Execute Action" + t);
            return pMapping.findForward("failure");
        } finally
        {
            if (tConnection != null)
            {
                tConnection.close();
            }
        }
    }
}
