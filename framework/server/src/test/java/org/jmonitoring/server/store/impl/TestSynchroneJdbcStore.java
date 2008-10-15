package org.jmonitoring.server.store.impl;

import java.io.IOException;

import org.jmonitoring.agent.store.IStoreWriter;
import org.jmonitoring.core.configuration.IInsertionDao;
import org.jmonitoring.core.persistence.InsertionDao;
import org.jmonitoring.test.dao.PersistanceTestCase;

public class TestSynchroneJdbcStore extends PersistanceTestCase {
    /**
     * @todo Check the name of this method.
     * @throws InterruptedException
     * @throws IOException
     */
    public void testNbToStringMethodCall() throws InterruptedException, IOException {
        IInsertionDao tFlowDao = new InsertionDao(getSession());
        assertEquals(0, tFlowDao.countFlows());

        IStoreWriter tWriter = new SynchroneJdbcStore();

        tWriter.writeExecutionFlow(buildNewFullFlow());

        closeAndRestartSession();

        tFlowDao = new InsertionDao(getSession());
        assertEquals(1, tFlowDao.countFlows());
    }

}
