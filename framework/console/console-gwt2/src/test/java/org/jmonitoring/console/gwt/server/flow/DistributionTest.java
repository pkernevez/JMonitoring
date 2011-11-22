package org.jmonitoring.console.gwt.server.flow;

import junit.framework.TestCase;

import org.junit.Test;

public class DistributionTest extends TestCase
{

    @Test
    public void testDistributionMySql()
    {
        new Distribution(34L, 3);
    }

    @Test
    public void testDistributionH2()
    {
        new Distribution(34L, 3.0d);
    }

    @Test
    public void testDistributionUnkownOtherDbJustInCase()
    {
        new Distribution(34L, 3.0f);
        new Distribution("34", "3");
    }

}
