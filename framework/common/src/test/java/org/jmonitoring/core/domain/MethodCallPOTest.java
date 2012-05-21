package org.jmonitoring.core.domain;

import junit.framework.TestCase;

import org.junit.Test;

public class MethodCallPOTest extends TestCase
{

    @Test
    public void testSetReturnValue()
    {
        StringBuilder tBuffer = new StringBuilder();
        for (int i = 0; i < MethodCallPO.MAX_STRING_SIZE; i++)
        {
            tBuffer.append("A");
        }
        String tBigString = tBuffer.toString();
        MethodCallPO tMeth = new MethodCallPO();
        tMeth.setReturnValue(tBigString);
        assertSame(tBigString, tMeth.getReturnValue());
        tBuffer.append("B");
        tMeth.setReturnValue(tBuffer.toString());
        assertEquals(MethodCallPO.MAX_STRING_SIZE, tMeth.getReturnValue().length());
        tMeth.setReturnValue(null);
        assertNull(tMeth.getReturnValue());
    }

    public void testUpdateChildrenWhenCreateWithParent()
    {
        MethodCallPO tParent =
            new MethodCallPO(null, MethodCallPOTest.class.getName(), "builNewFullFlow", "GrDefault", "");

        MethodCallPO tChild =
            new MethodCallPO(tParent, MethodCallPOTest.class.getName(), "builNewFullFlow2", "GrChild1", "");
        assertNull(tParent.getParentMethodCall());
        assertEquals(1, tParent.getChildren().size());
        assertSame(tParent, tChild.getParentMethodCall());

        tParent.removeChildren(tChild);
        assertNull(tParent.getParentMethodCall());
        assertEquals(0, tParent.getChildren().size());
        assertNull(tChild.getParentMethodCall());

    }

    public void testUpdateChildrenWhenAddParent()
    {
        MethodCallPO tParent =
            new MethodCallPO(null, MethodCallPOTest.class.getName(), "builNewFullFlow", "GrDefault", "");

        MethodCallPO tChild =
            new MethodCallPO(null, MethodCallPOTest.class.getName(), "builNewFullFlow2", "GrChild1", "");

        assertNull(tParent.getParentMethodCall());
        assertEquals(0, tParent.getChildren().size());
        assertNull(tChild.getParentMethodCall());

        tChild.setParentMethodCall(tParent);
        assertNull(tParent.getParentMethodCall());
        assertEquals(1, tParent.getChildren().size());
        assertSame(tParent, tChild.getParentMethodCall());

        tChild.setParentMethodCall(null);
        tChild.setParentMethodCall(null); // On teste avec null 2 fois...
        assertNull(tParent.getParentMethodCall());
        assertEquals(0, tParent.getChildren().size());
        assertNull(tChild.getParentMethodCall());

        tChild.setParentMethodCall(tParent);
        assertNull(tParent.getParentMethodCall());
        assertEquals(1, tParent.getChildren().size());
        assertSame(tParent, tChild.getParentMethodCall());

    }

    @Test
    public void testIsFirstChild() throws Exception
    {
        MethodCallPO tRoot = new MethodCallPO();
        MethodCallPO tFirstChild = new MethodCallPO(tRoot, "class", "method", "group", "param");
        MethodCallPO tSecondChild = new MethodCallPO(tRoot, "class", "method", "group", "param");
        MethodCallPO tThirdChild = new MethodCallPO(tRoot, "class", "method", "group", "param");
        MethodCallPO tOnlyChild = new MethodCallPO(tFirstChild, "class", "method", "group", "param");

        assertTrue("The root should be consider as a first child", tRoot.isFirstChild());
        assertTrue("The first child of the siblings should be first child", tFirstChild.isFirstChild());
        assertFalse("The second child of the siblings should not be first child", tSecondChild.isFirstChild());
        assertFalse("The last child should not be consider as a first child", tThirdChild.isFirstChild());
        assertTrue("The only child of the siblings should be consider as a first child", tOnlyChild.isFirstChild());
    }

    @Test
    public void testGetPositionInSiblinWhenChildIsInList() throws Exception
    {
        MethodCallPO tRoot = new MethodCallPO();
        MethodCallPO tFirstChild = new MethodCallPO(tRoot, "class", "method", "group", "param");
        MethodCallPO tSecondChild = new MethodCallPO(tRoot, "class", "method", "group", "param");
        MethodCallPO tThirdChild = new MethodCallPO(tRoot, "class", "method", "group", "param");
        MethodCallPO tOnlyChild = new MethodCallPO(tFirstChild, "class", "method", "group", "param");

        assertEquals("The root should be in 1st position", 0, tRoot.getPositionInSiblin());
        assertEquals("The first child should be in 1st position", 0, tFirstChild.getPositionInSiblin());
        assertEquals("The first child should be in 2nd position", 1, tSecondChild.getPositionInSiblin());
        assertEquals("The third and last child should be in 3rd position", 2, tThirdChild.getPositionInSiblin());
        assertEquals("The only child should be in 1st position", 0, tOnlyChild.getPositionInSiblin());

    }

    @Test(expected = RuntimeException.class)
    @SuppressWarnings("unused")
    public void testGetPositionInSiblinWhenChildIsNotInList() throws Exception
    {
        MethodCallPO tRoot = new MethodCallPO();
        MethodCallPO tFirstChild = new MethodCallPO(tRoot, "class", "method", "group", "param");
        MethodCallPO tSecondChild = new MethodCallPO(tRoot, "class", "method", "group", "param");
        MethodCallPO tThirdChild = new MethodCallPO(tRoot, "class", "method", "group", "param");
        MethodCallPO tOnlyChild = new MethodCallPO(tFirstChild, "class", "method", "group", "param");

        new MethodCallPO(tThirdChild, "class", "meth", "grp", "prm").getPositionInSiblin();
    }
}
