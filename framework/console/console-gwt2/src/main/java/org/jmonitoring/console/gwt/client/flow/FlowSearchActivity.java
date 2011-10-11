package org.jmonitoring.console.gwt.client.flow;

import org.jmonitoring.console.gwt.client.JMonitoringClientFactory;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

class FlowSearchActivity extends AbstractActivity
{
   private final FlowSearchPlace place;
   private final JMonitoringClientFactory clientFactory;
    
    public FlowSearchActivity(FlowSearchPlace pPlace, JMonitoringClientFactory pClientFactory)
    {
        place=pPlace;
        clientFactory=pClientFactory;
    }

    public void start(AcceptsOneWidget panel, EventBus eventBus)
    {
        
        panel.setWidget(new FlowSearch().setPresenter(this));
    }
}