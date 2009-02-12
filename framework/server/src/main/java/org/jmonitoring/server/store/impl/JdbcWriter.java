package org.jmonitoring.server.store.impl;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jmonitoring.agent.store.IStoreWriter;
import org.jmonitoring.core.configuration.IInsertionDao;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcWriter implements IStoreWriter
{

    private static Logger sLog = LoggerFactory.getLogger(JdbcWriter.class);;

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
