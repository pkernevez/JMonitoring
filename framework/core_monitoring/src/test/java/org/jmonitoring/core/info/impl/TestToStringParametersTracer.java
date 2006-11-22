package org.jmonitoring.core.info.impl;

import org.jmonitoring.core.info.IParamaterTracer;

import junit.framework.TestCase;

public class TestToStringParametersTracer extends TestCase
{
    public void testConvertToString()
    {
        Object[] tParam = new Object[] {new Integer(3), "34333", null};
        IParamaterTracer tTracer = new ToStringParametersTracer();
        assertEquals("[3, 34333, null]", tTracer.convertToString(tParam));
    }
    
    public void testStringLimitation()
    {
        Object[] tParam = new Object[] {new Integer(3), "34333", null};
        IParamaterTracer tTracer = new ToStringParametersTracer(2);
        assertEquals("[3", tTracer.convertToString(tParam));
    }
}
