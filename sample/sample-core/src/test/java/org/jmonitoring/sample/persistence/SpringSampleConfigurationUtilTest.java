package org.jmonitoring.sample.persistence;

import org.jmonitoring.sample.SamplePersistenceTestcase;
import org.junit.Test;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class SpringSampleConfigurationUtilTest extends SamplePersistenceTestcase
{

    @Test
    public void testGetBean()
    {
        assertNotNull(SpringSampleConfigurationUtil.getBean("sessionFactory"));
    }

}
