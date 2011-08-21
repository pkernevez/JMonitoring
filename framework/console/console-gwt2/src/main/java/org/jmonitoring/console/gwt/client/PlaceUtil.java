package org.jmonitoring.console.gwt.client;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.client.ui.SimplePanel;

public class PlaceUtil {

    public static PlaceController getController(SimplePanel root, PlaceHistoryMapper historyMapper, Place defaultPlace) {

        EventBus eventBus = new SimpleEventBus();
        PlaceController placeController = new PlaceController(eventBus);

        // Start ActivityManager for the main widget with our ActivityMapper
        ActivityMapper activityMapper = new JMonitoringActivityMapper();
        ActivityManager activityManager = new ActivityManager(activityMapper, eventBus);
        activityManager.setDisplay(root );

        // Start PlaceHistoryHandler with our PlaceHistoryMapper
        PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
        historyHandler.register(placeController, eventBus, defaultPlace);

        historyHandler.handleCurrentHistory();
        return placeController;
    }

}
