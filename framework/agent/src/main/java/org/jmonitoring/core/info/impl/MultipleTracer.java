package org.jmonitoring.core.info.impl;

import org.jmonitoring.core.info.IParamaterTracer;

/**
 * This class allow to chain tracer.
 * 
 * @author pke
 */
public class MultipleTracer implements IParamaterTracer
{

    private IParamaterTracer[] mTracers;

    public MultipleTracer(IParamaterTracer[] tTracers)
    {
        mTracers = tTracers;
    }

    public String convertToString(Object pTarget, Object[] pParameterObjects)
    {
        StringBuffer tBuffer = new StringBuffer();
        for (int i = 0; i < mTracers.length; i++)
        {
            if (mTracers[i] != null)
            {
                tBuffer.append(mTracers[i].convertToString(pTarget, pParameterObjects)).append("\n");
            }
        }
        return tBuffer.toString();
    }

}
