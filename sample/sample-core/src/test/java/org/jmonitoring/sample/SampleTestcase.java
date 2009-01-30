package org.jmonitoring.sample;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jmonitoring.agent.store.impl.MemoryWriter;
import org.jmonitoring.sample.persistence.SpringSampleConfigurationUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

@RunWith(JUnit4ClassRunner.class)
@Ignore
public class SampleTestcase extends TestCase
{
    protected Session mSession;

    @Before
    public void initContext()
    {
        SessionFactory tFact = (SessionFactory) SpringSampleConfigurationUtil.getBean("sessionFactory");
        mSession = tFact.openSession();
        mSession.beginTransaction();
        TransactionSynchronizationManager.bindResource(tFact, new SessionHolder(mSession));
        MemoryWriter.clear();
    }

    @After
    public void clearContext()
    {
        SessionFactory tFact = (SessionFactory) SpringSampleConfigurationUtil.getBean("sessionFactory");
        TransactionSynchronizationManager.unbindResource(tFact);
        mSession.getTransaction().rollback();
        mSession.close();
    }

    protected Object getBean(String pName)
    {
        return SpringSampleConfigurationUtil.getBean(pName);
    }

    protected Session getSampleSession()
    {
        return mSession;
    }

}
