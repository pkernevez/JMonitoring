package net.kernevez.performance.log;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.sql.Connection;
import java.sql.SQLException;

import net.kernevez.performance.configuration.Configuration;
import net.kernevez.performance.dao.ExecutionFlowMySqlDAO;
import net.kernevez.performance.dao.StandAloneConnectionManager;
import net.kernevez.performance.measure.ExecutionFlow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
        private static ThreadLocal mConnection = new ThreadLocal();

        private ExecutionFlow mExecutionFlowToLog;

        public AsynchroneJdbcLoggerRunnable(ExecutionFlow pExecutionFlowToLog)
        {
            mExecutionFlowToLog = pExecutionFlowToLog;
        }

        public void run()
        {
            Connection tCon = null;
            try
            {
                tCon = (Connection) mConnection.get();
                if (tCon == null)
                {
                    tCon = new StandAloneConnectionManager(Configuration.getInstance()).getConnection();
                }
                mConnection.set(tCon);
                ExecutionFlowMySqlDAO tDao = new ExecutionFlowMySqlDAO(tCon);
                int tId = tDao.insertFullExecutionFlow(mExecutionFlowToLog);
                tCon.commit();
                sLog.info("Inserted ExecutionFlow " + mExecutionFlowToLog);
            } catch (Exception e)
            {
                sLog.error("Exception receive during JobProcessing. Exception ignored.", e);
                if (tCon != null)
                {
                    try
                    {
                        tCon.rollback();
                    } catch (SQLException e2)
                    {
                        sLog.error("Unable to rollback the failed log of ExecutionFlow.", e);
                    }
                }
            }
        }

    }

    /**
     * @see AbstractAsynchroneLogger#getAsynchroneLogTask(ExecutionFlow)
     */
    protected Runnable getAsynchroneLogTask(ExecutionFlow pFlow)
    {
        return new AsynchroneJdbcLoggerRunnable(pFlow);
    };

}
