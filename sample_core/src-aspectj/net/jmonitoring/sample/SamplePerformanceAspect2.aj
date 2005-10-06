package net.kernevez.sample;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import net.kernevez.performance.aspects.PerformanceAspect;

/**
 * @author pke
 *
 * @todo To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public aspect SamplePerformanceAspect2 extends PerformanceAspect {
    public pointcut executionToLog() : execution( !static * net.kernevez.sample.*.*(..) )
    && !execution( static * *.main(String[]))
    && !within( junit.framework.TestCase+ )
    && !execution( * *.toString() )
    && !execution( * *.getID() );

public SamplePerformanceAspect2()
{
    mGroupName = "AllExecut";
}


}
