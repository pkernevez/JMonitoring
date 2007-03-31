package org.jmonitoring.sample.testtreetracer;

import org.jmonitoring.agent.aspect.PerformanceAspect;
import org.jmonitoring.agent.info.impl.TreeParameterTracer;
import org.jmonitoring.agent.info.impl.TreeResultTracer;

public aspect TreeTracerAspect extends PerformanceAspect
{
    public pointcut executionToLog() : execution(* org.jmonitoring.sample.testtreetracer.ToBeCall.*(..));

    public TreeTracerAspect()
    {
        mParamTracer = new TreeParameterTracer();
        mResultTracer = new TreeResultTracer();
    }
}
