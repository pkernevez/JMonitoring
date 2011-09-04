package org.jmonitoring.core.configuration;

import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

import javax.annotation.Resource;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/formater-test.xml" })
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class})   
public class FormaterBeanTest extends TestCase
{
    @Resource(name = "formater")
    private FormaterBean mFormater;

    /**
     * Get the jet lag between the local of the test author and the runner.
     * 
     * @return
     * @throws ParseException
     */
    private long getElapseTime() throws ParseException
    {
        TimeZone tAuthorTimeZone = TimeZone.getTimeZone("Europe/Zurich");
        TimeZone tDefaultTimeZone = TimeZone.getDefault();
        return tAuthorTimeZone.getOffset(10000000000L) - tDefaultTimeZone.getOffset(10000000000L);
    }

    @Test
    public void testFormatDateTime() throws ParseException
    {
        mFormater.setDateFormat("dd/MM/yy");
        mFormater.setTimeFormat("HH:mm:ss");
        assertEquals(TimeZone.getDefault().toString(), "26/04/70 18:46:40",
                     mFormater.formatDateTime(10000000000L + getElapseTime()));
        assertEquals(TimeZone.getDefault().toString(), "26/04/70 18:46:40",
                     mFormater.formatDateTime(new Date(10000000000L + getElapseTime())));
        mFormater.setDateFormat("yy/MM/dd");
        mFormater.setTimeFormat("HH:mm");
        assertEquals("70/04/26 18:46", mFormater.formatDateTime(10000000000L + getElapseTime()));
        assertEquals("70/04/26 18:46", mFormater.formatDateTime(new Date(10000000000L + getElapseTime())));
    }

    @Test
    public void testParseDateTime() throws ParseException
    {
        mFormater.setDateFormat("dd/MM/yy");
        mFormater.setTimeFormat("HH:mm:ss");
        assertEquals(10000000000L + getElapseTime(), mFormater.parseDateTime("26/04/70 18:46:40").getTime());
        mFormater.setDateFormat("yy/MM/dd");
        mFormater.setTimeFormat("HH:mm");
        assertEquals(9999960000L + getElapseTime(), mFormater.parseDateTime("70/04/26 18:46").getTime());
    }

}
