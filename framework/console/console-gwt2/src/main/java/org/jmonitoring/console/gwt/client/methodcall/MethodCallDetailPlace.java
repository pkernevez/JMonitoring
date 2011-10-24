package org.jmonitoring.console.gwt.client.methodcall;

import org.jmonitoring.console.gwt.client.ClientFactory;
import org.jmonitoring.console.gwt.client.JMonitoringActivityMapper.ActivityAware;
import org.jmonitoring.console.gwt.client.common.AbstractPlaceTokenizer;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class MethodCallDetailPlace extends Place implements ActivityAware
{

    int flowId;

    int position;

    @Prefix("methodcalldetail")
    public static class MethodCallDetailTokenizer extends AbstractPlaceTokenizer implements
                    PlaceTokenizer<MethodCallDetailPlace>
    {

        public MethodCallDetailPlace getPlace(String pToken)
        {
            tokenize(pToken);
            int tFlowId = Integer.parseInt(nextToken());
            int tPosition = Integer.parseInt(nextToken());
            return new MethodCallDetailPlace(tFlowId, tPosition);
        }

        public String getToken(MethodCallDetailPlace pPlace)
        {
            return getToken(String.valueOf(pPlace.flowId), String.valueOf(pPlace.position));
        }

    }

    public MethodCallDetailPlace(int pFlowId, int pPosition)
    {
        flowId = pFlowId;
        position = pPosition;
    }

    public Activity getActivity(ClientFactory pClientFactory)
    {
        return new MethodCallDetailActivity(this, pClientFactory);
    }

}