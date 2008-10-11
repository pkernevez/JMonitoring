package org.jmonitoring.core.dto;

import java.util.List;

import org.jmonitoring.core.dao.TestConsoleDao;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.jmonitoring.test.dao.PersistanceTestCase;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestDtoHelper extends PersistanceTestCase {
    public void testGetExecutionFlowDto() {
        ExecutionFlowPO tFlowPO = TestConsoleDao.buildAndSaveNewFullFlow(getSession());
        tFlowPO.setId(45);

        ExecutionFlowDTO tFlow = DtoHelper.getDeepCopy(tFlowPO);
        assertEquals("TEST-main", tFlow.getThreadName());
        assertEquals("myJVM", tFlow.getJvmIdentifier());
        assertEquals(45, tFlow.getId());
        MethodCallDTO curMeth = tFlow.getFirstMethodCall();
        assertEquals(1, curMeth.getPosition());
        assertEquals(45, curMeth.getFlowId());
        MethodCallDTO tParentDto = curMeth;
        curMeth = curMeth.getChild(0);
        assertSame(tParentDto, curMeth.getParent());
        assertEquals(2, curMeth.getPosition());
        assertEquals(45, curMeth.getFlowId());
    }

    public void testGetMethodCallDto() {
        ExecutionFlowPO tFlow = TestConsoleDao.buildNewFullFlow();
        MethodCallDTO tMeth = DtoHelper.getMethodCallDto(tFlow.getFirstMethodCall());
        assertNull(tMeth.getParent());
        assertEquals(PersistanceTestCase.class.getName(), tMeth.getClassName());
        assertEquals(PersistanceTestCase.class.getName(), tMeth.getRuntimeClassName());

        assertEquals(PersistanceTestCase.class.getName(), tMeth.getChild(0).getClassName());
        assertEquals(PersistanceTestCase.class.getName() + "iuiu", tMeth.getChild(0).getRuntimeClassName());

        assertEquals("builNewFullFlow", tMeth.getMethodName());
        assertEquals("GrDefault", tMeth.getGroupName());
        assertEquals("[]", tMeth.getParams());
        assertEquals(2, tMeth.getChildren().length);
        assertEquals(MethodCallDTO.class.getName(), tMeth.getChild(0).getClass().getName());
        assertEquals(tMeth, ((MethodCallDTO) tMeth.getChild(0)).getParent());
    }

    public void testGetFullMethodCallDto() {
        ExecutionFlowPO tFlow = TestConsoleDao.buildNewFullFlow();
        MethodCallDTO tMeth = DtoHelper.getFullMethodCallDto(tFlow.getFirstMethodCall(), 0);
        assertNotNull(tMeth);
        assertEquals(2, tMeth.getChildren().length);
        MethodCallDTO tChild1 = (MethodCallDTO) tMeth.getChild(0);
        assertNotNull(tChild1.getParent());
    }

    public void testGetExecutionFlowPO() {
        ExecutionFlowPO tFlowPO = TestConsoleDao.buildAndSaveNewFullFlow(getSession());
        tFlowPO.setId(45);

        ExecutionFlowDTO tFlow = DtoHelper.getDeepCopy(tFlowPO);
        ExecutionFlowPO tNewFlow = DtoHelper.getDeepCopy(tFlow);

        assertEquals("TEST-main", tNewFlow.getThreadName());
        assertEquals("myJVM", tNewFlow.getJvmIdentifier());
        assertEquals(45, tNewFlow.getId());
        MethodCallPO curMeth = tNewFlow.getFirstMethodCall();
        assertEquals(1, curMeth.getPosition());
        assertEquals(45, curMeth.getFlow().getId());
        MethodCallPO tParentDto = curMeth;
        curMeth = curMeth.getChild(0);
        assertSame(tParentDto, curMeth.getParentMethodCall());
        assertEquals(2, curMeth.getPosition());
        assertEquals(45, curMeth.getFlow().getId());
    }

    public void testGetMethodCallPO() {
        ExecutionFlowPO tFlow = TestConsoleDao.buildNewFullFlow();
        MethodCallDTO tMeth = DtoHelper.getMethodCallDto(tFlow.getFirstMethodCall());
        MethodCallPO tNewMeth = DtoHelper.getMethodCallPO(tMeth, null);
        assertNull(tNewMeth.getParentMethodCall());
        assertEquals(PersistanceTestCase.class.getName(), tNewMeth.getClassName());
        assertEquals("builNewFullFlow", tNewMeth.getMethodName());
        assertEquals("GrDefault", tNewMeth.getGroupName());
        assertEquals("[]", tNewMeth.getParams());
        assertEquals(2, tNewMeth.getChildren().size());
        assertEquals(MethodCallPO.class.getName(), tNewMeth.getChild(0).getClass().getName());
        assertEquals(tNewMeth, ((MethodCallPO) tNewMeth.getChild(0)).getParentMethodCall());
    }

    public void testGetFullMethodCallPO() {
        ExecutionFlowPO tFlow = TestConsoleDao.buildNewFullFlow();
        MethodCallDTO tMeth = DtoHelper.getFullMethodCallDto(tFlow.getFirstMethodCall(), 0);
        MethodCallPO tNewMeth = DtoHelper.getFullMethodCallPO(tMeth, null);
        assertNotNull(tNewMeth);
        assertEquals(2, tNewMeth.getChildren().size());
        MethodCallPO tChild1 = (MethodCallPO) tNewMeth.getChild(0);
        assertNotNull(tChild1.getParentMethodCall());
    }

    public void testCopyListMethodCallFullExtract() {
        ExecutionFlowPO tFlow = TestConsoleDao.buildNewFullFlow();
        MethodCallPO tMeth = tFlow.getFirstMethodCall();
        List tList = DtoHelper.copyListMethodCallFullExtract(tMeth.getChildren());
        assertEquals(2, tList.size());
        assertEquals(MethodCallFullExtractDTO.class, tList.get(0).getClass());
        assertEquals(MethodCallFullExtractDTO.class, tList.get(1).getClass());
    }

    public void testCopyMethodCallFullExtract() {
        ExecutionFlowPO tFlow = TestConsoleDao.buildNewFullFlow();
        MethodCallFullExtractDTO tMeth = DtoHelper.copyMethodCallFullExtract(tFlow.getFirstMethodCall().getChild(0));
        assertEquals(tFlow.getJvmIdentifier(), tMeth.getJvmName());
        assertEquals(tFlow.getThreadName(), tMeth.getThreadName());
        assertEquals(tFlow.getDuration(), tMeth.getFlowDuration());
        assertEquals(tFlow.getFirstMethodCall().getChild(0).getDuration(), tMeth.getDuration());
        assertEquals(tFlow.getId(), tMeth.getFlowId());
        assertEquals(tFlow.getFirstMethodCall().getChild(0).getPosition(), tMeth.getPosition());
    }

    public void testGetDeepCopy() {
        ExecutionFlowDTO tFlow = DtoHelper.getDeepCopy(TestConsoleDao.buildNewFullFlow());
        MethodCallDTO tParent = tFlow.getFirstMethodCall();

        assertEquals(0, tParent.getChildPosition());

        MethodCallDTO tMeth = tParent.getChild(0);
        assertEquals(0, tMeth.getChildPosition());

        tMeth = tParent.getChild(1);
        assertEquals(1, tMeth.getChildPosition());

        tParent = tMeth;
        tMeth = tParent.getChild(0);
        assertEquals(0, tMeth.getChildPosition());

        tMeth = tParent.getChild(1);
        assertEquals(1, tMeth.getChildPosition());

        tParent = tMeth;
        tMeth = tParent.getChild(0);
        assertEquals(0, tMeth.getChildPosition());
    }
}
