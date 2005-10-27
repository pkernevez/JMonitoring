package org.jmonitoring.core.measure;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * Exception réservé au bon fonctionnement du framework de mesure.
 * 
 * @author pke
 */
public class MeasureException extends RuntimeException
{

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 4049633486413838392L;

    /**
     * Default constructor.
     */
    public MeasureException()
    {
        super();
    }

    /**
     * Constructor with message. 
     * @param pMessage The message of the <code>Exception</code>.
     */
    public MeasureException(String pMessage)
    {
        super(pMessage);
    }

    /**
     * Constructor with message and original <code>Exception</code>. 
     * @param pMessage The message of the <code>Exception</code>.
     * @param pOriginalException The original <code>Exception</code>.
     */
    public MeasureException(String pMessage, Throwable pOriginalException)
    {
        super(pMessage, pOriginalException);
    }
}
