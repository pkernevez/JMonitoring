package org.jmonitoring.console.gwt.client.methodcall.treesearch;

import org.jmonitoring.console.gwt.client.ClientFactory;
import org.jmonitoring.console.gwt.client.common.GwtRemoteService;
import org.jmonitoring.console.gwt.client.common.GwtRemoteServiceAsync;
import org.jmonitoring.console.gwt.shared.method.treesearch.TreeBuilder;

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

        // exportMapMethClick();
        // JMonitoringAsyncCallBack<MethodCallDistributionDTO> tCallback =
        // new JMonitoringAsyncCallBack<MethodCallDistributionDTO>()
        // {
        // public void onSuccess(MethodCallDistributionDTO pResult)
        // {
        // MethodCallDistribution tMethodCallStat = clientFactory.getMethodCallStat();
        // tMethodCallStat.distribution.setUrl("image.dynamic?id=" + pResult.getClassName() + "/"
        // + pResult.getMethodName() + "/" + pResult.getInterval() + "&type=Distribution");
        // tMethodCallStat.distributionImageMap.add(new HTML(pResult.getMap()));
        // tMethodCallStat.distribution.getElement().setAttribute("usemap", "#chart");
        // tMethodCallStat.interval.setText(String.valueOf(pResult.getInterval()));
        // tMethodCallStat.fullMethodName.setText(pResult.getFullName());
        // tMethodCallStat.durationMin.setText(pResult.getMinDuration());
        // tMethodCallStat.durationAvg.setText(pResult.getAvgDuration());
        // tMethodCallStat.durationDeviance.setText(pResult.getStdDeviationDuration());
        // tMethodCallStat.durationMax.setText(pResult.getMaxDuration());
        // tMethodCallStat.nbOccurences.setText(pResult.getNbOccurences());
        // pPanel.setWidget(tMethodCallStat.setPresenter(MethodCallSearchActivity.this));
        // tMethodCallStat.interval.setFocus(true);
        // }
        //
        // };
        // service.getDistributionAndGenerateImage(place.flowId, place.position, place.interval, tCallback);
        // Create a model for the tree.
        TreeBuilder tBuilder = new TreeBuilder();
        tBuilder.addMethod("com.toto.Test.main", 2);
        tBuilder.addMethod("org.Test.run", 4);

        MethodCallTreeSearch tView = clientFactory.getMethodCallTreeSearch();
        tView.buildRoot(tBuilder.getRoot());
        tView.tree.setAnimationEnabled(true);
        pPanel.setWidget(tView.setPresenter(MethodCallTreeSearchActivity.this));

    }

}