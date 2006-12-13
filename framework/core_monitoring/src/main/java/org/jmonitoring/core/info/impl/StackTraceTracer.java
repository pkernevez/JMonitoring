package org.jmonitoring.core.info.impl;

import org.jmonitoring.core.info.IParamaterTracer;
import org.jmonitoring.core.info.IResultTracer;

/**
 * This class allow to trace the current StackTrace of the call.
 * 
 * @author pke
 * 
 */
public class StackTraceTracer implements IParamaterTracer, IResultTracer
{

    public String convertToString(Object pTarget, Object[] pParameterObjects)
    {
        return getStackTrace();
    }

    public String convertToString(Object pResultObject)
    {
        return getStackTrace();
    }

    String getStackTrace()
    {
        Throwable tException = new Throwable();
        StackTraceElement[] tElements = tException.getStackTrace();
        StringBuffer tBuffer = new StringBuffer();
        tBuffer.append("Stacktrace for this call:\n");
        for (int i = 0; i < tElements.length; i++)
        {
            tBuffer.append("    at ").append(tElements[i]).append("\n");
        }
        return tBuffer.toString();
    }

}
