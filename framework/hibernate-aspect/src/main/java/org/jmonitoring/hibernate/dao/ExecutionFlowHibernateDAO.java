package org.jmonitoring.hibernate.dao;

import java.util.List;

import org.hibernate.Session;
import org.jmonitoring.core.common.UnknownFlowException;
import org.jmonitoring.core.dao.ExecutionFlowDAO;
import org.jmonitoring.core.dao.ExecutionFlowDaoFactory;
import org.jmonitoring.core.dao.FlowSearchCriterion;
import org.jmonitoring.core.dao.IExecutionFlowDAO;
import org.jmonitoring.core.persistence.ExecutionFlowPO;
import org.jmonitoring.core.persistence.MethodCallPO;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * This class is exactly the same than <code>org.jmonitoring.core.dao.ExecutionFlowDAO</code>. But it overrides all
 * its method for exclusion weaving, because we don't want to weave the JMonitoring internal sql requests.
 */
public class ExecutionFlowHibernateDAO implements IExecutionFlowDAO
{
    private IExecutionFlowDAO mRealDao;

    public ExecutionFlowHibernateDAO(Session pSession)
    {
        mRealDao = new ExecutionFlowDAO(pSession);
    }

    public int countFlows()
    {
        return mRealDao.countFlows();
    }

    public void createDataBase()
    {
        mRealDao.createDataBase();
    }

    public void deleteAllFlows()
    {
        mRealDao.deleteAllFlows();
    }

    public void deleteFlow(int pId) throws UnknownFlowException
    {
        mRealDao.deleteFlow(pId);
    }

    public List getListOfExecutionFlowPO(FlowSearchCriterion pCriterion)
    {
        return mRealDao.getListOfExecutionFlowPO(pCriterion);
    }

    public List getListOfMethodCall(String pClassName, String pMethodName)
    {
        return mRealDao.getListOfMethodCall(pClassName, pMethodName);
    }

    public List getListOfMethodCallExtract()
    {
        return mRealDao.getListOfMethodCallExtract();
    }

    public List getMethodCallList(String pClassName, String pMethodName, long pDurationMin, long pDurationMax)
    {
        return mRealDao.getMethodCallList(pClassName, pMethodName, pDurationMin, pDurationMax);
    }

    public int insertFullExecutionFlow(ExecutionFlowPO pExecutionFlow)
    {
        return mRealDao.insertFullExecutionFlow(pExecutionFlow);
    }

    public ExecutionFlowPO readExecutionFlow(int pFlowId)
    {
        return mRealDao.readExecutionFlow(pFlowId);
    }

    public MethodCallPO readMethodCall(int pFlowId, int pMethodId)
    {
        return mRealDao.readMethodCall(pFlowId, pMethodId);
    }

}
