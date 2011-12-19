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

    String fullClassName;

    String methodName;

    long interval;

    @Prefix("methodcalldistribution")
    public static class MethodCallDistributionTokenizer extends AbstractPlaceTokenizer implements
                    PlaceTokenizer<MethodCallDistributionPlace>
    {

        public MethodCallDistributionPlace getPlace(String pToken)
        {
            tokenize(pToken);
            String tFullClassName = getParam("class");
            String tMethodName = getParam("method");
            long tInterval = getParamLong("interval");
            return new MethodCallDistributionPlace(tFullClassName, tMethodName, tInterval);
        }

        public String getToken(MethodCallDistributionPlace pPlace)
        {
            addParam("class", pPlace.fullClassName);
            addParam("method", pPlace.methodName);
            addParamLong("interval", pPlace.interval);
            return getToken();
        }

    }

    public MethodCallDistributionPlace(String pFullClassName, String pMethodName)
    {
        this(pFullClassName, pMethodName, -1);
    }

    public MethodCallDistributionPlace(String pFullClassName, String pMethodName, long pGapDuration)
    {
        fullClassName = pFullClassName;
        methodName = pMethodName;
        interval = pGapDuration;
    }

    public Activity getActivity(ClientFactory pClientFactory)
    {
        return new MethodCallDistributionActivity(this, pClientFactory);
    }

}