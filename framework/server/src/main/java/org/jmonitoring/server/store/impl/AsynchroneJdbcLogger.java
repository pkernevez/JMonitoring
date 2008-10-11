package org.jmonitoring.server.store.impl;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jmonitoring.agent.store.impl.AbstractAsynchroneWriter;
import org.jmonitoring.common.hibernate.HibernateManager;
import org.jmonitoring.core.configuration.ConfigurationHelper;
import org.jmonitoring.core.configuration.MeasureException;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.persistence.InsertionDao;

/**
 * @author pke
 * 
 * @todo impl�menter un maxfail si la base n'est pas dispo
 */
public class AsynchroneJdbcLogger extends AbstractAsynchroneWriter {

    private static Log sLog = LogFactory.getLog(AsynchroneJdbcLogger.class);;

    private static Constructor sConstructor;

    private boolean mAutoflush = false;

    /**
     * Default constructor.
     */
    public AsynchroneJdbcLogger() {
        this(false);
    }

    /**
     * Default constructor.
     * 
     * @param pAutoFlush Flush all the jdbc access after the inserts.
     */
    public AsynchroneJdbcLogger(boolean pAutoFlush) {
        mAutoflush = pAutoFlush;
    }

    private class AsynchroneJdbcLoggerRunnable implements Runnable {
        private ExecutionFlowPO mExecutionFlowToLog;

        public AsynchroneJdbcLoggerRunnable(ExecutionFlowPO pExecutionFlowToLog) {
            mExecutionFlowToLog = pExecutionFlowToLog;
        }

        public void run() {
            try {
                long tStartTime = System.currentTimeMillis();
                Session tPManager = null;
                try {
                    tPManager = (Session) HibernateManager.getSession();
                    Transaction tTransaction = tPManager.getTransaction();
                    tTransaction.begin();
                    getDao().insertFullExecutionFlow(mExecutionFlowToLog);
                    if (mAutoflush) {
                        tPManager.flush();
                    } else {
                        tTransaction.commit();
                    }
                    long tEndTime = System.currentTimeMillis();
                    sLog.info("Inserted ExecutionFlow " + mExecutionFlowToLog + " in " + (tEndTime - tStartTime)
                            + " ms.");
                } finally {
                    if (tPManager != null && tPManager.isOpen()) {
                        tPManager.close();
                    }
                }
            } catch (RuntimeException e) {
                sLog.error("Unable to insert ExecutionFlow into database", e);
            }
        }
    }

    /**
     * @see AbstractAsynchroneWriter#getAsynchroneLogTask(ExecutionFlowPO)
     */
    protected Runnable getAsynchroneLogTask(ExecutionFlowPO pFlow) {
        return new AsynchroneJdbcLoggerRunnable(pFlow);
    }

    private InsertionDao getDao() {
        Constructor tCon = sConstructor;
        if (tCon == null) {
            tCon = ConfigurationHelper.getDaoDefaultConstructor();
            sConstructor = tCon;
        }
        try {
            return (InsertionDao) tCon.newInstance(new Object[0]);
        } catch (IllegalArgumentException e) {
            throw new MeasureException("Unable to Call the default constructor of the DAO", e);
        } catch (InstantiationException e) {
            throw new MeasureException("Unable to Call the default constructor of the DAO", e);
        } catch (IllegalAccessException e) {
            throw new MeasureException("Unable to Call the default constructor of the DAO", e);
        } catch (InvocationTargetException e) {
            throw new MeasureException("Unable to Call the default constructor of the DAO", e);
        }
    }

}
