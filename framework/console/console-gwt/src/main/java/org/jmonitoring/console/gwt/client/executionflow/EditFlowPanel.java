package org.jmonitoring.console.gwt.client.executionflow;

import static org.jmonitoring.console.gwt.client.PanelUtil.addData;
import static org.jmonitoring.console.gwt.client.PanelUtil.addLabel;
import static org.jmonitoring.console.gwt.client.PanelUtil.addTitle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jmonitoring.console.gwt.client.JMonitoring;
import org.jmonitoring.console.gwt.client.dto.ExecutionFlowDTO;
import org.jmonitoring.console.gwt.client.dto.MethodCallDTO;
import org.jmonitoring.console.gwt.client.executionflow.images.FullExecutionFlow;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.TreeListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class EditFlowPanel extends VerticalPanel
{
    // private JMonitoring mMain;

    public EditFlowPanel(JMonitoring pMain, FullExecutionFlow pFlow)
    {
        FlexTable tFlexTable = new FlexTable();
        tFlexTable.setWidget(0, 0, addTitle("Flow Edition"));
        tFlexTable.getFlexCellFormatter().setColSpan(0, 0, 4);

        tFlexTable.setWidget(1, 0, addLabel("Action Delete"));
        tFlexTable.setWidget(1, 1, addLabel("Action Export"));

        tFlexTable.setWidget(2, 0, addLabel("JVM Name"));
        ExecutionFlowDTO tFlow = pFlow.getFlow();
        tFlexTable.setWidget(2, 1, addData(tFlow.getJvmIdentifier()));
        tFlexTable.setWidget(2, 2, addLabel("Thread Name"));
        tFlexTable.setWidget(2, 3, addData(tFlow.getThreadName()));

        tFlexTable.setWidget(3, 0, addLabel("Begin Time"));
        tFlexTable.setWidget(3, 1, addData(tFlow.getBeginTime()));
        tFlexTable.setWidget(3, 2, addLabel("End Time"));
        tFlexTable.setWidget(3, 3, addData(tFlow.getEndTime()));

        tFlexTable.setWidget(4, 0, addLabel("Duration"));
        tFlexTable.setWidget(4, 1, addData(tFlow.getDuration()));
        tFlexTable.setWidget(4, 2, addLabel("Group name"));
        MethodCallDTO tFirstMethodCall = tFlow.getFirstMethodCall();
        tFlexTable.setWidget(4, 3, addData(tFirstMethodCall.getGroupName()));

        tFlexTable.setWidget(5, 0, addLabel("Appel"));
        tFlexTable.setWidget(5, 1, addData(tFlow.getClassName() + "." + tFlow.getMethodName()));

        add(new HTML(pFlow.getImageMap()));
        FlowPanel tFlowPanel = new FlowPanel();
        tFlowPanel.add(addImage(ExecutionFlowService.DURATION_IN_GROUP));
        tFlowPanel.add(addImage(ExecutionFlowService.NB_CALL_TO_GROUP));
        tFlexTable.setWidget(6, 0, tFlowPanel);
        tFlexTable.getFlexCellFormatter().setColSpan(6, 0, 4);

        tFlexTable.setWidget(7, 0, addImage(ExecutionFlowService.CHART_BAR_FLOWS));
        tFlexTable.getFlexCellFormatter().setColSpan(7, 0, 4);

        HorizontalPanel tPanel = new HorizontalPanel();
        tPanel.add(new HTML("[" + tFirstMethodCall.getDuration() + "] " + tFlow.getJvmIdentifier() + " / "
            + tFlow.getThreadName() + " : " + tFirstMethodCall.getGroupName() + " -> "
            + tFirstMethodCall.getClassName() + tFirstMethodCall.getMethodName() + "()"));
        MethCallTreeItem tRoot = new MethCallTreeItem(tPanel, tFirstMethodCall);
        tRoot.mIsLoaded = true;
        Tree tTree = new ExecFlowTree(tFlow.getId());
        tTree.addItem(tRoot);
        tTree.addTreeListener(new ExecFlowTreeListener());
        tFlexTable.setWidget(8, 0, tTree);
        tFlexTable.getFlexCellFormatter().setColSpan(8, 0, 4);

        add(tFlexTable);
    }

    private Image addImage(String pChartBarFlows)
    {
        Image tImage = new Image("/DynamicImage?Id=" + pChartBarFlows);

        return tImage;
    }

    private static class ExecFlowTree extends Tree
    {
        private final int mFlowId;

        private ExecFlowTree(int pFlowId)
        {
            mFlowId = pFlowId;
        }
    }

    private static class MethCallTreeItem extends TreeItem
    {
        private final int mMethId;

        private boolean mIsLoaded = false;

        public MethCallTreeItem(Widget tWidget, MethodCallDTO pMeth)
        {
            super(tWidget);
            mMethId = (pMeth == null ? -1 : pMeth.getPosition());
            addSubItems(pMeth);
        }

        public MethCallTreeItem(MethodCallDTO pMeth)
        {
            this(getWidget(pMeth), pMeth);
        }

        private static Widget getWidget(MethodCallDTO pMeth)
        {
            return new HTML(pMeth.getClassName() + "." + pMeth.getMethodName());
        }

        private void addSubItems(MethodCallDTO pMethodCallDTO)
        {
            if (pMethodCallDTO.getChildren() != null)
            {
                for (MethodCallDTO tMeth : pMethodCallDTO.getChildren())
                {
                    addItem(new MethCallTreeItem(tMeth));
                }
            }
        }
    }

    private static class ExecFlowTreeListener implements TreeListener
    {

        public void onTreeItemSelected(TreeItem pTreeitem)
        {
        }

        public void onTreeItemStateChanged(TreeItem pTreeitem)
        {
            List<Integer> tList = new ArrayList<Integer>();
            MethCallTreeItem tItem = (MethCallTreeItem) pTreeitem;
            if (tItem.getState())
            {
                for (int i = 0; i < tItem.getChildCount(); i++)
                {
                    MethCallTreeItem tChild = (MethCallTreeItem) tItem.getChild(i);
                    if (!tChild.mIsLoaded)
                    {
                        tList.add(tChild.mMethId);
                    }
                }
                loadChildren(pTreeitem, tList);
            }
        }

        private void loadChildren(TreeItem pTreeitem, List<Integer> pList)
        {
            if (pList.size() > 0)
            {
                ExecutionFlowServiceAsync tService = GWT.create(ExecutionFlowService.class);
                ServiceDefTarget tEndpoint = (ServiceDefTarget) tService;
                tEndpoint.setServiceEntryPoint(JMonitoring.SERVICE_URL);
                int tFlowId = ((ExecFlowTree) pTreeitem.getTree()).mFlowId;
                tService.load(tFlowId, pList, new AsynchroneLoad((MethCallTreeItem) pTreeitem));
            }
        }
    }

    public static class AsynchroneLoad implements AsyncCallback<Map<Integer, MethodCallDTO>>
    {
        private final MethCallTreeItem mParent;

        private AsynchroneLoad(MethCallTreeItem pParent)
        {
            mParent = pParent;
        }

        public void onFailure(Throwable e)
        {
            GWT.log("Error", e);
        }

        public void onSuccess(Map<Integer, MethodCallDTO> pMap)
        {
            List<Integer> tIds = new ArrayList<Integer>();
            List<MethCallTreeItem> tRemove = new ArrayList<MethCallTreeItem>();
            for (int i = 0; i < mParent.getChildCount(); i++)
            {
                MethCallTreeItem tChild = (MethCallTreeItem) mParent.getChild(i);
                if (!tChild.mIsLoaded)
                {
                    tIds.add((tChild).mMethId);
                    tRemove.add(tChild);
                }
            }
            for (MethCallTreeItem tMethCallTreeItem : tRemove)
            {
                mParent.removeItem(tMethCallTreeItem);
            }
            for (Integer tChildId : tIds)
            {
                MethCallTreeItem tChild = new MethCallTreeItem(pMap.get(tChildId));
                tChild.mIsLoaded = true;
                mParent.addItem(tChild);
            }

        }
    }
}
