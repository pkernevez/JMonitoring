package org.jmonitoring.sample.console;

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
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jmonitoring.agent.store.impl.MemoryWriter;
import org.jmonitoring.sample.main.RunSample;
import org.jmonitoring.sample.persistence.SpringSampleConfigurationUtil;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @author pke
 * 
 * @todo To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 *       Templates
 */
public class SampleAlreadyWeavedActionIn extends Action
{
    /*
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
        Session mSession = null;
        SessionFactory tFact = (SessionFactory) SpringSampleConfigurationUtil.getBean("sessionFactory");
        try
        {
            mSession = tFact.openSession();
            mSession.beginTransaction();
            TransactionSynchronizationManager.bindResource(tFact, new SessionHolder(mSession));
            MemoryWriter.clear();

            new RunSample().run();
        } finally
        {
            TransactionSynchronizationManager.unbindResource(tFact);
            if (mSession != null)
            {
                mSession.getTransaction().rollback();
                mSession.close();
            }
        }
        return pMapping.findForward("success");

    }
}
