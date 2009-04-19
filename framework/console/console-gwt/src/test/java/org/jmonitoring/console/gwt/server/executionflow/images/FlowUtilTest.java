package org.jmonitoring.console.gwt.server.executionflow.images;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import junit.framework.TestCase;

import org.jmonitoring.core.configuration.ColorManager;
import org.jmonitoring.core.configuration.FormaterBean;
import org.jmonitoring.core.domain.MethodCallPO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners( {DependencyInjectionTestExecutionListener.class })
@ContextConfiguration(locations = {"/formater-test.xml", "/color-test.xml" })
@Service
public class FlowUtilTest extends TestCase implements ApplicationContextAware
{
    @Resource(name = "formater")
    private FormaterBean mFormater;

    @Resource(name = "color")
    private ColorManager mColor;

    public static final long START_TIME = 1149282668046L;

    MethodCallPO getSampleMeasurePoint()
    {
        MethodCallPO tPoint;
        // Fri Jun 02 23:11:08 CEST 2006
        Date tRefDate = new Date(START_TIME);
        tPoint = new MethodCallPO();
        tPoint.setClassName(FlowUtilTest.class.getName());
        tPoint.setMethodName("builNewFullFlow");
        tPoint.setGroupName("GrDefault");
        tPoint.setParams("[]");
        tPoint.setBeginTime(tRefDate.getTime());
        tPoint.setEndTime(tRefDate.getTime() + 106);

        MethodCallPO tChild1 = new MethodCallPO();
        tChild1.setParentMethodCall(tPoint);
        tChild1.setClassName(FlowUtilTest.class.getName());
        tChild1.setMethodName("builNewFullFlow2");
        tChild1.setGroupName("GrChild1");
        tChild1.setParams("[]");
        tChild1.setBeginTime(tRefDate.getTime() + 2);
        tChild1.setEndTime(tRefDate.getTime() + 45);

        MethodCallPO tChild3 = new MethodCallPO();
        tChild3.setParentMethodCall(tChild1);
        tChild3.setClassName(FlowUtilTest.class.getName());
        tChild3.setMethodName("builNewFullFlow4");
        tChild3.setGroupName("GrChild2");
        tChild3.setParams("[]");
        tChild3.setBeginTime(tRefDate.getTime() + 5);
        tChild3.setEndTime(tRefDate.getTime() + 17);

        MethodCallPO tChild4 = new MethodCallPO();
        tChild4.setParentMethodCall(tChild1);
        tChild4.setClassName(FlowUtilTest.class.getName());
        tChild4.setMethodName("builNewFullFlow4");
        tChild4.setGroupName("GrDefault");
        tChild4.setParams("[]");
        tChild4.setBeginTime(tRefDate.getTime() + 23);
        tChild4.setEndTime(tRefDate.getTime() + 27);

        MethodCallPO tChild2 = new MethodCallPO();
        tChild2.setParentMethodCall(tPoint);
        tChild2.setClassName(FlowUtilTest.class.getName());
        tChild2.setMethodName("builNewFullFlow3");
        tChild2.setGroupName("GrChild2");
        tChild2.setParams("[]");
        tChild2.setBeginTime(tRefDate.getTime() + 48);
        tChild2.setEndTime(tRefDate.getTime() + 75);

        return tPoint;
    }

    @Test
    public void testAddTimeWith()
    {
        FlowUtil tUtil = new FlowUtil(mColor, mFormater);
        tUtil.addTimeWith(getSampleMeasurePoint());
        Map<String, Integer> tMap = tUtil.getListOfGroup();
        assertEquals(40, (int) tMap.get("GrDefault"));
        assertEquals(27, (int) tMap.get("GrChild1"));
        assertEquals(39, (int) tMap.get("GrChild2"));
        assertEquals(106, 40 + 27 + 39);
    }

    @Test
    public void testAddNbCallWith()
    {
        FlowUtil tUtil = new FlowUtil(mColor, mFormater);
        tUtil.addNbCallWith(getSampleMeasurePoint());
        Map<String, Integer> tMap = tUtil.getListOfGroup();
        assertEquals(2, (int) tMap.get("GrDefault"));
        assertEquals(1, (int) tMap.get("GrChild1"));
        assertEquals(2, (int) tMap.get("GrChild2"));
    }

    protected ApplicationContext applicationContext;

    public ApplicationContext getApplicationContext()
    {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext pApplicationContext)
    {
        applicationContext = pApplicationContext;
    }

}
