package org.jmonitoring.core.info;

public class ToStringParametersTracer implements IParamaterTracer
{

    public String convertToString(Object[] pParameterObjects)
    {
        StringBuffer tBuffer = new StringBuffer();
        if (pParameterObjects != null)
        {
            boolean tFistTime = true;
            tBuffer.append("[");
            for (int i = 0; i < pParameterObjects.length; i++)
            {
                if (!tFistTime)
                {
                    tBuffer.append(", ");
                }
                tBuffer.append("" + pParameterObjects[i]);
                tFistTime = false;
            }
            tBuffer.append("]");
        }
        return tBuffer.toString();
    }
}
