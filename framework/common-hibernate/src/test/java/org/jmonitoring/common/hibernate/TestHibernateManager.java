package org.jmonitoring.common.hibernate;

import org.hibernate.Session;

import junit.framework.TestCase;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestHibernateManager extends TestCase
{

    public void testGetSession()
    {
        Session tSession = HibernateManager.getSession();
        assertNotNull(tSession);
        assertSame(tSession, HibernateManager.getSession());
        
        tSession.close();
        assertFalse(tSession.isOpen());
        Session tNewSession = HibernateManager.getSession();
        assertNotSame(tSession, tNewSession);
        assertTrue(tNewSession.isOpen());
    }

}
