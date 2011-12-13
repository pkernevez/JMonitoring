package org.jmonitoring.console.gwt.client.methodcall.treesearch;

import org.jmonitoring.console.gwt.client.ClientFactory;
import org.jmonitoring.console.gwt.client.JMonitoringActivityMapper.ActivityAware;
import org.jmonitoring.console.gwt.client.common.AbstractPlaceTokenizer;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class MethodCallTreeSearchPlace extends Place implements ActivityAware
{

    @Prefix("methodcalltreesearch")
    public static class MethodCallTreeSearchTokenizer extends AbstractPlaceTokenizer implements
                    PlaceTokenizer<MethodCallTreeSearchPlace>
    {

        public MethodCallTreeSearchPlace getPlace(String pToken)
        {
            return new MethodCallTreeSearchPlace();
        }

        public String getToken(MethodCallTreeSearchPlace pPlace)
        {
            return "";
        }

    }

    public MethodCallTreeSearchPlace()
    {
    }

    public Activity getActivity(ClientFactory pClientFactory)
    {
        return new MethodCallTreeSearchActivity(this, pClientFactory);
    }

}