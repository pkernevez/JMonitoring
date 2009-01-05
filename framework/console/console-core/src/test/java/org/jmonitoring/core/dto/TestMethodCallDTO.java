package org.jmonitoring.core.dto;

import org.jmonitoring.core.test.JMonitoringTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/formater-test.xml" })
public class TestMethodCallDTO extends JMonitoringTestCase
{
    @Autowired
    private DtoManager dtoManager;

    private MethodCallDTO buildMethodCall()
    {
        MethodCallDTO tParent = new MethodCallDTO();
        MethodCallDTO[] tChildren = new MethodCallDTO[3];
        tChildren[0] = new MethodCallDTO();
        tChildren[0].setClassName("1");
        tChildren[1] = new MethodCallDTO();
        tChildren[1].setClassName("2");
        tChildren[2] = new MethodCallDTO();
        tChildren[2].setClassName("3");
        tParent.setChildren(tChildren);
        return tParent;
    }

    @Test
    public void testRemoveChild()
    {
        MethodCallDTO tParent = buildMethodCall();
        tParent.removeChild(0);
        assertEquals(2, tParent.getChildren().length);
        assertEquals("2", tParent.getChild(0).getClassName());
        assertEquals("3", tParent.getChild(1).getClassName());

        tParent = buildMethodCall();
        tParent.removeChild(1);
        assertEquals(2, tParent.getChildren().length);
        assertEquals("1", tParent.getChild(0).getClassName());
        assertEquals("3", tParent.getChild(1).getClassName());

        tParent = buildMethodCall();
        tParent.removeChild(2);
        assertEquals(2, tParent.getChildren().length);
        assertEquals("1", tParent.getChild(0).getClassName());
        assertEquals("2", tParent.getChild(1).getClassName());
    }

    // @Test
    // public void testDurationFromPreviousCall()
    // {
    // ExecutionFlowDTO tFlow = dtoManager.getDeepCopy(TestConsoleDao.buildNewFullFlow());
    // MethodCallDTO tParentMeth = tFlow.getFirstMethodCall();
    //
    // assertEquals(0, tParentMeth.getDurationFromPreviousCall());
    //
    // MethodCallDTO curMeth = tParentMeth.getChild(0);
    // assertEquals(2, curMeth.getDurationFromPreviousCall());
    //
    // curMeth = tParentMeth.getChild(1);
    // assertEquals(3, curMeth.getDurationFromPreviousCall());
    //
    // tParentMeth = tParentMeth.getChild(1);
    // curMeth = tParentMeth.getChild(0);
    // assertEquals(6, curMeth.getDurationFromPreviousCall());
    //
    // curMeth = tParentMeth.getChild(1);
    // assertEquals(1, curMeth.getDurationFromPreviousCall());
    //
    // tParentMeth = tParentMeth.getChild(1);
    // curMeth = tParentMeth.getChild(0);
    // assertEquals(10, curMeth.getDurationFromPreviousCall());
    // }
    //
    // public long getDurationFromPreviousCall()
    // {
    // long tDuration;
    // if (mChildPosition == 0)
    // {
    // if (mParent == null)
    // {
    // tDuration = mFlow.getBeginTime().getTime() - mBeginTime.getTime();
    // } else
    // {
    // tDuration = mBeginTime.getTime() - mParent.getBeginTime().getTime();
    // }
    // } else
    // {
    // MethodCallDTO tPrecedentMethodCall = mParent.getChild(mChildPosition - 1);
    // tDuration = mBeginTime.getTime() - tPrecedentMethodCall.getEndTime().getTime();
    // }
    // return tDuration;
    // }

}
