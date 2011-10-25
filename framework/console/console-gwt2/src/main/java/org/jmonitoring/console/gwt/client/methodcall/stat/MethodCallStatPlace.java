package org.jmonitoring.console.gwt.client.methodcall.stat;

import org.jmonitoring.console.gwt.client.ClientFactory;
import org.jmonitoring.console.gwt.client.JMonitoringActivityMapper.ActivityAware;
import org.jmonitoring.console.gwt.client.common.AbstractPlaceTokenizer;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class MethodCallStatPlace extends Place implements ActivityAware
{

    int flowId;

    int position;

    @Prefix("methodcallstat")
    public static class MethodCallStatTokenizer extends AbstractPlaceTokenizer implements
                    PlaceTokenizer<MethodCallStatPlace>
    {

        public MethodCallStatPlace getPlace(String pToken)
        {
            tokenize(pToken);
            int tFlowId = Integer.parseInt(nextToken());
            int tPosition = Integer.parseInt(nextToken());
            return new MethodCallStatPlace(tFlowId, tPosition);
        }

        public String getToken(MethodCallStatPlace pPlace)
        {
            return getToken(String.valueOf(pPlace.flowId), String.valueOf(pPlace.position));
        }

    }

    public MethodCallStatPlace(int pFlowId, int pPosition)
    {
        flowId = pFlowId;
        position = pPosition;
    }

    public Activity getActivity(ClientFactory pClientFactory)
    {
        return new MethodCallStatActivity(this, pClientFactory);
    }

}