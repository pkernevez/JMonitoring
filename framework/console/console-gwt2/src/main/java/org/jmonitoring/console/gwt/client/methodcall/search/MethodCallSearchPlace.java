package org.jmonitoring.console.gwt.client.methodcall.search;

import org.jmonitoring.console.gwt.client.ClientFactory;
import org.jmonitoring.console.gwt.client.JMonitoringActivityMapper.ActivityAware;
import org.jmonitoring.console.gwt.client.common.AbstractPlaceTokenizer;
import org.jmonitoring.console.gwt.shared.method.MethodCallSearchCriterion;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class MethodCallSearchPlace extends Place implements ActivityAware
{

    private MethodCallSearchCriterion criterion;

    @Prefix("methodcallsearch")
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
            return getToken(pPlace.criterion.getClassName(), pPlace.criterion.getMethodName(),
                            pPlace.criterion.getMinDuration(), pPlace.criterion.getMaxDuration());
        }

    }

    public MethodCallSearchPlace(String pClassName, String pMethodName, String pMinDuration, String pMaxDuration)
    {
        criterion = new MethodCallSearchCriterion(pClassName, pMaxDuration, pMinDuration, pMaxDuration);
    }

    public MethodCallSearchPlace(MethodCallSearchCriterion pCriterion)
    {
        criterion = pCriterion;
    }

    public Activity getActivity(ClientFactory pClientFactory)
    {
        return new MethodCallSearchActivity(this, pClientFactory);
    }

    public MethodCallSearchCriterion getCriterion()
    {
        return criterion;
    }

}