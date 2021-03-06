package org.jmonitoring.agent.info.impl;

import junit.framework.TestCase;

import org.jmonitoring.core.info.IParamaterTracer;

public class ToStringParametersTracerTest extends TestCase
{
    public void testConvertToString()
    {
        Object[] tParam = new Object[] {new Integer(3), "34333", null };
        IParamaterTracer tTracer = new ToStringParametersTracer();
        assertEquals("[3, 34333, null]", tTracer.convertToString(this, tParam).toString());
    }

    public void testStringLimitation()
    {
        Object[] tParam = new Object[] {new Integer(3), "34333", null };
        IParamaterTracer tTracer = new ToStringParametersTracer(2);
        assertEquals("[3", tTracer.convertToString(this, tParam).toString());
    }

}
