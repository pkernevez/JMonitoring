package org.jmonitoring.sample.main;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jmonitoring.agent.store.impl.MemoryWriter;
import org.jmonitoring.core.configuration.IInsertionDao;
import org.jmonitoring.core.configuration.SpringConfigurationUtil;
import org.jmonitoring.core.dao.ConsoleDao;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.jmonitoring.sample.SamplePersistenceTestcase;
import org.junit.Test;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class RunSampleTest extends SamplePersistenceTestcase
{
    @Test
    public void testAllAspectAreAppliedIncludingThoseOfHibernateWithExtensionsInMemory()
    {
        MemoryWriter.clear();
        ShoppingCartPO.setCounter(0);
        assertEquals(0, MemoryWriter.countFlows());
        new RunSample(getSampleSession()).run();

        // assertEquals(3, MemoryStoreWriter.countFlow());
        assertEquals(1, MemoryWriter.countFlows());
        ExecutionFlowPO tFlow = MemoryWriter.getFlow(0);
        checkRun(tFlow);

        // Now check the save and load
        SessionFactory tCoreSessionFactory = (SessionFactory) SpringConfigurationUtil.getBean("sessionFactory");
        Session tCoreSession = tCoreSessionFactory.openSession();
        IInsertionDao tDao = (IInsertionDao) getBean("dao");
        assertEquals(1, MemoryWriter.countFlows());
        tDao.insertFullExecutionFlow(tFlow);
        tCoreSession.flush();
        assertEquals(1, MemoryWriter.countFlows());

        ConsoleDao tConsoleDao = (ConsoleDao) getBean("consoleDao");
        // Now check that no more Flow were captured
        assertEquals(1, MemoryWriter.countFlows());

        ExecutionFlowPO tNewFlow = tConsoleDao.readExecutionFlow(tFlow.getId());
        // Now we should captured the readExecutionFlow
        assertEquals(2, MemoryWriter.countFlows());
        checkReadFlow(MemoryWriter.getFlow(1));

        assertNotSame(tFlow, tNewFlow);
        checkRun(tNewFlow);
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
        assertEquals(11, pFlow.getFirstMethodCall().getChildren().size());

        MethodCallPO tCurMeth = pFlow.getFirstMethodCall().getChild(0);
        assertEquals("org.jmonitoring.sample.main.RunSample", tCurMeth.getClassName());
        assertEquals("checkDataBase", tCurMeth.getMethodName());

        tCurMeth = pFlow.getFirstMethodCall().getChild(1);
        assertEquals("org.jmonitoring.sample.main.Inventory", tCurMeth.getClassName());
        assertEquals("addItem", tCurMeth.getMethodName());

        tCurMeth = pFlow.getFirstMethodCall().getChild(2);
        assertEquals("org.jmonitoring.sample.main.Inventory", tCurMeth.getClassName());
        assertEquals("addItem", tCurMeth.getMethodName());

        tCurMeth = pFlow.getFirstMethodCall().getChild(3);
        assertEquals("org.jmonitoring.sample.main.Inventory", tCurMeth.getClassName());
        assertEquals("addItem", tCurMeth.getMethodName());

        tCurMeth = pFlow.getFirstMethodCall().getChild(4);
        assertEquals("org.jmonitoring.sample.main.Inventory", tCurMeth.getClassName());
        assertEquals("removeItem", tCurMeth.getMethodName());

        tCurMeth = pFlow.getFirstMethodCall().getChild(5);
        assertEquals("org.jmonitoring.sample.main.ShoppingCartPO", tCurMeth.getClassName());
        assertEquals("addItem", tCurMeth.getMethodName());

        tCurMeth = pFlow.getFirstMethodCall().getChild(6);
        assertEquals("org.jmonitoring.sample.main.Inventory", tCurMeth.getClassName());
        assertEquals("removeItem", tCurMeth.getMethodName());

        tCurMeth = pFlow.getFirstMethodCall().getChild(7);
        assertEquals("org.jmonitoring.sample.main.ShoppingCartPO", tCurMeth.getClassName());
        assertEquals("addItem", tCurMeth.getMethodName());

        tCurMeth = pFlow.getFirstMethodCall().getChild(8);
        assertEquals("org.jmonitoring.sample.persistence.SampleDao", tCurMeth.getClassName());
        assertEquals("save", tCurMeth.getMethodName());
        assertEquals(4, tCurMeth.getChildren().size());

        checkSqlMethodCall(pFlow);

        tCurMeth = pFlow.getFirstMethodCall().getChild(9);
        assertEquals("org.jmonitoring.sample.main.Inventory", tCurMeth.getClassName());
        assertEquals("removeItem", tCurMeth.getMethodName());
        // Remove Item3
        assertEquals("[Item: -1]", tCurMeth.getParams());
        assertEquals(0, tCurMeth.getChildren().size());

        tCurMeth = pFlow.getFirstMethodCall().getChild(10);
        assertEquals("org.jmonitoring.sample.main.ShoppingCartPO", tCurMeth.getClassName());
        assertEquals("addItem", tCurMeth.getMethodName());
        // Add Item3
        assertEquals("[Item: -1]", tCurMeth.getParams());
        assertEquals(0, tCurMeth.getChildren().size());
    }

    private void checkSqlMethodCall(ExecutionFlowPO tFlow)
    {
        MethodCallPO tCurMeth;
        MethodCallPO tCurParent = tFlow.getFirstMethodCall().getChild(8).getChild(0);
        assertEquals(6, tCurParent.getChildren().size());
        // Start of the Sql Request
        StringBuilder tTrace = new StringBuilder();
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
        tTrace = new StringBuilder();
        tTrace.append("PrepareStatement with Sql=[call identity()]\n");
        tTrace.append("Execute query\n");
        assertEquals(tTrace.toString(), tCurMeth.getReturnValue());
        assertEquals(0, tCurMeth.getChildren().size());

        tCurMeth = tCurParent.getChild(2);
        assertEquals("java.sql.PreparedStatement", tCurMeth.getClassName());
        assertEquals("executeUpdate", tCurMeth.getMethodName());
        tTrace = new StringBuilder();
        tTrace.append("PrepareStatement with Sql=[insert into ITEM (PRICE, ID) values (?, null)]\n");
        tTrace.append("Add Float parameter, pos=[1], value=[30.0]\n");
        tTrace.append("Execute update\n");
        assertEquals(tTrace.toString(), tCurMeth.getReturnValue());
        assertEquals(0, tCurMeth.getChildren().size());

        tCurMeth = tCurParent.getChild(3);
        assertEquals("java.sql.PreparedStatement", tCurMeth.getClassName());
        assertEquals("executeQuery", tCurMeth.getMethodName());
        tTrace = new StringBuilder();
        tTrace.append("PrepareStatement with Sql=[call identity()]\n");
        tTrace.append("Execute query\n");
        assertEquals(tTrace.toString(), tCurMeth.getReturnValue());
        assertEquals(0, tCurMeth.getChildren().size());

        tCurMeth = tCurParent.getChild(4);
        assertEquals("java.sql.PreparedStatement", tCurMeth.getClassName());
        assertEquals("executeUpdate", tCurMeth.getMethodName());
        tTrace = new StringBuilder();
        tTrace.append("PrepareStatement with Sql=[insert into ITEM (PRICE, ID) values (?, null)]\n");
        tTrace.append("Add Float parameter, pos=[1], value=[31.0]\n");
        tTrace.append("Execute update\n");
        assertEquals(tTrace.toString(), tCurMeth.getReturnValue());
        assertEquals(0, tCurMeth.getChildren().size());

        tCurMeth = tCurParent.getChild(5);
        assertEquals("java.sql.PreparedStatement", tCurMeth.getClassName());
        assertEquals("executeQuery", tCurMeth.getMethodName());
        tTrace = new StringBuilder();
        tTrace.append("PrepareStatement with Sql=[call identity()]\n");
        tTrace.append("Execute query\n");
        assertEquals(tTrace.toString(), tCurMeth.getReturnValue());
        assertEquals(0, tCurMeth.getChildren().size());

        tCurParent = tFlow.getFirstMethodCall().getChild(8);
        assertEquals(4, tCurParent.getChildren().size());
        tCurMeth = tCurParent.getChild(1);
        assertEquals("org.hibernate.Session", tCurMeth.getClassName());
        assertEquals("connection", tCurMeth.getMethodName());
        assertEquals("org.hsqldb.jdbc.jdbcConnection", tCurMeth.getReturnValue().substring(0, 30));
        assertEquals(0, tCurMeth.getChildren().size());

        tCurMeth = tCurParent.getChild(2);
        assertEquals("java.sql.Statement", tCurMeth.getClassName());
        assertEquals("execute", tCurMeth.getMethodName());
        assertEquals("Sql=[Select count(*) from SHOPPING_CART]\n", tCurMeth.getReturnValue());
        assertEquals(0, tCurMeth.getChildren().size());

        tCurMeth = tCurParent.getChild(3);
        assertEquals("java.sql.Statement", tCurMeth.getClassName());
        assertEquals("execute", tCurMeth.getMethodName());
        assertEquals("Sql=[Select * from SHOPPING_CART]\n", tCurMeth.getReturnValue());
        assertEquals(0, tCurMeth.getChildren().size());

    }

}
