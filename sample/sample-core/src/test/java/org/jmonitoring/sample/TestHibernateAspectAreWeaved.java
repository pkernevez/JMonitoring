package org.jmonitoring.sample;

import org.jmonitoring.core.configuration.ConfigurationHelper;
import org.jmonitoring.core.persistence.InsertionDao;
import org.jmonitoring.core.store.StoreManager;
import org.jmonitoring.core.store.impl.MemoryStoreWriter;
import org.jmonitoring.hibernate.dao.InsertionHibernateDAO;
import org.jmonitoring.sample.main.ItemPO;
import org.jmonitoring.sample.main.ShoppingCartPO;
import org.jmonitoring.server.store.impl.SynchroneJdbcStore;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestHibernateAspectAreWeaved extends SamplePersistenceTestcase
{

    public void testStandardHibernateAspectAreWeaved()
    {
        assertEquals(0, MemoryStoreWriter.countFlows());
        StoreManager.changeStoreManagerClass(MemoryStoreWriter.class);
        InsertionDao tDao = new InsertionDao(getSession());
        assertEquals(0, tDao.countFlows());
        assertEquals(0, tDao.countFlows());
        assertEquals(2, MemoryStoreWriter.countFlows());
        
    }

    public void testSpecialHibernateAspectAreWeaved()
    {
        ConfigurationHelper.getInstance().addProperty(ConfigurationHelper.STORE_CLASS, MemoryStoreWriter.class.getName());
        InsertionDao tDao = new InsertionHibernateDAO(getSession());
        assertEquals(0, tDao.countFlows());
        assertEquals(0, tDao.countFlows());
        assertEquals(0, MemoryStoreWriter.countFlows());
    }

    public void testSpecialHibernateAspectAreWeavedWithJdbc()
    {
        assertEquals(0, MemoryStoreWriter.countFlows());
        ConfigurationHelper.getInstance().setProperty(ConfigurationHelper.DAO_STORE_KEY, InsertionHibernateDAO.class.getName());
        StoreManager.changeStoreManagerClass(SynchroneJdbcStore.class);
        
        ShoppingCartPO tSc = new ShoppingCartPO();
        tSc.addItem(new ItemPO((float) 0.5));

        closeAndRestartSession();
        
        InsertionDao tDao = new InsertionDao(getSession());
        assertEquals(1, tDao.countFlows());
        closeAndRestartSession();
        tDao = new InsertionDao(getSession());
        assertEquals(0, MemoryStoreWriter.countFlows());
        assertEquals(2, tDao.countFlows());
    }

}
