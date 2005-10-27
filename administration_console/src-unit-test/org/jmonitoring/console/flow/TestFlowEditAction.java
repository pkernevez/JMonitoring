package org.jmonitoring.console.flow;

import junit.framework.TestCase;

import org.jmonitoring.core.dto.MethodCall;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * @author pke
 *  
 */
public class TestFlowEditAction extends TestCase
{
    /**
     * @return MethodCall.
     */
    public static MethodCall buildNewFullMeasure()
    {
        MethodCall tPoint, curPoint;
        long tCurrentTime = System.currentTimeMillis();
        tPoint = new MethodCall(null, TestFlowEditAction.class.getName(), "builNewFullFlow", "GrDefault",
                        new Object[0]);
        tPoint.setBeginTime(tCurrentTime);
        tPoint.setEndTime(tCurrentTime + 2 + 2 + 1); //Duration=5
        //This local variable is indireclty used by its parent
        curPoint = new MethodCall(tPoint, TestFlowEditAction.class.getName(), "builNewFullFlow2", "GrChild1",
                        new Object[0]);
        curPoint.setBeginTime(tCurrentTime);
        curPoint.setEndTime(tCurrentTime + 2); //Duration=2
        //This local variable is indireclty used by its parent
        curPoint = new MethodCall(tPoint, TestFlowEditAction.class.getName(), "builNewFullFlow2", "GrChild2",
                        new Object[0]);
        curPoint.setBeginTime(tCurrentTime);
        curPoint.setEndTime(tCurrentTime + 2 + 1); //Duration=3
        //This local variable is indireclty used by its parent
        curPoint = new MethodCall(curPoint, TestFlowEditAction.class.getName(), "builNewFullFlow3",
                        "GrChild2_1", new Object[0]);
        curPoint.setBeginTime(tCurrentTime);
        curPoint.setEndTime(tCurrentTime + 1); //Duration=1
        return tPoint;
    }

    public void testLimitMeasureWithDurationNoLimitation()
    {
        FlowEditAction tAction = new FlowEditAction();
        MethodCall tMeasure = buildNewFullMeasure();
        tAction.limitMeasureWithDuration(1, tMeasure);
        assertEquals(2, tMeasure.getChildren().size());
        MethodCall curMeasure = (MethodCall) tMeasure.getChildren().get(0); // Child1
        assertEquals(0, curMeasure.getChildren().size());
        curMeasure = (MethodCall) tMeasure.getChildren().get(1); //Child2
        assertEquals(1, curMeasure.getChildren().size());
        curMeasure = (MethodCall) curMeasure.getChildren().get(0); // Child2_2
        assertEquals(0, curMeasure.getChildren().size());
    }

    public void testLimitMeasureWithDurationLimitation2ndLevel()
    {
        FlowEditAction tAction = new FlowEditAction();
        MethodCall tMeasure = buildNewFullMeasure();
        tAction.limitMeasureWithDuration(2, tMeasure);
        assertEquals(2, tMeasure.getChildren().size());
        MethodCall curMeasure = (MethodCall) tMeasure.getChildren().get(0); // Child1
        assertEquals(0, curMeasure.getChildren().size());
        curMeasure = (MethodCall) tMeasure.getChildren().get(1); //Child2
        assertEquals(0, curMeasure.getChildren().size());
    }

    public void testLimitMeasureWithDurationLimitation2ndChild()
    {
        FlowEditAction tAction = new FlowEditAction();
        MethodCall tMeasure = buildNewFullMeasure();
        tAction.limitMeasureWithDuration(2 + 1, tMeasure);
        assertEquals(1, tMeasure.getChildren().size());
        MethodCall curMeasure = (MethodCall) tMeasure.getChildren().get(0); // Child2
        assertEquals(0, curMeasure.getChildren().size());
        assertEquals("GrChild2", curMeasure.getGroupName());
    }

    public void testLimitMeasureWithDurationLimitationNoChild()
    {
        FlowEditAction tAction = new FlowEditAction();
        MethodCall tMeasure = buildNewFullMeasure();
        tAction.limitMeasureWithDuration(2 + 2 + 2, tMeasure);
        assertEquals(0, tMeasure.getChildren().size());
    }

}
