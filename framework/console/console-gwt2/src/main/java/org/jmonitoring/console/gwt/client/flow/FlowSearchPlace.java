package org.jmonitoring.console.gwt.client.flow;

import org.jmonitoring.console.gwt.client.JMonitoringActivityMapper.ActivityAware;
import org.jmonitoring.console.gwt.client.JMonitoringClientFactory;
import org.jmonitoring.console.gwt.client.common.AbstractPlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class FlowSearchPlace extends AbstractPlace implements ActivityAware
{

    String thread;

    String minDuration;

    String firstMeasureClassName;

    String firstMeasureMethodName;

    String beginDate;

    @Prefix("flowsearch")
    public static class FlowSearchTokenizer implements PlaceTokenizer<FlowSearchPlace>
    {

        public FlowSearchPlace getPlace(String pToken)
        {
            FlowSearchPlace tPlace = new FlowSearchPlace();
            tPlace.tokenize(pToken);
            tPlace.thread = tPlace.nextToken();
            tPlace.minDuration = tPlace.nextToken();
            tPlace.firstMeasureClassName = tPlace.nextToken();
            tPlace.firstMeasureMethodName = tPlace.nextToken();
            tPlace.beginDate = tPlace.nextToken();
            return tPlace;
        }

        public String getToken(FlowSearchPlace place)
        {
            return place.getToken(place.thread, place.minDuration, place.firstMeasureClassName,
                                  place.firstMeasureMethodName, place.beginDate);
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

    public Activity getActivity(JMonitoringClientFactory pClientFactory)
    {
        return new FlowSearchActivity(this, pClientFactory);
    }
}