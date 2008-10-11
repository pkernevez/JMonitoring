package org.jmonitoring.core.domain;

import junit.framework.TestCase;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestMethodCallPO extends TestCase {
    public void testUpdateChildrenWhenCreateWithParent() {
        MethodCallPO tParent = new MethodCallPO(null, TestMethodCallPO.class.getName(), "builNewFullFlow", "GrDefault",
                "");

        MethodCallPO tChild = new MethodCallPO(tParent, TestMethodCallPO.class.getName(), "builNewFullFlow2",
                "GrChild1", "");
        assertNull(tParent.getParentMethodCall());
        assertEquals(1, tParent.getChildren().size());
        assertSame(tParent, tChild.getParentMethodCall());

        tParent.removeChildren(tChild);
        assertNull(tParent.getParentMethodCall());
        assertEquals(0, tParent.getChildren().size());
        assertNull(tChild.getParentMethodCall());

    }

    public void testUpdateChildrenWhenAddParent() {
        MethodCallPO tParent = new MethodCallPO(null, TestMethodCallPO.class.getName(), "builNewFullFlow", "GrDefault",
                "");

        MethodCallPO tChild = new MethodCallPO(null, TestMethodCallPO.class.getName(), "builNewFullFlow2", "GrChild1",
                "");

        assertNull(tParent.getParentMethodCall());
        assertEquals(0, tParent.getChildren().size());
        assertNull(tChild.getParentMethodCall());

        tChild.setParentMethodCall(tParent);
        assertNull(tParent.getParentMethodCall());
        assertEquals(1, tParent.getChildren().size());
        assertSame(tParent, tChild.getParentMethodCall());

        tChild.setParentMethodCall(null);
        tChild.setParentMethodCall(null); // On teste avec null 2 fois...
        assertNull(tParent.getParentMethodCall());
        assertEquals(0, tParent.getChildren().size());
        assertNull(tChild.getParentMethodCall());

        tChild.setParentMethodCall(tParent);
        assertNull(tParent.getParentMethodCall());
        assertEquals(1, tParent.getChildren().size());
        assertSame(tParent, tChild.getParentMethodCall());

    }
}
