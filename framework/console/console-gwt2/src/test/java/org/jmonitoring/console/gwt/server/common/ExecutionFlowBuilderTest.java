package org.jmonitoring.console.gwt.server.common;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;

import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.junit.Test;

public class ExecutionFlowBuilderTest
{

    @Test
    public void testGetSimple()
    {
        ExecutionFlowPO tExecFlow = ExecutionFlowBuilder.create(11111L).setMethodCall("class", "meth", "grp", 4).get();
        assertNotNull(tExecFlow);
        assertEquals("Main", tExecFlow.getThreadName());
        assertEquals("DefaultJvm", tExecFlow.getJvmIdentifier());
        assertEquals("class", tExecFlow.getFirstClassName());
        assertEquals("meth", tExecFlow.getFirstMethodName());
        assertEquals("grp", tExecFlow.getFirstMethodCall().getGroupName());
        assertEquals(4, tExecFlow.getDuration());
        assertNotNull(tExecFlow.getFirstMethodCall());
        assertEquals(0, tExecFlow.getFirstMethodCall().getChildren().size());
        assertEquals("class", tExecFlow.getFirstMethodCall().getClassName());
        assertEquals("meth", tExecFlow.getFirstMethodCall().getMethodName());
        assertEquals(11111L, tExecFlow.getFirstMethodCall().getBeginTime());
        assertEquals(11115, tExecFlow.getFirstMethodCall().getEndTime());
        assertEquals(4, tExecFlow.getFirstMethodCall().getDuration());
        assertEquals(4, tExecFlow.getDuration());
        assertEquals(11115L, tExecFlow.getEndTime());
        assertEquals(11111L, tExecFlow.getBeginTime());
    }

    @Test
    public void testGetSpecific()
    {
        ExecutionFlowPO tExecFlow =
            ExecutionFlowBuilder.create(11111L).setJvm("myJvm").setThread("MyThread")
                                .setMethodCall("class", "meth", "grp", 4).setRuntimeClassName("runtimeclass").get();
        assertNotNull(tExecFlow);
        assertEquals("MyThread", tExecFlow.getThreadName());
        assertEquals("myJvm", tExecFlow.getJvmIdentifier());
        assertEquals("runtimeclass", tExecFlow.getFirstMethodCall().getRuntimeClassName());
    }

    @Test
    public void testAddSimpleGraph()
    {
        MethodCallBuilder tFirstMeth =
            ExecutionFlowBuilder.create(11111L).setJvm("myJvm").setThread("MyThread")
                                .setMethodCall("class", "meth", "grp", 40);
        tFirstMeth.addSubMethod("class1", "meth1", "grp1", 4);
        tFirstMeth.addSubMethod("class2", "meth2", "grp2", 4, 10);
        tFirstMeth.addSubMethod("class3", "meth3", "grp3", 30, 5).setThrowable("Error", "An error occured");
        ExecutionFlowPO tFlow = tFirstMeth.get();
        assertEquals("class", tFlow.getFirstClassName());
        assertEquals("meth", tFlow.getFirstMethodName());
        assertEquals("class", tFlow.getFirstMethodCall().getClassName());
        assertEquals("meth", tFlow.getFirstMethodCall().getMethodName());

        assertEquals(40, tFlow.getFirstMethodCall().getDuration());
        assertEquals(11151L, tFlow.getFirstMethodCall().getEndTime());

        assertEquals(3, tFlow.getFirstMethodCall().getChildren().size());
        MethodCallPO tFirstChild = tFlow.getFirstMethodCall().getChild(0);
        assertEquals("class1", tFirstChild.getClassName());
        assertEquals("meth1", tFirstChild.getMethodName());
        assertEquals(11111L, tFirstChild.getBeginTime());
        assertEquals(11115L, tFirstChild.getEndTime());
        assertEquals(4, tFirstChild.getDuration());
        assertEquals("grp1", tFirstChild.getGroupName());

        MethodCallPO tSecondChild = tFlow.getFirstMethodCall().getChild(1);
        assertEquals("class2", tSecondChild.getClassName());
        assertEquals("meth2", tSecondChild.getMethodName());
        assertEquals(11115L, tSecondChild.getBeginTime());
        assertEquals(11125L, tSecondChild.getEndTime());
        assertEquals(10, tSecondChild.getDuration());

        MethodCallPO tThirdChild = tFlow.getFirstMethodCall().getChild(2);
        assertEquals("class3", tThirdChild.getClassName());
        assertEquals("meth3", tThirdChild.getMethodName());
        assertEquals(11141L, tThirdChild.getBeginTime());
        assertEquals(11146L, tThirdChild.getEndTime());
        assertEquals(5, tThirdChild.getDuration());
        assertEquals("Error", tThirdChild.getThrowableClass());
        assertEquals("An error occured", tThirdChild.getThrowableMessage());

    }

