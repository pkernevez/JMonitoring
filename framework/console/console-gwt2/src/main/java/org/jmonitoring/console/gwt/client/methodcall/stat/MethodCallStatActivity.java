package org.jmonitoring.console.gwt.client.methodcall.stat;

import org.jmonitoring.console.gwt.client.ClientFactory;
import org.jmonitoring.console.gwt.client.common.GwtRemoteService;
import org.jmonitoring.console.gwt.client.common.GwtRemoteServiceAsync;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class MethodCallStatActivity extends AbstractActivity
{
    private final MethodCallStatPlace place;

    private final ClientFactory clientFactory;

    GwtRemoteServiceAsync service = GWT.create(GwtRemoteService.class);

    public MethodCallStatActivity(MethodCallStatPlace pMethodCallDetailPlace, ClientFactory pClientFactory)
    {
        place = pMethodCallDetailPlace;
        clientFactory = pClientFactory;
    }

    public void start(final AcceptsOneWidget panel, EventBus eventBus)
    {
        // service.loadMethodCall(place.flowId, place.position, new JMonitoringAsyncCallBack<MethodCallDTO>()
        // {
        //
        // public void onSuccess(MethodCallDTO pResult)
        // {
        MethodCallStat tMethodCallStat = clientFactory.getMethodCallStat();
        panel.setWidget(tMethodCallStat.setPresenter(MethodCallStatActivity.this));
        // }
        //
        // });
    }
}