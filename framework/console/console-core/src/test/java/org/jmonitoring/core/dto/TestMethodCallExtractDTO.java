package org.jmonitoring.core.dto;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import junit.framework.TestCase;

import org.junit.Test;

/**
 * @author pke
 * 
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 * Templates
 */
public class TestMethodCallExtractDTO extends TestCase
{
    @Test
    public void testGetMethodName()
    {
        MethodCallExtractDTO tExtract = new MethodCallExtractDTO("org.kernevez.Test", "getTest", "Grp1", 3);
        assertEquals("getTest", tExtract.getMethodName());
        assertEquals("org.kernevez.Test.getTest", tExtract.getName());
        assertEquals("Grp1", tExtract.getGroupName());
        assertEquals(3, tExtract.getOccurenceNumber());
    }

    @Test
    public void testGetFullName()
    {
        assertEquals("net.jmonitoring.Test.getMeth",
                     MethodCallExtractDTO.getFullName("net.jmonitoring.Test", "getMeth"));
        assertEquals("net.jmonitoring.Test.getMeth", MethodCallExtractDTO.getFullName("net.jmonitoring.Test",
                                                                                      ".getMeth"));
        assertEquals("net.jmonitoring.Test.getMeth", MethodCallExtractDTO.getFullName("net.jmonitoring.Test.",
                                                                                      ".getMeth"));
    }

    @Test
    public void testGetGroupName()
    {
        assertEquals("Ceci est un group", MethodCallExtractDTO.getGroupName("Ceci est un group"));
        assertEquals("Ceciestungroup", MethodCallExtractDTO.getGroupName("Ceci.est.un..group"));
    }

}
