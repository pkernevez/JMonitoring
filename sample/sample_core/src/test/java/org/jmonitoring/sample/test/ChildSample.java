package org.jmonitoring.sample.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ChildSample extends AbstractSample
{
    public void methodWithOverride()
    {
        Log tLog = LogFactory.getLog(AbstractSample.class);
        tLog.debug("Message bidon");
    }

}
