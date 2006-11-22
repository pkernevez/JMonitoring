package org.jmonitoring.core.info.impl;

import java.util.StringTokenizer;

import junit.framework.TestCase;

public class TestStackTraceTracer extends TestCase
{

    public void testConvertToStringObjectArray()
    {
        StackTraceTracer tTracer = new StackTraceTracer();
        assertTrue(tTracer.convertToString(new Object[]{"tr",new Integer(4)}).length()>10);
    }

    public void testConvertToStringObject()
    {
        StackTraceTracer tTracer = new StackTraceTracer();
        assertTrue(tTracer.convertToString(new Integer(4)).length()>10);
    }

    public void testGetStackTrace()
    {
        StackTraceTracer tTracer = new StackTraceTracer();
        StringTokenizer tTok = new StringTokenizer(tTracer.getStackTrace(), "\n");
        assertEquals("Stacktrace for this call:", tTok.nextToken());
        assertEquals("    at org.jmonitoring.core.info.impl.StackTraceTracer.getStackTrace(StackTraceTracer.java:", tTok.nextToken().substring(0, 91));
        assertEquals("    at org.jmonitoring.core.info.impl.TestStackTraceTracer.testGetStackTrace(TestStackTraceTracer.java:", tTok.nextToken().substring(0, 103));
    }

}
