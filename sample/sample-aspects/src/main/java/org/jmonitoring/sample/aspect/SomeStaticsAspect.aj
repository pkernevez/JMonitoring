package org.jmonitoring.sample.aspect;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import org.jmonitoring.agent.core.PerformanceAspect;

/**
 * @author pke
 * 
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 * Templates
 */
public aspect SomeStaticsAspect extends PerformanceAspect {
    public pointcut executionToLog() : execution( static * org.jmonitoring.sample.*.*.*(..) )
        && !execution( static * *.main(String[]))
        && !execution( static * org.jmonitoring.sample.persistence.SpringSampleConfigurationUtil.getBean(String));

    public SomeStaticsAspect()
    {
        super();
        mGroupName = "SomeStatics";
        mParamTracer = null;
        mResultTracer = null;
    }

    
}
