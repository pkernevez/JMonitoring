package org.jmonitoring.console;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jmonitoring.core.configuration.SpringConfigurationUtil;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public abstract class AbstractSpringAction extends Action
{
    @Override
    public final ActionForward execute(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
        HttpServletResponse pResponse) throws Exception
    {
        try
        {
            before();
            ActionForward tResult = executeWithSpringContext(pMapping, pForm, pRequest, pResponse);
            commit();
            return tResult;
        } catch (RuntimeException r)
        {
            rollback();
            throw r;
        } catch (Error e)
        {
            rollback();
            throw e;
        }
    }

    public static void before()
    {
        SessionFactory tFact = (SessionFactory) SpringConfigurationUtil.getBean("sessionFactory");
        if (!SpringConfigurationUtil.isTestMode())
        {
            Session tSession = tFact.openSession();
            tSession.beginTransaction();
            TransactionSynchronizationManager.bindResource(tFact, new SessionHolder(tSession));
        } else
        {
            if (TransactionSynchronizationManager.getResource(tFact) == null)
            {
                throw new RuntimeException("There must be a session associated to this thread...");
            }
        }
    }

    public static void commit()
    {
        SessionFactory tFact = (SessionFactory) SpringConfigurationUtil.getBean("sessionFactory");
        if (!SpringConfigurationUtil.isTestMode())
        {
            SessionHolder tHolder = (SessionHolder) TransactionSynchronizationManager.unbindResource(tFact);
            Session tSession = tHolder.getSession();
            if (tSession.getTransaction().isActive())
            {
                tSession.getTransaction().commit();
            }
            if (tSession.isOpen())
            {
                tSession.close();
            }
        } else
        {
            if (TransactionSynchronizationManager.getResource(tFact) == null)
            {
                throw new RuntimeException("There must be a session associated to this thread...");
            }
        }
    }

    public static void rollback()
    {
        SessionFactory tFact = (SessionFactory) SpringConfigurationUtil.getBean("sessionFactory");
        if (!SpringConfigurationUtil.isTestMode())
        {
            SessionHolder tHolder = (SessionHolder) TransactionSynchronizationManager.unbindResource(tFact);
            Session tSession = tHolder.getSession();
            if (tSession.getTransaction().isActive())
            {
                tSession.getTransaction().rollback();
            }
            if (tSession.isOpen())
            {
                tSession.close();
            }
        } else
        {
            if (TransactionSynchronizationManager.getResource(tFact) == null)
            {
                throw new RuntimeException("There must be a session associated to this thread...");
            }
        }
    }

    public abstract ActionForward executeWithSpringContext(ActionMapping pMapping, ActionForm pForm,
        HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception;

}
