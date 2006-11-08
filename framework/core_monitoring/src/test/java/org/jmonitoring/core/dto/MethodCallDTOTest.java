package org.jmonitoring.core.dto;

import junit.framework.TestCase;

public class MethodCallDTOTest extends TestCase
{

    private MethodCallDTO buildMethodCall()
    {
        MethodCallDTO tParent = new MethodCallDTO();
        MethodCallDTO [] tChildren = new MethodCallDTO[3]; 
        tChildren[0] = new MethodCallDTO();
        tChildren[0].setClassName("1");
        tChildren[1] = new MethodCallDTO();
        tChildren[1].setClassName("2");
        tChildren[2] = new MethodCallDTO();
        tChildren[2].setClassName("3");
        tParent.setChildren(tChildren);
        return tParent;
    }
    
    public void testRemoveChild()
    {
        MethodCallDTO tParent=buildMethodCall();
        tParent.removeChild(0);
        assertEquals(2, tParent.getChildren().length);
        assertEquals("2", tParent.getChild(0).getClassName());
        assertEquals("3", tParent.getChild(1).getClassName());
        
        tParent=buildMethodCall();
        tParent.removeChild(1);
        assertEquals(2, tParent.getChildren().length);
        assertEquals("1", tParent.getChild(0).getClassName());
        assertEquals("3", tParent.getChild(1).getClassName());
        
        tParent=buildMethodCall();
        tParent.removeChild(2);
        assertEquals(2, tParent.getChildren().length);
        assertEquals("1", tParent.getChild(0).getClassName());
        assertEquals("2", tParent.getChild(1).getClassName());
    }

}
