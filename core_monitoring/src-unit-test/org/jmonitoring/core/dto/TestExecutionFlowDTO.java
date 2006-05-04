package org.jmonitoring.core.dto;

import org.jmonitoring.core.dao.TestExecutionFlowDAO;
import org.jmonitoring.core.persistence.ExecutionFlowPO;

import junit.framework.TestCase;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

public class TestExecutionFlowDTO extends TestCase
{
    /**
     * Test MeasureCount.
     */
    public void testMeasureCount()
    {
        ExecutionFlowDTO tFlow = DtoHelper.getDeepCopy(TestExecutionFlowDAO.buildNewFullFlow());
        assertEquals(2 + 1, tFlow.getMeasureCount());
        MethodCallDTO curMeasure = tFlow.getFirstMethodCall();
        assertEquals(2 + 1, curMeasure.getSubMeasureCount());
        curMeasure = (MethodCallDTO) curMeasure.getChildren().get(1);
        assertEquals(1, curMeasure.getSubMeasureCount());
    }

}
