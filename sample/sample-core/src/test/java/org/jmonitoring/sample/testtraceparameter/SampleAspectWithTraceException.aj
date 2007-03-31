package org.jmonitoring.sample.testtraceparameter;

import org.jmonitoring.agent.aspect.PerformanceAspect;

public aspect SampleAspectWithTraceException extends PerformanceAspect
{
    public pointcut executionToLog() : execution(* org.jmonitoring.sample.testtraceparameter.ClassToBeCall.toBeCallWithException(..));

    public SampleAspectWithTraceException()
    {
        mParamTracer = null;
        mResultTracer = null;
     }
}