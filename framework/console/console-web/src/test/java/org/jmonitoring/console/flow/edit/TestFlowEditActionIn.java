package org.jmonitoring.console.flow.edit;

import java.net.URL;
import java.util.Date;
import java.util.Enumeration;

import org.jmonitoring.console.JMonitoringMockStrustTestCase;
import org.jmonitoring.console.flow.FlowBuilderUtil;
import org.jmonitoring.console.flow.jfreechart.FlowUtil;
import org.jmonitoring.core.dto.ExecutionFlowDTO;
import org.jmonitoring.core.dto.MethodCallDTO;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * @author pke
 * 
 */
public class TestFlowEditActionIn extends JMonitoringMockStrustTestCase {

    /*
     * (non-Javadoc)
     * 
     * @see servletunit.struts.MockStrutsTestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        FlowEditActionIn.setMaxFlowToShow(5);
    }

    public void testActionWithSmallExecutionFlow() {
        FlowBuilderUtil tUtil = new FlowBuilderUtil();
        tUtil.createSchema();
        ExecutionFlowDTO tFirstDto = tUtil.buildAndSaveNewDto(FlowEditActionIn.getMaxFlowToShow() - 1);
        tUtil.buildAndSaveNewDto(FlowEditActionIn.getMaxFlowToShow() - 1);

        assertNull(getSession().getAttribute(FlowUtil.DURATION_IN_GROUP));
        assertNull(getSession().getAttribute(FlowUtil.NB_CALL_TO_GROUP));

        assertEquals(2, tUtil.countFlows());
        tUtil.clear();

        FlowEditForm tForm = new FlowEditForm();
        tForm.setId(tFirstDto.getId());

        // Delete First Flow
        setRequestPathInfo("/FlowEditIn");
        setActionForm(tForm);
        actionPerform();
        verifyForwardPath("/pages/layout/layout.jsp");

        verifyTilesForward("success", "floweditinternal");
        assertNotNull(getSession().getAttribute(FlowUtil.DURATION_IN_GROUP));
        assertNotNull(getSession().getAttribute(FlowUtil.NB_CALL_TO_GROUP));

        assertEquals(
                ("<MAP NAME=\"ChartBar\">\r\n"
                        + "<AREA SHAPE=\"RECT\" COORDS=\"121,79,140,118\" href=\"MethodCallEditIn.do?flowId=1&position=1\">\r\n"
                        + "<AREA SHAPE=\"RECT\" COORDS=\"216,79,235,118\" href=\"MethodCallEditIn.do?flowId=1&position=1\">\r\n"
                        + "<AREA SHAPE=\"RECT\" COORDS=\"311,79,330,118\" href=\"MethodCallEditIn.do?flowId=1&position=1\">\r\n"
                        + "<AREA SHAPE=\"RECT\" COORDS=\"407,79,426,118\" href=\"MethodCallEditIn.do?flowId=1&position=1\">\r\n"
                        + "<AREA SHAPE=\"RECT\" COORDS=\"502,79,883,118\" href=\"MethodCallEditIn.do?flowId=1&position=1\">\r\n"
                        + "<AREA SHAPE=\"RECT\" COORDS=\"140,141,216,180\" href=\"MethodCallEditIn.do?flowId=1&position=2\">\r\n"
                        + "<AREA SHAPE=\"RECT\" COORDS=\"235,141,311,180\" href=\"MethodCallEditIn.do?flowId=1&position=3\">\r\n"
                        + "<AREA SHAPE=\"RECT\" COORDS=\"330,141,406,180\" href=\"MethodCallEditIn.do?flowId=1&position=4\">\r\n"
                        + "<AREA SHAPE=\"RECT\" COORDS=\"426,141,502,180\" href=\"MethodCallEditIn.do?flowId=1&position=5\">\r\n"
                        + "</MAP>\r\n"),
                tForm.getImageMap());
    }

    public void testActionWithLongExecutionFlow() {
        FlowBuilderUtil tUtil = new FlowBuilderUtil();
        tUtil.createSchema();
        ExecutionFlowDTO tFirstDto = tUtil.buildAndSaveNewDto(FlowEditActionIn.getMaxFlowToShow() + 1);
        tUtil.buildAndSaveNewDto(FlowEditActionIn.getMaxFlowToShow() + 1);

        assertEquals(2, tUtil.countFlows());
        tUtil.clear();

        FlowEditForm tForm = new FlowEditForm();
        tForm.setId(tFirstDto.getId());

        // Delete First Flow
        setRequestPathInfo("/FlowEditIn");
        setActionForm(tForm);
        actionPerform();
        verifyForwardPath("/pages/layout/layout.jsp");

        verifyTilesForward("required_info", "floweditrequiredinfo");
        assertNull(getSession().getAttribute(FlowUtil.DURATION_IN_GROUP));
        assertNull(getSession().getAttribute(FlowUtil.NB_CALL_TO_GROUP));

    }

    public void testActionWithLongExecutionFlowAndGraphOnly() {
        FlowBuilderUtil tUtil = new FlowBuilderUtil();
        tUtil.createSchema();
        ExecutionFlowDTO tFirstDto = tUtil.buildAndSaveNewDto(FlowEditActionIn.getMaxFlowToShow() + 1);
        tUtil.buildAndSaveNewDto(FlowEditActionIn.getMaxFlowToShow() + 1);

        assertEquals(2, tUtil.countFlows());
        tUtil.clear();

        FlowEditForm tForm = new FlowEditForm();
        tForm.setKindOfAction(FlowEditForm.ACTION_ONLY_GRAPH);
        tForm.setId(tFirstDto.getId());

        // Delete First Flow
        setRequestPathInfo("/FlowEditIn");
        setActionForm(tForm);
        actionPerform();
        verifyForwardPath("/pages/layout/layout.jsp");

        verifyTilesForward("success", "floweditinternal");

        assertNotNull(getSession().getAttribute(FlowUtil.DURATION_IN_GROUP));
        assertNotNull(getSession().getAttribute(FlowUtil.NB_CALL_TO_GROUP));

    }

    /**
     * 
     */
    public void testActionWithLongExecutionFlowAndForce() {
        FlowBuilderUtil tUtil = new FlowBuilderUtil();
        tUtil.createSchema();
        ExecutionFlowDTO tFirstDto = tUtil.buildAndSaveNewDto(FlowEditActionIn.getMaxFlowToShow() + 1);
        tUtil.buildAndSaveNewDto(FlowEditActionIn.getMaxFlowToShow() + 1);

        assertEquals(2, tUtil.countFlows());
        tUtil.clear();

        FlowEditForm tForm = new FlowEditForm();
        tForm.setKindOfAction(FlowEditForm.ACTION_FORCE);
        tForm.setId(tFirstDto.getId());

        // Delete First Flow
        setRequestPathInfo("/FlowEditIn");
        setActionForm(tForm);
        actionPerform();
        verifyForwardPath("/pages/layout/layout.jsp");

        verifyTilesForward("success", "floweditinternal");

        assertNotNull(getSession().getAttribute(FlowUtil.DURATION_IN_GROUP));
        assertNotNull(getSession().getAttribute(FlowUtil.NB_CALL_TO_GROUP));

    }

