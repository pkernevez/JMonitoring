package org.jmonitoring.core.configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    /**
     * Get the jet lag between the local of the test author and the runner.
     * 
     * @return
     * @throws ParseException
     */
    private long getElapseTime() throws ParseException
    {
        Locale tAuthorLocal = new Locale("fr", "CH");
        Locale tDefaultLocal = Locale.getDefault();
        SimpleDateFormat tAuthorFormatter = new SimpleDateFormat("26/04/70 18:46:40", tAuthorLocal);
        SimpleDateFormat tDefaultFormatter = new SimpleDateFormat("26/04/70 18:46:40", tDefaultLocal);
        long tResult =
            tDefaultFormatter.parse("26/04/70 18:46:40").getTime()
                - tAuthorFormatter.parse("26/04/70 18:46:40").getTime();
        return tResult;
    }

    @Test
    public void testElapseTime() throws ParseException
    {
        assertEquals(0, getElapseTime());
    }

    @Test
    public void testFormatDateTime() throws ParseException
    {
        mFormater.setDateFormat("dd/MM/yy");
        mFormater.setTimeFormat("HH:mm:ss");
        assertEquals(Locale.getDefault().toString(), "26/04/70 18:46:40",
                     mFormater.formatDateTime(10000000000L + getElapseTime()));
        assertEquals(Locale.getDefault().toString(), "26/04/70 18:46:40",
                     mFormater.formatDateTime(new Date(10000000000L + getElapseTime())));
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
