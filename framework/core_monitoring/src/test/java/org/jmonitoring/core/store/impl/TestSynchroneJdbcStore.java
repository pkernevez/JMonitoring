package org.jmonitoring.core.store.impl;

import org.jmonitoring.core.dao.ExecutionFlowDaoFactory;
import org.jmonitoring.core.dao.IExecutionFlowDAO;
import org.jmonitoring.core.dao.PersistanceTestCase;
import org.jmonitoring.core.store.AspectLoggerEmulator;

public class TestSynchroneJdbcStore extends PersistanceTestCase
{
    public void testNbToStringMethodCall() throws InterruptedException
    {
        int tInitialFlowCount;
        // We check the result into DB
        int tFinalFlowCount;
        IExecutionFlowDAO tFlowDao = ExecutionFlowDaoFactory.getExecutionFlowDao(getSession());
        tInitialFlowCount = tFlowDao.countFlows();

        MockAbstractAsynchroneLogger.resetNbLog();
        MockAbstractAsynchroneLogger.resetNbPublish();
        AspectLoggerEmulator tEmulator = new AspectLoggerEmulator(new SynchroneJdbcStore());
        tEmulator.simulateExecutionFlow(true);

        // Now check the number of toString called
        assertEquals(3, AspectLoggerEmulator.Param.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Parent.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Child1.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Child2.getNbToString());

        tFinalFlowCount = tFlowDao.countFlows();

        getSession().flush();
        assertEquals(tInitialFlowCount + 1, tFinalFlowCount);
    }
}
