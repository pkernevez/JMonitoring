package org.jmonitoring.sample;

import org.jmonitoring.agent.store.StoreManager;
import org.jmonitoring.agent.store.impl.MemoryWriter;
import org.jmonitoring.core.configuration.ConfigurationHelper;
import org.jmonitoring.core.configuration.IInsertionDao;
import org.jmonitoring.core.persistence.InsertionDao;
import org.jmonitoring.hibernate.dao.InsertionHibernateDAO;
import org.jmonitoring.sample.main.ItemPO;
import org.jmonitoring.sample.main.ShoppingCartPO;
import org.jmonitoring.server.store.impl.JdbcWriter;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestHibernateAspectAreWeaved extends SamplePersistenceTestcase
{

    public void testStandardHibernateAspectAreWeaved()
    {
        assertEquals(0, MemoryWriter.countFlows());
        StoreManager.changeStoreWriterClass(MemoryWriter.class);
        IInsertionDao tDao = new InsertionDao(getSession());
        assertEquals(0, tDao.countFlows());
        assertEquals(0, tDao.countFlows());
        assertEquals(2, MemoryWriter.countFlows());

    }

    public void testSpecialHibernateAspectAreWeaved()
    {
        ConfigurationHelper.setProperty(ConfigurationHelper.STORE_CLASS, MemoryWriter.class.getName());
        IInsertionDao tDao = new InsertionHibernateDAO(getSession());
        assertEquals(0, tDao.countFlows());
        assertEquals(0, tDao.countFlows());
        assertEquals(0, MemoryWriter.countFlows());
    }

    public void testSpecialHibernateAspectAreWeavedWithJdbc()
    {
        assertEquals(0, MemoryWriter.countFlows());
        ConfigurationHelper.setProperty(ConfigurationHelper.DAO_STORE_KEY, InsertionHibernateDAO.class.getName());
        StoreManager.changeStoreWriterClass(JdbcWriter.class);

        ShoppingCartPO tSc = new ShoppingCartPO();
        tSc.addItem(new ItemPO((float) 0.5));

        closeAndRestartSession();

        IInsertionDao tDao = new InsertionDao(getSession());
        assertEquals(1, tDao.countFlows());
        closeAndRestartSession();
        tDao = new InsertionDao(getSession());
        assertEquals(0, MemoryWriter.countFlows());
        assertEquals(2, tDao.countFlows());
    }

}
