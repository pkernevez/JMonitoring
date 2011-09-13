package org.jmonitoring.core.domain;

import junit.framework.TestCase;

import org.junit.Test;

public class MethodCallPOTest extends TestCase
{

    @Test
    public void testSetReturnValue()
    {
        StringBuilder tBuffer =new StringBuilder();
        for (int i=0;i<MethodCallPO.MAX_STRING_SIZE;i++){
            tBuffer.append("A");
        }
        String tBigString=tBuffer.toString();
        MethodCallPO tMeth = new MethodCallPO();
        tMeth.setReturnValue(tBigString);
        assertSame(tBigString, tMeth.getReturnValue());
        tBuffer.append("B");
        tMeth.setReturnValue(tBuffer.toString());
        assertEquals(MethodCallPO.MAX_STRING_SIZE, tMeth.getReturnValue().length());
        tMeth.setReturnValue(null);
        assertNull(tMeth.getReturnValue());
    }

}
