package org.jmonitoring.console.gwt.client;

import org.jmonitoring.console.gwt.client.common.message.Message;
import org.jmonitoring.console.gwt.client.flow.delete.FlowDelete;
import org.jmonitoring.console.gwt.client.flow.detail.FlowDetail;
import org.jmonitoring.console.gwt.client.flow.search.FlowSearch;
import org.jmonitoring.console.gwt.client.flow.search.FlowSearchPlace;
import org.jmonitoring.console.gwt.client.main.Main;
import org.jmonitoring.console.gwt.client.methodcall.detail.MethodCallDetail;
import org.jmonitoring.console.gwt.client.methodcall.distribution.MethodCallDistribution;
import org.jmonitoring.console.gwt.client.methodcall.search.MethodCallSearch;
import org.jmonitoring.console.gwt.client.methodcall.treesearch.MethodCallTreeSearch;
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

    // private final FlowSearch flowSearch = new FlowSearch();

    private final Message message = new Message();

    // private final FlowDetail flowDetail = new FlowDetail();

    // private MethodCallDetail methodCallDetail = new MethodCallDetail();

    // private MethodCallDistribution methodCallStat = new MethodCallDistribution();

    // private MethodCallSearch methodCallSearch = new MethodCallSearch();

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

    public static ClientFactory getInstance()
    {
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
        return new FlowDetail();
    }

    public FlowSearch getFlowSearch()
    {
        return new FlowSearch();
    }

    public MethodCallDetail getMethodCallDetail()
    {
        return new MethodCallDetail();
    }

    public MethodCallDistribution getMethodCallStat()
    {
        return new MethodCallDistribution();
    }

    public Message getMessage()
    {
        return message;
    }

    public MethodCallSearch getMethodCallSearch()
    {
        return new MethodCallSearch();
    }

    public MethodCallTreeSearch getMethodCallTreeSearch()
    {
        return new MethodCallTreeSearch();
    }

    public FlowDelete getFlowDelete()
    {
        return new FlowDelete();
    }
}