    /**
     * @return MethodCallDTO.
     */
    public static MethodCallDTO buildNewFullMeasure() {
        MethodCallDTO tPoint, curPoint;
        long tCurrentTime = System.currentTimeMillis();
        tPoint = new MethodCallDTO();
        tPoint.setParent(null);
        tPoint.setClassName(TestFlowEditActionIn.class.getName());
        tPoint.setMethodName("builNewFullFlow");
        tPoint.setGroupName("GrDefault");
        tPoint.setParams("[]");
        tPoint.setBeginTime(new Date(tCurrentTime));
        tPoint.setEndTime(new Date(tCurrentTime + 2 + 2 + 1)); // Duration=5
        MethodCallDTO[] tChildren1 = new MethodCallDTO[2];
        MethodCallDTO[] tChildren2 = new MethodCallDTO[1];

        // This local variable is indireclty used by its parent
        curPoint = new MethodCallDTO();
        curPoint.setParent(tPoint);
        tChildren1[0] = curPoint;
        curPoint.setClassName(TestFlowEditActionIn.class.getName());
        curPoint.setMethodName("builNewFullFlow2");
        curPoint.setGroupName("GrChild1");
        curPoint.setParams("[]");
        curPoint.setBeginTime(new Date(tCurrentTime));
        curPoint.setEndTime(new Date(tCurrentTime + 2)); // Duration=2

        // This local variable is indireclty used by its parent
        curPoint = new MethodCallDTO();
        curPoint.setParent(tPoint);
        tChildren1[1] = curPoint;
        curPoint.setClassName(TestFlowEditActionIn.class.getName());
        curPoint.setMethodName("builNewFullFlow2");
        curPoint.setGroupName("GrChild2");
        curPoint.setParams("[]");
        curPoint.setBeginTime(new Date(tCurrentTime));
        curPoint.setEndTime(new Date(tCurrentTime + 2 + 1)); // Duration=3
        curPoint.setChildren(tChildren2);

        // This local variable is indireclty used by its parent
        MethodCallDTO tOldPoint = curPoint;
        curPoint = new MethodCallDTO();
        curPoint.setParent(tOldPoint);
        tChildren2[0] = curPoint;
        curPoint.setClassName(TestFlowEditActionIn.class.getName());
        curPoint.setMethodName("builNewFullFlow3");
        curPoint.setGroupName("GrChild2_1");
        curPoint.setParams("[]");
        curPoint.setBeginTime(new Date(tCurrentTime));
        curPoint.setEndTime(new Date(tCurrentTime + 1)); // Duration=1
        tPoint.setChildren(tChildren1);

        return tPoint;
    }

