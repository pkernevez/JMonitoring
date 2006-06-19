package org.jmonitoring.core.dto;

import java.util.List;

import junit.framework.TestCase;

import org.jmonitoring.core.dao.PersistanceTestCase;
import org.jmonitoring.core.dao.TestExecutionFlowDAO;
import org.jmonitoring.core.persistence.ExecutionFlowPO;
import org.jmonitoring.core.persistence.MethodCallPO;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestDtoHelper extends PersistanceTestCase
{
    public void testGetExecutionFlowDto()
    {
        ExecutionFlowPO tFlowPO = TestExecutionFlowDAO.buildAndSaveNewFullFlow(mSession);
        tFlowPO.setId(45);

        ExecutionFlowDTO tFlow = DtoHelper.getDeepCopy(tFlowPO);
        assertEquals("TEST-main", tFlow.getThreadName());
        assertEquals("myJVM", tFlow.getJvmIdentifier());
        assertEquals(45, tFlow.getId());
        MethodCallDTO curMeth = tFlow.getFirstMethodCall();
        assertEquals(1, curMeth.getId());
        assertEquals(45, curMeth.getFlowId());
        curMeth = curMeth.getChild(0);
        assertEquals(2, curMeth.getId());
        assertEquals(45, curMeth.getFlowId());
    }

    public void testGetMethodCallDto()
    {
        ExecutionFlowPO tFlow = TestExecutionFlowDAO.buildNewFullFlow();
        DtoHelper tHelper = new DtoHelper();
        MethodCallDTO tMeth = tHelper.getMethodCallDto(tFlow.getFirstMethodCall());
        assertNull(tMeth.getParent());
        assertEquals(TestExecutionFlowDAO.class.getName(), tMeth.getClassName());
        assertEquals("builNewFullFlow", tMeth.getMethodName());
        assertEquals("GrDefault", tMeth.getGroupName());
        assertEquals("[]", tMeth.getParams());
        assertEquals(2, tMeth.getChildren().size());
        assertEquals(MethodCallDTO.class.getName(), tMeth.getChildren().get(0).getClass().getName());
        assertEquals(tMeth, ((MethodCallDTO) tMeth.getChildren().get(0)).getParent());
    }

    public void testGetFullMethodCallDto()
    {
        ExecutionFlowPO tFlow = TestExecutionFlowDAO.buildNewFullFlow();
        DtoHelper tHelper = new DtoHelper();
        MethodCallDTO tMeth = tHelper.getFullMethodCallDto(tFlow.getFirstMethodCall());
        assertNotNull(tMeth);
        assertEquals(2, tMeth.getChildren().size());
        MethodCallDTO tChild1 = (MethodCallDTO) tMeth.getChildren().get(0);
        assertNotNull(tChild1.getParent());
    }

    public void testCopyListMethodCallFullExtract()
    {
        ExecutionFlowPO tFlow = TestExecutionFlowDAO.buildNewFullFlow();
        MethodCallPO tMeth = tFlow.getFirstMethodCall();
        List tList = DtoHelper.copyListMethodCallFullExtract(tMeth.getChildren());
        assertEquals(2, tList.size());
        assertEquals(MethodCallFullExtractDTO.class, tList.get(0).getClass());
        assertEquals(MethodCallFullExtractDTO.class, tList.get(1).getClass());
    }

    public void testCopyMethodCallFullExtract()
    {
        ExecutionFlowPO tFlow = TestExecutionFlowDAO.buildNewFullFlow();
        MethodCallFullExtractDTO tMeth = DtoHelper.copyMethodCallFullExtract(tFlow.getFirstMethodCall().getChild(0));
        assertEquals(tFlow.getJvmIdentifier(), tMeth.getJvmName());
        assertEquals(tFlow.getThreadName(), tMeth.getThreadName());
        assertEquals(tFlow.getDuration(), tMeth.getFlowDuration());
        assertEquals(tFlow.getFirstMethodCall().getChild(0).getDuration(), tMeth.getDuration());
        assertEquals(tFlow.getId(), tMeth.getFlowId());
        assertEquals(tFlow.getFirstMethodCall().getChild(0).getId(), tMeth.getId());
    }
}
