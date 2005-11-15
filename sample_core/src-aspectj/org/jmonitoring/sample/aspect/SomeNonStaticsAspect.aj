package org.jmonitoring.sample.aspect;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import org.jmonitoring.core.aspects.PerformanceAspect;

/**
 * @author pke
 *
 * @todo To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public aspect SomeNonStaticsAspect extends PerformanceAspect {
    public pointcut executionToLog() : execution( !static * org.jmonitoring.sample.*.*.*(..) )
    && !execution( static * *.main(String[]))
    && !within( junit.framework.TestCase+ )
    && !execution( * *.toString() )
    && !execution( * *.getID() );

public SomeNonStaticsAspect()
{
    mGroupName = "SomeNonStatics";
}


}
