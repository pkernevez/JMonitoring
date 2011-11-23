package org.jmonitoring.console.gwt.client.methodcall.search;

import it.pianetatecno.gwt.utility.client.table.Filter;
import it.pianetatecno.gwt.utility.client.table.StringFilter;

import java.util.ArrayList;
import java.util.List;

import org.jmonitoring.console.gwt.client.ClientFactory;
import org.jmonitoring.console.gwt.client.common.GwtRemoteService;
import org.jmonitoring.console.gwt.client.common.GwtRemoteServiceAsync;
import org.jmonitoring.console.gwt.shared.method.MethodCallSearchCriterion;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class MethodCallSearchActivity extends AbstractActivity
{
    MethodCallSearchDriver driver = GWT.create(MethodCallSearchDriver.class);

    interface MethodCallSearchDriver extends SimpleBeanEditorDriver<MethodCallSearchCriterion, MethodCallSearch>
    {
    }

    private MethodCallSearchPlace place;

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
        MethodCallSearch tMethodCallSearch = clientFactory.getMethodCallSearch();
        pPanel.setWidget(tMethodCallSearch.setPresenter(MethodCallSearchActivity.this));
        driver.initialize(tMethodCallSearch);
        MethodCallSearchCriterion tCrit = new MethodCallSearchCriterion();
        tCrit.setFlowThread("Threaddddd");
        driver.edit(tCrit);
        filterData(tCrit, tMethodCallSearch, null);

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
    void filterData(MethodCallSearch pView, KeyPressEvent pEvent)
    {
        filterData(driver.flush(), pView, pEvent);
    }

    void filterData(MethodCallSearchCriterion pCriterion, MethodCallSearch pView, KeyPressEvent pEvent)
    {
        if (pEvent == null || (pEvent.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER))
        {
            place = new MethodCallSearchPlace(pCriterion);
            ClientFactory.addHistory(place);
            pView.table.filterData(createFilters());
        }
    }

    List<Filter<?>> createFilters()
    {
        List<Filter<?>> tFilters = new ArrayList<Filter<?>>();
        // addFilter(tFilters, place.thread, HibernateConstant.THREAD);
        return tFilters;
    }

    private void addFilter(List<Filter<?>> pFilters, String pText, String pPropertyName)
    {
        if (pText.length() > 0)
        {
            StringFilter curFilter = new StringFilter();
            curFilter.setPropertyName(pPropertyName);
            curFilter.setValue(pText);
            pFilters.add(curFilter);
        }
    }

}