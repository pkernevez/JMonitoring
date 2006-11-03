package org.jmonitoring.core.common;

public class DataBaseException extends MeasureException
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public DataBaseException()
    {
        super();
    }

    public DataBaseException(String pMessage, Throwable pOriginalException)
    {
        super(pMessage, pOriginalException);
    }

    public DataBaseException(String pMessage)
    {
        super(pMessage);
    }

}
