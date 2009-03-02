package org.jmonitoring.console.gwt.server.executionflow;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jmonitoring.console.gwt.client.dto.ExecutionFlowDTO;
import org.jmonitoring.console.gwt.client.executionflow.ExecutionFlowService;
import org.jmonitoring.console.gwt.client.executionflow.SearchCriteria;
import org.jmonitoring.console.gwt.server.ConsoleManager;
import org.jmonitoring.core.configuration.SpringConfigurationUtil;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class ExecutionFlowServiceImpl extends RemoteServiceServlet implements ExecutionFlowService
{

    private Session mSession;

    private Transaction mTransaction;

    private SessionFactory mSessionFactory;

    private void before()
    {
        mSessionFactory = (SessionFactory) SpringConfigurationUtil.getBean("sessionFactory");
        mSession = mSessionFactory.openSession();
        mTransaction = mSession.beginTransaction();
        TransactionSynchronizationManager.bindResource(mSessionFactory, new SessionHolder(mSession));
    }

    public List<ExecutionFlowDTO> search(SearchCriteria pCriteria)
    {
        try
        {
            before();
            ConsoleManager tMgr = (ConsoleManager) SpringConfigurationUtil.getBean("consoleManager");
            return tMgr.getListOfExecutionFlowDto(pCriteria);
        } finally
        {
            try
            {
                TransactionSynchronizationManager.unbindResource(mSessionFactory);
            } finally
            {
                if (mSession.isOpen())
                {
                    if (mTransaction.isActive())
                    {
                        mTransaction.rollback();
                    }
                    mSession.close();
                }
            }
        }
    }
}
