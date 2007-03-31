package org.jmonitoring.agent.info.impl;

import java.util.StringTokenizer;

import junit.framework.TestCase;

import org.jmonitoring.agent.info.impl.MultipleTracer;
import org.jmonitoring.agent.info.impl.StackTraceTracer;
import org.jmonitoring.agent.info.impl.ToStringParametersTracer;
import org.jmonitoring.core.info.IParamaterTracer;

public class TestMultipleTracer extends TestCase
{

    public void testConvertToString()
    {
        IParamaterTracer[] tTracers = new IParamaterTracer[] {new ToStringParametersTracer(), new StackTraceTracer() };
        MultipleTracer tTracer = new MultipleTracer(tTracers);
        StringTokenizer tTok = new StringTokenizer(tTracer.convertToString(this, new Object[] {"45", "56" }), "\n\n");
        assertEquals("[45, 56]", tTok.nextToken());
        assertEquals("Stack", tTok.nextToken().substring(0, 5));

    }

}
