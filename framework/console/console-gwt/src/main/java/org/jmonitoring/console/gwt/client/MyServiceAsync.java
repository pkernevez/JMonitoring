package org.jmonitoring.console.gwt.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MyServiceAsync
{
    public void myMethod(String s, AsyncCallback<String> callback);
}
