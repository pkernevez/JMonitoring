package org.jmonitoring.console.gwt.server.executionflow;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jmonitoring.console.gwt.client.dto.ClientUnknowFlowException;
import org.jmonitoring.console.gwt.client.dto.ExecutionFlowDTO;
import org.jmonitoring.console.gwt.client.dto.FullExecutionFlowDTO;
import org.jmonitoring.console.gwt.client.dto.MapDto;
import org.jmonitoring.console.gwt.client.dto.MethodCallDTO;
import org.jmonitoring.console.gwt.client.dto.RootMethodCallDTO;
import org.jmonitoring.console.gwt.client.dto.StatMethodCallDTO;
import org.jmonitoring.console.gwt.client.service.ExecutionFlowService;
import org.jmonitoring.console.gwt.client.service.SearchCriteria;
import org.jmonitoring.console.gwt.server.ConsoleManager;
import org.jmonitoring.console.gwt.server.dto.DtoManager;
import org.jmonitoring.console.gwt.server.executionflow.images.ChartManager;
import org.jmonitoring.console.gwt.server.executionflow.images.FlowChartBarUtil;
import org.jmonitoring.console.gwt.server.executionflow.images.FlowUtil;
import org.jmonitoring.core.common.UnknownFlowException;
import org.jmonitoring.core.configuration.ColorManager;
import org.jmonitoring.core.configuration.FormaterBean;
import org.jmonitoring.core.configuration.SpringConfigurationUtil;
import org.jmonitoring.core.domain.ExecutionFlowPO;
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
    private static final long serialVersionUID = -331539104207957626L;

    private SessionFactory mSessionFactory;

    private final Logger sLog = LoggerFactory.getLogger(ExecutionFlowServiceImpl.class);

    public List<ExecutionFlowDTO> search(SearchCriteria pCriteria)
    {
        try
        {
            before(mSessionFactory);
            ConsoleManager tMgr = (ConsoleManager) SpringConfigurationUtil.getBean("consoleManager");
            return tMgr.getListOfExecutionFlowDto(pCriteria);
        } finally
        {
            afterRollBack(mSessionFactory);
        }
    }

    public Map<Integer, MethodCallDTO> load(int pFlowId, List<Integer> pMethIDs)
    {
        try
        {
            before(mSessionFactory);
            ConsoleManager tMgr = (ConsoleManager) SpringConfigurationUtil.getBean("consoleManager");
            // Optimisation start to load the full flow from db in one read
            tMgr.readExecutionFlow(pFlowId);
            return tMgr.getListOfMethodCall(pFlowId, pMethIDs);
        } finally
        {
            afterRollBack(mSessionFactory);
        }
    }

    public FullExecutionFlowDTO load(int pFlowId)
    {
        try
        {
            before(mSessionFactory);
            ConsoleManager tMgr = (ConsoleManager) SpringConfigurationUtil.getBean("consoleManager");
            DtoManager tDtoMgr = (DtoManager) SpringConfigurationUtil.getBean("dtoManager");
            sLog.debug("Read flow from database, Id=[" + pFlowId + "]");
            ExecutionFlowPO tFlowPo = tMgr.readExecutionFlow(pFlowId);
            ExecutionFlowDTO tReadFullExecutionFlow = tDtoMgr.getLimitedCopy(tFlowPo);

            FullExecutionFlowDTO tResult = new FullExecutionFlowDTO(tReadFullExecutionFlow);
            FormaterBean tFormater = (FormaterBean) SpringConfigurationUtil.getBean("formater");
            ColorManager tColor = (ColorManager) SpringConfigurationUtil.getBean("color");
            ChartManager tChartManager = (ChartManager) SpringConfigurationUtil.getBean("chartManager");
            HttpSession tSession = getThreadLocalRequest().getSession();
            MapDto tMap = FlowChartBarUtil.writeImageIntoSession(tFormater, tColor, tSession, tFlowPo, tChartManager);
            tResult.setImageMap(tMap);
            sLog.debug("add GantBarChart into HttpSession");
            FlowUtil tUtil = new FlowUtil(tColor, tFormater);
            tUtil.writeImageIntoSession(tSession, tFlowPo.getFirstMethodCall());
            sLog.debug("add PieCharts into HttpSession");

            return tResult;
        } finally
        {
            after(mSessionFactory);
        }
    }

    public RootMethodCallDTO load(int pFlowId, int pMethPosition)
    {
        try
        {
            before(mSessionFactory);
            ConsoleManager tMgr = (ConsoleManager) SpringConfigurationUtil.getBean("consoleManager");
            return tMgr.readFullMethodCall(pFlowId, pMethPosition);
        } catch (RuntimeException e)
        {
            sLog.error("Unable to load Methodcall", e);
            throw e;
        } finally
        {
            afterRollBack(mSessionFactory);
        }
    }

    public StatMethodCallDTO loadStat(String pClassName, String pMethodName, int pAggregationScope)
    {
        try
        {
            before(mSessionFactory);
            ConsoleManager tMgr = (ConsoleManager) SpringConfigurationUtil.getBean("consoleManager");
            return tMgr.readStatMethodCall(getThreadLocalRequest().getSession(), pClassName, pMethodName,
                                           pAggregationScope);
        } catch (RuntimeException e)
        {
            sLog.error("Unable to load Methodcall", e);
            throw e;
        } finally
        {
            afterRollBack(mSessionFactory);
        }
    }

    public void delete(int pFlowId) throws ClientUnknowFlowException
    {
        try
        {
            before(mSessionFactory);
            ConsoleManager tMgr = (ConsoleManager) SpringConfigurationUtil.getBean("consoleManager");
            tMgr.deleteFlow(pFlowId);
        } catch (RuntimeException e)
        {
            sLog.error("Unable to load Methodcall", e);
            throw e;
        } catch (UnknownFlowException e)
        {
            throw new ClientUnknowFlowException();
        } finally
        {
            after(mSessionFactory);
        }
    }

    public static void before()
    {
        before((SessionFactory) SpringConfigurationUtil.getBean("sessionFactory"));
    }

    public static void before(SessionFactory pSessionFactory)
    {
        Session tSession = pSessionFactory.openSession();
        tSession.beginTransaction();
        TransactionSynchronizationManager.bindResource(pSessionFactory, new SessionHolder(tSession));
    }

    public static void after()
    {
        after((SessionFactory) SpringConfigurationUtil.getBean("sessionFactory"));
    }

    public static void after(SessionFactory pSessionFactory)
    {
        if (pSessionFactory != null)
        {
            Session tSession = null;
            try
            {
                SessionHolder tHolder =
                    (SessionHolder) TransactionSynchronizationManager.unbindResource(pSessionFactory);
                tSession = (tHolder == null ? null : tHolder.getSession());
            } finally
            {
                if (tSession != null && tSession.isOpen())
                {
                    if (tSession.getTransaction().isActive())
                    {
                        tSession.getTransaction().commit();
                    }
                    tSession.close();
                }
            }
        }
    }

    public static void afterRollBack()
    {
        afterRollBack((SessionFactory) SpringConfigurationUtil.getBean("sessionFactory"));
    }

    private static void afterRollBack(SessionFactory pSessionFactory)
    {
        if (pSessionFactory != null)
        {
            Session tSession = null;
            try
            {
                SessionHolder tHolder =
                    (SessionHolder) TransactionSynchronizationManager.unbindResource(pSessionFactory);
                tSession = (tHolder == null ? null : tHolder.getSession());
            } finally
            {
                if (tSession != null && tSession.isOpen())
                {
                    if (tSession.getTransaction().isActive())
                    {
                        tSession.getTransaction().rollback();
                    }
                    tSession.close();
                }
            }
        }
    }

    @Override
    public void init() throws ServletException
    {
        super.init();
        mSessionFactory = (SessionFactory) SpringConfigurationUtil.getBean("sessionFactory");
    }

}