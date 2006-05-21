package org.jmonitoring.core.common;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

public class UnknownFlowException extends Exception
{
    private static final long serialVersionUID = -3016354090452894479L;

    public UnknownFlowException()
    {
    }

    public UnknownFlowException( String pMsg)
    {
        super(pMsg);
    }
    
}
