package org.jmonitoring.console.gwt.shared.method.treesearch;

import junit.framework.TestCase;

import org.junit.Test;

public class ClassDTOTest extends TestCase
{

    @Test
    public void testEquals()
    {
        ClassDTO tRef = new ClassDTO(null, "toto");
        assertEquals(tRef, new ClassDTO(null, "toto"));
        assertEquals(tRef, new ClassDTO(new PackageDTO(), "toto"));
        assertFalse(tRef.equals(new ClassDTO(null, "titi")));
        tRef.getMethods().add(new MethodDTO(tRef, "yop", 3));
        assertEquals(tRef, new ClassDTO(null, "toto"));
    }

    @Test
    public void testHashCode()
    {
        ClassDTO tRef = new ClassDTO(null, "toto");
        assertEquals(tRef.hashCode(), new ClassDTO(null, "toto").hashCode());
        assertEquals(tRef.hashCode(), new ClassDTO(new PackageDTO(), "toto").hashCode());
        tRef.getMethods().add(new MethodDTO(tRef, "yop", 3));
        assertEquals(tRef.hashCode(), new ClassDTO(null, "toto").hashCode());
    }

}
