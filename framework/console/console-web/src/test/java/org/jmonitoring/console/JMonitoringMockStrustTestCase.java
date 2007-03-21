package org.jmonitoring.console;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jmonitoring.common.hibernate.HibernateManager;

import servletunit.struts.MockStrutsTestCase;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public abstract class JMonitoringMockStrustTestCase extends MockStrutsTestCase
{
    private Transaction mTx;

    private Session mSession;

    protected void setUp() throws Exception
    {
        super.setUp();
        mSession = HibernateManager.getSession();
        mTx = mSession.beginTransaction();
    }

    public void closeAndRestartSession()
    {
        if (mSession.isOpen())
        {
            if (mTx.isActive())
            {
                mTx.commit();
            }
            mSession.close();
        }

        mSession = HibernateManager.getSession();
        mTx = mSession.beginTransaction();
    }

    protected void tearDown() throws Exception
    {
        if (mSession.isOpen())
        {
            if (mTx.isActive())
            {
                mTx.rollback();
            }
            mSession.close();
        }
        if (HibernateManager.getSession().isOpen())
        {
            HibernateManager.getSession().close();
        }
    }

}
