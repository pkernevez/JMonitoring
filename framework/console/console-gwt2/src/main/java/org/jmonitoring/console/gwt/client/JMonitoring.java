package org.jmonitoring.console.gwt.client;

import org.jmonitoring.console.gwt.client.main.Main;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class JMonitoring implements EntryPoint
{

    // private ConsoleImageBundle bundle;

    /**
     * This is the entry point method.
     */
    public void onModuleLoad()
    {
        // bundle = GWT.create(ConsoleImageBundle.class);

        ClientFactory tClientFactory = ClientFactory.init();
        Main tMain = new Main();
        RootLayoutPanel.get().add(tMain);
        tClientFactory.displayDefaultPlace(tMain);
    }

}
