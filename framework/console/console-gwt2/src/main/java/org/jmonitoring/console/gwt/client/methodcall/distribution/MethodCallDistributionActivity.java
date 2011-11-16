package org.jmonitoring.console.gwt.client.methodcall.distribution;

import org.jmonitoring.console.gwt.client.ClientFactory;
import org.jmonitoring.console.gwt.client.common.GwtRemoteService;
import org.jmonitoring.console.gwt.client.common.GwtRemoteServiceAsync;
import org.jmonitoring.console.gwt.client.common.JMonitoringAsyncCallBack;
import org.jmonitoring.console.gwt.shared.method.MethodCallDistributionDTO;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class MethodCallDistributionActivity extends AbstractActivity
{
    private final MethodCallDistributionPlace place;

    private final ClientFactory clientFactory;

    GwtRemoteServiceAsync service = GWT.create(GwtRemoteService.class);

    public MethodCallDistributionActivity(MethodCallDistributionPlace pMethodCallDetailPlace,
        ClientFactory pClientFactory)
    {
        place = pMethodCallDetailPlace;
        clientFactory = pClientFactory;
    }

    public void start(final AcceptsOneWidget pPanel, EventBus pEventBus)
    {
        JMonitoringAsyncCallBack<MethodCallDistributionDTO> tCallback =
            new JMonitoringAsyncCallBack<MethodCallDistributionDTO>()
            {

                public void onSuccess(MethodCallDistributionDTO pResult)
                {
                    MethodCallDistribution tMethodCallStat = clientFactory.getMethodCallStat();
                    tMethodCallStat.distribution.setUrl("image.dynamic?id=" + pResult.getClassName() + "/"
                        + pResult.getMethodName() + "/10" + "&type=Distribution");
                    tMethodCallStat.gapDuration.setText(String.valueOf(place.gapDuration));
                    tMethodCallStat.fullMethodName.setText(pResult.getFullName());
                    tMethodCallStat.durationMin.setText(pResult.getMinDuration());
                    tMethodCallStat.durationAvg.setText(pResult.getAvgDuration());
                    tMethodCallStat.durationDeviance.setText(pResult.getStdDeviationDuration());
                    tMethodCallStat.durationMax.setText(pResult.getMaxDuration());
                    tMethodCallStat.nbOccurences.setText(pResult.getNbOccurences());
                    pPanel.setWidget(tMethodCallStat.setPresenter(MethodCallDistributionActivity.this));
                }

            };
        service.getDistributionAndGenerateImage(place.flowId, place.position, 10, tCallback);
    }
}