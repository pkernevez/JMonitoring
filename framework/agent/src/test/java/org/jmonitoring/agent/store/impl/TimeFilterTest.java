package org.jmonitoring.agent.store.impl;

import static org.junit.Assert.*;

import org.jmonitoring.core.domain.MethodCallPO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TimeFilterTest
{

    private TimeFilter timeFilter;

    private MethodCallPO parent;

    private MethodCallPO current;

    @Before
    public void createTimeFilter() throws Exception
    {
        timeFilter = new TimeFilter(5);
        parent = new MethodCallPO();
        parent.setBeginTime(10000);
        parent.setEndTime(10020);

        current = new MethodCallPO(parent, null, null, null, null);
        current.setBeginTime(10001);
    }

    @Test
    public void testKeepWith0Duration()
    {
        current.setEndTime(10001);
        assertFalse(timeFilter.keep(current));
    }

    @Test
    public void testKeepWithExactlyMinDuration()
    {
        current.setEndTime(10006);
        assertTrue(timeFilter.keep(current));
    }

    @Test
    public void testKeepWithGreaterDuration()
    {
        current.setEndTime(10007);
        assertTrue(timeFilter.keep(current));
    }

}
