package org.jmonitoring.hibernate.info;

import org.jmonitoring.agent.store.StoreManager;
import org.jmonitoring.agent.store.impl.MemoryStoreWriter;

import junit.framework.TestCase;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestBidon extends TestCase
{

    public void testTest1()
    {
        StoreManager.changeStoreManagerClass(MemoryStoreWriter.class);
        Bidon tBid = new Bidon();
        tBid.method1();
        tBid.method2();
        tBid.method3();
    }
}
