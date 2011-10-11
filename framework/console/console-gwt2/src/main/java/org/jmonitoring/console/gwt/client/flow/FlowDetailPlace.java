package org.jmonitoring.console.gwt.client.flow;

import org.jmonitoring.console.gwt.client.JMonitoringActivityMapper.ActivityAware;
import org.jmonitoring.console.gwt.client.JMonitoringClientFactory;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class FlowDetailPlace extends Place implements ActivityAware {

    @Prefix("flowdetail")
    public static class FlowDetailTokenizer implements PlaceTokenizer<FlowDetailPlace> {

        public FlowDetailPlace getPlace(String token) {
            return new FlowDetailPlace();
        }

        public String getToken(FlowDetailPlace place) {
            return "";
        }

    }

    public Activity getActivity(JMonitoringClientFactory pClientFactory)
    {
        return new FlowDetailActivity(this, pClientFactory);
    }
    
}