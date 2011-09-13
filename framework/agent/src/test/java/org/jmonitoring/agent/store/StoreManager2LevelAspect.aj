package org.jmonitoring.agent.store;

import org.jmonitoring.agent.core.PerformanceAspect;

public aspect StoreManager2LevelAspect extends PerformanceAspect
{
    private static Filter sFilter;

    private static StoreManager2LevelAspect singleton;

    public pointcut executionToLog() : execution(private *  org.jmonitoring.agent.store.StoreManagerTest.callWithFilterOk2Level*());

    public StoreManager2LevelAspect()
    {
        super();
        // By default aspect are singleton
        singleton = this;
        filter = sFilter;
    }

    public static void setFilter(Filter pFilter)
    {
        StoreManager2LevelAspect.sFilter = pFilter;
        if (singleton != null)
        {
            singleton.filter = sFilter;
        }
    }

}
