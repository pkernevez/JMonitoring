package org.jmonitoring.console.gwt.client.main;

import org.jmonitoring.console.gwt.client.JMonitoring;
import org.jmonitoring.console.gwt.client.dto.ExecutionFlowDTO;
import org.jmonitoring.console.gwt.client.executionflow.EditFlowPanel;
import org.jmonitoring.console.gwt.client.executionflow.ExecutionFlowService;
import org.jmonitoring.console.gwt.client.executionflow.ExecutionFlowServiceAsync;
import org.jmonitoring.console.gwt.client.executionflow.SearchFlowPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.HistoryListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class Controller implements HistoryListener
{
    public static String HISTORY_EDIT = "edit";

    public static String HISTORY_HOME = "home";

    public static String HISTORY_SEARCH = "search";

    private SearchFlowPanel mLastSearch;

    private final JMonitoring mMain;

    public Controller(JMonitoring pMain)
    {
        mMain = pMain;
    }

    public void onHistoryChanged(String pHisoryToken)
    {
        if (pHisoryToken != null && pHisoryToken.startsWith(HISTORY_EDIT))
        {
            int pFlowId = Integer.parseInt(pHisoryToken.substring(HISTORY_EDIT.length()));
            navigateEdit(pFlowId);
        } else if (HISTORY_SEARCH.equals(pHisoryToken))
        {
            if (mLastSearch == null)
            {
                mLastSearch = new SearchFlowPanel(mMain);
            }
            mMain.setContentMain(mLastSearch);
        } else if (HISTORY_HOME.equals(pHisoryToken))
        {
            mMain.setContentMain(new SimplePanel());
        } else
        {
            mMain.setContentMain(new HTML("Unknown navigate panel..."));
        }
    }

    private void navigateEdit(int pFlowId)
    {
        ExecutionFlowServiceAsync tService = GWT.create(ExecutionFlowService.class);
        ServiceDefTarget tEndpoint = (ServiceDefTarget) tService;
        tEndpoint.setServiceEntryPoint(JMonitoring.SERVICE_URL);
        AsyncCallback<ExecutionFlowDTO> tCallBack = new AsyncCallback<ExecutionFlowDTO>()
        {
            public void onFailure(Throwable e)
            {
                GWT.log("Error", e);
                mMain.setContentMain(new HTML("<h2 class=\"error\">Unexpected error on server</h2>"));
            }

            public void onSuccess(ExecutionFlowDTO pFlow)
            {
                Widget tWidget = mMain.getContentMain();
                if (tWidget instanceof SearchFlowPanel)
                {
                    mLastSearch = (SearchFlowPanel) tWidget;
                }
                mMain.setContentMain(new EditFlowPanel(mMain, pFlow));
            }

        };
        tService.load(pFlowId, tCallBack);
    }
}
