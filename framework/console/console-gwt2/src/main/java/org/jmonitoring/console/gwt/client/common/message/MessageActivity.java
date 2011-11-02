package org.jmonitoring.console.gwt.client.common.message;

import org.jmonitoring.console.gwt.client.ClientFactory;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

class MessageActivity extends AbstractActivity
{
    private final MessagePlace place;

    public MessageActivity(MessagePlace pFlowDetailPlace)
    {
        place = pFlowDetailPlace;
    }

    public void start(final AcceptsOneWidget panel, EventBus eventBus)
    {
        Message tMsg = ClientFactory.getInstance().getMessage();
        tMsg.setMessage(place.getMessage());
        panel.setWidget(tMsg);
    }

}