package org.jmonitoring.hibernate.info;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class Bidon {

    public void method1() {
        System.out.println("method1");
    }

    public void method2() {
        System.out.println("method2");
        method1();
    }

    public void method3() {
        System.out.println("method3");
        method2();
    }

}
