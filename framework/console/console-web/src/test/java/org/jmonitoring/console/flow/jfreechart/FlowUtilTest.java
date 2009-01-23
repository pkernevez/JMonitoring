package org.jmonitoring.console.flow.jfreechart;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.jmonitoring.core.configuration.ColorManager;
import org.jmonitoring.core.configuration.FormaterBean;
import org.jmonitoring.core.dto.MethodCallDTO;
import org.jmonitoring.core.tests.JMonitoringTestCase;
import org.junit.Test;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = {"/console.xml", "/formater-test.xml", "/color-test.xml", "/persistence-test.xml" })
@Service
public class FlowUtilTest extends JMonitoringTestCase
{
    @Resource(name = "formater")
    private FormaterBean mFormater;

    @Resource(name = "color")
    private ColorManager mColor;

    public static final long START_TIME = 1149282668046L;

    MethodCallDTO getSampleMeasurePoint()
    {
        MethodCallDTO tPoint;
        // Fri Jun 02 23:11:08 CEST 2006
        Date tRefDate = new Date(START_TIME);
        tPoint = new MethodCallDTO();
        tPoint.setParent(null);
        tPoint.setClassName(FlowUtilTest.class.getName());
        tPoint.setMethodName("builNewFullFlow");
        tPoint.setGroupName("GrDefault");
        tPoint.setParams("[]");
        tPoint.setBeginMilliSeconds(tRefDate.getTime());
        tPoint.setBeginTimeString(mFormater.formatDateTime(tRefDate));
        tPoint.setEndMilliSeconds(tRefDate.getTime() + 106);
        tPoint.setEndTimeString(mFormater.formatDateTime(new Date(tRefDate.getTime() + 106)));
        MethodCallDTO[] tChildren1 = new MethodCallDTO[2];
        MethodCallDTO[] tChildren2 = new MethodCallDTO[2];

        MethodCallDTO tChild1 = new MethodCallDTO();
        tChild1.setParent(tPoint);
        tChildren1[0] = tChild1;
        tChild1.setClassName(FlowUtilTest.class.getName());
        tChild1.setMethodName("builNewFullFlow2");
        tChild1.setGroupName("GrChild1");
        tChild1.setParams("[]");
        tChild1.setBeginMilliSeconds(tRefDate.getTime() + 2);
        tChild1.setBeginTimeString(mFormater.formatDateTime(new Date(tRefDate.getTime() + 2)));
        tChild1.setEndMilliSeconds(tRefDate.getTime() + 45);
        tChild1.setEndTimeString(mFormater.formatDateTime(new Date(tRefDate.getTime() + 45)));

        MethodCallDTO tChild3 = new MethodCallDTO();
        tChild3.setParent(tChild1);
        tChildren2[0] = tChild3;
        tChild3.setClassName(FlowUtilTest.class.getName());
        tChild3.setMethodName("builNewFullFlow4");
        tChild3.setGroupName("GrChild2");
        tChild3.setParams("[]");
        tChild3.setBeginMilliSeconds(tRefDate.getTime() + 5);
        tChild3.setBeginTimeString(mFormater.formatDateTime(new Date(tRefDate.getTime() + 5)));
        tChild3.setEndMilliSeconds(tRefDate.getTime() + 17);
        tChild3.setEndTimeString(mFormater.formatDateTime(new Date(tRefDate.getTime() + 17)));

        MethodCallDTO tChild4 = new MethodCallDTO();
        tChild4.setParent(tChild1);
        tChildren2[1] = tChild4;
        tChild4.setClassName(FlowUtilTest.class.getName());
        tChild4.setMethodName("builNewFullFlow4");
        tChild4.setGroupName("GrDefault");
        tChild4.setParams("[]");
        tChild4.setBeginMilliSeconds(tRefDate.getTime() + 23);
        tChild4.setBeginTimeString(mFormater.formatDateTime(new Date(tRefDate.getTime() + 23)));
        tChild4.setEndMilliSeconds(tRefDate.getTime() + 27);
        tChild4.setEndTimeString(mFormater.formatDateTime(new Date(tRefDate.getTime() + 27)));
        tChild1.setChildren(tChildren2);

        MethodCallDTO tChild2 = new MethodCallDTO();
        tChild2.setParent(tPoint);
        tChildren1[1] = tChild2;
        tChild2.setClassName(FlowUtilTest.class.getName());
        tChild2.setMethodName("builNewFullFlow3");
        tChild2.setGroupName("GrChild2");
        tChild2.setParams("[]");
        tChild2.setBeginMilliSeconds(tRefDate.getTime() + 48);
        tChild2.setBeginTimeString(mFormater.formatDateTime(new Date(tRefDate.getTime() + 48)));
        tChild2.setEndMilliSeconds(tRefDate.getTime() + 75);
        tChild2.setEndTimeString(mFormater.formatDateTime(new Date(tRefDate.getTime() + 75)));
        tPoint.setChildren(tChildren1);

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

}
