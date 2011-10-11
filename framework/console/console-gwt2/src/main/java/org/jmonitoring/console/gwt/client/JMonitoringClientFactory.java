package org.jmonitoring.console.gwt.client;

import org.jmonitoring.console.gwt.client.flow.FlowDetail;
import org.jmonitoring.console.gwt.client.flow.FlowSearch;
import org.jmonitoring.console.gwt.client.flow.FlowSearchPlace;
import org.jmonitoring.console.gwt.client.main.Main;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryMapper;

public class JMonitoringClientFactory
{
    private static final EventBus sEventBus = new SimpleEventBus();

    private static final PlaceController sPlaceController = new PlaceController(sEventBus);

    private static PlaceHistoryMapper sHistoryMapper;

    private FlowDetail flowDetail = new FlowDetail();

    private FlowSearch flowSearch = new FlowSearch();

    private static JMonitoringClientFactory sClientFactory;

    private static PlaceHistoryHandler sHistoryHandler;

    EventBus getEventBus()
    {
        return sEventBus;
    }

    public static PlaceController getPlaceController()
    {
        return sPlaceController;
    }

    public FlowDetail getFlowDetail()
    {
        return flowDetail;
    }

    public FlowSearch getFlowSearch()
    {
        return flowSearch;
    }

    public static JMonitoringClientFactory init()
    {
        sClientFactory = new JMonitoringClientFactory();
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
}
