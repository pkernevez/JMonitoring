package org.jmonitoring.hibernate.info;

import junit.framework.TestCase;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestBidon extends TestCase
{

    public void testTest1()
    {
        Bidon tBid = new Bidon();
        System.out.println("Test1");
        tBid.method1();
        System.out.println("Test2");
        tBid.method2();
        System.out.println("Test3");
        tBid.method3();
    }
}
