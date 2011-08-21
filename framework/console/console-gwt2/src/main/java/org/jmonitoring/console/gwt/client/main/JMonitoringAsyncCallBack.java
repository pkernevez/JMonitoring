package org.jmonitoring.console.gwt.client.main;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class JMonitoringAsyncCallBack<T> implements AsyncCallback<T>
{
    private static Logger sLog = Logger.getLogger(JMonitoringAsyncCallBack.class.getName());

    public void onFailure(Throwable throwable)
    {
        //TODO Use a more friendly modal window
        Window.alert("An error occured during remote service call, see log for more information");
        sLog.log(Level.SEVERE, "An error occured during remote service call", throwable);
    }

}
