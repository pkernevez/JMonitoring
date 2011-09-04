package org.jmonitoring.server.store.impl;

import java.io.IOException;

import org.jmonitoring.core.configuration.IInsertionDao;
import org.jmonitoring.test.dao.PersistanceTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = {"/jdbc-synchrone-test.xml" })
public class TestJdbcWriter extends PersistanceTestCase
{
    @Autowired
    private IInsertionDao mFlowDao;

    @Autowired
    private JdbcWriter mWriter;

    @Test
    public void testJdbcInsert() throws InterruptedException, IOException
    {
        assertEquals(0, mFlowDao.countFlows());

        mWriter.writeExecutionFlow(buildNewFullFlow());

        commitAndRestartSession();

        assertEquals(1, mFlowDao.countFlows());
    }

}
