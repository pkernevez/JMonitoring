package org.jmonitoring.sample.testtreetracer;

import org.jmonitoring.core.aspects.PerformanceAspect;
import org.jmonitoring.core.info.impl.TreeParameterTracer;
import org.jmonitoring.core.info.impl.TreeResultTracer;

public aspect TreeTracerAspect extends PerformanceAspect
{
    public pointcut executionToLog() : execution(* org.jmonitoring.sample.testtreetracer.ToBeCall.*(..));

    public TreeTracerAspect()
    {
        mParamTracer = new TreeParameterTracer();
        mResultTracer = new TreeResultTracer();
    }
}
