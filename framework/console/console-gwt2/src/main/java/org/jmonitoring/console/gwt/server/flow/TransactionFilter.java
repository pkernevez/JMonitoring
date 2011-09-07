package org.jmonitoring.console.gwt.server.flow;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class TransactionFilter implements Filter
{
    SessionFactory sessionFactory;
    
    public void init(FilterConfig pConfig) throws ServletException
    {
        WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(pConfig.getServletContext());
        sessionFactory = (SessionFactory) springContext.getBean(SessionFactory.class);
    }

    public void doFilter(ServletRequest pRequest, ServletResponse pResponse, FilterChain pChain) throws IOException,
        ServletException
    {
        Session tSession = sessionFactory.openSession();
        tSession.beginTransaction();
        TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(tSession));
        pChain.doFilter(pRequest, pResponse);
        HttpServletResponse tResponse = (HttpServletResponse) pResponse;
//        if (tResponse.)
    }

    public void destroy()
    {
    }
}
