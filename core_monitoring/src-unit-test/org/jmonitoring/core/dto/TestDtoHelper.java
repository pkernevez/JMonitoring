package org.jmonitoring.core.dto;

import org.jmonitoring.core.dao.TestExecutionFlowDAO;
import org.jmonitoring.core.persistence.ExecutionFlowPO;
import org.jmonitoring.core.persistence.MethodCallPO;

import junit.framework.TestCase;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestDtoHelper extends TestCase
{
    public void testGetExecutionFlowDto()
    {
        ExecutionFlowDTO tFlow = DtoHelper.getExecutionFlowDto(TestExecutionFlowDAO.buildNewFullFlow());
        assertEquals("TEST-main", tFlow.getThreadName());
        assertEquals(-1, tFlow.getId());
        assertEquals("myJVM", tFlow.getJvmIdentifier());
    }

    public void testGetMethodCallDto()
    {
        ExecutionFlowPO tFlow = TestExecutionFlowDAO.buildNewFullFlow();
        MethodCallDTO tMeth = DtoHelper.getMethodCallDto(tFlow.getFirstMethodCall());
        assertNull(tMeth.getParent());
        assertEquals(TestExecutionFlowDAO.class.getName(), tMeth.getClassName());
        assertEquals("builNewFullFlow", tMeth.getMethodName());
        assertEquals("GrDefault", tMeth.getGroupName());
        assertEquals("[]", tMeth.getParams());
        assertEquals(2, tMeth.getChildren().size());
        assertEquals(MethodCallDTO.class.getName(), tMeth.getChildren().get(0).getClass().getName());
        assertEquals(tMeth, ((MethodCallDTO)tMeth.getChildren().get(0)).getParent());
    }
}
