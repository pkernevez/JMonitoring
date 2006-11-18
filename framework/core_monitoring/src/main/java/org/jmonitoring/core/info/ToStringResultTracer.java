package org.jmonitoring.core.info;

public class ToStringResultTracer implements IResultTracer
{

    public String convertToString(Object pResultObject)
    {
        return (pResultObject == null ? "" : pResultObject.toString());
    }
}
