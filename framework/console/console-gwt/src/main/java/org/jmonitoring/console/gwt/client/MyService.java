package org.jmonitoring.console.gwt.client;

import com.google.gwt.user.client.rpc.RemoteService;

public interface MyService extends RemoteService
{

    public String myMethod(String pText);
}
