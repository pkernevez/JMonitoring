package org.jmonitoring.console.gwt.client.flow.detail;

import org.jmonitoring.console.gwt.client.ClientFactory;
import org.jmonitoring.console.gwt.client.JMonitoringActivityMapper.ActivityAware;
import org.jmonitoring.console.gwt.client.common.AbstractPlaceTokenizer;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class FlowDetailPlace extends Place implements ActivityAware
{

    private int id;

    @Prefix("flowdetail")
    public static class FlowDetailTokenizer extends AbstractPlaceTokenizer implements PlaceTokenizer<FlowDetailPlace>
    {

        public FlowDetailPlace getPlace(String pToken)
        {
            tokenize(pToken);
            return new FlowDetailPlace(getParamInt("flowId"));
        }

        public String getToken(FlowDetailPlace pPlace)
        {
            return addParamInt("flowId", pPlace.id).getToken();
        }

    }

    public FlowDetailPlace(int pId)
    {
        id = pId;
    }

    public FlowDetailPlace(String pId)
    {
        id = Integer.parseInt(pId);
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