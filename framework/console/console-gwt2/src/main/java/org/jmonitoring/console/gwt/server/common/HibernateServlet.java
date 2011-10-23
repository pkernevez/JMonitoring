package org.jmonitoring.console.gwt.server.common;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jmonitoring.console.gwt.server.flow.FlowServiceImpl;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public abstract class HibernateServlet extends HttpServlet
{
    protected SessionFactory sessionFactory;

    protected FlowServiceImpl flowService;

    @Override
    protected void doGet(HttpServletRequest pReq, HttpServletResponse pResp) throws ServletException, IOException
    {
        Session tSession = null;
        Transaction tCurrentTx = null;
        try
        {
            tSession = sessionFactory.openSession();
            tCurrentTx = tSession.beginTransaction();
            TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(tSession));
            doGetWithSpring(pReq, pResp);
            tCurrentTx.commit();
        } catch (RuntimeException r)
        {
            tCurrentTx.rollback();
            throw r;
        } catch (Error e)
        {
            tCurrentTx.rollback();
            throw e;
        } finally
        {
            if (tSession != null)
            {
                tSession.close();
                TransactionSynchronizationManager.unbindResource(sessionFactory);
            }
        }
    }

    @Override
    public void init(ServletConfig pConfig) throws ServletException
    {
        super.init(pConfig);
        WebApplicationContext tSpringContext =
            WebApplicationContextUtils.getWebApplicationContext(pConfig.getServletContext());
        sessionFactory = tSpringContext.getBean(SessionFactory.class);
        flowService = tSpringContext.getBean(FlowServiceImpl.class);
        init(tSpringContext);
    }

    protected void init(WebApplicationContext pSpringContext)
    {
    }

    protected abstract void doGetWithSpring(HttpServletRequest pReq, HttpServletResponse pResp)
        throws ServletException, IOException;

}
