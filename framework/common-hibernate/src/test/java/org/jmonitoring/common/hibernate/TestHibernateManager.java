package org.jmonitoring.common.hibernate;

import junit.framework.TestCase;

import org.hibernate.Session;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestHibernateManager extends TestCase {

    public void testGetSession() {
        HibernateManager.getSessionFactory("test.hibernate.xml", "test.hibernate.properties");
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
