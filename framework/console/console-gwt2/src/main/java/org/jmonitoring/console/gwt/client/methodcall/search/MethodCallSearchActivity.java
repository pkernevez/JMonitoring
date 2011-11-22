package org.jmonitoring.console.gwt.client.methodcall.search;

import org.jmonitoring.console.gwt.client.ClientFactory;
import org.jmonitoring.console.gwt.client.common.GwtRemoteService;
import org.jmonitoring.console.gwt.client.common.GwtRemoteServiceAsync;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class MethodCallSearchActivity extends AbstractActivity
{
    private final MethodCallSearchPlace place;

    private final ClientFactory clientFactory;

    GwtRemoteServiceAsync service = GWT.create(GwtRemoteService.class);

    public MethodCallSearchActivity(MethodCallSearchPlace pMethodCallDetailPlace, ClientFactory pClientFactory)
    {
        place = pMethodCallDetailPlace;
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
        pPanel.setWidget(clientFactory.getMethodCallSearch().setPresenter(MethodCallSearchActivity.this));
    }

    // public static native void exportMapMethClick() /*-{
    // $wnd.methClick =
    // $entry(@org.jmonitoring.console.gwt.client.methodcall.distribution.MethodCallDistributionActivity::methClick(Ljava/lang/String;Ljava/lang/String;II));
    // }-*/;
    //
    // /** Method use to trap clicks on the map of the flow. Used by native javascript. */
    // public static void methClick(String pClassName, String pMethodName, int pFlowId, int pPosition)
    // {
    // ClientFactory.goTo(new MethodCallDetailPlace(pFlowId, pPosition));
    // }
    //

}