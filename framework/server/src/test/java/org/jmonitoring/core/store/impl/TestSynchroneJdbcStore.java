package org.jmonitoring.core.store.impl;


import org.jmonitoring.core.persistence.InsertionDao;
import org.jmonitoring.core.store.IStoreWriter;
import org.jmonitoring.core.store.impl.MockAbstractAsynchroneLogger;
import org.jmonitoring.core.store.impl.SynchroneJdbcStore;
import org.jmonitoring.test.dao.PersistanceTestCase;

public class TestSynchroneJdbcStore extends PersistanceTestCase
{
    public void testNbToStringMethodCall() throws InterruptedException
    {

        InsertionDao tFlowDao = new InsertionDao(getSession());
        assertEquals(0, tFlowDao.countFlows());
        
        IStoreWriter tWriter = new SynchroneJdbcStore();
        
        tWriter.writeExecutionFlow(buildNewFullFlow());
        
        getSession().flush();
        assertEquals(1, tFlowDao.countFlows());
    }
}
