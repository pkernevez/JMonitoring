package org.jmonitoring.core.store.impl;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.jmonitoring.core.dao.ExecutionFlowDAO;
import org.jmonitoring.core.persistence.ExecutionFlowPO;
import org.jmonitoring.core.persistence.HibernateManager;

/**
 * @author pke
 * 
 * @todo refactoring pour avoir une clase abstraite avec Asynchronisme
 * @todo Utiliser une datasource pour la connection à la base
 * @todo implémenter un maxfail si la base n'est pas dispo
 */
public class AsynchroneJdbcLogger extends AbstractAsynchroneLogger
{

    private static Log sLog;

    private static boolean mAutoflush = false;

    /**
     * Default constructor.
     */
    public AsynchroneJdbcLogger()
    {
        if (sLog == null)
        {
            sLog = LogFactory.getLog(AsynchroneJdbcLogger.class);
        }
    }

    /**
     * Default constructor.
     * 
     * @param pAutoFlush
     */
    public AsynchroneJdbcLogger(boolean pAutoFlush)
    {
        mAutoflush = pAutoFlush;
        if (sLog == null)
        {
            sLog = LogFactory.getLog(AsynchroneJdbcLogger.class);
        }
    }

    private static class AsynchroneJdbcLoggerRunnable implements Runnable
    {
        private ExecutionFlowPO mExecutionFlowToLog;

        public AsynchroneJdbcLoggerRunnable(ExecutionFlowPO pExecutionFlowToLog)
        {
            mExecutionFlowToLog = pExecutionFlowToLog;
        }

        public void run()
        {
            Session tPManager = (Session) HibernateManager.getSession();
            ExecutionFlowDAO tDao = new ExecutionFlowDAO(tPManager);
            int tId = tDao.insertFullExecutionFlow(mExecutionFlowToLog);
            if (mAutoflush)
            {
                tPManager.flush();
            }
            sLog.info("Inserted ExecutionFlow " + mExecutionFlowToLog);
        }
    }

    /**
     * @see AbstractAsynchroneLogger#getAsynchroneLogTask(ExecutionFlowPO)
     */
    protected Runnable getAsynchroneLogTask(ExecutionFlowPO pFlow)
    {
        return new AsynchroneJdbcLoggerRunnable(pFlow);
    };

}
