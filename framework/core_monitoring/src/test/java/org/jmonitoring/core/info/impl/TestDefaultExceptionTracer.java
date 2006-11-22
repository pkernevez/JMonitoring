package org.jmonitoring.core.info.impl;

import org.jmonitoring.core.info.IThrowableTracer;

import junit.framework.TestCase;

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
