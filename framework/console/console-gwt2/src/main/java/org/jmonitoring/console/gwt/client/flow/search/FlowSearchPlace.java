package org.jmonitoring.console.gwt.client.flow.search;

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
            tPlace.thread = getParam("thread");
            tPlace.minDuration = getParam("minDuration");
            tPlace.firstMeasureClassName = getParam("className");
            tPlace.firstMeasureMethodName = getParam("methodName");
            tPlace.beginDate = getParam("beginDate");
            return tPlace;
        }

        public String getToken(FlowSearchPlace place)
        {
            addParam("thread", place.thread);
            addParam("minDuration", place.minDuration);
            addParam("className", place.firstMeasureClassName);
            addParam("methodName", place.firstMeasureMethodName);
            addParam("beginDate", place.beginDate);
            return getToken();
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