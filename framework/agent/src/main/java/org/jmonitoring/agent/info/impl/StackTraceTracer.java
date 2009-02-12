package org.jmonitoring.agent.info.impl;

import org.jmonitoring.core.info.IParamaterTracer;
import org.jmonitoring.core.info.IResultTracer;

/**
 * This class allow to trace the current StackTrace of the call.
 * 
 * @author pke
 * 
 */
public class StackTraceTracer implements IParamaterTracer, IResultTracer {

    public CharSequence convertToString(Object pTarget, Object[] pParameterObjects) {
        return getStackTrace();
    }

    public CharSequence convertToString(Object pTarget, Object pResultObject) {
        return getStackTrace();
    }

    CharSequence getStackTrace() {
        Throwable tException = new Throwable();
        StackTraceElement[] tElements = tException.getStackTrace();
        StringBuilder tBuffer = new StringBuilder();
        tBuffer.append("Stacktrace for this call:\n");
        for (int i = 0; i < tElements.length; i++) {
            tBuffer.append("    at ").append(tElements[i]).append("\n");
        }
        return tBuffer;
    }

}
