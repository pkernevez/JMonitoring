package org.jmonitoring.console.gwt.server.flow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Distribution
{
    private static Logger sLog = LoggerFactory.getLogger(Distribution.class);

    public long numberOfOccurence;

    public long duration;

    public Distribution()
    {
    }

    public Distribution(String pNbOccurence, String pDuration)
    {
    }

    public Distribution(long pNbOccurence, int pDuration)
    {
        this(pNbOccurence, (long) pDuration);
        sLog.debug("use constructor(int, int)");
    }

    public Distribution(long pNbOccurence, long pDuration)
    {
        numberOfOccurence = pNbOccurence;
        duration = Math.round(pDuration);
        sLog.debug("use constructor(long, long)");
    }

    public Distribution(long pNbOccurence, float pDuration)
    {
        this(pNbOccurence, (double) pDuration);
        sLog.debug("use constructor(long, float)");
    }

    public Distribution(long pNbOccurence, double pDuration)
    {
        this(pNbOccurence, Math.round(pDuration));
        sLog.debug("use constructor(long, double)");
    }
}
