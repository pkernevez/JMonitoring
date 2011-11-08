package org.jmonitoring.console.gwt.server.flow;

public class Distribution
{
    public long numberOfOccurence;

    public long duration;

    public Distribution()
    {
    }

    public Distribution(String pNbOccurence, String pDuration)
    {
    }

    public Distribution(long pNbOccurence, long pDuration)
    {
        numberOfOccurence = pNbOccurence;
        duration = pDuration;
    }
}
