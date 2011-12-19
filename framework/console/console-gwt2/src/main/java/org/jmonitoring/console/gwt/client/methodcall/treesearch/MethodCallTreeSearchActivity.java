package org.jmonitoring.console.gwt.client.methodcall.treesearch;

import org.jmonitoring.console.gwt.client.ClientFactory;
import org.jmonitoring.console.gwt.client.common.GwtRemoteService;
import org.jmonitoring.console.gwt.client.common.GwtRemoteServiceAsync;
import org.jmonitoring.console.gwt.client.common.JMonitoringAsyncCallBack;
import org.jmonitoring.console.gwt.client.methodcall.distribution.MethodCallDistributionPlace;
import org.jmonitoring.console.gwt.shared.method.treesearch.PackageDTO;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class MethodCallTreeSearchActivity extends AbstractActivity
{
    private MethodCallTreeSearchPlace place;

    private final ClientFactory clientFactory;

    GwtRemoteServiceAsync service = GWT.create(GwtRemoteService.class);

    public MethodCallTreeSearchActivity(MethodCallTreeSearchPlace pPlace, ClientFactory pClientFactory)
    {
        place = pPlace;
        clientFactory = pClientFactory;
    }

    public void start(final AcceptsOneWidget pPanel, EventBus pEventBus)
    {
        JMonitoringAsyncCallBack<PackageDTO> tCallback = new JMonitoringAsyncCallBack<PackageDTO>()
        {
            public void onSuccess(PackageDTO pResult)
            {
                MethodCallTreeSearch tView = clientFactory.getMethodCallTreeSearch();
                tView.buildRoot(pResult);
                tView.tree.setAnimationEnabled(true);
                pPanel.setWidget(tView.setPresenter(MethodCallTreeSearchActivity.this));
            }
        };
        service.loadMethodCallTreeSearch(tCallback);
    }

    public void goToDistribution(String pFullClassName, String pMethod)
    {
        ClientFactory.goTo(new MethodCallDistributionPlace(pFullClassName, pMethod));

    }

}