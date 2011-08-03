package org.jmonitoring.agent.info.impl;

import junit.framework.TestCase;

import org.jmonitoring.core.info.IResultTracer;

public class ToStringResultTracerTest extends TestCase {

    public void testConvertToString() {
        IResultTracer tTracer = new ToStringResultTracer();
        assertEquals("4", tTracer.convertToString(null, new Integer(4)));
        assertEquals("è-è-è-", tTracer.convertToString(null, "è-è-è-"));
        assertEquals("", tTracer.convertToString(null, null));
    }

    public void testStringLimitation() {
        ToStringResultTracer tTracer = new ToStringResultTracer(2);
        assertEquals(4, "è-è-".length());
        assertEquals("è-", tTracer.convertToString(null, "è-è-"));
        }
}