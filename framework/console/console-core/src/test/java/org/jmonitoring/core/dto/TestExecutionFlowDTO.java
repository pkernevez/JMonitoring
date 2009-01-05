package org.jmonitoring.core.dto;

import org.jmonitoring.core.dao.TestConsoleDao;
import org.jmonitoring.core.test.JMonitoringTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

@ContextConfiguration(locations = {"/formater-test.xml" })
public class TestExecutionFlowDTO extends JMonitoringTestCase
{

    @Autowired
    private DtoManager dtoManager;

    @Test
    public void testMeasureCount()
    {
        ExecutionFlowDTO tFlow = dtoManager.getDeepCopy(TestConsoleDao.buildNewFullFlow());
        assertEquals(6, tFlow.getMeasureCount());
        MethodCallDTO curMeasure = tFlow.getFirstMethodCall();
        assertEquals(6, curMeasure.getSubMeasureCount());
        curMeasure = curMeasure.getChild(1);
        assertEquals(4, curMeasure.getSubMeasureCount());
    }

}
