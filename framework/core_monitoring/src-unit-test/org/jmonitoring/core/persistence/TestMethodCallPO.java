package org.jmonitoring.core.persistence;

import org.jmonitoring.core.dao.PersistanceTestCase;
import org.jmonitoring.core.dao.TestExecutionFlowDAO;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

public class TestMethodCallPO extends PersistanceTestCase
{
    public void testUpdateChildrenWhenCreateWithParent()
    {
        MethodCallPO tParent = new MethodCallPO(null, TestExecutionFlowDAO.class.getName(), "builNewFullFlow", "GrDefault",
                        new Object[0]);

        MethodCallPO tChild = new MethodCallPO(tParent, TestExecutionFlowDAO.class.getName(), "builNewFullFlow2",
                        "GrChild1", new Object[0]);    
        assertNull(tParent.getParentMethodCall());
        assertEquals(1, tParent.getChildren().size());
        assertSame(tParent, tChild.getParentMethodCall());
        
        tParent.removeChildren(tChild);
        assertNull(tParent.getParentMethodCall());
        assertEquals(0, tParent.getChildren().size());
        assertNull(tChild.getParentMethodCall());
        
        
    }
    
    public void testUpdateChildrenWhenAddParent()
    {
        MethodCallPO tParent = new MethodCallPO(null, TestExecutionFlowDAO.class.getName(), "builNewFullFlow", "GrDefault",
                        new Object[0]);

        MethodCallPO tChild = new MethodCallPO(null, TestExecutionFlowDAO.class.getName(), "builNewFullFlow2",
                        "GrChild1", new Object[0]);    

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
