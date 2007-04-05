package org.jmonitoring.server.store.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jmonitoring.agent.store.IStoreWriter;
import org.jmonitoring.common.hibernate.HibernateManager;
import org.jmonitoring.core.configuration.ConfigurationHelper;
import org.jmonitoring.core.configuration.MeasureException;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.persistence.InsertionDao;

public class SynchroneJdbcStore implements IStoreWriter
{

    private static Log sLog = LogFactory.getLog(SynchroneJdbcStore.class);;

    private static Constructor sConstructor;

    public void writeExecutionFlow(ExecutionFlowPO pExecutionFlow)
    {
        Session tPManager = null;
        try
        {
            long tStartTime = System.currentTimeMillis();
            tPManager = (Session) HibernateManager.getSession();
            Transaction tTransaction = tPManager.getTransaction();
            tTransaction.begin();
            getDao().insertFullExecutionFlow(pExecutionFlow);

            tTransaction.commit();

            long tEndTime = System.currentTimeMillis();
            sLog.info("Inserted ExecutionFlow " + pExecutionFlow + " in " + (tEndTime - tStartTime) + " ms.");
        } catch (RuntimeException e)
        {
            sLog.error("Unable to insert ExecutionFlow into database", e);
        } finally
        {
            if (tPManager != null && tPManager.isOpen())
            {
                tPManager.close();
            }
        }
    }

    private InsertionDao getDao()
    {
        Constructor tCon = sConstructor;
        if (tCon == null)
        {
            tCon = ConfigurationHelper.getDaoDefaultConstructor();
            sConstructor = tCon;
        }
        try
        {
            return (InsertionDao) tCon.newInstance(new Object[0]);
        } catch (IllegalArgumentException e)
        {
            throw new MeasureException("Unable to Call the default constructor of the DAO", e);
        } catch (InstantiationException e)
        {
            throw new MeasureException("Unable to Call the default constructor of the DAO", e);
        } catch (IllegalAccessException e)
        {
            throw new MeasureException("Unable to Call the default constructor of the DAO", e);
        } catch (InvocationTargetException e)
        {
            throw new MeasureException("Unable to Call the default constructor of the DAO", e);
        }
    }

}
