package org.jmonitoring.core.configuration;

import junit.framework.TestCase;

import org.junit.Test;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class SpringConfigurationUtilTest extends TestCase
{

    @Test
    public void testGetBeanGlobal()
    {
        ForTestBean tBeanGA = getBean("beanGA");
        ForTestBean tBeanGB = getBean("beanGB");
        assertEquals("GA", tBeanGA.getText());
        assertEquals("GB", tBeanGB.getText());
        assertEquals(tBeanGA, tBeanGB.getRelation());
        assertEquals(tBeanGB, tBeanGA.getRelation());
    }

    @Test
    public void testGetBeanGlobalOtherThread() throws InterruptedException
    {
        ForTestBean tBeanGA = getBeanInAnotherThread("beanGA");
        ForTestBean tBeanGB = getBeanInAnotherThread("beanGB");
        assertEquals("GA", tBeanGA.getText());
        assertEquals("GB", tBeanGB.getText());
        assertEquals(tBeanGA, tBeanGB.getRelation());
        assertEquals(tBeanGB, tBeanGA.getRelation());

        assertSame(tBeanGA, getBean("beanGA"));
        assertSame(tBeanGB, getBean("beanGB"));
    }

    @Test
    public void testGetBeanThread()
    {
        ForTestBean tBeanGA = getBean("beanGA");
        // TestBean tBeanGB = getBean("beanGB");
        ForTestBean tBeanTA = getBean("beanTA");
        ForTestBean tBeanTB = getBean("beanTB");
        assertEquals("TA", tBeanTA.getText());
        assertEquals("TB", tBeanTB.getText());
        assertSame(tBeanTB, tBeanTA.getRelation());
        assertSame(tBeanGA, tBeanTB.getRelation());
        assertSame(tBeanTA, getBean("beanTA"));
        assertSame(tBeanTB, getBean("beanTB"));
    }

    @Test
    public void testGetBeanThreadInAnotherThread() throws InterruptedException
    {
        ForTestBean tBeanGA = getBean("beanGA");
        ForTestBean tBeanGABis = getBeanInAnotherThread("beanGA");
        ForTestBean tBeanTA = getBean("beanTA");
        ForTestBean tBeanTB = getBean("beanTB");
        ForTestBean tBeanTABis = getBeanInAnotherThread("beanTA");
        ForTestBean tBeanTBBis = getBeanInAnotherThread("beanTB");
        assertEquals("TA", tBeanTA.getText());
        assertEquals("TB", tBeanTB.getText());
        assertSame(tBeanTB, tBeanTA.getRelation());
        assertSame(tBeanGA, tBeanTB.getRelation());
        assertEquals("TB", tBeanTABis.getRelation().getText());
        assertSame(tBeanGA, tBeanTBBis.getRelation());

        assertSame(tBeanGABis, tBeanGA);
        assertSame(tBeanTA, tBeanTABis);
        assertSame(tBeanTB, tBeanTABis.getRelation());

    }

    @Test
    public void testIsTestMode()
    {
        assertFalse(SpringConfigurationUtil.isTestMode());
        SpringConfigurationUtil.setContext(null);
        assertTrue(SpringConfigurationUtil.isTestMode());
    }

    private ForTestBean getBean(String pName)
    {
        return (ForTestBean) SpringConfigurationUtil.getBean(pName);
    }

    private ForTestBean getBeanInAnotherThread(String pName) throws InterruptedException
    {
        GetBeanInOtherThread tWorker = new GetBeanInOtherThread(pName);
        new Thread(tWorker).start();
        // Can't use wait(XXX) with JUnit
        Thread.sleep(300);
        return tWorker.getFoundBean();
    }

    private static class GetBeanInOtherThread implements Runnable
    {

        private ForTestBean mFoundBean;

        private final String mName;

        public GetBeanInOtherThread(String pName)
        {
            super();
            mName = pName;
        }

        public void run()
        {
            setFoundBean((ForTestBean) SpringConfigurationUtil.getBean(mName));
        }

        /**
         * @return the foundBean
         */
        public synchronized ForTestBean getFoundBean()
        {
            return mFoundBean;
        }

        /**
         * @param pFoundBean the foundBean to set
         */
        public synchronized void setFoundBean(ForTestBean pFoundBean)
        {
            mFoundBean = pFoundBean;
        }

    }
}
