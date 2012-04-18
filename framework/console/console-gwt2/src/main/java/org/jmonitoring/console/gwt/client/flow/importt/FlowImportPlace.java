package org.jmonitoring.console.gwt.client.flow.importt;

import org.jmonitoring.console.gwt.client.ClientFactory;
import org.jmonitoring.console.gwt.client.JMonitoringActivityMapper.ActivityAware;
import org.jmonitoring.console.gwt.client.common.AbstractPlaceTokenizer;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class FlowImportPlace extends Place implements ActivityAware
{

    @Prefix("flowimport")
    public static class FlowImportTokenizer extends AbstractPlaceTokenizer implements PlaceTokenizer<FlowImportPlace>
    {

        public FlowImportPlace getPlace(String pToken)
        {
            return new FlowImportPlace();
        }

        public String getToken(FlowImportPlace pPlace)
        {
            return getToken();
        }

    }

    public FlowImportPlace()
    {
    }

    public Activity getActivity(ClientFactory pClientFactory)
    {
        return new FlowImportActivity(this, pClientFactory);
    }
}