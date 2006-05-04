package org.jmonitoring.core.process;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jmonitoring.core.dao.ExecutionFlowDAO;
import org.jmonitoring.core.dao.FlowSearchCriterion;
import org.jmonitoring.core.dto.DtoHelper;
import org.jmonitoring.core.dto.ExecutionFlowDTO;
import org.jmonitoring.core.persistence.ExecutionFlowPO;
import org.jmonitoring.core.persistence.HibernateManager;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class JMonitoringProcess
{
    private static Log sLog = LogFactory.getLog(JMonitoringProcess.class);

    JMonitoringProcess()
    {
    }

    public void deleteFlow(int pId)
    {
        Session tSession = HibernateManager.getSession();
        Transaction tTransaction = tSession.beginTransaction();
        try
        {
            ExecutionFlowDAO tDao = new ExecutionFlowDAO(tSession);
            tDao.deleteFlow(pId);
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
            ExecutionFlowPO tFlowPo = tDao.readFullExecutionFlow(pId);
            return DtoHelper.getDeepCopy(tFlowPo);
        } finally
        {
            tSession.close();
        }
    }

    public List getListOfDto(FlowSearchCriterion pCriterion)
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
}
