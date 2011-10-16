package org.jmonitoring.console.gwt.client;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;

public class JMonitoringActivityMapper implements ActivityMapper
{
    private final ClientFactory clientFactory;

    public JMonitoringActivityMapper(ClientFactory pClientFactory)
    {
        clientFactory = pClientFactory;
    }

    public Activity getActivity(Place place)
    {
        return ((ActivityAware) place).getActivity(clientFactory);
    }

    public interface ActivityAware
    {
        Activity getActivity(ClientFactory pClientFactory);
    }
}
