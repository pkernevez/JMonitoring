package org.jmonitoring.console.gwt.client.flow.delete;

import org.jmonitoring.console.gwt.client.ClientFactory;
import org.jmonitoring.console.gwt.client.JMonitoringActivityMapper.ActivityAware;
import org.jmonitoring.console.gwt.client.common.AbstractPlaceTokenizer;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class FlowDeletePlace extends Place implements ActivityAware
{

    @Prefix("flowdelete")
    public static class FlowDeleteTokenizer extends AbstractPlaceTokenizer implements PlaceTokenizer<FlowDeletePlace>
    {

        public FlowDeletePlace getPlace(String pToken)
        {
            return new FlowDeletePlace();
        }

        public String getToken(FlowDeletePlace pPlace)
        {
            return getToken();
        }

    }

    public FlowDeletePlace()
    {
    }

    public Activity getActivity(ClientFactory pClientFactory)
    {
        return new FlowDeleteActivity(this, pClientFactory);
    }
}