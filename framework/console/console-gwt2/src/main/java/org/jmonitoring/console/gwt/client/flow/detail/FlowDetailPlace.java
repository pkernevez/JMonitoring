package org.jmonitoring.console.gwt.client.flow.detail;

import org.jmonitoring.console.gwt.client.ClientFactory;
import org.jmonitoring.console.gwt.client.JMonitoringActivityMapper.ActivityAware;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class FlowDetailPlace extends Place implements ActivityAware
{

    private int id;

    @Prefix("flowdetail")
    public static class FlowDetailTokenizer implements PlaceTokenizer<FlowDetailPlace>
    {

        public FlowDetailPlace getPlace(String pToken)
        {
            int tId = Integer.parseInt(pToken);
            return new FlowDetailPlace(tId);
        }

        public String getToken(FlowDetailPlace pPlace)
        {
            return String.valueOf(pPlace.id);
        }

    }

    public FlowDetailPlace(int pId)
    {
        id = pId;
    }

    public Activity getActivity(ClientFactory pClientFactory)
    {
        return new FlowDetailActivity(this, pClientFactory);
    }

    public int getId()
    {
        return id;
    }

}