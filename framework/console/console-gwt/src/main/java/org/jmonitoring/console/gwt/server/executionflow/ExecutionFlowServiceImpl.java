package org.jmonitoring.console.gwt.server.executionflow;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jmonitoring.console.gwt.client.dto.ExecutionFlowDTO;
import org.jmonitoring.console.gwt.client.dto.FullExecutionFlowDTO;
import org.jmonitoring.console.gwt.client.dto.MethodCallDTO;
import org.jmonitoring.console.gwt.client.dto.RootMethodCallDTO;
import org.jmonitoring.console.gwt.client.service.ExecutionFlowService;
import org.jmonitoring.console.gwt.client.service.SearchCriteria;
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

    private final ThreadLocal<Transaction> mTransaction = new ThreadLocal<Transaction>();

    private SessionFactory mSessionFactory;

    private final Logger sLog = LoggerFactory.getLogger(ExecutionFlowServiceImpl.class);

    void before()
    {
        mSessionFactory = (SessionFactory) SpringConfigurationUtil.getBean("sessionFactory");
        Session tSession = mSessionFactory.openSession();
        mTransaction.set(tSession.beginTransaction());
        TransactionSynchronizationManager.bindResource(mSessionFactory, new SessionHolder(tSession));
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
            afterRollBack();
        }
    }

    public Map<Integer, MethodCallDTO> load(int pFlowId, List<Integer> pMethIDs)
    {
        try
        {
            before();
            ConsoleManager tMgr = (ConsoleManager) SpringConfigurationUtil.getBean("consoleManager");
            // Optimisation start to load the full flow from db in one read
            tMgr.readFullExecutionFlow(pFlowId);
            return tMgr.getListOfMethodCall(pFlowId, pMethIDs);
        } finally
        {
            afterRollBack();
        }
    }

    public FullExecutionFlowDTO load(int pFlowId)
    {
        try
        {
            before();
            ConsoleManager tMgr = (ConsoleManager) SpringConfigurationUtil.getBean("consoleManager");
            ExecutionFlowDTO tReadFullExecutionFlow = tMgr.readFullExecutionFlow(pFlowId);
            FullExecutionFlowDTO tResult = new FullExecutionFlowDTO(tReadFullExecutionFlow);

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
            afterRollBack();
        }
    }

    public RootMethodCallDTO load(int pFlowId, int pMethPosition)
    {
        try
        {
            before();
            ConsoleManager tMgr = (ConsoleManager) SpringConfigurationUtil.getBean("consoleManager");
            return tMgr.readFullMethodCall(pFlowId, pMethPosition);
        } catch (RuntimeException e)
        {
            sLog.error("Unable to load Methodcall", e);
            throw e;
        } finally
        {
            afterRollBack();
        }
    }

    private void after()
    {
        Session tSession = null;
        try
        {
            tSession = (Session) TransactionSynchronizationManager.unbindResource(mSessionFactory);
        } finally
        {
            if (tSession != null && tSession.isOpen())
            {
                if (mTransaction.get().isActive())
                {
                    mTransaction.get().commit();
                }
                tSession.close();
            }
        }
    }

    private void afterRollBack()
    {
        if (mSessionFactory != null)
        {
            Session tSession = null;
            try
            {
                SessionHolder tHolder =
                    (SessionHolder) TransactionSynchronizationManager.unbindResource(mSessionFactory);
                tSession = (tHolder == null ? null : tHolder.getSession());
            } finally
            {
                if (tSession != null && tSession.isOpen())
                {
                    if (mTransaction.get().isActive())
                    {
                        mTransaction.get().rollback();
                    }
                    tSession.close();
                }
            }
        }
    }

}