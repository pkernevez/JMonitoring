package org.jmonitoring.console.gwt.client;

import java.util.logging.Logger;

import org.jmonitoring.console.gwt.client.flow.FlowSearchPlace;
import org.jmonitoring.console.gwt.client.main.Main;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.logging.client.FirebugLogHandler;
import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class JMonitoring implements EntryPoint
{

    private static PlaceHistoryMapper historyMapper = GWT.create(GwtPlaceHistoryMapper.class);

    public static PlaceController placeController;

//    private ConsoleImageBundle bundle;

    /**
     * This is the entry point method.
     */
    public void onModuleLoad()
    {
//        bundle = GWT.create(ConsoleImageBundle.class);

        Main tMain = new Main();
        RootLayoutPanel.get().add(tMain);
        placeController = PlaceUtil.getController(tMain.getContent(), historyMapper, new FlowSearchPlace());

    }

}
