package org.jmonitoring.sample.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AbstractSample
{

    public void methodATester()
    {
        Log tLog = LogFactory.getLog(AbstractSample.class);
        tLog.debug("Message bidon");
    }
    
    public void methodWithOverride()
    {
        Log tLog = LogFactory.getLog(AbstractSample.class);
        tLog.debug("Message bidon");
    }
}
