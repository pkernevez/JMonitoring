package org.jmonitoring.sample.testruntimeclassname;

import org.jmonitoring.core.aspects.PerformanceAspect;

public aspect SampleAspect extends PerformanceAspect
{

    public pointcut executionToLog() : execution(* org.jmonitoring.sample.testruntimeclassname.AbstractSample+.*());
}
