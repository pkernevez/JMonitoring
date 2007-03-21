package org.jmonitoring.sample.main;

import org.jmonitoring.core.configuration.ConfigurationHelper;
import org.jmonitoring.core.dao.ConsoleDao;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.jmonitoring.core.persistence.InsertionDao;
import org.jmonitoring.core.store.StoreFactory;
import org.jmonitoring.core.store.StoreManager;
import org.jmonitoring.core.store.impl.MemoryStoreWriter;
import org.jmonitoring.hibernate.dao.InsertionHibernateDAO;
import org.jmonitoring.sample.SamplePersistenceTestcase;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestRunSample extends SamplePersistenceTestcase
{

    public void testAllAspectAreAppliedIncludingThoseOfHibernateWithExtensionsInMemory()
    {
        ShoppingCartPO.setCounter(0);
        StoreManager.changeStoreManagerClass(MemoryStoreWriter.class);
        new RunSample(getSampleSession()).run();

        // assertEquals(3, MemoryStoreWriter.countFlow());
        assertEquals(1, MemoryStoreWriter.countFlows());
        ExecutionFlowPO tFlow = MemoryStoreWriter.getFlow(0);
        checkRun(tFlow);

        // Now check the save and load
        InsertionDao tDao = new InsertionHibernateDAO(getSession());
        assertEquals(1, MemoryStoreWriter.countFlows());
        tDao.insertFullExecutionFlow(tFlow);
        assertEquals(1, MemoryStoreWriter.countFlows());

        closeAndRestartSession();
        ConsoleDao tConsoleDao = new ConsoleDao(getSession());
        // Now check that no more Flow were captured
        assertEquals(1, MemoryStoreWriter.countFlows());

        ExecutionFlowPO tNewFlow = tConsoleDao.readExecutionFlow(tFlow.getId());
        // Now we should captured the readExecutionFlow
        assertEquals(2, MemoryStoreWriter.countFlows());
        checkReadFlow(MemoryStoreWriter.getFlow(1));

        assertNotSame(tFlow, tNewFlow);
        checkRun(tNewFlow);
        assertEquals(MemoryStoreWriter.class.getName(), ConfigurationHelper.getInstance().getString(
            ConfigurationHelper.STORE_CLASS));
    }

    private void checkReadFlow(ExecutionFlowPO pFlow)
    {
        MethodCallPO tMeth = pFlow.getFirstMethodCall();
        assertNotNull(tMeth);
        assertEquals("java.sql.PreparedStatement", tMeth.getClassName());
        assertEquals("executeQuery", tMeth.getMethodName());
    }

    /**
     * @todo check if we need 3 or 1 on the next test.
     * 
     */
    private void checkRun(ExecutionFlowPO pFlow)
    {
        assertEquals("org.jmonitoring.sample.main.RunSample", pFlow.getFirstMethodCall().getClassName());
        assertEquals("run", pFlow.getFirstMethodCall().getMethodName());
        assertEquals(10, pFlow.getFirstMethodCall().getChildren().size());

        MethodCallPO tCurMeth = pFlow.getFirstMethodCall().getChild(0);
        assertEquals("org.jmonitoring.sample.main.Inventory", tCurMeth.getClassName());
        assertEquals("addItem", tCurMeth.getMethodName());

        tCurMeth = pFlow.getFirstMethodCall().getChild(1);
        assertEquals("org.jmonitoring.sample.main.Inventory", tCurMeth.getClassName());
        assertEquals("addItem", tCurMeth.getMethodName());

        tCurMeth = pFlow.getFirstMethodCall().getChild(2);
        assertEquals("org.jmonitoring.sample.main.Inventory", tCurMeth.getClassName());
        assertEquals("addItem", tCurMeth.getMethodName());

        tCurMeth = pFlow.getFirstMethodCall().getChild(3);
        assertEquals("org.jmonitoring.sample.main.Inventory", tCurMeth.getClassName());
        assertEquals("removeItem", tCurMeth.getMethodName());

        tCurMeth = pFlow.getFirstMethodCall().getChild(4);
        assertEquals("org.jmonitoring.sample.main.ShoppingCartPO", tCurMeth.getClassName());
        assertEquals("addItem", tCurMeth.getMethodName());

        tCurMeth = pFlow.getFirstMethodCall().getChild(5);
        assertEquals("org.jmonitoring.sample.main.Inventory", tCurMeth.getClassName());
        assertEquals("removeItem", tCurMeth.getMethodName());

        tCurMeth = pFlow.getFirstMethodCall().getChild(6);
        assertEquals("org.jmonitoring.sample.main.ShoppingCartPO", tCurMeth.getClassName());
        assertEquals("addItem", tCurMeth.getMethodName());

        tCurMeth = pFlow.getFirstMethodCall().getChild(7);
        assertEquals("org.jmonitoring.sample.persistence.SampleDao", tCurMeth.getClassName());
        assertEquals("save", tCurMeth.getMethodName());
        assertEquals(4, tCurMeth.getChildren().size());

        checkSqlMethodCall(pFlow);

        tCurMeth = pFlow.getFirstMethodCall().getChild(8);
        assertEquals("org.jmonitoring.sample.main.Inventory", tCurMeth.getClassName());
        assertEquals("removeItem", tCurMeth.getMethodName());
        // Remove Item3
        assertEquals("[Item: -1]", tCurMeth.getParams());
        assertEquals(0, tCurMeth.getChildren().size());

        tCurMeth = pFlow.getFirstMethodCall().getChild(9);
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
        assertEquals("org.hsqldb.jdbc.jdbcConnection", tCurMeth.getReturnValue().substring(0, 30));
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
