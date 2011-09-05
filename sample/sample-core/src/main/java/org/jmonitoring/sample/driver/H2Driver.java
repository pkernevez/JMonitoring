package org.jmonitoring.sample.driver;

import org.jmonitoring.agent.sql.GenericDriver;


public class H2Driver extends GenericDriver {

    public H2Driver()
    {
        super(new org.h2.Driver());
    }

    static {
        GenericDriver.registerDriver(new H2Driver());
    }
}
