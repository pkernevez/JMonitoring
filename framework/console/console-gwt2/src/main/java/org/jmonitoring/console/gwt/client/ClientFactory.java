package org.jmonitoring.console.gwt.client;

import org.jmonitoring.console.gwt.client.flow.detail.FlowDetail;
import org.jmonitoring.console.gwt.client.flow.search.FlowSearch;
import org.jmonitoring.console.gwt.client.flow.search.FlowSearchPlace;
import org.jmonitoring.console.gwt.client.main.Main;
import org.jmonitoring.console.gwt.client.methodcall.detail.MethodCallDetail;
import org.jmonitoring.console.gwt.client.methodcall.stat.MethodCallStat;
import org.jmonitoring.console.gwt.client.resources.ConsoleImageBundle;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.client.History;

public class ClientFactory
{
    private static final EventBus sEventBus = new SimpleEventBus();

    private static final PlaceController sPlaceController = new PlaceController(sEventBus);

    private static PlaceHistoryMapper sHistoryMapper;

    private final FlowSearch flowSearch = new FlowSearch();

    private final FlowDetail flowDetail = new FlowDetail();

    private MethodCallDetail methodCallDetail = new MethodCallDetail();

    private MethodCallStat methodCallStat = new MethodCallStat();

    public final static ConsoleImageBundle imageBundle = GWT.create(ConsoleImageBundle.class);

    private static ClientFactory sClientFactory;

    private static PlaceHistoryHandler sHistoryHandler;

    EventBus getEventBus()
    {
        return sEventBus;
    }

    public static void goTo(Place pPlace)
    {
        sPlaceController.goTo(pPlace);
    }

    public static ClientFactory init()
    {
        sClientFactory = new ClientFactory();
        // Start ActivityManager for the main widget with our ActivityMapper

        sHistoryMapper = GWT.create(JMonitoringPlaceHistoryMapper.class);
        sHistoryHandler = new PlaceHistoryHandler(sHistoryMapper);
        sHistoryHandler.register(sPlaceController, sEventBus, new FlowSearchPlace());
        return sClientFactory;
    }

    public static String getToken(Place pPlace)
    {
        return sHistoryMapper.getToken(pPlace);
    }

    public void displayDefaultPlace(Main pMain)
    {
        ActivityMapper activityMapper = new JMonitoringActivityMapper(sClientFactory);
        ActivityManager activityManager = new ActivityManager(activityMapper, sEventBus);
        activityManager.setDisplay(pMain.getContent());
        sHistoryHandler.handleCurrentHistory();
    }

    public static void addHistory(Place pPlace)
    {
        History.newItem(getToken(pPlace), false);
    }

    public FlowDetail getFlowDetail()
    {
        return flowDetail;
    }

    public FlowSearch getFlowSearch()
    {
        return flowSearch;
    }

    public MethodCallDetail getMethodCallDetail()
    {
        return methodCallDetail;
    }

    public MethodCallStat getMethodCallStat()
    {
        return methodCallStat;
    }
}
