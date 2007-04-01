package org.jmonitoring.core.configuration;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class ConfigurationException extends RuntimeException
{
    private static final long serialVersionUID = 6288797068969932514L;

    public ConfigurationException()
    {
        super();
    }

    public ConfigurationException(String pMessage, Throwable pCause)
    {
        super(pMessage, pCause);
    }

    public ConfigurationException(String pMessage)
    {
        super(pMessage);
    }

    public ConfigurationException(Throwable pCause)
    {
        super(pCause);
    }

}