    @Test
    public void testAdd2LevelGraph()
    {
        MethodCallBuilder tFirstMeth =
            ExecutionFlowBuilder.create(11111L).setJvm("myJvm").setThread("MyThread")
                                .setMethodCall("class", "meth","grp", 40);
        tFirstMeth.addSubMethod("class1", "meth1","grp1", 4).addSubMethod("class1_1", "method1_1", "grp1_1",2, 1);
        tFirstMeth.addSubMethod("class2", "meth2","grp2", 4, 10);
    }

    @Test
    public void testAddGraphChildEndTooLate()
    {
        MethodCallBuilder tFirstMeth =
            ExecutionFlowBuilder.create(11111L).setJvm("myJvm").setThread("MyThread")
                                .setMethodCall("class", "meth", "grp",40);
        try
        {
            tFirstMeth.addSubMethod("class", "meth","grp", 50);
            fail("Should not pass");
        } catch (ExecutionFlowInvalidExeption e)
        {
            assertEquals("The child could not end after the parent. Child endTime is 11161 and parent is 11151",
                         e.getMessage());
        }
    }

    @Test
    public void testAddGraphChildEndTooLateWithOffset()
    {
        MethodCallBuilder tFirstMeth =
            ExecutionFlowBuilder.create(11111L).setJvm("myJvm").setThread("MyThread")
                                .setMethodCall("class", "meth", "grp",40);
        try
        {
            tFirstMeth.addSubMethod("class", "meth","grp", 20, 30);
            fail("Should not pass");
        } catch (ExecutionFlowInvalidExeption e)
        {
            assertEquals("The child could not end after the parent. Child endTime is 11161 and parent is 11151",
                         e.getMessage());
        }
    }

    @Test
    public void testAddGraphInvalidDurationStartTimeShouldBeInParentRange()
    {
        MethodCallBuilder tFirstMeth =
            ExecutionFlowBuilder.create(11111L).setJvm("myJvm").setThread("MyThread")
                                .setMethodCall("class", "meth","grp", 40);
        try
        {
            tFirstMeth.addSubMethod("class", "meth", "grp",45, 10);
            fail("Should not pass");
        } catch (ExecutionFlowInvalidExeption e)
        {
            assertEquals("The child could not start after the parent ended. Child beginTime is 11156 and parent endTime is 11151",
                         e.getMessage());
        }
    }

    @Test
    public void testAddGraphInvalidDurationStartTimeShouldBeAfertTheParentStartTime()
    {
        MethodCallBuilder tFirstMeth =
            ExecutionFlowBuilder.create(11111L).setJvm("myJvm").setThread("MyThread")
                                .setMethodCall("class", "meth","grp", 40);
        try
        {
            tFirstMeth.addSubMethod("class", "meth", "grp",-5, 10);
            fail("Should not pass");
        } catch (ExecutionFlowInvalidExeption e)
        {
            assertEquals("The child could not start before the parent beginTime. Child beginTime is 11106 and parent beginTime is 11111",
                         e.getMessage());
        }
    }

    @Test
    public void testAddGraphInvalidDurationNextAndPreviousMustBeCompatible()
    {
        MethodCallBuilder tFirstMeth =
            ExecutionFlowBuilder.create(11111L).setJvm("myJvm").setThread("MyThread")
                                .setMethodCall("class", "meth","grp", 40);
        try
        {
            tFirstMeth.addSubMethod("class", "meth","grp", 10, 10);
            tFirstMeth.addSubMethod("class", "meth","grp", 14, 5);
            fail("Should not pass");
        } catch (ExecutionFlowInvalidExeption e)
        {
            assertEquals("The next methodCall could not start before the previous endTime. Previous endTime is 11131 and next beginTime is 11125",
                         e.getMessage());
        }
    }

    @Test
    public void testGetInvalid()
    {
        try
        {
            ExecutionFlowBuilder.create(11111L).get();
            fail("Should not pass !");
        } catch (ExecutionFlowInvalidExeption e)
        {
            assertEquals("You should define at least one sub method call", e.getMessage());
        }
    }
}
