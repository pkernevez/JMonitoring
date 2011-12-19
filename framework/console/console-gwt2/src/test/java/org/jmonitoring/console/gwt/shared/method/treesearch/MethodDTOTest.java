package org.jmonitoring.console.gwt.shared.method.treesearch;

import junit.framework.TestCase;

import org.junit.Test;

public class MethodDTOTest extends TestCase
{

    @Test
    public void testGetFullClassName()
    {
        TreeBuilder tBuilder = new TreeBuilder();
        tBuilder.addMethod("com.test.MaClass.run", 10);
        PackageDTO tCom = tBuilder.getRoot().getSubPackages().get(0);
        PackageDTO tTest = tCom.getSubPackages().get(0);
        ClassDTO tMaClass = tTest.getClasses().get(0);
        MethodDTO tRun = tMaClass.getMethods().get(0);
        assertEquals("com.test.MaClass", tRun.getFullClassName());
        assertEquals("run", tRun.getName());
    }
}
