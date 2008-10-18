package org.jmonitoring.core.info;

public interface IResultTracer {

    /**
     * Convert an object to String.
     * 
     * @param pTarget The taget instance on which the Method was called.
     * @param pResultObject The object return by the intercepted method.
     * 
     * @return The object convert to String.
     */
    CharSequence convertToString(Object pTarget, Object pResultObject);

}
