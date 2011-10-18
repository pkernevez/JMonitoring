package org.jmonitoring.console.gwt.server.flow;

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

}
