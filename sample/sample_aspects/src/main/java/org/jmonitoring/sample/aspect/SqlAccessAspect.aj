package org.jmonitoring.sample.aspect;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import org.jmonitoring.core.aspects.PerformanceAspect;
import org.jmonitoring.core.info.impl.TreeParameterTracer;
import org.jmonitoring.core.info.impl.TreeResultTracer;

/**
 * @author pke
 * 
 * @todo To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 *       Templates
 */
public aspect SqlAccessAspect extends PerformanceAspect
{
    public pointcut executionToLog() : ( call( * *.*(..)) );
                    

    public SqlAccessAspect()
    {
        mGroupName = "SQL";
        mParamTracer = new TreeParameterTracer();
        mResultTracer = new TreeResultTracer();
    }

}
