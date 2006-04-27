package org.jmonitoring.core.store.impl;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.jmonitoring.core.configuration.Configuration;
import org.jmonitoring.core.dao.ExecutionFlowMySqlDAO;
import org.jmonitoring.core.dao.StandAloneConnectionManager;
import org.jmonitoring.core.dto.ExecutionFlowDTO;

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

    private static class AsynchroneJdbcLoggerRunnable implements Runnable
    {
        private static ThreadLocal mPersistanceManager = new ThreadLocal();

        private ExecutionFlowDTO mExecutionFlowToLog;

        public AsynchroneJdbcLoggerRunnable(ExecutionFlowDTO pExecutionFlowToLog)
        {
            mExecutionFlowToLog = pExecutionFlowToLog;
        }

        public void run()
        {
            Session tPManager = (Session) mPersistanceManager.get();
//try
//{
//            if (tPManager == null)
//                {
//                    tPManager =new StandAloneConnectionManager(Configuration.getInstance());
//                }
//                mPersistanceManager.set(tPManager);
//                ExecutionFlowMySqlDAO tDao = new ExecutionFlowMySqlDAO(tPManager);
//                int tId = tDao.insertFullExecutionFlow(mExecutionFlowToLog);
//                //mPersistanceManager.;
//                sLog.info("Inserted ExecutionFlow " + mExecutionFlowToLog);
//            } catch (Exception e)
//            {
//                sLog.error("Exception receive during JobProcessing. Exception ignored.", e);
//                if (tCon != null)
//                {
//                    try
//                    {
//                        tCon.rollback();
//                    } catch (SQLException e2)
//                    {
//                        sLog.error("Unable to rollback the failed log of ExecutionFlow.", e);
//                    }
//                }
//            }
        }

    }

    /**
     * @see AbstractAsynchroneLogger#getAsynchroneLogTask(ExecutionFlow)
     */
    protected Runnable getAsynchroneLogTask(ExecutionFlowDTO pFlow)
    {
        return new AsynchroneJdbcLoggerRunnable(pFlow);
    };

}
