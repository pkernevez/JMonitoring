package org.jmonitoring.core.info;

public class DefaultExceptionTracer implements IThrowableTracer
{

    public String convertToString(Throwable pException)
    {
        return pException.getMessage();
    }

}
