package org.jmonitoring.console.gwt.server.flow;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

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
        WebApplicationContext springContext =
            WebApplicationContextUtils.getWebApplicationContext(pConfig.getServletContext());
        sessionFactory = (SessionFactory) springContext.getBean(SessionFactory.class);
    }

    public void doFilter(ServletRequest pRequest, ServletResponse pResponse, FilterChain pChain) throws IOException,
        ServletException
    {
        Session tSession = sessionFactory.openSession();
        Transaction tCurrentTx = tSession.beginTransaction();
        TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(tSession));
        HttpServletResponseWithStatus tResponse = new HttpServletResponseWithStatus((HttpServletResponse) pResponse);
        pChain.doFilter(pRequest, tResponse);
        if (tResponse.httpStatus == 200){
            tCurrentTx.commit();
        } else {
            tCurrentTx.rollback();
        }
        tSession.close();
        TransactionSynchronizationManager.unbindResource(sessionFactory);
    }

    public void destroy()
    {
    }

    public static class HttpServletResponseWithStatus extends HttpServletResponseWrapper
    {
        private int httpStatus = 200;

        public HttpServletResponseWithStatus(HttpServletResponse pResponse)
        {
            super(pResponse);
        }

        @Override
        public void sendError(int sc) throws IOException {
            httpStatus = sc;
            super.sendError(sc);
        }

        @Override
        public void sendError(int sc, String msg) throws IOException {
            httpStatus = sc;
            super.sendError(sc, msg);
        }


        @Override
        public void setStatus(int sc) {
            httpStatus = sc;
            super.setStatus(sc);
        }

        public int getStatus() {
            return httpStatus;
        }
        
        @Override
        public void sendRedirect(String location) throws IOException {
            httpStatus = 302;
            super.sendRedirect(location);
        }

    }

}
