package org.jmonitoring.agent.store;

import org.jmonitoring.agent.core.PerformanceAspect;

public aspect StoreManager1LevelAspect extends PerformanceAspect
{
    private static Filter sFilter;

    private static StoreManager1LevelAspect singleton;

    public pointcut executionToLog() : execution(private *  org.jmonitoring.agent.store.StoreManagerTest.callWithFilterOk1Level*());

    public StoreManager1LevelAspect()
    {
        super();
        // By default aspect are singleton
        singleton = this;
        filter = sFilter;
    }

    public static void setFilter(Filter pFilter)
    {
        StoreManager1LevelAspect.sFilter = pFilter;
        if (singleton != null)
        {
            singleton.filter = sFilter;
        }
    }

}
