package org.jmonitoring.agent.info.impl;

import org.jmonitoring.core.info.IParamaterTracer;

public class ToStringParametersTracer implements IParamaterTracer
{

    private int mMaxLength = -1;

    public ToStringParametersTracer()
    {
    }

    /**
     * Limit return value lenght to ensure the String lenght compatibily with SqlType.
     * 
     * @param pMaxLength The max lenght of the String.
     */
    public ToStringParametersTracer(int pMaxLength)
    {
        mMaxLength = pMaxLength;
    }

    public CharSequence convertToString(Object pTarget, Object[] pParameterObjects)
    {
        StringBuilder tBuffer = new StringBuilder();
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
        if ((mMaxLength > -1) && (tBuffer.length() > mMaxLength))
        {
            return tBuffer.substring(0, mMaxLength);
        } else
        {
            return tBuffer;
        }
    }
}
