package org.jmonitoring.sample.testtraceparameter;

import org.jmonitoring.agent.aspect.PerformanceAspect;

public aspect SampleAspectWithTraceParameter extends PerformanceAspect
{
    public pointcut executionToLog() : execution(* org.jmonitoring.sample.testtraceparameter.ClassToBeCall.toBeCallWithParameter(..));

    public SampleAspectWithTraceParameter()
    {
        mResultTracer = null;
        mThowableTracer = null;
     }
}
