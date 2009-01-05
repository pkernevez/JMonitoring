package org.jmonitoring.core.configuration;

import java.util.Date;

import javax.annotation.Resource;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/formater-test.xml" })
public class FormaterBeanTest extends TestCase
{
    @Resource(name = "formater")
    private FormaterBean mFormater;

    @Test
    public void testFormatDateTime()
    {
        mFormater.setDateFormat("dd/MM/yy");
        mFormater.setTimeFormat("HH:mm:ss");
        assertEquals("26/04/70 18:46:40", mFormater.formatDateTime(10000000000L));
        assertEquals("26/04/70 18:46:40", mFormater.formatDateTime(new Date(10000000000L)));
        mFormater.setDateFormat("yy/MM/dd");
        mFormater.setTimeFormat("HH:mm");
        assertEquals("70/04/26 18:46", mFormater.formatDateTime(10000000000L));
        assertEquals("70/04/26 18:46", mFormater.formatDateTime(new Date(10000000000L)));
    }

    @Test
    public void testParseDateTime()
    {
        mFormater.setDateFormat("dd/MM/yy");
        mFormater.setTimeFormat("HH:mm:ss");
        assertEquals(10000000000L, mFormater.parseDateTime("26/04/70 18:46:40").getTime());
        mFormater.setDateFormat("yy/MM/dd");
        mFormater.setTimeFormat("HH:mm");
        assertEquals(9999960000L, mFormater.parseDateTime("70/04/26 18:46").getTime());
    }

}
