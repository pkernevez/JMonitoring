package org.jmonitoring.sample.aspects;

import org.jmonitoring.agent.aspect.PerformanceAspect;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import org.jmonitoring.agent.aspect.PerformanceAspect;

/**
 * @author pke
 * 
 * @todo To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 *       Templates
 */
public aspect SomeNonStaticsAspect extends PerformanceAspect
{
    public pointcut executionToLog() : execution( !static * org.jmonitoring.sample.*.*.*(..) )
    && !execution( static * *.main(String[]))
    && !within( junit.framework.TestCase+ )
    && !execution( * *.toString() )
    && !execution( * org.jmonitoring.sample.main.ShoppingCartPO.get*() )
    && !execution( * org.jmonitoring.sample.main.ShoppingCartPO.set*(..) )
    && !execution( * org.jmonitoring.sample.main.ItemPO.get*() )
    && !execution( * org.jmonitoring.sample.main.ItemPO.set*(..) )
    && !within( org.jmonitoring.sample.testtraceparameter.ClassToBeCall );

    public SomeNonStaticsAspect()
    {
        mGroupName = "SomeNonStatics";
    }
}
