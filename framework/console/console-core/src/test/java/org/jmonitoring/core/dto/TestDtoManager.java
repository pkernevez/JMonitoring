package org.jmonitoring.core.dto;

import java.util.List;

import javax.annotation.Resource;

import org.jmonitoring.core.dao.ConsoleDao;
import org.jmonitoring.core.dao.ConsoleDaoTest;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.jmonitoring.test.dao.PersistanceTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

@ContextConfiguration(locations = {"/console.xml" })
public class TestDtoManager extends PersistanceTestCase
{
    @Autowired
    private DtoManager mDtoManager;

    @Resource(name = "dao")
    private ConsoleDao mDao;

    @Test
    public void testGetExecutionFlowDto()
    {
        ExecutionFlowPO tFlowPO = buildNewFullFlow();
        mDao.insertFullExecutionFlow(tFlowPO);
        getSession().flush();

        tFlowPO.setId(45);

        ExecutionFlowDTO tFlow = mDtoManager.getDeepCopy(tFlowPO);
        assertEquals("TEST-main", tFlow.getThreadName());
        assertEquals("myJVM", tFlow.getJvmIdentifier());
        assertEquals(45, tFlow.getId());
        MethodCallDTO curMeth = tFlow.getFirstMethodCall();
        assertEquals("#6cca68", curMeth.getGroupColor());
        assertEquals(1, curMeth.getPosition());
        assertEquals(45, curMeth.getFlowId());
        MethodCallDTO tParentDto = curMeth;
        curMeth = curMeth.getChild(0);
        assertSame(tParentDto, curMeth.getParent());
        assertEquals(2, curMeth.getPosition());
        assertEquals(45, curMeth.getFlowId());
    }

    @Test
    public void testGetMethodCallDto()
    {
        ExecutionFlowPO tFlow = buildNewFullFlow();
        MethodCallDTO tMeth = mDtoManager.getMethodCallDto(tFlow.getFirstMethodCall());
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
        assertEquals(tMeth, (tMeth.getChild(0)).getParent());
    }

    @Test
    public void testGetFullMethodCallDto()
    {
        ExecutionFlowPO tFlow = buildNewFullFlow();
        MethodCallDTO tMeth = mDtoManager.getFullMethodCallDto(tFlow.getFirstMethodCall(), 0);
        assertNotNull(tMeth);
        assertEquals(2, tMeth.getChildren().length);
        MethodCallDTO tChild1 = tMeth.getChild(0);
        assertNotNull(tChild1.getParent());
    }

    @Test
    public void testGetExecutionFlowPO()
    {
        ExecutionFlowPO tFlowPO = buildNewFullFlow();
        mDao.insertFullExecutionFlow(tFlowPO);
        getSession().flush();

        ;
        tFlowPO.setId(45);

        ExecutionFlowDTO tFlow = mDtoManager.getDeepCopy(tFlowPO);
        ExecutionFlowPO tNewFlow = mDtoManager.getDeepCopy(tFlow);

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

    @Test
    public void testGetMethodCallPO()
    {
        ExecutionFlowPO tFlow = ConsoleDaoTest.buildNewFullFlow();
        MethodCallDTO tMeth = mDtoManager.getMethodCallDto(tFlow.getFirstMethodCall());
        MethodCallPO tNewMeth = mDtoManager.getMethodCallPO(tMeth, null);
        assertNull(tNewMeth.getParentMethodCall());
        assertEquals(PersistanceTestCase.class.getName(), tNewMeth.getClassName());
        assertEquals("builNewFullFlow", tNewMeth.getMethodName());
        assertEquals("GrDefault", tNewMeth.getGroupName());
        assertEquals("[]", tNewMeth.getParams());
        assertEquals(2, tNewMeth.getChildren().size());
        assertEquals(MethodCallPO.class.getName(), tNewMeth.getChild(0).getClass().getName());
        assertEquals(tNewMeth, (tNewMeth.getChild(0)).getParentMethodCall());
    }

    @Test
    public void testGetFullMethodCallPO()
    {
        ExecutionFlowPO tFlow = ConsoleDaoTest.buildNewFullFlow();
        MethodCallDTO tMeth = mDtoManager.getFullMethodCallDto(tFlow.getFirstMethodCall(), 0);
        MethodCallPO tNewMeth = mDtoManager.getFullMethodCallPO(tMeth, null);
        assertNotNull(tNewMeth);
        assertEquals(2, tNewMeth.getChildren().size());
        MethodCallPO tChild1 = tNewMeth.getChild(0);
        assertNotNull(tChild1.getParentMethodCall());
    }

    @Test
    public void testCopyListMethodCallFullExtract()
    {
        ExecutionFlowPO tFlow = ConsoleDaoTest.buildNewFullFlow();
        MethodCallPO tMeth = tFlow.getFirstMethodCall();
        List<MethodCallFullExtractDTO> tList = mDtoManager.copyListMethodCallFullExtract(tMeth.getChildren());
        assertEquals(2, tList.size());
        assertEquals(MethodCallFullExtractDTO.class, tList.get(0).getClass());
        assertEquals(MethodCallFullExtractDTO.class, tList.get(1).getClass());
    }

    @Test
    public void testCopyMethodCallFullExtract()
    {
        ExecutionFlowPO tFlow = ConsoleDaoTest.buildNewFullFlow();
        MethodCallFullExtractDTO tMeth = mDtoManager.copyMethodCallFullExtract(tFlow.getFirstMethodCall().getChild(0));
        assertEquals(tFlow.getJvmIdentifier(), tMeth.getJvmName());
        assertEquals(tFlow.getThreadName(), tMeth.getThreadName());
        assertEquals(tFlow.getDuration(), tMeth.getFlowDuration());
        assertEquals(tFlow.getFirstMethodCall().getChild(0).getDuration(), tMeth.getDuration());
        assertEquals(tFlow.getId(), tMeth.getFlowId());
        assertEquals(tFlow.getFirstMethodCall().getChild(0).getPosition(), tMeth.getPosition());
    }

    @Test
    public void testGetDeepCopy()
    {
        ExecutionFlowDTO tFlow = mDtoManager.getDeepCopy(ConsoleDaoTest.buildNewFullFlow());
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

    @Test
    public void testGetSimpleCopy()
    {
        ExecutionFlowPO tFlow = new ExecutionFlowPO();
        tFlow.setFirstClassName("test Class");
        tFlow.setFirstMethodName("test Method");
        ExecutionFlowDTO tDto = mDtoManager.getSimpleCopy(tFlow);
        assertEquals("test Class", tDto.getClassName());
        assertEquals("test Method", tDto.getMethodName());
        tFlow = mDtoManager.getSimpleCopy(tDto);
        assertEquals("test Class", tFlow.getFirstClassName());
        assertEquals("test Method", tFlow.getFirstMethodName());

    }
}
