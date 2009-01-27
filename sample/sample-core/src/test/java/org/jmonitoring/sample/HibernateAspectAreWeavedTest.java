package org.jmonitoring.sample;

import org.jmonitoring.agent.store.impl.MemoryWriter;
import org.jmonitoring.core.configuration.IInsertionDao;
import org.jmonitoring.core.configuration.SpringConfigurationUtil;
import org.jmonitoring.sample.main.ItemPO;
import org.jmonitoring.sample.main.ShoppingCartPO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class HibernateAspectAreWeavedTest extends SamplePersistenceTestcase
{
    @Before
    public void initDB()
    {
        registerAgentSession();
    }

    @After
    public void clearDB()
    {
        rollbackAgentSession();
    }

    @Test
    public void testStandardHibernateAspectAreWeaved()
    {
        assertEquals(0, MemoryWriter.countFlows());
        IInsertionDao tDao = (IInsertionDao) SpringConfigurationUtil.getBean("realDao");
        assertEquals(0, tDao.countFlows());
        assertEquals(0, tDao.countFlows());
        assertEquals(2, MemoryWriter.countFlows());
    }

    @Test
    public void testSpecialHibernateAspectAreWeaved()
    {
        IInsertionDao tDao = (IInsertionDao) SpringConfigurationUtil.getBean("dao");
        assertEquals(0, tDao.countFlows());
        assertEquals(0, tDao.countFlows());
        assertEquals(0, MemoryWriter.countFlows());
    }

    @Test
    public void testSpecialHibernateAspectAreWeavedWithJdbc()
    {
        assertEquals(0, MemoryWriter.countFlows());
        // ConfigurationHelper.setProperty(ConfigurationHelper.DAO_STORE_KEY, InsertionHibernateDAO.class.getName());
        // StoreManager.changeStoreWriterClass(JdbcWriter.class);

        ShoppingCartPO tSc = new ShoppingCartPO();
        tSc.addItem(new ItemPO((float) 0.5));

        // closeAndRestartSession();

        IInsertionDao tDao = (IInsertionDao) SpringConfigurationUtil.getBean("dao");
        // IInsertionDao tDao = new InsertionDao(getSession());
        assertEquals(1, tDao.countFlows());
        // closeAndRestartSession();
        // tDao = new InsertionDao(getSession());
        assertEquals(0, MemoryWriter.countFlows());
        assertEquals(2, tDao.countFlows());
    }

}
