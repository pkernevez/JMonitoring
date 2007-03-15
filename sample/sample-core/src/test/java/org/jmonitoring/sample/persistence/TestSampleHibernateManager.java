package org.jmonitoring.sample.persistence;

import org.jmonitoring.sample.persistence.SampleHibernateManager;

import junit.framework.TestCase;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

public class TestSampleHibernateManager extends TestCase
{

    public void testGetSession()
    {
        assertNotNull(SampleHibernateManager.getSession());
    }

}
