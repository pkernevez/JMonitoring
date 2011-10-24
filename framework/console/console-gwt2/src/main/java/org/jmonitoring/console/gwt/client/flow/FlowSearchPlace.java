package org.jmonitoring.console.gwt.client.flow;

import org.jmonitoring.console.gwt.client.ClientFactory;
import org.jmonitoring.console.gwt.client.JMonitoringActivityMapper.ActivityAware;
import org.jmonitoring.console.gwt.client.common.AbstractPlaceTokenizer;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class FlowSearchPlace extends Place implements ActivityAware
{

    String thread;

    String minDuration;

    String firstMeasureClassName;

    String firstMeasureMethodName;

    String beginDate;

    @Prefix("flowsearch")
    public static class FlowSearchTokenizer extends AbstractPlaceTokenizer implements PlaceTokenizer<FlowSearchPlace>
    {

        public FlowSearchPlace getPlace(String pToken)
        {
            FlowSearchPlace tPlace = new FlowSearchPlace();
            tokenize(pToken);
            tPlace.thread = nextToken();
            tPlace.minDuration = nextToken();
            tPlace.firstMeasureClassName = nextToken();
            tPlace.firstMeasureMethodName = nextToken();
            tPlace.beginDate = nextToken();
            return tPlace;
        }

        public String getToken(FlowSearchPlace place)
        {
            return getToken(place.thread, place.minDuration, place.firstMeasureClassName, place.firstMeasureMethodName,
                            place.beginDate);
        }

    }

    public FlowSearchPlace()
    {
    }

    public FlowSearchPlace(String pThread, String pMinDuration, String pFirstMeasureClassName,
        String pFirstMeasureMethodName, String pBeginDate)
    {
        thread = pThread;
        minDuration = pMinDuration;
        firstMeasureClassName = pFirstMeasureClassName;
        firstMeasureMethodName = pFirstMeasureMethodName;
        beginDate = pBeginDate;
    }

    public Activity getActivity(ClientFactory pClientFactory)
    {
        return new FlowSearchActivity(this, pClientFactory);
    }
}