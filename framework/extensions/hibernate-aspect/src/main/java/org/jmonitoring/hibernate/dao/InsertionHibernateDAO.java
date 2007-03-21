package org.jmonitoring.hibernate.dao;

import org.hibernate.Session;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.persistence.InsertionDao;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * This class is exactly the same than <code>org.jmonitoring.core.dao.ExecutionFlowDAO</code>. But it overrides all
 * its method for exclusion weaving, because we don't want to weave the JMonitoring internal sql requests.
 */
public class InsertionHibernateDAO extends InsertionDao
{
    private InsertionDao mRealDao;

    public InsertionHibernateDAO(Session pSession)
    {
        super(pSession);
        mRealDao = new InsertionDao(pSession);
    }

    public InsertionHibernateDAO()
    {
        super();
        mRealDao = new InsertionDao(getSession());
    }

    public int countFlows()
    {
        return mRealDao.countFlows();
    }

    public int insertFullExecutionFlow(ExecutionFlowPO pExecutionFlow)
    {
        return mRealDao.insertFullExecutionFlow(pExecutionFlow);
    }

}
