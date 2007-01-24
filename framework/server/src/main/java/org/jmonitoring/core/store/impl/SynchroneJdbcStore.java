package org.jmonitoring.core.store.impl;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jmonitoring.common.hibernate.HibernateManager;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.persistence.InsertionDao;
import org.jmonitoring.core.store.IStoreWriter;


public class SynchroneJdbcStore implements IStoreWriter
{

    private static Log sLog = LogFactory.getLog(SynchroneJdbcStore.class);;


    public void writeExecutionFlow(ExecutionFlowPO pExecutionFlow)
    {
        try
        {
            long tStartTime = System.currentTimeMillis();
            Session tPManager = (Session) HibernateManager.getSession();
            Transaction tTransaction = tPManager.getTransaction();
            tTransaction.begin();
            InsertionDao tDao = new InsertionDao(tPManager);
            tDao.insertFullExecutionFlow(pExecutionFlow);

            tTransaction.commit();
            tPManager.close();

            long tEndTime = System.currentTimeMillis();
            sLog.info("Inserted ExecutionFlow " + pExecutionFlow + " in " + (tEndTime - tStartTime) + " ms.");
        } catch (RuntimeException e)
        {
            sLog.error("Unable to insert ExecutionFlow into database", e);
        }
    }

}
