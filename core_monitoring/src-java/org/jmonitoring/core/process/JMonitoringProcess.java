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
import org.jmonitoring.core.dto.MethodCallDTO;
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
        Session tSession = null;
        try
        {
            tSession = HibernateManager.getSession();
            ExecutionFlowDAO tDao = new ExecutionFlowDAO(tSession);
            tDao.countFlows();
            return true;
        } catch (SQLGrammarException t)
        {
            return false;
        } finally
        {
            if (tSession != null)
            {
                tSession.close();
            }
        }

    }

    public void deleteFlow(int pId) throws UnknownFlowException
    {
        Session tSession = null;
        Transaction tTransaction = null;
        try
        {
            tSession = HibernateManager.getSession();
            tTransaction = tSession.beginTransaction();
            ExecutionFlowDAO tDao = new ExecutionFlowDAO(tSession);
            tDao.deleteFlow(pId);
            tTransaction.commit();

        } catch (RuntimeException t)
        {
            LogFactory.getLog(this.getClass()).error("Unable to Execute Action" + t);
            if (tTransaction != null)
            {
                tTransaction.rollback();
            }
        } finally
        {
            if (tSession != null)
            {
                tSession.close();
            }
        }
    }

    public void deleteAllFlows()
    {
        Session tSession = null;
        Transaction tTransaction = null;
        try
        {
            tSession = HibernateManager.getSession();
            tTransaction = tSession.beginTransaction();
            ExecutionFlowDAO tDao = new ExecutionFlowDAO(tSession);
            tDao.deleteAllFlows();
            tTransaction.commit();

        } catch (Throwable t)
        {
            LogFactory.getLog(this.getClass()).error("Unable to Execute Action" + t);
            if (tTransaction != null)
            {
                tTransaction.rollback();
            }
        } finally
        {
            if (tSession != null)
            {
                tSession.close();
            }
        }
    }

    public MethodCallDTO readFullMethodCall(int pId)
    {
        Session tSession = null;
        try
        {
            tSession = HibernateManager.getSession();
            sLog.debug("Read method call from database, Id=[" + pId + "]");
            ExecutionFlowDAO tDao = new ExecutionFlowDAO(tSession);
            MethodCallPO tMethodCallPo = tDao.readMethodCall(pId);
            return DtoHelper.getFullMethodCallDto(tMethodCallPo);
        } finally
        {
            if (tSession != null)
            {
                tSession.close();
            }
        }
    }

    public ExecutionFlowDTO readFullExecutionFlow(int pId)
    {
        Session tSession = null;
        try
        {
            tSession = HibernateManager.getSession();
            sLog.debug("Read flow from database, Id=[" + pId + "]");
            ExecutionFlowDAO tDao = new ExecutionFlowDAO(tSession);
            ExecutionFlowPO tFlowPo = tDao.readExecutionFlow(pId);
            return DtoHelper.getDeepCopy(tFlowPo);
        } finally
        {
            if (tSession != null)
            {
                tSession.close();
            }
        }
    }

    public List getListOfExecutionFlowDto(FlowSearchCriterion pCriterion)
    {
        Session tSession = null;
        try
        {
            tSession = HibernateManager.getSession();
            List tList = new ArrayList();
            ExecutionFlowDAO tDao = new ExecutionFlowDAO(tSession);
            for (Iterator tIt = tDao.getListOfExecutionFlowPO(pCriterion).iterator(); tIt.hasNext();)
            {
                tList.add(DtoHelper.getSimpleCopy((ExecutionFlowPO) tIt.next()));
            }
            return tList;
        } finally
        {
            if (tSession != null)
            {
                tSession.close();
            }
        }
    }

    public MethodCallDTO readMethodCall(int pFlowId, int pMethodCallId)
    {
        Session tSession = null;
        try
        {
            tSession = HibernateManager.getSession();
            ExecutionFlowDAO tDao = new ExecutionFlowDAO(tSession);
            // ExecutionFlowPO tFlow = tDao.readExecutionFlow(pFlowId);
            MethodCallPO tMethod = tDao.readMethodCall(pMethodCallId);
            return DtoHelper.simpleCopy(tMethod);
        } finally
        {
            if (tSession != null)
            {
                tSession.close();
            }
        }

    }

    public List getListOfMethodCallFromClassAndMethodName(String pClassName, String pMethodName)
    {
        Session tSession = null;
        try
        {
            tSession = HibernateManager.getSession();
            ExecutionFlowDAO tDao = new ExecutionFlowDAO(tSession);
            List tResult = tDao.getListOfMethodCall(pClassName, pMethodName);
            return DtoHelper.simpleCopyListOfMethodPO(tResult);
        } finally
        {
            if (tSession != null)
            {
                tSession.close();
            }
        }
    }

    public List getListOfMethodCallExtract()
    {
        Session tSession = null;
        try
        {
            tSession = HibernateManager.getSession();
            ExecutionFlowDAO tDao = new ExecutionFlowDAO(tSession);
            return tDao.getListOfMethodCallExtract();
        } finally
        {
            if (tSession != null)
            {
                tSession.close();
            }
        }
    }

    public List getListOfMethodCallFullExtract(String pClassName, String pMethodName, long pDurationMin, long pDurationMax)
    {
        Session tSession = null;
        try
        {
            tSession = HibernateManager.getSession();
            ExecutionFlowDAO tDao = new ExecutionFlowDAO(tSession);
            List tListOfMethodCall = tDao.getMethodCallList(pClassName, pMethodName, pDurationMin, pDurationMax);
            return DtoHelper.copyListMethodCallFullExtract(tListOfMethodCall);
        } finally
        {
            if (tSession != null)
            {
                tSession.close();
            }
        }
    }

    public void createDataBase()
    {
        Session tSession = null;
        try
        {
            tSession = HibernateManager.getSession();
            ExecutionFlowDAO tDao = new ExecutionFlowDAO(tSession);
            tDao.createDataBase();
        } finally
        {
            if (tSession != null)
            {
                tSession.close();
            }
        }
    }
}
