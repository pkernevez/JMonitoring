package org.jmonitoring.console.gwt.shared.method.treesearch;

import junit.framework.TestCase;

import org.junit.Test;

public class TreeBuilderTest extends TestCase
{

    @Test
    public void testNewBuilder()
    {
        TreeBuilder tBuilder = new TreeBuilder();
        assertEquals("", tBuilder.getRoot().getName());
        assertEquals(0, tBuilder.getRoot().getClasses().size());
        assertEquals(0, tBuilder.getRoot().getSubPackages().size());
    }

    @Test
    public void testAddMethod()
    {
        TreeBuilder tBuilder = new TreeBuilder();
        tBuilder.addMethod("com.octo.TestClass.run");
        assertEquals("", tBuilder.getRoot().getName());
        assertEquals(0, tBuilder.getRoot().getClasses().size());
        assertEquals(1, tBuilder.getRoot().getSubPackages().size());
        PackageDTO tComNode = tBuilder.getRoot().getSubPackages().get(0);
        assertEquals("com", tComNode.getName());
        assertEquals(0, tComNode.getClasses().size());
        assertEquals(1, tComNode.getSubPackages().size());
        PackageDTO tOctoNode = tComNode.getSubPackages().get(0);
        assertEquals("octo", tOctoNode.getName());
        assertEquals(1, tOctoNode.getClasses().size());
        assertEquals(0, tOctoNode.getSubPackages().size());
        ClassDTO tTestClass = tOctoNode.getClasses().get(0);
        assertEquals(1, tTestClass.getMethods().size());
        assertEquals("run", tTestClass.getMethods().get(0));
    }

    @Test
    public void testAddMethodInDefaultPackage()
    {
        TreeBuilder tBuilder = new TreeBuilder();
        tBuilder.addMethod("TestClass.go");
        PackageDTO tRoot = tBuilder.getRoot();
        assertEquals("", tRoot.getName());
        assertEquals(1, tRoot.getClasses().size());
        assertEquals(0, tRoot.getSubPackages().size());
        ClassDTO tTestClass = tRoot.getClasses().get(0);
        assertEquals("TestClass", tTestClass.getName());
        assertEquals(1, tTestClass.getMethods().size());
        assertEquals("go", tTestClass.getMethods().get(0));
    }

    @Test
    public void testAddMethodSeveral()
    {
        TreeBuilder tBuilder = new TreeBuilder();
        tBuilder.addMethod("com.octo.testClass.go1");
        tBuilder.addMethod("com.Octo.TestClass.go1");
        tBuilder.addMethod("testClass.go2");
        PackageDTO tRoot = tBuilder.getRoot();
        assertEquals("", tRoot.getName());
        assertEquals(1, tRoot.getClasses().size());
        ClassDTO tRootTestClass = tRoot.getClasses().get(0);
        assertEquals("testClass", tRootTestClass.getName());
        assertEquals(1, tRootTestClass.getMethods().size());
        assertEquals("go2", tRootTestClass.getMethods().get(0));
        assertEquals(1, tRoot.getSubPackages().size());
        PackageDTO tComNode = tRoot.getSubPackages().get(0);
        assertEquals("com", tComNode.getName());
        assertEquals(0, tComNode.getClasses().size());
        assertEquals(2, tComNode.getSubPackages().size());
        int tLowerPosition = tComNode.getSubPackages().indexOf(new PackageDTO("octo"));
        PackageDTO tOctoLower = tComNode.getSubPackages().get(tLowerPosition);
        PackageDTO tOctoUpper = tComNode.getSubPackages().get(1 - tLowerPosition);
        assertEquals("octo", tOctoLower.getName());
        assertEquals("Octo", tOctoUpper.getName());
        assertEquals(1, tOctoLower.getClasses().size());
        assertEquals(0, tOctoLower.getSubPackages().size());
        assertEquals(1, tOctoUpper.getClasses().size());
        assertEquals(0, tOctoUpper.getSubPackages().size());
        ClassDTO tLowerOctoClass = tOctoLower.getClasses().get(0);
        assertEquals("testClass", tLowerOctoClass.getName());
        assertEquals(1, tLowerOctoClass.getMethods().size());
        assertEquals("go1", tLowerOctoClass.getMethods().get(0));
        ClassDTO tUpperOctoClass = tOctoUpper.getClasses().get(0);
        assertEquals("TestClass", tUpperOctoClass.getName());
        assertEquals(1, tUpperOctoClass.getMethods().size());
        assertEquals("go1", tUpperOctoClass.getMethods().get(0));
    }

    @Test
    public void testAddMethodInvalidName()
    {
        TreeBuilder tBuilder = new TreeBuilder();
        try
        {
            tBuilder.addMethod(null);
            fail("Should not pass !");
        } catch (RuntimeException e)
        {
            assertEquals(RuntimeException.class, e.getClass());
            assertEquals("Invalid name, should contains at least a class and a name : \"$classs.$method\".",
                         e.getMessage());
        }
        try
        {
            tBuilder.addMethod("");
            fail("Should not pass !");
        } catch (RuntimeException e)
        {
            assertEquals(RuntimeException.class, e.getClass());
            assertEquals("Invalid name, should contains at least a class and a name : \"$classs.$method\".",
                         e.getMessage());
        }
        try
        {
            tBuilder.addMethod("main");
            fail("Should not pass !");
        } catch (RuntimeException e)
        {
            assertEquals(RuntimeException.class, e.getClass());
            assertEquals("Invalid name, should contains at least a class and a name : \"$classs.$method\".",
                         e.getMessage());
        }
    }
}
