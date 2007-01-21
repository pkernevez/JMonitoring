package org.jmonitoring.core.store.impl;

import junit.framework.TestCase;

import org.jmonitoring.core.configuration.Configuration;
import org.jmonitoring.core.persistence.ExecutionFlowPO;
import org.jmonitoring.core.store.IStoreWriter;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestStoreFactory extends TestCase
{

    public void testDefaultStoreFactoryOk()
    {
        IStoreWriter tWriter = StoreFactory.getWriter();
        assertNotNull(tWriter);
        assertEquals("org.jmonitoring.core.store.impl.AsynchroneJdbcLogger", tWriter.getClass().getName());
        IStoreWriter tWriterBis = StoreFactory.getWriter();
        assertNotNull(tWriterBis);
        assertEquals("org.jmonitoring.core.store.impl.AsynchroneJdbcLogger", tWriterBis.getClass().getName());
        assertNotSame(tWriter, tWriterBis);
    }

    public void testDefaultStoreFactoryBadConstructor()
    {
        Configuration tConfiguration = Configuration.getInstance();
        tConfiguration.setMeasurePointStoreClass(BadStoreClassWithoutConstructor.class);
        try
        {
            StoreFactory.getWriter();
            fail("Should not had a writer");
        } catch (Exception e)
        {
            assertEquals("org.jmonitoring.core.common.MeasureException", e.getClass().getName());
            assertEquals(
                "Unable to find constructor without parameter for class [class org.jmonitoring.core.store.impl.TestStoreFactory$BadStoreClassWithoutConstructor]",
                e.getMessage());
        }
    }

    public void testDefaultStoreFactoryWithPrivateConstructor()
    {
        Configuration tConfiguration = Configuration.getInstance();
        tConfiguration.setMeasurePointStoreClass(BadStoreClassWithPrivateConstructor.class);
        try
        {
            StoreFactory.getWriter();
            fail("Should not had a writer");
        } catch (Exception e)
        {
            assertEquals("org.jmonitoring.core.common.MeasureException", e.getClass().getName());
            assertEquals(
                "Unable to find constructor without parameter for class [class org.jmonitoring.core.store.impl.TestStoreFactory$BadStoreClassWithPrivateConstructor]",
                e.getMessage());
        }
    }

    public void testDefaultStoreFactoryBadInterface()
    {
        Configuration tConfiguration = Configuration.getInstance();
        tConfiguration.setMeasurePointStoreClass(BadStoreClassNotWriter.class);
        try
        {
            StoreFactory.getWriter();
            fail("Should not had a writer");
        } catch (Exception e)
        {
            assertEquals("org.jmonitoring.core.common.MeasureException", e.getClass().getName());
            assertEquals("The writer : [class org.jmonitoring.core.store.impl.TestStoreFactory$BadStoreClassNotWriter]"
                + " is not an instance of IStoreWriter", e.getMessage());
        }
    }

    public static class BadStoreClassWithoutConstructor implements IStoreWriter
    {
        public BadStoreClassWithoutConstructor(String pString)
        {
        }

        public void writeExecutionFlow(ExecutionFlowPO pExecutionFlow)
        { // Nothing to do, it will never be called
        }
    }

    public static class BadStoreClassWithPrivateConstructor implements IStoreWriter
    {
        private BadStoreClassWithPrivateConstructor()
        {
        }

        public void writeExecutionFlow(ExecutionFlowPO pExecutionFlow)
        { // Nothing to do, it will never be called
        }
    }

    public static class BadStoreClassNotWriter
    {
        public BadStoreClassNotWriter()
        {
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        StoreFactory.clear();
    }
}
