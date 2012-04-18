package org.jmonitoring.console.gwt.client.flow.delete;

import org.jmonitoring.console.gwt.client.ClientFactory;
import org.jmonitoring.console.gwt.client.common.GwtRemoteService;
import org.jmonitoring.console.gwt.client.common.GwtRemoteServiceAsync;
import org.jmonitoring.console.gwt.client.common.JMonitoringAsyncCallBack;
import org.jmonitoring.console.gwt.client.common.message.MessagePlace;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class FlowDeleteActivity extends AbstractActivity
{
    private final FlowDeletePlace place;

    private final ClientFactory clientFactory;

    GwtRemoteServiceAsync service = GWT.create(GwtRemoteService.class);

    public FlowDeleteActivity(FlowDeletePlace pFlowImportPlace, ClientFactory pClientFactory)
    {
        place = pFlowImportPlace;
        clientFactory = pClientFactory;
    }

    public void start(final AcceptsOneWidget panel, EventBus eventBus)
    {
        FlowDelete tPanel = clientFactory.getFlowDelete();
        panel.setWidget(tPanel.setPresenter(this));

    }

    public void deleteAll()
    {
        service.deleteAll(new JMonitoringAsyncCallBack<Void>()
        {
            public void onSuccess(Void pResult)
            {
                ClientFactory.goTo(new MessagePlace("All the flows have been deleted"));
            }
        });
    }
}
