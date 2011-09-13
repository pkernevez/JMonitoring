package org.jmonitoring.agent.store.impl;

import org.jmonitoring.agent.store.Filter;
import org.jmonitoring.core.domain.MethodCallPO;

public class TimeFilter implements Filter
{
    private long minDuration;

    /**
     * If the duration of the <code>MethoodCall</code> is less or equals than the pMinDuration, it will be kept in the
     * flow. In the other case it will be discard and will not be add to the <code>ExecutionFlow</code>.
     * 
     * @param pMinDuration The min duration that a <code>MethodCall</code> should endured to be kept.
     */
    public TimeFilter(long pMinDuration)
    {
        minDuration=pMinDuration;
    }

    public boolean keep(MethodCallPO pCurrent)
    {
        return pCurrent.getDuration() >= minDuration;
    }
}