    public void testLimitMeasureWithDurationNoLimitation() {
        FlowEditActionIn tAction = new FlowEditActionIn();
        MethodCallDTO tMeasure = buildNewFullMeasure();
        tAction.limitMeasureWithDuration(1, tMeasure);
        assertEquals(2, tMeasure.getChildren().length);
        MethodCallDTO curMeasure = (MethodCallDTO) tMeasure.getChild(0); // Child1
        assertEquals(0, curMeasure.getChildren().length);
        curMeasure = (MethodCallDTO) tMeasure.getChild(1); // Child2
        assertEquals(1, curMeasure.getChildren().length);
        curMeasure = (MethodCallDTO) curMeasure.getChild(0); // Child2_2
        assertEquals(0, curMeasure.getChildren().length);
    }

    public void testLimitMeasureWithDurationLimitation2ndLevel() {
        FlowEditActionIn tAction = new FlowEditActionIn();
        MethodCallDTO tMeasure = buildNewFullMeasure();
        tAction.limitMeasureWithDuration(2, tMeasure);
        assertEquals(2, tMeasure.getChildren().length);
        MethodCallDTO curMeasure = (MethodCallDTO) tMeasure.getChild(0); // Child1
        assertEquals(0, curMeasure.getChildren().length);
        curMeasure = (MethodCallDTO) tMeasure.getChild(1); // Child2
        assertEquals(0, curMeasure.getChildren().length);
    }

    public void testLimitMeasureWithDurationLimitation2ndChild() {
        FlowEditActionIn tAction = new FlowEditActionIn();
        MethodCallDTO tMeasure = buildNewFullMeasure();
        tAction.limitMeasureWithDuration(2 + 1, tMeasure);
        assertEquals(1, tMeasure.getChildren().length);
        MethodCallDTO curMeasure = (MethodCallDTO) tMeasure.getChild(0); // Child2
        assertEquals(0, curMeasure.getChildren().length);
        assertEquals("GrChild2", curMeasure.getGroupName());
    }

    public void testLimitMeasureWithDurationLimitationNoChild() {
        FlowEditActionIn tAction = new FlowEditActionIn();
        MethodCallDTO tMeasure = buildNewFullMeasure();
        tAction.limitMeasureWithDuration(2 + 2 + 2, tMeasure);
        assertEquals(0, tMeasure.getChildren().length);
    }

}
