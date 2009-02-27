package org.jmonitoring.console.gwt.server;

import org.jmonitoring.console.gwt.client.MyService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class MyServiceImpl extends RemoteServiceServlet implements MyService
{

    public String myMethod(String pText)
    {
        System.out.println("Call with " + pText);
        return "Hello : " + pText;
    }
}
