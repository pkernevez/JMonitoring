package org.jmonitoring.console.gwt.client.main;

import org.jmonitoring.console.gwt.client.JMonitoring;
import org.jmonitoring.console.gwt.client.dto.FullExecutionFlowDTO;
import org.jmonitoring.console.gwt.client.dto.MethodCallDTO;
import org.jmonitoring.console.gwt.client.dto.RootMethodCallDTO;
import org.jmonitoring.console.gwt.client.dto.StatMethodCallDTO;
import org.jmonitoring.console.gwt.client.panel.flow.EditFlowPanel;
import org.jmonitoring.console.gwt.client.panel.flow.ImportPanel;
import org.jmonitoring.console.gwt.client.panel.flow.SearchFlowPanel;
import org.jmonitoring.console.gwt.client.panel.methodcall.EditMethodCallPanel;
import org.jmonitoring.console.gwt.client.panel.methodcall.StatMethodCallPanel;
import org.jmonitoring.console.gwt.client.service.ExecutionFlowService;
import org.jmonitoring.console.gwt.client.service.ExecutionFlowServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
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

    public static final String HISTORY_STAT_METH = "statMeth";

    public static final String HISTORY_LIST_METH = "listMeth";

    public static final String HISTORY_IMPORT_FLOW = "importFlow";

    public static String HISTORY_SEARCH = "search";

    public static String HISTORY_DELETE_FLOW = "deleteFlow";

    private SearchFlowPanel mLastSearch;

    private static Controller sController;

    public Controller()
    {
        sController = this;
    }

    public void onHistoryChanged(String pHisoryToken)
    {
        if (pHisoryToken != null && pHisoryToken.startsWith(HISTORY_EDIT_METH))
        {
            int tSepPosition = pHisoryToken.indexOf("&");
            int tFlowId = Integer.parseInt(pHisoryToken.substring(HISTORY_EDIT_METH.length(), tSepPosition));
            int tMethPosition = Integer.parseInt(pHisoryToken.substring(tSepPosition + 1));
            navigateEditMethodCall(tFlowId, tMethPosition);
        } else if (pHisoryToken != null && pHisoryToken.startsWith(HISTORY_STAT_METH))
        {
            int tSepPosition = pHisoryToken.indexOf("&");
            String tClassName = pHisoryToken.substring(HISTORY_STAT_METH.length(), tSepPosition);
            int tNextSepPosition = pHisoryToken.indexOf("&", tSepPosition + 1);
            String tMethodName = pHisoryToken.substring(tSepPosition + 1, tNextSepPosition);
            String tAggregationScope = pHisoryToken.substring(tNextSepPosition + 1);
            tAggregationScope = (tAggregationScope.length() == 0 ? "0" : tAggregationScope);
            navigateStatMethodCall(tClassName, tMethodName, Integer.parseInt(tAggregationScope));
        } else if (pHisoryToken != null && pHisoryToken.startsWith(HISTORY_EDIT_FLOW))
        {
            int pFlowId = Integer.parseInt(pHisoryToken.substring(HISTORY_EDIT_FLOW.length()));
            navigateEditFlow(pFlowId);
        } else if (HISTORY_SEARCH.equals(pHisoryToken))
        {
            if (mLastSearch == null)
            {
                mLastSearch = new SearchFlowPanel();
            }
            JMonitoring.setContentMain(mLastSearch);
        } else if (HISTORY_HOME.equals(pHisoryToken) || pHisoryToken.length() == 0)
        {
            JMonitoring.setContentMain(new SimplePanel());
        } else if (HISTORY_IMPORT_FLOW.equals(pHisoryToken))
        {
            JMonitoring.setContentMain(new ImportPanel());
        } else
        {
            JMonitoring.setContentMain(new HTML("Unknown panel..."));
        }
    }

    private void navigateStatMethodCall(String pClassName, String pMethodName, int pAggregationScope)
    {
        ExecutionFlowServiceAsync tService = getService();
        AsyncCallback<StatMethodCallDTO> tCallBack = new DefaultCallBack<StatMethodCallDTO>()
        {
            public void onSuccess(StatMethodCallDTO pMeth)
            {
                JMonitoring.setContentMain(new StatMethodCallPanel(pMeth));
            }

        };
        tService.loadStat(pClassName, pMethodName, pAggregationScope, tCallBack);
    }

    private void navigateEditMethodCall(int pFlowId, int pMethPosition)
    {
        ExecutionFlowServiceAsync tService = getService();
        AsyncCallback<RootMethodCallDTO> tCallBack = new DefaultCallBack<RootMethodCallDTO>()
        {
            public void onSuccess(RootMethodCallDTO pMeth)
            {
                JMonitoring.setContentMain(new EditMethodCallPanel(pMeth));
            }

        };
        tService.load(pFlowId, pMethPosition, tCallBack);
    }

    private void navigateEditFlow(int pFlowId)
    {
        ExecutionFlowServiceAsync tService = getService();
        AsyncCallback<FullExecutionFlowDTO> tCallBack = new DefaultCallBack<FullExecutionFlowDTO>()
        {

            public void onSuccess(FullExecutionFlowDTO pFlow)
            {
                Widget tWidget = JMonitoring.getContentMain();
                if (tWidget instanceof SearchFlowPanel)
                {
                    mLastSearch = (SearchFlowPanel) tWidget;
                }
                JMonitoring.setContentMain(new EditFlowPanel(pFlow));
            }

        };
        tService.load(pFlowId, tCallBack);
    }

    public static String createListMethodToken(String pClassName, String pMethodName, int pDurationMin, int pDurationMax)
    {
        return Controller.HISTORY_LIST_METH + pClassName + "&" + pMethodName + "&" + pDurationMin + "&" + pDurationMax;
    }

    public static String createStatToken(MethodCallDTO pMeth)
    {
        return createStatToken(pMeth.getClassName(), pMeth.getMethodName(), 0);
    }

    public static String createStatToken(String pClassName, String pMethodName, int pAggregationScope)
    {
        return Controller.HISTORY_STAT_METH + pClassName + "&" + pMethodName + "&" + pAggregationScope;
    }

    public static String createEditMethShortToken(MethodCallDTO pMethod)
    {
        return Controller.HISTORY_EDIT_METH + pMethod.getFlowId() + "&";
    }

    public static String createEditMethToken(MethodCallDTO pMethod)
    {
        return Controller.HISTORY_EDIT_METH + pMethod.getFlowId() + "&" + pMethod.getPosition();
    }

    /**
     * Show search panel and refresh it content.
     * 
     * @param pMessage
     */
    public static void returnToSearch(String pMessage)
    {
        if (sController.mLastSearch == null)
        {
            sController.mLastSearch = new SearchFlowPanel();
        }
        History.newItem(HISTORY_SEARCH);
        JMonitoring.setContentMain(sController.mLastSearch);
        sController.mLastSearch.callSearch();
        sController.mLastSearch.setInfoMsg(pMessage);
    }

    private static ExecutionFlowServiceAsync sService;

    public static ExecutionFlowServiceAsync getService()
    {
        if (sService == null)
        {
            sService = GWT.create(ExecutionFlowService.class);
            ServiceDefTarget tEndpoint = (ServiceDefTarget) sService;
            tEndpoint.setServiceEntryPoint(JMonitoring.SERVICE_URL);
        }
        return sService;
    }
}
