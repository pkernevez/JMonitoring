package org.jmonitoring.console.gwt.client.main;

import org.jmonitoring.console.gwt.client.JMonitoring;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public abstract class DefaultCallBack<T> implements AsyncCallback<T>
{

    public void onFailure(Throwable e)
    {
        GWT.log("Error", e);
        JMonitoring.setContentMain(new HTML("<h2 class=\"error\">Unexpected error on server</h2>"));
    }

}
