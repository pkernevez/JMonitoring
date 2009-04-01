package org.jmonitoring.console.gwt.client.main;

import org.jmonitoring.console.gwt.client.JMonitoring;
import org.jmonitoring.console.gwt.client.dto.FullExecutionFlowDTO;
import org.jmonitoring.console.gwt.client.dto.MethodCallDTO;
import org.jmonitoring.console.gwt.client.panel.flow.EditFlowPanel;
import org.jmonitoring.console.gwt.client.panel.flow.SearchFlowPanel;
import org.jmonitoring.console.gwt.client.panel.methodcall.EditMethodCallPanel;
import org.jmonitoring.console.gwt.client.service.ExecutionFlowService;
import org.jmonitoring.console.gwt.client.service.ExecutionFlowServiceAsync;

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
    public static final String HISTORY_EDIT_FLOW = "editFlow";

    public static final String HISTORY_HOME = "home";

    public static final String HISTORY_EDIT_METH = "editMeth";

    public static String HISTORY_SEARCH = "search";

    public static String HISTORY_DELETE_FLOW = "deleteFlow";

    private SearchFlowPanel mLastSearch;

    private final JMonitoring mMain;

    public Controller(JMonitoring pMain)
    {
        mMain = pMain;
    }

    public void onHistoryChanged(String pHisoryToken)
    {
        if (pHisoryToken != null && pHisoryToken.startsWith(HISTORY_EDIT_METH))
        {
            int tSepPosition = pHisoryToken.indexOf("&");
            int tFlowId = Integer.parseInt(pHisoryToken.substring(HISTORY_EDIT_METH.length(), tSepPosition));
            int tMethPosition = Integer.parseInt(pHisoryToken.substring(tSepPosition + 1));
            navigateEditMethodCall(tFlowId, tMethPosition);
        } else if (pHisoryToken != null && pHisoryToken.startsWith(HISTORY_EDIT_FLOW))
        {
            int pFlowId = Integer.parseInt(pHisoryToken.substring(HISTORY_EDIT_FLOW.length()));
            navigateEditFlow(pFlowId);
        } else if (HISTORY_SEARCH.equals(pHisoryToken))
        {
            if (mLastSearch == null)
            {
                mLastSearch = new SearchFlowPanel(mMain);
            }
            mMain.setContentMain(mLastSearch);
        } else if (HISTORY_HOME.equals(pHisoryToken) || pHisoryToken.length() == 0)
        {
            mMain.setContentMain(new SimplePanel());
        } else
        {
            mMain.setContentMain(new HTML("Unknown panel..."));
        }
    }

    private void navigateEditMethodCall(int pFlowId, int pMethPosition)
    {
        ExecutionFlowServiceAsync tService = GWT.create(ExecutionFlowService.class);
        ServiceDefTarget tEndpoint = (ServiceDefTarget) tService;
        tEndpoint.setServiceEntryPoint(JMonitoring.SERVICE_URL);
        AsyncCallback<MethodCallDTO> tCallBack = new AsyncCallback<MethodCallDTO>()
        {
            public void onFailure(Throwable e)
            {
                GWT.log("Error", e);
                mMain.setContentMain(new HTML("<h2 class=\"error\">Unexpected error on server</h2>"));
            }

            public void onSuccess(MethodCallDTO pMeth)
            {
                mMain.setContentMain(new EditMethodCallPanel(mMain, pMeth));
            }

        };
        tService.load(pFlowId, pMethPosition, tCallBack);
    }

    private void navigateEditFlow(int pFlowId)
    {
        ExecutionFlowServiceAsync tService = GWT.create(ExecutionFlowService.class);
        ServiceDefTarget tEndpoint = (ServiceDefTarget) tService;
        tEndpoint.setServiceEntryPoint(JMonitoring.SERVICE_URL);
        AsyncCallback<FullExecutionFlowDTO> tCallBack = new AsyncCallback<FullExecutionFlowDTO>()
        {
            public void onFailure(Throwable e)
            {
                GWT.log("Error", e);
                mMain.setContentMain(new HTML("<h2 class=\"error\">Unexpected error on server</h2>"));
            }

            public void onSuccess(FullExecutionFlowDTO pFlow)
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
