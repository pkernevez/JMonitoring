package org.jmonitoring.console.gwt.client.flow;

import org.jmonitoring.console.gwt.client.JMonitoringClientFactory;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

class FlowDetailActivity extends AbstractActivity
{
    private final FlowDetailPlace place;

    private final JMonitoringClientFactory clientFactory;

    public FlowDetailActivity(FlowDetailPlace pFlowDetailPlace, JMonitoringClientFactory pClientFactory)
    {
        place = pFlowDetailPlace;
        clientFactory = pClientFactory;
    }

    public void start(AcceptsOneWidget panel, EventBus eventBus)
    {
        panel.setWidget(new FlowDetail());
    }
}