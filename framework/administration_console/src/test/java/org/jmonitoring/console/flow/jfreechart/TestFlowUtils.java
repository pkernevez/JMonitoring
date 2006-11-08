package org.jmonitoring.console.flow.jfreechart;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.util.Date;
import java.util.Map;

import junit.framework.TestCase;

import org.jmonitoring.core.dto.MethodCallDTO;

/**
 * @author pke
 * 
 * @todo To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 *       Templates
 */
public class TestFlowUtils extends TestCase
{
    public static final long START_TIME = 1149282668046L;

    static MethodCallDTO getSampleMeasurePoint()
    {
        MethodCallDTO tPoint;
        // Fri Jun 02 23:11:08 CEST 2006
        Date tRefDate = new Date(START_TIME);
        tPoint = new MethodCallDTO();
        tPoint.setParent(null);
        tPoint.setClassName(TestFlowUtils.class.getName());
        tPoint.setMethodName("builNewFullFlow");
        tPoint.setGroupName("GrDefault");
        tPoint.setParams("[]");
        tPoint.setBeginTime(tRefDate);
        tPoint.setEndTime(new Date(tRefDate.getTime() + 106));
        MethodCallDTO[] tChildren1 = new MethodCallDTO[2];
        MethodCallDTO[] tChildren2 = new MethodCallDTO[2];
        
        MethodCallDTO tChild1 = new MethodCallDTO();
        tChild1.setParent(tPoint);
        tChildren1[0]=tChild1;
        tChild1.setClassName(TestFlowUtils.class.getName());
        tChild1.setMethodName("builNewFullFlow2");
        tChild1.setGroupName("GrChild1");
        tChild1.setParams("[]");
        tChild1.setBeginTime(new Date(tRefDate.getTime() + 2));
        tChild1.setEndTime(new Date(tRefDate.getTime() + 45));

        MethodCallDTO tChild3 = new MethodCallDTO();
        tChild3.setParent(tChild1);
        tChildren2[0]=tChild3;
        tChild3.setClassName(TestFlowUtils.class.getName());
        tChild3.setMethodName("builNewFullFlow4");
        tChild3.setGroupName("GrChild2");
        tChild3.setParams("[]");
        tChild3.setBeginTime(new Date(tRefDate.getTime() + 5));
        tChild3.setEndTime(new Date(tRefDate.getTime() + 17));

        MethodCallDTO tChild4 = new MethodCallDTO();
        tChild4.setParent(tChild1);
        tChildren2[1]=tChild4;
        tChild4.setClassName(TestFlowUtils.class.getName());
        tChild4.setMethodName("builNewFullFlow4");
        tChild4.setGroupName("GrDefault");
        tChild4.setParams("[]");
        tChild4.setBeginTime(new Date(tRefDate.getTime() + 23));
        tChild4.setEndTime(new Date(tRefDate.getTime() + 27));
tChild1.setChildren(tChildren2);

        MethodCallDTO tChild2 = new MethodCallDTO();
        tChild2.setParent(tPoint);
        tChildren1[1]=tChild2;
        tChild2.setClassName(TestFlowUtils.class.getName());
        tChild2.setMethodName("builNewFullFlow3");
        tChild2.setGroupName("GrChild2");
        tChild2.setParams("[]");
        tChild2.setBeginTime(new Date(tRefDate.getTime() + 48));
        tChild2.setEndTime(new Date(tRefDate.getTime() + 75));
        tPoint.setChildren(tChildren1);
        
        return tPoint;
    }

    public void testAddTimeWith()
    {
        FlowUtil tUtil = new FlowUtil();
        tUtil.addTimeWith(getSampleMeasurePoint());
        Map tMap = tUtil.getListOfGroup();
        assertEquals(new Long(40), tMap.get("GrDefault"));
        assertEquals(new Long(27), tMap.get("GrChild1"));
        assertEquals(new Long(39), tMap.get("GrChild2"));
        assertEquals(106, 40 + 27 + 39);
    }

    public void testAddNbCallWith()
    {
        FlowUtil tUtil = new FlowUtil();
        tUtil.addNbCallWith(getSampleMeasurePoint());
        Map tMap = tUtil.getListOfGroup();
        assertEquals(new Integer(2), tMap.get("GrDefault"));
        assertEquals(new Integer(1), tMap.get("GrChild1"));
        assertEquals(new Integer(2), tMap.get("GrChild2"));
    }

}
