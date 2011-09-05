package org.jmonitoring.agent.sql;


public class H2Driver extends GenericDriver
{

    public H2Driver()
    {
        super(new org.h2.Driver());
    }

    static {
        GenericDriver.registerDriver(new H2Driver());
    }
}
