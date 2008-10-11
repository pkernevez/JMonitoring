package org.jmonitoring.core.info;

public interface IParamaterTracer {
    /**
     * Convert an object array to String.
     * 
     * @param pTarget The taget instance on which the Method was called.
     * @param pParameterObjects The objects passed to the intercepted method.
     * 
     * @return The objects as String.
     */
    String convertToString(Object pTarget, Object[] pParameterObjects);

}
