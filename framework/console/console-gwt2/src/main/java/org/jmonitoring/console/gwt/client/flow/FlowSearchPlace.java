package org.jmonitoring.console.gwt.client.flow;

import org.jmonitoring.console.gwt.client.JMonitoringActivityMapper.ActivityAware;
import org.jmonitoring.console.gwt.client.JMonitoringClientFactory;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class FlowSearchPlace extends Place implements ActivityAware
{

    @Prefix("flowsearch")
    public static class FlowSearchTokenizer implements PlaceTokenizer<FlowSearchPlace>
    {

        public FlowSearchPlace getPlace(String token)
        {
            return new FlowSearchPlace();
        }

        public String getToken(FlowSearchPlace place)
        {
            return "";
        }

    }

    public Activity getActivity(JMonitoringClientFactory pClientFactory)
    {
        return new FlowSearchActivity(this, pClientFactory);
    }
}