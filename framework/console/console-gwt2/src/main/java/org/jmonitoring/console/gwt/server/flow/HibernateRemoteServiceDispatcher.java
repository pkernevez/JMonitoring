package org.jmonitoring.console.gwt.server.flow;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gwtrpcspring.RemoteServiceDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateRemoteServiceDispatcher extends RemoteServiceDispatcher
{
    private static final long serialVersionUID = -7029029723207716574L;

    private static Logger sLog = LoggerFactory.getLogger(HibernateRemoteServiceDispatcher.class);

    @Override
    protected void onBeforeServiceCall(Object obj)
    {
        sLog.debug("Create Hibernate session and transaction");
        super.onBeforeServiceCall(obj);
    }

    @Override
    protected void onAfterServiceCall(Object obj)
    {
        sLog.debug("Create Hibernate session and transaction");
        super.onAfterServiceCall(obj);
    }

//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
//    {
//        // TODO Auto-generated method stub
//        super.doPut(req, resp);
//    }

}
