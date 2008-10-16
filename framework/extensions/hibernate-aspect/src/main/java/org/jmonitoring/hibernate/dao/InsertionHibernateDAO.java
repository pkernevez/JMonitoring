package org.jmonitoring.hibernate.dao;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.jmonitoring.core.configuration.IInsertionDao;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.jmonitoring.core.persistence.InsertionDao;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * This class is exactly the same than <code>org.jmonitoring.core.persistence.InsertionDao</code>. But it overrides
 * all its method for exclusion weaving, because we don't want to weave the JMonitoring internal sql requests.
 */
public class InsertionHibernateDAO extends InsertionDao {
    private IInsertionDao mRealDao;

    public InsertionHibernateDAO(Session pSession) {
        super(pSession);
        mRealDao = new InsertionDao(pSession);
    }

    public InsertionHibernateDAO() {
        super();
        mRealDao = new InsertionDao(getSession());
    }

    public Session getSession() {
        return super.getSession();
    }

    public int countFlows() {
        return mRealDao.countFlows();
    }

    public int insertFullExecutionFlow(ExecutionFlowPO pExecutionFlow) {
        return mRealDao.insertFullExecutionFlow(pExecutionFlow);
    }

    /**
     * @param pFlowId The execution flow identifier to read.
     * @return The corresponding ExecutionFlowDTO.
     */
    public ExecutionFlowPO readExecutionFlow(int pFlowId) {
        Session tSession = getSession();
        ExecutionFlowPO tFlow = (ExecutionFlowPO) tSession.get(ExecutionFlowPO.class, new Integer(pFlowId));
        Criteria tCriteria = tSession.createCriteria(MethodCallPO.class).setFetchMode("children", FetchMode.JOIN);
        tCriteria.add(Restrictions.eq("flow.id", new Integer(pFlowId)));
        tCriteria.list();
        return tFlow;
    }

}
