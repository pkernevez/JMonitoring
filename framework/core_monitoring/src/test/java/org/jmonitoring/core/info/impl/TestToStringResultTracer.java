package org.jmonitoring.core.info.impl;

import junit.framework.TestCase;

import org.jmonitoring.core.info.IResultTracer;

public class TestToStringResultTracer extends TestCase
{

    public void testConvertToString()
    {
        IResultTracer tTracer = new ToStringResultTracer();
        assertEquals("4", tTracer.convertToString(new Integer(4)));
        assertEquals("è-è-è-", tTracer.convertToString("è-è-è-"));
        assertEquals("", tTracer.convertToString(null));
    }

    public void testStringLimitation()
    {
        ToStringResultTracer tTracer = new ToStringResultTracer(2);
        assertEquals("è-", tTracer.convertToString("è-è-"));
    }
}
