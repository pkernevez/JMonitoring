package org.jmonitoring.core.process;

import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jmonitoring.common.hibernate.HibernateManager;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public aspect TransactionAspect
{
    pointcut transactionToStart() : execution(public * org.jmonitoring.core.process.JMonitoringProcess.*(..))
     && !execution(public * org.jmonitoring.core.process.JMonitoringProcess.convert*(..));

    private static Log sLog = LogFactory.getLog(TransactionAspect.class);

    Object around() : transactionToStart()
    {
        Session tSession = HibernateManager.getSession();
        sLog.info("Session started...");
        Transaction tTransaction = tSession.beginTransaction();
        sLog.info("Transaction started...");
        try
        {
            Object tResult = proceed();
            tTransaction.commit();
            sLog.info("Transaction commited...");
            tTransaction = null;
            return tResult;
        } finally
        {
            try
            {
                if (tTransaction != null && tTransaction.isActive())
                {
                    sLog.info("Transaction rollbacked...");
                    tTransaction.rollback();
                }
            } finally
            {
                if (tSession != null)
                {
                    tSession.close();
                    sLog.info("Session closed...");
                }
            }
        }
    }
}
