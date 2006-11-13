package org.jmonitoring.sample.test;

import org.jmonitoring.core.aspects.PerformanceAspect;

public aspect SampleAspect extends PerformanceAspect
{

    public pointcut executionToLog() : execution(* org.jmonitoring.sample.test.AbstractSample+.*());
}
