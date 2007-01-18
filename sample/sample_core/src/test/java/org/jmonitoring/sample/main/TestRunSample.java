package org.jmonitoring.sample.main;

import org.jmonitoring.core.configuration.Configuration;
import org.jmonitoring.core.dao.ExecutionFlowDaoFactory;
import org.jmonitoring.core.dao.IExecutionFlowDAO;
import org.jmonitoring.core.persistence.ExecutionFlowPO;
import org.jmonitoring.core.persistence.MethodCallPO;
import org.jmonitoring.core.store.impl.SynchroneJdbcStore;
import org.jmonitoring.sample.SamplePersistenceTestcase;
import org.jmonitoring.sample.persistence.SampleHibernateManager;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestRunSample extends SamplePersistenceTestcase
{

    public void testRun()
    {
        ShoppingCartPO.setCounter(0);
        Configuration.getInstance().setMeasurePointStoreClass(SynchroneJdbcStore.class);
        new RunSample(getSampleSession()).run();
        SampleHibernateManager.flush();
        SampleHibernateManager.clear();

        checkRun();

    }

    private void checkRun()
    {
        IExecutionFlowDAO tDao = ExecutionFlowDaoFactory.getExecutionFlowDao(getSession());
        assertEquals(3, tDao.countFlows());
        ExecutionFlowPO tFlow = tDao.readExecutionFlow(1);
        assertEquals("org.jmonitoring.sample.main.RunSample", tFlow.getFirstMethodCall().getClassName());
        assertEquals("run", tFlow.getFirstMethodCall().getMethodName());
        assertEquals(10, tFlow.getFirstMethodCall().getChildren().size());

        MethodCallPO tCurMeth = tFlow.getFirstMethodCall().getChild(0);
        assertEquals("org.jmonitoring.sample.main.Inventory", tCurMeth.getClassName());
        assertEquals("addItem", tCurMeth.getMethodName());

        tCurMeth = tFlow.getFirstMethodCall().getChild(1);
        assertEquals("org.jmonitoring.sample.main.Inventory", tCurMeth.getClassName());
        assertEquals("addItem", tCurMeth.getMethodName());

        tCurMeth = tFlow.getFirstMethodCall().getChild(2);
        assertEquals("org.jmonitoring.sample.main.Inventory", tCurMeth.getClassName());
        assertEquals("addItem", tCurMeth.getMethodName());

        tCurMeth = tFlow.getFirstMethodCall().getChild(3);
        assertEquals("org.jmonitoring.sample.main.Inventory", tCurMeth.getClassName());
        assertEquals("removeItem", tCurMeth.getMethodName());

        tCurMeth = tFlow.getFirstMethodCall().getChild(4);
        assertEquals("org.jmonitoring.sample.main.ShoppingCartPO", tCurMeth.getClassName());
        assertEquals("addItem", tCurMeth.getMethodName());

        tCurMeth = tFlow.getFirstMethodCall().getChild(5);
        assertEquals("org.jmonitoring.sample.main.Inventory", tCurMeth.getClassName());
        assertEquals("removeItem", tCurMeth.getMethodName());

        tCurMeth = tFlow.getFirstMethodCall().getChild(6);
        assertEquals("org.jmonitoring.sample.main.ShoppingCartPO", tCurMeth.getClassName());
        assertEquals("addItem", tCurMeth.getMethodName());

        tCurMeth = tFlow.getFirstMethodCall().getChild(7);
        assertEquals("org.jmonitoring.sample.persistence.SampleDao", tCurMeth.getClassName());
        assertEquals("save", tCurMeth.getMethodName());
        assertEquals(4, tCurMeth.getChildren().size());

        checkSqlMethodCall(tFlow);

        tCurMeth = tFlow.getFirstMethodCall().getChild(8);
        assertEquals("org.jmonitoring.sample.main.Inventory", tCurMeth.getClassName());
        assertEquals("removeItem", tCurMeth.getMethodName());
        // Remove Item3
        assertEquals("[Item: -1]", tCurMeth.getParams());
        assertEquals(0, tCurMeth.getChildren().size());

        tCurMeth = tFlow.getFirstMethodCall().getChild(9);
        assertEquals("org.jmonitoring.sample.main.ShoppingCartPO", tCurMeth.getClassName());
        assertEquals("addItem", tCurMeth.getMethodName());
        // Add Item3
        assertEquals("[Item: -1]", tCurMeth.getParams());
        assertEquals(0, tCurMeth.getChildren().size());
    }

    private void checkSqlMethodCall(ExecutionFlowPO tFlow)
    {
        MethodCallPO tCurMeth;
        MethodCallPO tCurParent = tFlow.getFirstMethodCall().getChild(7).getChild(0); 
        assertEquals(6, tCurParent.getChildren().size());
        // Start of the Sql Request
        StringBuffer tTrace = new StringBuffer();
        tCurMeth = tCurParent.getChild(0);
        assertEquals("java.sql.PreparedStatement", tCurMeth.getClassName());
        assertEquals("executeUpdate", tCurMeth.getMethodName());
        tTrace.append("PrepareStatement with Sql=[insert into SHOPPING_CART (ID) values (null)]\n");
        tTrace.append("Execute update\n");
        assertEquals(tTrace.toString(), tCurMeth.getReturnValue());
        assertEquals(0, tCurMeth.getChildren().size());

        tCurMeth = tCurParent.getChild(1);
        assertEquals("java.sql.PreparedStatement", tCurMeth.getClassName());
        assertEquals("executeQuery", tCurMeth.getMethodName());
        tTrace = new StringBuffer();
        tTrace.append("PrepareStatement with Sql=[call identity()]\n");
        tTrace.append("Execute query\n");
        assertEquals(tTrace.toString(), tCurMeth.getReturnValue());
        assertEquals(0, tCurMeth.getChildren().size());

        tCurMeth = tCurParent.getChild(2);
        assertEquals("java.sql.PreparedStatement", tCurMeth.getClassName());
        assertEquals("executeUpdate", tCurMeth.getMethodName());
        tTrace = new StringBuffer();
        tTrace.append("PrepareStatement with Sql=[insert into ITEM (PRICE, ID) values (?, null)]\n");
        tTrace.append("Add Float parameter, pos=[1], value=[30.0]\n");
        tTrace.append("Execute update\n");
        assertEquals(tTrace.toString(), tCurMeth.getReturnValue());
        assertEquals(0, tCurMeth.getChildren().size());

        tCurMeth = tCurParent.getChild(3);
        assertEquals("java.sql.PreparedStatement", tCurMeth.getClassName());
        assertEquals("executeQuery", tCurMeth.getMethodName());
        tTrace = new StringBuffer();
        tTrace.append("PrepareStatement with Sql=[call identity()]\n");
        tTrace.append("Execute query\n");
        assertEquals(tTrace.toString(), tCurMeth.getReturnValue());
        assertEquals(0, tCurMeth.getChildren().size());

        tCurMeth = tCurParent.getChild(4);
        assertEquals("java.sql.PreparedStatement", tCurMeth.getClassName());
        assertEquals("executeUpdate", tCurMeth.getMethodName());
        tTrace = new StringBuffer();
        tTrace.append("PrepareStatement with Sql=[insert into ITEM (PRICE, ID) values (?, null)]\n");
        tTrace.append("Add Float parameter, pos=[1], value=[31.0]\n");
        tTrace.append("Execute update\n");
        assertEquals(tTrace.toString(), tCurMeth.getReturnValue());
        assertEquals(0, tCurMeth.getChildren().size());

        tCurMeth = tCurParent.getChild(5);
        assertEquals("java.sql.PreparedStatement", tCurMeth.getClassName());
        assertEquals("executeQuery", tCurMeth.getMethodName());
        tTrace = new StringBuffer();
        tTrace.append("PrepareStatement with Sql=[call identity()]\n");
        tTrace.append("Execute query\n");
        assertEquals(tTrace.toString(), tCurMeth.getReturnValue());
        assertEquals(0, tCurMeth.getChildren().size());
        
        tCurParent = tFlow.getFirstMethodCall().getChild(7); 
        assertEquals(4, tCurParent.getChildren().size());
        tCurMeth = tCurParent.getChild(1);
        assertEquals("org.hibernate.Session", tCurMeth.getClassName());
        assertEquals("connection", tCurMeth.getMethodName());
        assertEquals("org.hsqldb.jdbc.jdbcConnection", tCurMeth.getReturnValue().substring(0,30));
        assertEquals(0, tCurMeth.getChildren().size());
        
        tCurMeth = tCurParent.getChild(2);
        assertEquals("java.sql.Statement", tCurMeth.getClassName());
        assertEquals("execute", tCurMeth.getMethodName());
        tTrace = new StringBuffer();
        tTrace.append("Sql=[Select count(*) from SHOPPING_CART]\n");
        assertEquals(tTrace.toString(), tCurMeth.getReturnValue());
        assertEquals(0, tCurMeth.getChildren().size());
        
        tCurMeth = tCurParent.getChild(3);
        assertEquals("java.sql.Statement", tCurMeth.getClassName());
        assertEquals("execute", tCurMeth.getMethodName());
        tTrace = new StringBuffer();
        tTrace.append("Sql=[Select * from SHOPPING_CART]\n");
        assertEquals(tTrace.toString(), tCurMeth.getReturnValue());
        assertEquals(0, tCurMeth.getChildren().size());
        
        
    }

}
