package org.jmonitoring.console.flow;

import java.text.ParseException;

import javax.annotation.Resource;

import org.jmonitoring.console.JMonitoringMockStrustTestCase;
import org.jmonitoring.core.configuration.FormaterBean;
import org.jmonitoring.core.dao.FlowSearchCriterion;
import org.jmonitoring.core.dto.ExecutionFlowDTO;
import org.springframework.test.context.ContextConfiguration;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * @author pke
 * 
 */
@ContextConfiguration(locations = {"/formater-test.xml" })
public class TestFlowSearchAction extends JMonitoringMockStrustTestCase
{
    @Resource(name = "formater")
    private FormaterBean mFormater;

    /**
     * Test class.
     * 
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
        tForm.setJVM("my server n�2");
        tForm.setThreadName("MainThread");

        FlowSearchCriterion tCrit = FlowSearchActionOut.copyBeanFormToCriterion(mFormater, tForm);
        assertEquals("27/09/05", mFormater.formatDateTime(tCrit.getBeginDate()));
        // Format Date only ??
        assertEquals("11:30:34", mFormater.formatDateTime(tCrit.getBeginTimeMin()));
        assertEquals(new Long(2 + 1), tCrit.getDurationMin());
        assertEquals(tForm.getFirstMeasureClassName(), tCrit.getClassName());
        assertEquals(tForm.getFirstMeasureGroupName(), tCrit.getGroupName());
        assertEquals(tForm.getFirstMeasureMethodName(), tCrit.getMethodName());
        assertEquals(tForm.getJVM(), tCrit.getJVM());
        assertEquals(tForm.getThreadName(), tCrit.getThreadName());

        tForm.setBeginDate("");
        tForm.setBeginTimeMin("");
        tCrit = FlowSearchActionOut.copyBeanFormToCriterion(mFormater, tForm);
        assertNull(tCrit.getBeginDate());
        assertNull(tCrit.getBeginDate());

        tForm.setBeginDate(null);
        tForm.setBeginTimeMin(null);
        tCrit = FlowSearchActionOut.copyBeanFormToCriterion(mFormater, tForm);
        assertNull(tCrit.getBeginDate());
        assertNull(tCrit.getBeginDate());

    }

    public void testActionSearchOutWithCriteria()
    {
        FlowBuilderUtil tUtil = new FlowBuilderUtil();
        tUtil.createSchema();
        tUtil.buildAndSaveNewDto(3);

        assertEquals(1, tUtil.countFlows());
        clear();

        FlowSearchForm tForm = new FlowSearchForm();
        tForm.setDurationMin("5");

        assertNull(tForm.getListOfFlows());

        setRequestPathInfo("/FlowSearchOut");
        setActionForm(tForm);
        actionPerform();
        verifyForwardPath("/pages/layout/layout.jsp");

        verifyTilesForward("success", "flowsearch");

        assertNotNull(tForm.getListOfFlows());
        assertEquals(1, tForm.getListOfFlows().size());
        ExecutionFlowDTO tFlow = tForm.getListOfFlows().get(0);

        // On ne ram�ne pas les grappes d'objets, mais seulement les ExecutionFlowDto
        assertNull(tFlow.getFirstMethodCall());
        // assertEquals(3, tFlow.getFirstMethodCall().getChildren().size());
    }

    public void testActionSearchIn()
    {
        setRequestPathInfo("/FlowSearchIn");
        actionPerform();
        verifyForwardPath("/pages/layout/layout.jsp");

        // verifyTilesForward("success", "flowsearch");

        assertNotNull(getActionForm());
    }

}
