package org.jmonitoring.sample.testtraceparameter;

import org.jmonitoring.core.aspects.PerformanceAspect;

public aspect SampleAspectWithTraceParameterAndResult extends PerformanceAspect
{
    public pointcut executionToLog() : execution(* org.jmonitoring.sample.testtraceparameter.ClassToBeCall.toBeCallWithParameterAndResult(..));

    public SampleAspectWithTraceParameterAndResult()
    {
        mThowableTracer = null;
     }
}