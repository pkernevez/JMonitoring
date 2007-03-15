package org.jmonitoring.sample.testtraceparameter;

import org.jmonitoring.core.aspects.PerformanceAspect;

public aspect SampleAspectWithTraceResult extends PerformanceAspect
{
    public pointcut executionToLog() : execution(* org.jmonitoring.sample.testtraceparameter.ClassToBeCall.toBeCallWithResult(..));

    public SampleAspectWithTraceResult()
    {
        mParamTracer = null;
        mThowableTracer = null;
     }
}
