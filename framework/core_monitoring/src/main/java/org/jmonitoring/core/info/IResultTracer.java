package org.jmonitoring.core.info;

public interface IResultTracer
{

    /** 
     * Convert an object to String. 
     * @param pResultObject The object return by the intercepted method.
     * @return The object convert to String.
     */
    String convertToString(Object pResultObject);
    
}
