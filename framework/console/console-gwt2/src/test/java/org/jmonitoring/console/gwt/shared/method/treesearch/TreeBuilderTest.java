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
        tBuilder.addMethod("com.octo.TestClass.run", 5);
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
        MethodDTO tMethod = tTestClass.getMethods().get(0);
        assertEquals("run", tMethod.getName());
        assertEquals(5, tMethod.getNbOccurence());
    }

    @Test
    public void testAddMethodInDefaultPackage()
    {
        TreeBuilder tBuilder = new TreeBuilder();
        tBuilder.addMethod("TestClass.go", 6);
        PackageDTO tRoot = tBuilder.getRoot();
        assertEquals("", tRoot.getName());
        assertEquals(1, tRoot.getClasses().size());
        assertEquals(0, tRoot.getSubPackages().size());
        ClassDTO tTestClass = tRoot.getClasses().get(0);
        assertEquals("TestClass", tTestClass.getName());
        assertEquals(1, tTestClass.getMethods().size());
        MethodDTO tActual = tTestClass.getMethods().get(0);
        assertEquals("go", tActual.getName());
        assertEquals(6, tActual.getNbOccurence());
    }

    @Test
    public void testAddMethodSeveral()
    {
        TreeBuilder tBuilder = new TreeBuilder();
        tBuilder.addMethod("com.octo.testClass.go1", 1);
        tBuilder.addMethod("com.Octo.TestClass.go1", 2);
        tBuilder.addMethod("testClass.go2", 3);
        PackageDTO tRoot = tBuilder.getRoot();
        assertEquals("", tRoot.getName());
        assertEquals(1, tRoot.getClasses().size());
        ClassDTO tRootTestClass = tRoot.getClasses().get(0);
        assertEquals("testClass", tRootTestClass.getName());
        assertEquals(1, tRootTestClass.getMethods().size());
        MethodDTO tGo2 = tRootTestClass.getMethods().get(0);
        assertEquals("go2", tGo2.getName());
        assertEquals(3, tGo2.getNbOccurence());
        assertEquals(1, tRoot.getSubPackages().size());
        PackageDTO tComNode = tRoot.getSubPackages().get(0);
        assertEquals("com", tComNode.getName());
        assertEquals(0, tComNode.getClasses().size());
        assertEquals(2, tComNode.getSubPackages().size());
        int tLowerPosition = tComNode.getSubPackages().indexOf(new PackageDTO(null, "octo"));
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
        MethodDTO tGo1A = tLowerOctoClass.getMethods().get(0);
        assertEquals("go1", tGo1A.getName());
        assertEquals(1, tGo1A.getNbOccurence());
        ClassDTO tUpperOctoClass = tOctoUpper.getClasses().get(0);
        assertEquals("TestClass", tUpperOctoClass.getName());
        assertEquals(1, tUpperOctoClass.getMethods().size());
        MethodDTO tGo1B = tUpperOctoClass.getMethods().get(0);
        assertEquals("go1", tGo1B.getName());
        assertEquals(2, tGo1B.getNbOccurence());
    }

    @Test
    public void testAddMethodInvalidName()
    {
        TreeBuilder tBuilder = new TreeBuilder();
        try
        {
            tBuilder.addMethod(null, 1);
            fail("Should not pass !");
        } catch (RuntimeException e)
        {
            assertEquals(RuntimeException.class, e.getClass());
            assertEquals("Invalid name, should contains at least a class and a name : \"$classs.$method\".",
                         e.getMessage());
        }
        try
        {
            tBuilder.addMethod("", 3);
            fail("Should not pass !");
        } catch (RuntimeException e)
        {
            assertEquals(RuntimeException.class, e.getClass());
            assertEquals("Invalid name, should contains at least a class and a name : \"$classs.$method\".",
                         e.getMessage());
        }
        try
        {
            tBuilder.addMethod("main", 6);
            fail("Should not pass !");
        } catch (RuntimeException e)
        {
            assertEquals(RuntimeException.class, e.getClass());
            assertEquals("Invalid name, should contains at least a class and a name : \"$classs.$method\".",
                         e.getMessage());
        }
    }
}
