package org.jmonitoring.core.test;

import junit.framework.TestCase;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners( {DependencyInjectionTestExecutionListener.class })
@ContextConfiguration(locations = {"/default-test.xml" })
@Ignore
public class JMonitoringTestCase extends TestCase implements ApplicationContextAware
{

    protected StaticApplicationContext applicationContext;

    public StaticApplicationContext getApplicationContext()
    {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext pApplicationContext)
    {
        applicationContext = new StaticApplicationContext(pApplicationContext);
    }

}
