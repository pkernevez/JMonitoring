package org.jmonitoring.core.process;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.jmonitoring.core.common.UnknownFlowException;
import org.jmonitoring.core.dao.ExecutionFlowDAO;
import org.jmonitoring.core.dao.FlowSearchCriterion;
import org.jmonitoring.core.dto.DtoHelper;
import org.jmonitoring.core.dto.ExecutionFlowDTO;
import org.jmonitoring.core.persistence.ExecutionFlowPO;
import org.jmonitoring.core.persistence.HibernateManager;
import org.jmonitoring.core.persistence.MethodCallPO;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class JMonitoringProcess
{
    private static Log sLog = LogFactory.getLog(JMonitoringProcess.class);

    JMonitoringProcess()
    {
    }

    public boolean doDatabaseExist()
    {
        Session tSession = HibernateManager.getSession();
        try
        {
            ExecutionFlowDAO tDao = new ExecutionFlowDAO(tSession);
            tDao.countFlows();
            return true;
        } catch (SQLGrammarException t)
        {
            return false;
        } finally
        {
            tSession.close();
        }

    }

    public void deleteFlow(int pId) throws UnknownFlowException
    {
        Session tSession = HibernateManager.getSession();
        Transaction tTransaction = tSession.beginTransaction();
        try
        {
            ExecutionFlowDAO tDao = new ExecutionFlowDAO(tSession);
            tDao.deleteFlow(pId);
            tTransaction.commit();

        } catch (RuntimeException t)
        {
            LogFactory.getLog(this.getClass()).error("Unable to Execute Action" + t);
            tTransaction.rollback();
        } finally
        {
            tSession.close();
        }
    }

    public void deleteAllFlows()
    {
        Session tSession = HibernateManager.getSession();
        Transaction tTransaction = tSession.beginTransaction();
        try
        {
            ExecutionFlowDAO tDao = new ExecutionFlowDAO(tSession);
            tDao.deleteAllFlows();
            tTransaction.commit();

        } catch (Throwable t)
        {
            LogFactory.getLog(this.getClass()).error("Unable to Execute Action" + t);
            tTransaction.rollback();
        } finally
        {
            tSession.close();
        }
    }

    public ExecutionFlowDTO readFullExecutionFlow(int pId)
    {
        Session tSession = HibernateManager.getSession();
        try
        {
            sLog.debug("Read flow from database, Id=[" + pId + "]");
            ExecutionFlowDAO tDao = new ExecutionFlowDAO(tSession);
            ExecutionFlowPO tFlowPo = tDao.readExecutionFlow(pId);
            return DtoHelper.getDeepCopy(tFlowPo);
        } finally
        {
            tSession.close();
        }
    }

    public List getListOfExecutionFlowDto(FlowSearchCriterion pCriterion)
    {
        Session tSession = HibernateManager.getSession();
        try
        {
            List tList = new ArrayList();
            ExecutionFlowDAO tDao = new ExecutionFlowDAO(tSession);
            for (Iterator tIt = tDao.getListOfExecutionFlowPO(pCriterion).iterator(); tIt.hasNext();)
            {
                tList.add(DtoHelper.getSimpleCopy((ExecutionFlowPO) tIt.next()));
            }
            return tList;
        } finally
        {
            tSession.close();
        }
    }

    public List getListOfMethodCallFromFlowId(int pFlowId)
    {
        Session tSession = HibernateManager.getSession();
        try
        {
            ExecutionFlowDAO tDao = new ExecutionFlowDAO(tSession);
            ExecutionFlowPO tFlow = tDao.readExecutionFlow(pFlowId);
            MethodCallPO tMethod = tFlow.getFirstMethodCall();
            List tResult = tDao.getListOfMethodCall(tMethod.getClassName(), tMethod.getMethodName());
            return DtoHelper.copyListOfMethodPO(tResult);
        } finally
        {
            tSession.close();
        }
    }

    public List getListOfMethodCallFromClassAndMethodName(String pClassName, String pMethodName)
    {
        Session tSession = HibernateManager.getSession();
        try
        {
            ExecutionFlowDAO tDao = new ExecutionFlowDAO(tSession);
            List tResult = tDao.getListOfMethodCall(pClassName, pMethodName);
            return DtoHelper.copyListOfMethodPO(tResult);
        } finally
        {
            tSession.close();
        }
    }

    public void createDataBase()
    {
        Session tSession = HibernateManager.getSession();
        try
        {
            ExecutionFlowDAO tDao = new ExecutionFlowDAO(tSession);
            tDao.createDataBase();
        } finally
        {
            tSession.close();
        }
    }
}
