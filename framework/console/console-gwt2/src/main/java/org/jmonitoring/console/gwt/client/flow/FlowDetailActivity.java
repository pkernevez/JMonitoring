package org.jmonitoring.console.gwt.client.flow;

import org.jmonitoring.console.gwt.client.ClientFactory;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

class FlowDetailActivity extends AbstractActivity
{
    private final FlowDetailPlace place;

    private final ClientFactory clientFactory;

    public FlowDetailActivity(FlowDetailPlace pFlowDetailPlace, ClientFactory pClientFactory)
    {
        place = pFlowDetailPlace;
        clientFactory = pClientFactory;
    }

    public void start(AcceptsOneWidget panel, EventBus eventBus)
    {
        panel.setWidget(new FlowDetail());
    }
}