package org.jmonitoring.hibernate.info;

import junit.framework.TestCase;

import org.jmonitoring.agent.store.StoreManager;
import org.jmonitoring.agent.store.impl.MemoryWriter;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestBidon extends TestCase {

    public void testTest1() {
        StoreManager.changeStoreWriterClass(MemoryWriter.class);
        Bidon tBid = new Bidon();
        tBid.method1();
        tBid.method2();
        tBid.method3();
    }
}
