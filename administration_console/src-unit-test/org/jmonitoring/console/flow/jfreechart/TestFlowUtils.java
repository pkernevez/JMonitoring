package org.jmonitoring.console.flow.jfreechart;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.util.Date;
import java.util.Map;

import junit.framework.TestCase;

import org.jmonitoring.core.dto.MethodCall;

/**
 * @author pke
 * 
 * @todo To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 *       Templates
 */
public class TestFlowUtils extends TestCase
{

    private MethodCall getSampleMeasurePoint()
    {
        MethodCall tPoint;
        long tRefDate = new Date().getTime();
        tPoint = new MethodCall(null, this.getClass().getName(), "builNewFullFlow", "GrDefault", new Object[0]);
        tPoint.setBeginTime(tRefDate);
        tPoint.setEndTime(tRefDate + 106);
        MethodCall tChild1 = new MethodCall(tPoint, this.getClass().getName(), "builNewFullFlow2", "GrChild1",
                        new Object[0]);
        tChild1.setBeginTime(tRefDate + 2);
        tChild1.setEndTime(tRefDate + 45);
        MethodCall tChild2 = new MethodCall(tPoint, this.getClass().getName(), "builNewFullFlow3", "GrChild2",
                        new Object[0]);
        tChild2.setBeginTime(tRefDate + 48);
        tChild2.setEndTime(tRefDate + 75);
        MethodCall tChild3 = new MethodCall(tChild1, this.getClass().getName(), "builNewFullFlow4", "GrChild1",
                        new Object[0]);
        tChild3.setBeginTime(tRefDate + 5);
        tChild3.setEndTime(tRefDate + 17);
        MethodCall tChild4 = new MethodCall(tChild1, this.getClass().getName(), "builNewFullFlow4", "GrDefault",
                        new Object[0]);
        tChild4.setBeginTime(tRefDate + 23);
        tChild4.setEndTime(tRefDate + 27);
        return tPoint;
    }

    public void testAddTimeWith()
    {
        FlowUtil tUtil = new FlowUtil();
        tUtil.addTimeWith(getSampleMeasurePoint());
        Map tMap = tUtil.getListOfGroup();
        assertEquals(new Long(40), tMap.get("GrDefault"));
        assertEquals(new Long(39), tMap.get("GrChild1"));
        assertEquals(new Long(27), tMap.get("GrChild2"));
    }

    public void testAddNbCallWith()
    {
        FlowUtil tUtil = new FlowUtil();
        tUtil.addNbCallWith(getSampleMeasurePoint());
        Map tMap = tUtil.getListOfGroup();
        assertEquals(new Integer(2), tMap.get("GrDefault"));
        assertEquals(new Integer(2), tMap.get("GrChild1"));
        assertEquals(new Integer(1), tMap.get("GrChild2"));
    }

}
