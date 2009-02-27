package org.jmonitoring.console.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class JMonitoring implements EntryPoint
{

    /**
     * This is the entry point method.
     */
    public void onModuleLoad()
    {

        MyServiceAsync svc = (MyServiceAsync) GWT.create(MyService.class);
        ServiceDefTarget endpoint = (ServiceDefTarget) svc;
        endpoint.setServiceEntryPoint("/MyService");
        AsyncCallback<String> callback = new AsyncCallback<String>()
        {
            public void onSuccess(String result)
            {
                RootPanel.get().add(new HTML(result.toString()));
            }

            public void onFailure(Throwable ex)
            {
                RootPanel.get().add(new HTML(ex.toString()));
            }
        };
        GWT.log("Avant appel", null);
        svc.myMethod("Do Stuff", callback);
    }

}
