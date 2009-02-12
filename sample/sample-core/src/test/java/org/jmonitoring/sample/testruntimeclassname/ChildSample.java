package org.jmonitoring.sample.testruntimeclassname;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChildSample extends AbstractSample
{
    @Override
    public void methodWithOverride()
    {
        Logger tLog = LoggerFactory.getLogger(AbstractSample.class);
        tLog.debug("Message bidon");
    }

}
