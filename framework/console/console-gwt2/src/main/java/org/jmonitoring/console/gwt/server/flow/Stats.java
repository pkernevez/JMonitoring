package org.jmonitoring.console.gwt.server.flow;

public class Stats
{
    public long min;

    public long max;

    public double stdDeviation;

    public double average;

    public long nbOccurence;

    public Stats(long pMin, long pMax, double pAverage, long pNbOccurence)
    {
        super();
        min = pMin;
        max = pMax;
        average = pAverage;
        nbOccurence = pNbOccurence;
    }

    public Stats setStdDeviation(double pStdDeviation)
    {
        stdDeviation = pStdDeviation;
        return this;
    }

}