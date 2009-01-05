package org.jmonitoring.agent.store.impl;

import org.jmonitoring.agent.store.AspectLoggerEmulator;
import org.jmonitoring.core.test.JMonitoringTestCase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

@ContextConfiguration(locations = {"/memory-manager-test.xml" })
public class MemoryWriterTest extends JMonitoringTestCase
{
    private AspectLoggerEmulator mEmulator;

    @Before
    public void init()
    {
        mEmulator = new AspectLoggerEmulator(applicationContext);
        MemoryWriter.clear();
    }

    @Test
    public void testCountFlows()
    {
        assertEquals("Should not have memorised anything", 0, MemoryWriter.countFlows());
        mEmulator.simulateExecutionFlow(true);
        assertEquals("Should memorised one flow", 1, MemoryWriter.countFlows());
        mEmulator.simulateExecutionFlow(true);
        assertEquals("Should memorised two flows", 2, MemoryWriter.countFlows());

    }

    @Test
    public void testGetFlow()
    {
        try
        {
            MemoryWriter.getFlow(0);
            fail("Should not have memorised anything");
        } catch (ArrayIndexOutOfBoundsException e)
        {
            assertEquals("Should not have memorised anything", "Array index out of range: 0", e.getMessage());
        }
        mEmulator.simulateExecutionFlow(true);
        assertNotNull("Should memorised one flow", MemoryWriter.getFlow(0));
        try
        {
            assertNull("Should memorised only one flow", MemoryWriter.getFlow(1));
            fail("Should not have memorised anything");
        } catch (ArrayIndexOutOfBoundsException e)
        {
            assertEquals("Should not have memorised anything", "Array index out of range: 1", e.getMessage());
        }
    }

}
