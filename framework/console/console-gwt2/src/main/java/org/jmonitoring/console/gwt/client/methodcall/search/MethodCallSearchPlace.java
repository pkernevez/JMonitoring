package org.jmonitoring.console.gwt.client.methodcall.search;

import org.jmonitoring.console.gwt.client.ClientFactory;
import org.jmonitoring.console.gwt.client.JMonitoringActivityMapper.ActivityAware;
import org.jmonitoring.console.gwt.client.common.AbstractPlaceTokenizer;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class MethodCallSearchPlace extends Place implements ActivityAware
{

    String className;

    String methodName;

    long minDuration;

    long maxDuration;

    @Prefix("methodcallstat")
    public static class MethodCallSearchTokenizer extends AbstractPlaceTokenizer implements
                    PlaceTokenizer<MethodCallSearchPlace>
    {

        public MethodCallSearchPlace getPlace(String pToken)
        {
            tokenize(pToken);
            return new MethodCallSearchPlace(nextToken(), nextToken(), nextToken(), nextToken());
        }

        public String getToken(MethodCallSearchPlace pPlace)
        {
            return getToken(pPlace.className, pPlace.methodName, String.valueOf(pPlace.minDuration),
                            String.valueOf(pPlace.maxDuration));
        }

    }

    public MethodCallSearchPlace(String pClassName, String pMethodName, String pMinDuration, String pMaxDuration)
    {
        className = pClassName;
        methodName = pMethodName;
        minDuration = Long.parseLong(pMinDuration);
        maxDuration = Long.parseLong(pMaxDuration);
    }

    public Activity getActivity(ClientFactory pClientFactory)
    {
        return new MethodCallSearchActivity(this, pClientFactory);
    }

}