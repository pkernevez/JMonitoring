package org.jmonitoring.core.dao;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import junit.framework.TestCase;

/**
 * @author pke
 * 
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 * Templates
 */
public class TestMeasureExtract extends TestCase
{
    public void testGetMethodName()
    {
        MethodCallExtract tExtract = new MethodCallExtract("org.kernevez.Test.getTest", "Grp1", new Integer(3));
        assertEquals("getTest", tExtract.getMethodName());
        assertEquals("org.kernevez.Test.getTest", tExtract.getName());
        assertEquals("Grp1", tExtract.getGroupName());
        assertEquals(3, tExtract.getOccurenceNumber());
    }

}
