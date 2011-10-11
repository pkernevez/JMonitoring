package org.jmonitoring.console.gwt.client;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;

public class JMonitoringActivityMapper implements ActivityMapper
{
    private final JMonitoringClientFactory clientFactory;

    public JMonitoringActivityMapper(JMonitoringClientFactory pClientFactory)
    {
        clientFactory = pClientFactory;
    }

    public Activity getActivity(Place place)
    {
        return ((ActivityAware) place).getActivity(clientFactory);
    }

    public interface ActivityAware
    {
        Activity getActivity(JMonitoringClientFactory pClientFactory);
    }
}
