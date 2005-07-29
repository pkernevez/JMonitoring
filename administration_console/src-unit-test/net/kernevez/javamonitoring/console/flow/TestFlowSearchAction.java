package net.kernevez.javamonitoring.console.flow;

import java.text.ParseException;

import net.kernevez.performance.configuration.Configuration;
import net.kernevez.performance.dao.FlowSearchCriterion;
import junit.framework.TestCase;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * @author pke
 *  
 */
public class TestFlowSearchAction extends TestCase
{
    /**
     * Test class.
     * @throws ParseException Exception.
     */
    public void testCopyBeanFormToCriterion() throws ParseException
    {
        FlowSearchForm tForm = new FlowSearchForm();
        tForm.setBeginDate("27/09/05");
        tForm.setBeginTimeMin("11:30:34");
        tForm.setDurationMin("3");
        tForm.setFirstMeasureClassName("net.test.Class");
        tForm.setFirstMeasureGroupName("myGroup");
        tForm.setFirstMeasureMethodName("myTest");
        tForm.setJVM("my server n°2");
        tForm.setThreadName("MainThread");

        FlowSearchCriterion tCrit = FlowSearchAction.copyBeanFormToCriterion(tForm);
        assertEquals("27/09/05", Configuration.getInstance().getDateFormater().format(tCrit.getBeginDate()));
        assertEquals("11:30:34", Configuration.getInstance().getTimeFormater().format(tCrit.getBeginTimeMin()));
        assertEquals(new Long(2 + 1), tCrit.getDurationMin());
        assertEquals(tForm.getFirstMeasureClassName(), tCrit.getFirstMeasureClassName());
        assertEquals(tForm.getFirstMeasureGroupName(), tCrit.getFirstMeasureGroupName());
        assertEquals(tForm.getFirstMeasureMethodName(), tCrit.getFirstMeasureMethodName());
        assertEquals(tForm.getJVM(), tCrit.getJVM());
        assertEquals(tForm.getThreadName(), tCrit.getThreadName());

        tForm.setBeginDate("");
        tForm.setBeginTimeMin("");
        tCrit = FlowSearchAction.copyBeanFormToCriterion(tForm);
        assertNull(tCrit.getBeginDate());
        assertNull(tCrit.getBeginTimeMin());

        tForm.setBeginDate(null);
        tForm.setBeginTimeMin(null);
        tCrit = FlowSearchAction.copyBeanFormToCriterion(tForm);
        assertNull(tCrit.getBeginDate());
        assertNull(tCrit.getBeginTimeMin());

    }
}
