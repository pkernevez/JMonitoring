package org.jmonitoring.core.persistence;

import java.sql.SQLException;
import java.util.List;

import org.jmonitoring.core.dao.TestExecutionFlowMySqlDAO;
import org.jmonitoring.core.dao.PersistanceTestCase;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

public class TestMethodCallPO extends PersistanceTestCase
{
    public void testUpdateChildrenWhenCreateWithParent()
    {
        MethodCallPO tParent = new MethodCallPO(null, TestExecutionFlowMySqlDAO.class.getName(), "builNewFullFlow", "GrDefault",
                        new Object[0]);

        MethodCallPO tChild = new MethodCallPO(tParent, TestExecutionFlowMySqlDAO.class.getName(), "builNewFullFlow2",
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
        MethodCallPO tParent = new MethodCallPO(null, TestExecutionFlowMySqlDAO.class.getName(), "builNewFullFlow", "GrDefault",
                        new Object[0]);

        MethodCallPO tChild = new MethodCallPO(null, TestExecutionFlowMySqlDAO.class.getName(), "builNewFullFlow2",
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
    
//    public void testGetListOfExecutionFlow() throws SQLException 
//    {
//
//        createDataSet("/dataset.xml");
//        List tResult = HibernateManager.getSession().createQuery("from ExecutionFlowPO").list();
//        assertEquals(3, tResult.size());
//   }
}
