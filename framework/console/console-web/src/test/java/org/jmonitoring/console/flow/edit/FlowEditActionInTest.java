package org.jmonitoring.console.flow.edit;

import java.util.Date;

import javax.annotation.Resource;

import org.jmonitoring.console.JMonitoringMockStrustTestCase;
import org.jmonitoring.console.flow.FlowBuilderUtil;
import org.jmonitoring.console.flow.jfreechart.FlowUtil;
import org.jmonitoring.core.configuration.FormaterBean;
import org.jmonitoring.core.dto.ExecutionFlowDTO;
import org.jmonitoring.core.dto.MethodCallDTO;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * @author pke
 * 
 */
public class FlowEditActionInTest extends JMonitoringMockStrustTestCase
{

    @Resource(name = "formater")
    private FormaterBean mFormater;

    @Autowired
    FlowBuilderUtil mUtil;

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        FlowEditActionIn.setMaxFlowToShow(5);
    }

    @Before
    public void init()
    {
        // MockServletContext sandbox = new MockServletContext("");
        // sandbox
        // .addInitParameter(ContextLoader.CONFIG_LOCATION_PARAM,
        // "classpath*:/console.xml;classpath*:/core-test.xml;classpath*:/default-test.xml;classpath*:/persistence-test.xml");
        // // Create a context loader and load the WebApplicationContext into the sandbox
        // ServletContextListener contextListener = new ContextLoaderListener();
        // ServletContextEvent event = new ServletContextEvent(sandbox);
        // contextListener.contextInitialized(event);
        //
        // // Fetch the WebApplicationContext from the sandbox and move it to the StrutsTestCase ServletContext
        // getSession().getServletContext()
        // .setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,
        // // sandbox.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE));
        //
        // Session session = mSessionFactory.openSession();
        // TransactionSynchronizationManager.bindResource(mSessionFactory, new SessionHolder(session));
        // session.beginTransaction();
    }

    @After
    public void close()
    {
        // Session session = mSessionFactory.getCurrentSession();
        // TransactionSynchronizationManager.unbindResource(mSessionFactory);
        // session.getTransaction().rollback();
    }

    @Test
    @Ignore
    public void testActionWithSmallExecutionFlow()
    {
        ExecutionFlowDTO tFirstDto = mUtil.buildAndSaveNewDto(FlowEditActionIn.getMaxFlowToShow() - 1);
        mUtil.buildAndSaveNewDto(FlowEditActionIn.getMaxFlowToShow() - 1);

        assertNull(getSession().getAttribute(FlowUtil.DURATION_IN_GROUP));
        assertNull(getSession().getAttribute(FlowUtil.NB_CALL_TO_GROUP));

        assertEquals(2, mUtil.countFlows());
        clear();

        FlowEditForm tForm = new FlowEditForm();
        tForm.setId(tFirstDto.getId());
        // mSessionFactory.getCurrentSession().getTransaction().commit();
        // mSessionFactory.getCurrentSession().beginTransaction();

        // mSessionFactory.getCurrentSession().close();
        //
        // TransactionSynchronizationManager.unbindResource(mSessionFactory);
        // TransactionSynchronizationManager.bindResource(mSessionFactory,
        // new SessionHolder(mSessionFactory.openSession()));

        // assertEquals(2, mUtil.countFlows());
        // mSessionFactory.getCurrentSession().close();

        // Delete First Flow
        setRequestPathInfo("/FlowEditIn");
        setActionForm(tForm);
        actionPerform();
        verifyForwardPath("/pages/layout/layout.jsp");

        verifyTilesForward("success", "floweditinternal");
        assertNotNull(getSession().getAttribute(FlowUtil.DURATION_IN_GROUP));
        assertNotNull(getSession().getAttribute(FlowUtil.NB_CALL_TO_GROUP));

        assertEquals(("<MAP NAME=\"ChartBar\">\r\n"
            + "<AREA SHAPE=\"RECT\" COORDS=\"121,79,140,118\" href=\"MethodCallEditIn.do?flowId=1&position=1\">\r\n"
            + "<AREA SHAPE=\"RECT\" COORDS=\"216,79,235,118\" href=\"MethodCallEditIn.do?flowId=1&position=1\">\r\n"
            + "<AREA SHAPE=\"RECT\" COORDS=\"311,79,330,118\" href=\"MethodCallEditIn.do?flowId=1&position=1\">\r\n"
            + "<AREA SHAPE=\"RECT\" COORDS=\"407,79,426,118\" href=\"MethodCallEditIn.do?flowId=1&position=1\">\r\n"
            + "<AREA SHAPE=\"RECT\" COORDS=\"502,79,883,118\" href=\"MethodCallEditIn.do?flowId=1&position=1\">\r\n"
            + "<AREA SHAPE=\"RECT\" COORDS=\"140,141,216,180\" href=\"MethodCallEditIn.do?flowId=1&position=2\">\r\n"
            + "<AREA SHAPE=\"RECT\" COORDS=\"235,141,311,180\" href=\"MethodCallEditIn.do?flowId=1&position=3\">\r\n"
            + "<AREA SHAPE=\"RECT\" COORDS=\"330,141,406,180\" href=\"MethodCallEditIn.do?flowId=1&position=4\">\r\n"
            + "<AREA SHAPE=\"RECT\" COORDS=\"426,141,502,180\" href=\"MethodCallEditIn.do?flowId=1&position=5\">\r\n"
            + "</MAP>\r\n"), tForm.getImageMap());
    }

    @Test
    public void testActionWithLongExecutionFlow()
    {
        ExecutionFlowDTO tFirstDto = mUtil.buildAndSaveNewDto(FlowEditActionIn.getMaxFlowToShow() + 1);
        mUtil.buildAndSaveNewDto(FlowEditActionIn.getMaxFlowToShow() + 1);

        assertEquals(2, mUtil.countFlows());
        clear();

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

    @Test
    public void testActionWithLongExecutionFlowAndGraphOnly()
    {
        ExecutionFlowDTO tFirstDto = mUtil.buildAndSaveNewDto(FlowEditActionIn.getMaxFlowToShow() + 1);
        mUtil.buildAndSaveNewDto(FlowEditActionIn.getMaxFlowToShow() + 1);

        assertEquals(2, mUtil.countFlows());
        clear();

        FlowEditForm tForm = new FlowEditForm();
        tForm.setKindOfAction(FlowEditForm.ACTION_ONLY_GRAPH);
        tForm.setId(tFirstDto.getId());

        // Delete First Flow
        setRequestPathInfo("/FlowEditIn");
        setActionForm(tForm);
        // Temp
        // mSessionFactory.getCurrentSession().getTransaction().commit();
        // mSessionFactory.getCurrentSession().beginTransaction();
        actionPerform();
        verifyForwardPath("/pages/layout/layout.jsp");

        verifyTilesForward("success", "floweditinternal");

        assertNotNull(getSession().getAttribute(FlowUtil.DURATION_IN_GROUP));
        assertNotNull(getSession().getAttribute(FlowUtil.NB_CALL_TO_GROUP));

    }

    @Test
    public void testActionWithLongExecutionFlowAndForce()
    {
        ExecutionFlowDTO tFirstDto = mUtil.buildAndSaveNewDto(FlowEditActionIn.getMaxFlowToShow() + 1);
        mUtil.buildAndSaveNewDto(FlowEditActionIn.getMaxFlowToShow() + 1);

        assertEquals(2, mUtil.countFlows());
        clear();

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

    public MethodCallDTO buildNewFullMeasure()
    {
        MethodCallDTO tPoint, curPoint;
        long tCurrentTime = System.currentTimeMillis();
        tPoint = new MethodCallDTO();
        tPoint.setParent(null);
        tPoint.setClassName(FlowEditActionInTest.class.getName());
        tPoint.setMethodName("builNewFullFlow");
        tPoint.setGroupName("GrDefault");
        tPoint.setParams("[]");
        tPoint.setBeginTime(mFormater.formatDateTime(new Date(tCurrentTime)), tCurrentTime);
        long tEndTime = tCurrentTime + 2 + 2 + 1;
        tPoint.setEndTime(mFormater.formatDateTime(new Date(tEndTime)), tEndTime); // Duration=5
        MethodCallDTO[] tChildren1 = new MethodCallDTO[2];
        MethodCallDTO[] tChildren2 = new MethodCallDTO[1];

        // This local variable is indirectly used by its parent
        curPoint = new MethodCallDTO();
        curPoint.setParent(tPoint);
        tChildren1[0] = curPoint;
        curPoint.setClassName(FlowEditActionInTest.class.getName());
        curPoint.setMethodName("builNewFullFlow2");
        curPoint.setGroupName("GrChild1");
        curPoint.setParams("[]");
        curPoint.setBeginTime(mFormater.formatDateTime(new Date(tCurrentTime)), tCurrentTime);
        curPoint.setEndTime(mFormater.formatDateTime(new Date(tCurrentTime + 2)), tCurrentTime + 2); // Duration=2

        // This local variable is indireclty used by its parent
        curPoint = new MethodCallDTO();
        curPoint.setParent(tPoint);
        tChildren1[1] = curPoint;
        curPoint.setClassName(FlowEditActionInTest.class.getName());
        curPoint.setMethodName("builNewFullFlow2");
        curPoint.setGroupName("GrChild2");
        curPoint.setParams("[]");
        curPoint.setBeginTime(mFormater.formatDateTime(new Date(tCurrentTime)), tCurrentTime);
        curPoint.setEndTime(mFormater.formatDateTime(new Date(tCurrentTime + 2 + 1)), tCurrentTime + 2 + 1); // Duration=3
        curPoint.setChildren(tChildren2);

        // This local variable is indirectly used by its parent
        MethodCallDTO tOldPoint = curPoint;
        curPoint = new MethodCallDTO();
        curPoint.setParent(tOldPoint);
        tChildren2[0] = curPoint;
        curPoint.setClassName(FlowEditActionInTest.class.getName());
        curPoint.setMethodName("builNewFullFlow3");
        curPoint.setGroupName("GrChild2_1");
        curPoint.setParams("[]");
        curPoint.setBeginTime(mFormater.formatDateTime(new Date(tCurrentTime)), tCurrentTime);
        curPoint.setEndTime(mFormater.formatDateTime(new Date(tCurrentTime + 1)), tCurrentTime + 1); // Duration=1
        tPoint.setChildren(tChildren1);

        return tPoint;
    }

    @Test
    public void testLimitMeasureWithDurationNoLimitation()
    {
        FlowEditActionIn tAction = new FlowEditActionIn();
        MethodCallDTO tMeasure = buildNewFullMeasure();
        assertEquals(2, tMeasure.getChildren().length);
        tAction.limitMeasureWithDuration(1, tMeasure);
        assertEquals(2, tMeasure.getChildren().length);
        MethodCallDTO curMeasure = tMeasure.getChild(0); // Child1
        assertEquals(0, curMeasure.getChildren().length);
        curMeasure = tMeasure.getChild(1); // Child2
        assertEquals(1, curMeasure.getChildren().length);
        curMeasure = curMeasure.getChild(0); // Child2_2
        assertEquals(0, curMeasure.getChildren().length);
    }

    @Test
    public void testLimitMeasureWithDurationLimitation2ndLevel()
    {
        FlowEditActionIn tAction = new FlowEditActionIn();
        MethodCallDTO tMeasure = buildNewFullMeasure();
        tAction.limitMeasureWithDuration(2, tMeasure);
        assertEquals(2, tMeasure.getChildren().length);
        MethodCallDTO curMeasure = tMeasure.getChild(0); // Child1
        assertEquals(0, curMeasure.getChildren().length);
        curMeasure = tMeasure.getChild(1); // Child2
        assertEquals(0, curMeasure.getChildren().length);
    }

    @Test
    public void testLimitMeasureWithDurationLimitation2ndChild()
    {
        FlowEditActionIn tAction = new FlowEditActionIn();
        MethodCallDTO tMeasure = buildNewFullMeasure();
        tAction.limitMeasureWithDuration(2 + 1, tMeasure);
        assertEquals(1, tMeasure.getChildren().length);
        MethodCallDTO curMeasure = tMeasure.getChild(0); // Child2
        assertEquals(0, curMeasure.getChildren().length);
        assertEquals("GrChild2", curMeasure.getGroupName());
    }

    @Test
    public void testLimitMeasureWithDurationLimitationNoChild()
    {
        FlowEditActionIn tAction = new FlowEditActionIn();
        MethodCallDTO tMeasure = buildNewFullMeasure();
        tAction.limitMeasureWithDuration(2 + 2 + 2, tMeasure);
        assertEquals(0, tMeasure.getChildren().length);
    }

}
