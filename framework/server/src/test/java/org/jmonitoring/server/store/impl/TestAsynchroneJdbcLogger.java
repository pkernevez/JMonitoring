package org.jmonitoring.server.store.impl;


import java.sql.SQLException;

import org.jmonitoring.core.persistence.InsertionDao;
import org.jmonitoring.core.store.IStoreWriter;
import org.jmonitoring.core.store.impl.MockAbstractAsynchroneLogger;
import org.jmonitoring.server.store.impl.AsynchroneJdbcLogger;
import org.jmonitoring.test.dao.PersistanceTestCase;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * @author pke
 * 
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 * Templates
 */
public class TestAsynchroneJdbcLogger extends PersistanceTestCase
{
    private static final int TIME_TO_WAIT = 1500;

    /**
     * @todo Check the name of this method.
     * @todo check that NbToString is really tested
     * @throws InterruptedException
     */
    public void testNbToStringMethodCall() throws InterruptedException
    {

        InsertionDao tFlowDao = new InsertionDao(getSession());
        assertEquals(0, tFlowDao.countFlows());
        
        IStoreWriter tWriter = new AsynchroneJdbcLogger();
        
        tWriter.writeExecutionFlow(buildNewFullFlow());
        Thread.sleep(TIME_TO_WAIT);
        
        getSession().flush();
        assertEquals(1, tFlowDao.countFlows());
    }
}
