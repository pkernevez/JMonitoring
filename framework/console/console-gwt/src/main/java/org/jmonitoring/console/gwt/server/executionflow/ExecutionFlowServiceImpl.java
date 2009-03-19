package org.jmonitoring.console.gwt.server.executionflow;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jmonitoring.console.gwt.client.dto.ExecutionFlowDTO;
import org.jmonitoring.console.gwt.client.dto.MethodCallDTO;
import org.jmonitoring.console.gwt.client.executionflow.ExecutionFlowService;
import org.jmonitoring.console.gwt.client.executionflow.SearchCriteria;
import org.jmonitoring.console.gwt.client.executionflow.images.FullExecutionFlow;
import org.jmonitoring.console.gwt.server.ConsoleManager;
import org.jmonitoring.console.gwt.server.executionflow.images.ChartManager;
import org.jmonitoring.console.gwt.server.executionflow.images.FlowChartBarUtil;
import org.jmonitoring.console.gwt.server.executionflow.images.FlowUtil;
import org.jmonitoring.core.configuration.ColorManager;
import org.jmonitoring.core.configuration.FormaterBean;
import org.jmonitoring.core.configuration.SpringConfigurationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger sLog = LoggerFactory.getLogger(ExecutionFlowServiceImpl.class);

    void before()
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
            if (mSessionFactory != null)
            {
                try
                {
                    TransactionSynchronizationManager.unbindResource(mSessionFactory);
                } finally
                {
                    if (mSession != null && mSession.isOpen())
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

    public Map<Integer, MethodCallDTO> load(int pFlowId, List<Integer> pMethIDs)
    {
        try
        {
            before();
            ConsoleManager tMgr = (ConsoleManager) SpringConfigurationUtil.getBean("consoleManager");
            return tMgr.getListOfMethodCall(pFlowId, pMethIDs);
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

    public FullExecutionFlow load(int pFlowId)
    {
        try
        {
            before();
            ConsoleManager tMgr = (ConsoleManager) SpringConfigurationUtil.getBean("consoleManager");
            ExecutionFlowDTO tReadFullExecutionFlow = tMgr.readFullExecutionFlow(pFlowId);
            FullExecutionFlow tResult = new FullExecutionFlow(tReadFullExecutionFlow);

            FormaterBean tFormater = (FormaterBean) SpringConfigurationUtil.getBean("formater");
            ColorManager tColor = (ColorManager) SpringConfigurationUtil.getBean("color");
            ChartManager tChartManager = (ChartManager) SpringConfigurationUtil.getBean("chartManager");
            HttpSession tSession = getThreadLocalRequest().getSession();
            FlowChartBarUtil.writeImageIntoSession(tFormater, tColor, tSession, tResult, tChartManager);
            sLog.debug("add GantBarChart into HttpSession");
            FlowUtil tUtil = new FlowUtil(tColor, tFormater);
            tUtil.writeImageIntoSession(tSession, tReadFullExecutionFlow.getFirstMethodCall());
            sLog.debug("add PieCharts into HttpSession");

            return tResult;
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