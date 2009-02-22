package org.jmonitoring.sample.main;

import org.jmonitoring.agent.store.impl.MemoryWriter;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.jmonitoring.sample.SampleTestcase;
import org.junit.Test;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class RunSampleTest extends SampleTestcase
{
    @Test
    public void testAllAspectAreAppliedIncludingThoseOfHibernateWithExtensionsInMemory()
    {
        MemoryWriter.clear();
        ShoppingCartPO.setCounter(0);
        assertEquals(0, MemoryWriter.countFlows());
        new RunSample().run();

        // assertEquals(3, MemoryStoreWriter.countFlow());
        assertEquals(1, MemoryWriter.countFlows());
        ExecutionFlowPO tFlow = MemoryWriter.getFlow(0);
        checkRun(tFlow);
    }

    private void checkRun(ExecutionFlowPO pFlow)
    {
        assertEquals("org.jmonitoring.sample.main.RunSample", pFlow.getFirstMethodCall().getClassName());
        assertEquals("run", pFlow.getFirstMethodCall().getMethodName());
        assertEquals(12, pFlow.getFirstMethodCall().getChildren().size());

        MethodCallPO tCurMeth = pFlow.getFirstMethodCall().getChild(0);
        assertEquals("org.jmonitoring.sample.main.RunSample", tCurMeth.getClassName());
        assertEquals("checkDataBase", tCurMeth.getMethodName());
        assertEquals(2, tCurMeth.getChildren().size());
        assertEquals("java.sql.PreparedStatement", tCurMeth.getChild(0).getClassName());
        assertEquals("executeQuery", tCurMeth.getChild(0).getMethodName());
        assertEquals(0, tCurMeth.getChild(0).getChildren().size());
        assertEquals("java.sql.ResultSet", tCurMeth.getChild(1).getClassName());
        assertEquals("close", tCurMeth.getChild(1).getMethodName());
        assertEquals(0, tCurMeth.getChild(1).getChildren().size());

        tCurMeth = pFlow.getFirstMethodCall().getChild(1);
        assertEquals("org.jmonitoring.sample.main.Inventory", tCurMeth.getClassName());
        assertEquals("addItem", tCurMeth.getMethodName());
        assertEquals(0, tCurMeth.getChildren().size());

        tCurMeth = pFlow.getFirstMethodCall().getChild(2);
        assertEquals("org.jmonitoring.sample.main.Inventory", tCurMeth.getClassName());
        assertEquals("addItem", tCurMeth.getMethodName());
        assertEquals(0, tCurMeth.getChildren().size());

        tCurMeth = pFlow.getFirstMethodCall().getChild(3);
        assertEquals("org.jmonitoring.sample.main.Inventory", tCurMeth.getClassName());
        assertEquals("addItem", tCurMeth.getMethodName());
        assertEquals(0, tCurMeth.getChildren().size());

        tCurMeth = pFlow.getFirstMethodCall().getChild(4);
        assertEquals("org.jmonitoring.sample.main.Inventory", tCurMeth.getClassName());
        assertEquals("removeItem", tCurMeth.getMethodName());
        assertEquals(0, tCurMeth.getChildren().size());

        tCurMeth = pFlow.getFirstMethodCall().getChild(5);
        assertEquals("org.jmonitoring.sample.main.ShoppingCartPO", tCurMeth.getClassName());
        assertEquals("addItem", tCurMeth.getMethodName());
        assertEquals(0, tCurMeth.getChildren().size());

        tCurMeth = pFlow.getFirstMethodCall().getChild(6);
        assertEquals("org.jmonitoring.sample.main.Inventory", tCurMeth.getClassName());
        assertEquals("removeItem", tCurMeth.getMethodName());
        assertEquals(0, tCurMeth.getChildren().size());

        tCurMeth = pFlow.getFirstMethodCall().getChild(7);
        assertEquals("org.jmonitoring.sample.main.ShoppingCartPO", tCurMeth.getClassName());
        assertEquals("addItem", tCurMeth.getMethodName());
        assertEquals(0, tCurMeth.getChildren().size());

        tCurMeth = pFlow.getFirstMethodCall().getChild(8);
        assertEquals("org.jmonitoring.sample.persistence.SampleDao", tCurMeth.getClassName());
        assertEquals("save", tCurMeth.getMethodName());
        assertEquals(8, tCurMeth.getChildren().size());

        checkSqlMethodCall(tCurMeth);

        tCurMeth = pFlow.getFirstMethodCall().getChild(9);
        assertEquals("java.sql.Statement", tCurMeth.getClassName());
        assertEquals("executeBatch", tCurMeth.getMethodName());
        assertEquals(0, tCurMeth.getChildren().size());

        tCurMeth = pFlow.getFirstMethodCall().getChild(10);
        assertEquals("org.jmonitoring.sample.main.Inventory", tCurMeth.getClassName());
        assertEquals("removeItem", tCurMeth.getMethodName());
        // Remove Item3
        assertEquals("[Item: -1]", tCurMeth.getParams());
        assertEquals(0, tCurMeth.getChildren().size());

        tCurMeth = pFlow.getFirstMethodCall().getChild(11);
        assertEquals("org.jmonitoring.sample.main.ShoppingCartPO", tCurMeth.getClassName());
        assertEquals("addItem", tCurMeth.getMethodName());
        // Add Item3
        assertEquals("[Item: -1]", tCurMeth.getParams());
        assertEquals(0, tCurMeth.getChildren().size());
    }

    private void checkSqlMethodCall(MethodCallPO pMeth)
    {
        MethodCallPO tCurMeth;
        MethodCallPO tCurParent = pMeth.getChild(0);
        // Start of the Sql Request
        StringBuilder tTrace = new StringBuilder();
        tCurMeth = pMeth.getChild(0);
        assertEquals("java.sql.PreparedStatement", tCurMeth.getClassName());
        assertEquals("executeUpdate", tCurMeth.getMethodName());
        tTrace.append("PrepareStatement with Sql=[insert into SHOPPING_CART (ID) values (null)]\n");
        tTrace.append("Execute update\nResult=[1]\n");
        assertEquals(tTrace.toString(), tCurMeth.getReturnValue());
        assertEquals(0, tCurMeth.getChildren().size());

        tCurMeth = pMeth.getChild(1);
        assertEquals("java.sql.ResultSet", tCurMeth.getClassName());
        assertEquals("close", tCurMeth.getMethodName());
        tTrace = new StringBuilder();
        tTrace.append("Statistics of resultSet :\nInserted=[0]\nUpdated=[0]\nDelete=[0]\nPrevious=[0]\nNext=[1]");
        assertEquals(tTrace.toString(), tCurMeth.getReturnValue());
        assertEquals(0, tCurMeth.getChildren().size());

        tCurMeth = pMeth.getChild(2);
        assertEquals("java.sql.PreparedStatement", tCurMeth.getClassName());
        assertEquals("executeUpdate", tCurMeth.getMethodName());
        tTrace = new StringBuilder();
        tTrace.append("PrepareStatement with Sql=[insert into ITEM (ID, PRICE) values (null, ?)]\n");
        tTrace.append("Add Float parameter, pos=[1], value=[30.0]\n");
        tTrace.append("Execute update\nResult=[1]\n");
        assertEquals(tTrace.toString(), tCurMeth.getReturnValue());
        assertEquals(0, tCurMeth.getChildren().size());

        tCurMeth = pMeth.getChild(3);
        assertEquals("java.sql.ResultSet", tCurMeth.getClassName());
        assertEquals("close", tCurMeth.getMethodName());
        tTrace = new StringBuilder();
        tTrace.append("Statistics of resultSet :\nInserted=[0]\nUpdated=[0]\nDelete=[0]\nPrevious=[0]\nNext=[1]");
        assertEquals(tTrace.toString(), tCurMeth.getReturnValue());
        assertEquals(0, tCurMeth.getChildren().size());

        tCurMeth = pMeth.getChild(4);
        assertEquals("java.sql.PreparedStatement", tCurMeth.getClassName());
        assertEquals("executeUpdate", tCurMeth.getMethodName());
        tTrace = new StringBuilder();
        tTrace.append("PrepareStatement with Sql=[insert into ITEM (ID, PRICE) values (null, ?)]\n");
        tTrace.append("Add Float parameter, pos=[1], value=[31.0]\n");
        tTrace.append("Execute update\nResult=[1]\n");
        assertEquals(tTrace.toString(), tCurMeth.getReturnValue());
        assertEquals(0, tCurMeth.getChildren().size());

        tCurMeth = pMeth.getChild(5);
        assertEquals("java.sql.ResultSet", tCurMeth.getClassName());
        assertEquals("close", tCurMeth.getMethodName());
        tTrace = new StringBuilder();
        tTrace.append("Statistics of resultSet :\nInserted=[0]\nUpdated=[0]\nDelete=[0]\nPrevious=[0]\nNext=[1]");
        assertEquals(tTrace.toString(), tCurMeth.getReturnValue());
        assertEquals(0, tCurMeth.getChildren().size());

        tCurMeth = pMeth.getChild(6);
        assertEquals("java.sql.Statement", tCurMeth.getClassName());
        assertEquals("execute", tCurMeth.getMethodName());
        assertEquals("Sql=[Select count(*) from SHOPPING_CART]\nResult=[true]\n", tCurMeth.getReturnValue());
        assertEquals(0, tCurMeth.getChildren().size());

        tCurMeth = pMeth.getChild(7);
        assertEquals("java.sql.Statement", tCurMeth.getClassName());
        assertEquals("execute", tCurMeth.getMethodName());
        assertEquals("Sql=[Select * from SHOPPING_CART]\nResult=[true]\n", tCurMeth.getReturnValue());
        assertEquals(0, tCurMeth.getChildren().size());

    }

}
