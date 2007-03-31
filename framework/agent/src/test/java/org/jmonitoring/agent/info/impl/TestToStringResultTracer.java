package org.jmonitoring.agent.info.impl;

import junit.framework.TestCase;

import org.jmonitoring.agent.info.impl.ToStringResultTracer;
import org.jmonitoring.core.info.IResultTracer;

public class TestToStringResultTracer extends TestCase
{

    public void testConvertToString()
    {
        IResultTracer tTracer = new ToStringResultTracer();
        assertEquals("4", tTracer.convertToString(null, new Integer(4)));
        assertEquals("�-�-�-", tTracer.convertToString(null, "�-�-�-"));
        assertEquals("", tTracer.convertToString(null, null));
    }

    public void testStringLimitation()
    {
        ToStringResultTracer tTracer = new ToStringResultTracer(2);
        assertEquals("�-", tTracer.convertToString(null, "�-�-"));
    }
}
