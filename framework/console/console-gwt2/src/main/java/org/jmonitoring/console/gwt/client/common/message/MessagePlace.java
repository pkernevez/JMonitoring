package org.jmonitoring.console.gwt.client.common.message;

import org.jmonitoring.console.gwt.client.ClientFactory;
import org.jmonitoring.console.gwt.client.JMonitoringActivityMapper.ActivityAware;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class MessagePlace extends Place implements ActivityAware
{

    private String message;

    @Prefix("message")
    public static class MessageTokenizer implements PlaceTokenizer<MessagePlace>
    {

        public MessagePlace getPlace(String pToken)
        {
            return new MessagePlace(pToken);
        }

        public String getToken(MessagePlace pPlace)
        {
            return String.valueOf(pPlace.message);
        }

    }

    public MessagePlace(String pMessage)
    {
        message = pMessage;
    }

    public Activity getActivity(ClientFactory pClientFactory)
    {
        return new MessageActivity(this);
    }

    public String getMessage()
    {
        return message;
    }

}