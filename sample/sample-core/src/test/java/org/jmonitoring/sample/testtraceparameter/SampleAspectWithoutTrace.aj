package org.jmonitoring.sample.testtraceparameter;

import org.jmonitoring.core.aspects.PerformanceAspect;

public aspect SampleAspectWithoutTrace extends PerformanceAspect
{
    public pointcut executionToLog() : execution(* org.jmonitoring.sample.testtraceparameter.ClassToBeCall.toBeCallWithoutTrace(..));

    public SampleAspectWithoutTrace()
    {
        super();
        mParamTracer = null;
        mResultTracer = null;
        mThowableTracer = null;
    }

}
