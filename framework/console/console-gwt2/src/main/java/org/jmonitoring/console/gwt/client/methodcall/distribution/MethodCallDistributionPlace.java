package org.jmonitoring.console.gwt.client.methodcall.distribution;

import org.jmonitoring.console.gwt.client.ClientFactory;
import org.jmonitoring.console.gwt.client.JMonitoringActivityMapper.ActivityAware;
import org.jmonitoring.console.gwt.client.common.AbstractPlaceTokenizer;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class MethodCallDistributionPlace extends Place implements ActivityAware
{

    int flowId;

    int position;

    long interval;

    @Prefix("methodcalldistribution")
    public static class MethodCallDistributionTokenizer extends AbstractPlaceTokenizer implements
                    PlaceTokenizer<MethodCallDistributionPlace>
    {

        public MethodCallDistributionPlace getPlace(String pToken)
        {
            tokenize(pToken);
            int tFlowId = Integer.parseInt(nextToken());
            int tPosition = Integer.parseInt(nextToken());
            int tInterval = Integer.parseInt(nextToken());
            return new MethodCallDistributionPlace(tFlowId, tPosition, tInterval);
        }

        public String getToken(MethodCallDistributionPlace pPlace)
        {
            return getToken(String.valueOf(pPlace.flowId), String.valueOf(pPlace.position),
                            String.valueOf(pPlace.interval));
        }

    }

    public MethodCallDistributionPlace(int pFlowId, int pPosition)
    {
        this(pFlowId, pPosition, -1);
    }

    public MethodCallDistributionPlace(String pFlowId, String pPosition)
    {
        this(Integer.parseInt(pFlowId), Integer.parseInt(pPosition), -1);
    }

    public MethodCallDistributionPlace(int pFlowId, int pPosition, int pGapDuration)
    {
        flowId = pFlowId;
        position = pPosition;
        interval = pGapDuration;
    }

    public Activity getActivity(ClientFactory pClientFactory)
    {
        return new MethodCallDistributionActivity(this, pClientFactory);
    }

}