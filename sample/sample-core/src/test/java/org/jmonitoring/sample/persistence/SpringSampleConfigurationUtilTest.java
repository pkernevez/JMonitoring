package org.jmonitoring.sample.persistence;

import org.jmonitoring.sample.SampleTestcase;
import org.junit.Test;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class SpringSampleConfigurationUtilTest extends SampleTestcase
{

    @Test
    public void testGetBean()
    {
        assertNotNull(SpringSampleConfigurationUtil.getBean("sessionFactory"));
    }

}
