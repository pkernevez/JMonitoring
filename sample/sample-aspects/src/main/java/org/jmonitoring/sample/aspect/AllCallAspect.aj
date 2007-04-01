package org.jmonitoring.sample.aspect;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import org.jmonitoring.agent.aspect.PerformanceAspect;
import org.jmonitoring.agent.info.impl.ToStringParametersTracer;
import org.jmonitoring.agent.info.impl.ToStringResultTracer;

/**
 * @author pke
 * 
 * @todo To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 *       Templates
 */
public aspect AllCallAspect extends PerformanceAspect
{
    public pointcut executionToLog() : ( call( * *.*(..)) );
                    

    public AllCallAspect()
    {
        mGroupName = "AllCalls";
        mParamTracer = new ToStringParametersTracer();
        mResultTracer = new ToStringResultTracer();
    }

}
