package org.jmonitoring.core.info.impl;

import junit.framework.TestCase;

import org.jmonitoring.core.info.IThrowableTracer;

public class TestDefaultExceptionTracer extends TestCase
{

    public void testConvertToString()
    {
        IThrowableTracer tTracer = new DefaultExceptionTracer();
        Exception tException = new Exception("kjkjkjkj");
        assertEquals("kjkjkjkj", tTracer.convertToString(tException));
        assertEquals("", tTracer.convertToString(null));
    }

}
