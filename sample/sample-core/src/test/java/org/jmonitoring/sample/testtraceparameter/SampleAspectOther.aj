package org.jmonitoring.sample.testtraceparameter;

import org.jmonitoring.agent.info.impl.ToStringParametersTracer;
import org.jmonitoring.agent.aspect.PerformanceAspect;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public aspect SampleAspectOther extends PerformanceAspect
{
    public pointcut executionToLog() : execution(* org.jmonitoring.sample.testtraceparameter.ClassToBeCall.toBeCallOther(..));

    public SampleAspectOther()
    {
        super();
        mParamTracer = new ToStringParametersTracer();
        mResultTracer = null;
        mThowableTracer = null;
        mGroupName = "Other";
    }


}
