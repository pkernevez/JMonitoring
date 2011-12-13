package org.jmonitoring.console.gwt.client.methodcall.treesearch;

import org.jmonitoring.console.gwt.client.ClientFactory;
import org.jmonitoring.console.gwt.client.common.GwtRemoteService;
import org.jmonitoring.console.gwt.client.common.GwtRemoteServiceAsync;
import org.jmonitoring.console.gwt.shared.method.treesearch.ClassDTO;
import org.jmonitoring.console.gwt.shared.method.treesearch.PackageDTO;
import org.jmonitoring.console.gwt.shared.method.treesearch.TreeBuilder;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasTreeItems;
import com.google.gwt.user.client.ui.TreeItem;

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
        tBuilder.addMethod("com.toto.Test");
        tBuilder.addMethod("org.Test");

        MethodCallTreeSearch tView = clientFactory.getMethodCallTreeSearch();
        buildRoot(tView.tree, tBuilder.getRoot());
        pPanel.setWidget(tView.setPresenter(MethodCallTreeSearchActivity.this));

    }

    private void buildRoot(HasTreeItems pParent, PackageDTO pPackage)
    {
        for (PackageDTO curChild : pPackage.getSubPackages())
        {
            buildNode(pParent, curChild);
        }
        for (ClassDTO curClass : pPackage.getClasses())
        {
            buildNode(pParent, curClass);
        }
    }

    private void buildNode(HasTreeItems pParent, PackageDTO pPackage)
    {
        TreeItem curWidget = pParent.addItem(new HTML(pPackage.getName()));
        for (PackageDTO curChild : pPackage.getSubPackages())
        {
            buildNode(curWidget, curChild);
        }
        for (ClassDTO curClass : pPackage.getClasses())
        {
            buildNode(curWidget, curClass);
        }
    }

    private void buildNode(HasTreeItems pParent, ClassDTO pClass)
    {
        pParent.addItem(new HTML(pClass.getName()));
    }

}