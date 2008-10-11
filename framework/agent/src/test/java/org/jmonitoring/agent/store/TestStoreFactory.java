package org.jmonitoring.agent.store;

import junit.framework.TestCase;

import org.jmonitoring.agent.store.impl.MemoryStoreWriter;
import org.jmonitoring.agent.store.impl.XmlFileLogger;
import org.jmonitoring.core.configuration.ConfigurationHelper;
import org.jmonitoring.core.domain.ExecutionFlowPO;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestStoreFactory extends TestCase {

    public void testDefaultStoreFactoryOk() {
        StoreManager.changeStoreManagerClass(XmlFileLogger.class);
        IStoreWriter tWriter = StoreFactory.getWriter();
        assertNotNull(tWriter);
        assertEquals(XmlFileLogger.class.getName(), tWriter.getClass().getName());
        IStoreWriter tWriterBis = StoreFactory.getWriter();
        assertNotNull(tWriterBis);
        assertEquals(XmlFileLogger.class.getName(), tWriterBis.getClass().getName());
        assertNotSame(tWriter, tWriterBis);
    }

    public void testDefaultStoreFactoryBadConstructor() {
        ConfigurationHelper.setProperty(
                ConfigurationHelper.STORE_CLASS,
                BadStoreClassWithoutConstructor.class.getName());
        try {
            StoreFactory.getWriter();
            fail("Should not had a writer");
        } catch (Exception e) {
            assertEquals("org.jmonitoring.core.configuration.MeasureException", e.getClass().getName());
            assertEquals(
                    "Unable to find constructor without parameter for class [class org.jmonitoring.agent.store.TestStoreFactory$BadStoreClassWithoutConstructor]",
                    e.getMessage());
        }
    }

    public void testDefaultStoreFactoryWithPrivateConstructor() {
        ConfigurationHelper.setProperty(
                ConfigurationHelper.STORE_CLASS,
                BadStoreClassWithPrivateConstructor.class.getName());
        try {
            StoreFactory.getWriter();
            fail("Should not had a writer");
        } catch (Exception e) {
            assertEquals("org.jmonitoring.core.configuration.MeasureException", e.getClass().getName());
            assertEquals(
                    "Unable to find constructor without parameter for class [class org.jmonitoring.agent.store.TestStoreFactory$BadStoreClassWithPrivateConstructor]",
                    e.getMessage());
        }
    }

    public void testDefaultStoreFactoryBadInterface() {
        ConfigurationHelper.setProperty(ConfigurationHelper.STORE_CLASS, BadStoreClassNotWriter.class.getName());
        try {
            StoreFactory.getWriter();
            fail("Should not had a writer");
        } catch (Exception e) {
            assertEquals("org.jmonitoring.core.configuration.MeasureException", e.getClass().getName());
            assertEquals("The writer : [class org.jmonitoring.agent.store.TestStoreFactory$BadStoreClassNotWriter]"
                    + " is not an instance of IStoreWriter", e.getMessage());
        }
    }

    public static class BadStoreClassWithoutConstructor implements IStoreWriter {
        public BadStoreClassWithoutConstructor(String pString) {
        }

        public void writeExecutionFlow(ExecutionFlowPO pExecutionFlow) { // Nothing to do, it will never be called
        }
    }

    public static class BadStoreClassWithPrivateConstructor implements IStoreWriter {
        private BadStoreClassWithPrivateConstructor() {
        }

        public void writeExecutionFlow(ExecutionFlowPO pExecutionFlow) { // Nothing to do, it will never be called
        }
    }

    public static class BadStoreClassNotWriter {
        public BadStoreClassNotWriter() {
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        StoreFactory.clear();
    }

    public void testClear() {
        ConfigurationHelper.setProperty(ConfigurationHelper.STORE_CLASS, TestStoreManager.MyWriter.class.getName());
        StoreFactory.clear();
        IStoreWriter tWriter = StoreFactory.getWriter();
        ConfigurationHelper.setProperty(ConfigurationHelper.STORE_CLASS, MemoryStoreWriter.class.getName());
        StoreFactory.clear();
        assertNotSame(tWriter, StoreFactory.getWriter());
        assertEquals(MemoryStoreWriter.class.getName(), StoreFactory.getWriter().getClass().getName());

    }
}
