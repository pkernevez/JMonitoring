package org.jmonitoring.console.gwt.server.executionflow;

import junit.framework.TestCase;

import org.jmonitoring.core.configuration.SpringConfigurationUtil;
import org.junit.Test;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class ExecutionFlowServiceImplTest extends TestCase
{

    @Test
    public void testBefore()
    {
        assertNotNull(SpringConfigurationUtil.getBean("sessionFactory"));
    }
}
