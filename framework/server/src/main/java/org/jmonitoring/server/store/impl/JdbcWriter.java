package org.jmonitoring.server.store.impl;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jmonitoring.agent.store.IStoreWriter;
import org.jmonitoring.core.configuration.IInsertionDao;
import org.jmonitoring.core.domain.ExecutionFlowPO;

public class JdbcWriter implements IStoreWriter
{

    private static Log sLog = LogFactory.getLog(JdbcWriter.class);;

    @Resource(name = "dao")
    private IInsertionDao mDao;

    @Resource(name = "sessionFactory")
    private SessionFactory mFactory;

    public void writeExecutionFlow(ExecutionFlowPO pExecutionFlow)
    {
        Session tSession = null;
        try
        {
            long tStartTime = System.currentTimeMillis();
            tSession = mFactory.getCurrentSession();
            Transaction tTransaction = tSession.getTransaction();
            tTransaction.begin();
            mDao.insertFullExecutionFlow(pExecutionFlow);
            tTransaction.commit();
            long tEndTime = System.currentTimeMillis();
            sLog.info("Inserted ExecutionFlow " + pExecutionFlow + " in " + (tEndTime - tStartTime) + " ms.");
        } catch (RuntimeException e)
        {
            sLog.error("Unable to insert ExecutionFlow into database", e);
        } finally
        {
            if (tSession != null && tSession.isOpen())
            {
                tSession.close();
            }
        }
    }
}
