package org.jmonitoring.sample.testruntimeclassname;

import org.jmonitoring.agent.aspect.PerformanceAspect;

public aspect SampleAspect extends PerformanceAspect
{

    public pointcut executionToLog() : execution(* org.jmonitoring.sample.testruntimeclassname.AbstractSample+.*());
}
