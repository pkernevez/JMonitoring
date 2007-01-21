package org.jmonitoring.core.dto;

import junit.framework.TestCase;

import org.jmonitoring.core.dao.TestExecutionFlowDAO;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestExecutionFlowDTO extends TestCase
{
    /**
     * Test MeasureCount.
     */
    public void testMeasureCount()
    {
        ExecutionFlowDTO tFlow = DtoHelper.getDeepCopy(TestExecutionFlowDAO.buildNewFullFlow());
        assertEquals(6, tFlow.getMeasureCount());
        MethodCallDTO curMeasure = tFlow.getFirstMethodCall();
        assertEquals(6, curMeasure.getSubMeasureCount());
        curMeasure = (MethodCallDTO) curMeasure.getChild(1);
        assertEquals(4, curMeasure.getSubMeasureCount());
    }

}
