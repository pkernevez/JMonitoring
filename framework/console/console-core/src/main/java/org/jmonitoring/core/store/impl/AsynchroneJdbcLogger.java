package org.jmonitoring.core.store.impl;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jmonitoring.core.dao.ExecutionFlowDaoFactory;
import org.jmonitoring.core.dao.IExecutionFlowDAO;
import org.jmonitoring.core.persistence.ExecutionFlowPO;
import org.jmonitoring.core.persistence.HibernateManager;

/**
 * @author pke
 * 
 * @todo impl�menter un maxfail si la base n'est pas dispo
 */
public class AsynchroneJdbcLogger extends AbstractAsynchroneLogger
{

    private static Log sLog = LogFactory.getLog(AsynchroneJdbcLogger.class);;

    private boolean mAutoflush = false;

    /**
     * Default constructor.
     */
    public AsynchroneJdbcLogger()
    {
        this(false);
    }

    /**
     * Default constructor.
     * 
     * @param pAutoFlush Flush all the jdbc access after the inserts.
     */
    public AsynchroneJdbcLogger(boolean pAutoFlush)
    {
        mAutoflush = pAutoFlush;
    }

    private class AsynchroneJdbcLoggerRunnable implements Runnable
    {
        private ExecutionFlowPO mExecutionFlowToLog;

        public AsynchroneJdbcLoggerRunnable(ExecutionFlowPO pExecutionFlowToLog)
        {
            mExecutionFlowToLog = pExecutionFlowToLog;
        }

        public void run()
        {
            try
            {
                long tStartTime = System.currentTimeMillis();
                Session tPManager = null;
                try
                {
                    tPManager = (Session) HibernateManager.getSession();
                    Transaction tTransaction = tPManager.getTransaction();
                    tTransaction.begin();
                    IExecutionFlowDAO tDao = ExecutionFlowDaoFactory.getExecutionFlowDao(tPManager);
                    tDao.insertFullExecutionFlow(mExecutionFlowToLog);
                    if (mAutoflush)
                    {
                        tPManager.flush();
                    } else
                    {
                        tTransaction.commit();
                        tPManager.close();
                    }
                    long tEndTime = System.currentTimeMillis();
                    sLog.info("Inserted ExecutionFlow " + mExecutionFlowToLog + " in " + (tEndTime - tStartTime)
                        + " ms.");
                } finally
                {
                    if (tPManager != null)
                    {
                        tPManager.close();
                    }
                }
            } catch (RuntimeException e)
            {
                sLog.error("Unable to insert ExecutionFlow into database", e);
            }
        }
    }

    /**
     * @see AbstractAsynchroneLogger#getAsynchroneLogTask(ExecutionFlowPO)
     */
    protected Runnable getAsynchroneLogTask(ExecutionFlowPO pFlow)
    {
        return new AsynchroneJdbcLoggerRunnable(pFlow);
    }
}