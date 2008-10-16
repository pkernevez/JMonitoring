package org.jmonitoring.core.process;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jmonitoring.common.hibernate.HibernateManager;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TransactionHelper
{
    private static Log sLog = LogFactory.getLog(TransactionHelper.class);

    private Transaction mTransaction;

    private final Session mSession;

    public TransactionHelper()
    {
        mSession = HibernateManager.getSession();
        sLog.info("Session started...");
        mTransaction = mSession.beginTransaction();
        sLog.info("Transaction started...");
    }

    public void rollBack()
    {
        try
        {
            if (mTransaction != null && mTransaction.isActive())
            {
                sLog.info("Transaction rollbacked...");
                mTransaction.rollback();
            }
        } finally
        {
            if (mSession != null && mSession.isOpen())
            {
                mSession.close();
                sLog.info("Session closed...");
            }
        }

    }

    public void commit()
    {
        mTransaction.commit();
        sLog.info("Transaction commited...");
        mTransaction = null;
    }
}
