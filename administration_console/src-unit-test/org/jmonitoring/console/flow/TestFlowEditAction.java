package org.jmonitoring.console.flow;

import java.sql.Date;

import junit.framework.TestCase;

import org.jmonitoring.core.dto.MethodCallDTO;

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
     * @return MethodCallDTO.
     */
    public static MethodCallDTO buildNewFullMeasure()
    {
        MethodCallDTO tPoint, curPoint;
        long tCurrentTime = System.currentTimeMillis();
        tPoint = new MethodCallDTO();
        tPoint.setParent(null);
        tPoint.setClassName( TestFlowEditAction.class.getName());
        tPoint.setMethodName( "builNewFullFlow");
        tPoint.setGroupName( "GrDefault");
        tPoint.setParams("[]");
        tPoint.setBeginTime(new Date(tCurrentTime));
        tPoint.setEndTime(new Date(tCurrentTime + 2 + 2 + 1)); //Duration=5
        
        //This local variable is indireclty used by its parent
        curPoint = new MethodCallDTO();
        curPoint.setParent(tPoint);
        tPoint.addChildren(curPoint);
        curPoint.setClassName( TestFlowEditAction.class.getName());
        curPoint.setMethodName( "builNewFullFlow2");
        curPoint.setGroupName( "GrChild1");
        curPoint.setParams("[]");
        curPoint.setBeginTime(new Date(tCurrentTime));
        curPoint.setEndTime(new Date(tCurrentTime + 2)); //Duration=2
        
        //This local variable is indireclty used by its parent
        curPoint = new MethodCallDTO();
        curPoint.setParent(tPoint);
        tPoint.addChildren(curPoint);
        curPoint.setClassName( TestFlowEditAction.class.getName());
        curPoint.setMethodName( "builNewFullFlow2");
        curPoint.setGroupName( "GrChild2");
        curPoint.setParams("[]");
        curPoint.setBeginTime(new Date(tCurrentTime));
        curPoint.setEndTime(new Date(tCurrentTime + 2 + 1)); //Duration=3
        
        //This local variable is indireclty used by its parent
        MethodCallDTO tOldPoint = curPoint;
        curPoint = new MethodCallDTO();
        curPoint.setParent(tOldPoint);
        tOldPoint.addChildren(curPoint);
        curPoint.setClassName( TestFlowEditAction.class.getName());
        curPoint.setMethodName( "builNewFullFlow3");
        curPoint.setGroupName( "GrChild2_1");
        curPoint.setParams("[]");
        curPoint.setBeginTime(new Date(tCurrentTime));
        curPoint.setEndTime(new Date(tCurrentTime + 1)); //Duration=1
        
        return tPoint;
    }

    public void testLimitMeasureWithDurationNoLimitation()
    {
        FlowEditAction tAction = new FlowEditAction();
        MethodCallDTO tMeasure = buildNewFullMeasure();
        tAction.limitMeasureWithDuration(1, tMeasure);
        assertEquals(2, tMeasure.getChildren().size());
        MethodCallDTO curMeasure = (MethodCallDTO) tMeasure.getChildren().get(0); // Child1
        assertEquals(0, curMeasure.getChildren().size());
        curMeasure = (MethodCallDTO) tMeasure.getChildren().get(1); //Child2
        assertEquals(1, curMeasure.getChildren().size());
        curMeasure = (MethodCallDTO) curMeasure.getChildren().get(0); // Child2_2
        assertEquals(0, curMeasure.getChildren().size());
    }

    public void testLimitMeasureWithDurationLimitation2ndLevel()
    {
        FlowEditAction tAction = new FlowEditAction();
        MethodCallDTO tMeasure = buildNewFullMeasure();
        tAction.limitMeasureWithDuration(2, tMeasure);
        assertEquals(2, tMeasure.getChildren().size());
        MethodCallDTO curMeasure = (MethodCallDTO) tMeasure.getChildren().get(0); // Child1
        assertEquals(0, curMeasure.getChildren().size());
        curMeasure = (MethodCallDTO) tMeasure.getChildren().get(1); //Child2
        assertEquals(0, curMeasure.getChildren().size());
    }

    public void testLimitMeasureWithDurationLimitation2ndChild()
    {
        FlowEditAction tAction = new FlowEditAction();
        MethodCallDTO tMeasure = buildNewFullMeasure();
        tAction.limitMeasureWithDuration(2 + 1, tMeasure);
        assertEquals(1, tMeasure.getChildren().size());
        MethodCallDTO curMeasure = (MethodCallDTO) tMeasure.getChildren().get(0); // Child2
        assertEquals(0, curMeasure.getChildren().size());
        assertEquals("GrChild2", curMeasure.getGroupName());
    }

    public void testLimitMeasureWithDurationLimitationNoChild()
    {
        FlowEditAction tAction = new FlowEditAction();
        MethodCallDTO tMeasure = buildNewFullMeasure();
        tAction.limitMeasureWithDuration(2 + 2 + 2, tMeasure);
        assertEquals(0, tMeasure.getChildren().size());
    }

}
