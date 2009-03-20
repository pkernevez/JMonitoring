package org.jmonitoring.console.gwt.client.executionflow;

import static org.jmonitoring.console.gwt.client.PanelUtil.addData;
import static org.jmonitoring.console.gwt.client.PanelUtil.addLabel;
import static org.jmonitoring.console.gwt.client.PanelUtil.addTitle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jmonitoring.console.gwt.client.JMonitoring;
import org.jmonitoring.console.gwt.client.dto.ExecutionFlowDTO;
import org.jmonitoring.console.gwt.client.dto.FullExecutionFlowDTO;
import org.jmonitoring.console.gwt.client.dto.MapAreaDTO;
import org.jmonitoring.console.gwt.client.dto.MapDto;
import org.jmonitoring.console.gwt.client.dto.MethodCallDTO;
import org.jmonitoring.console.gwt.client.executionflow.images.AreaWidget;
import org.jmonitoring.console.gwt.client.executionflow.images.MapWidget;
import org.jmonitoring.console.gwt.client.main.Controller;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.ClickListener;
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
    private final JMonitoring mMain;

    public EditFlowPanel(JMonitoring pMain, FullExecutionFlowDTO pFlow)
    {
        mMain = pMain;
        VerticalPanel tPanel = new VerticalPanel();
        tPanel.add(addTitle("Flow Edition"));

        HorizontalPanel tLine = new HorizontalPanel();
        tLine.add(SearchFlowPanel.createLinkedImage(mMain.getImageBundle().delete(), "Delete this flow",
                                                    new NavigationListener(Controller.HISTORY_DELETE_FLOW
                                                        + pFlow.getFlow().getId())));
        tLine.add(SearchFlowPanel.createLinkedImage(mMain.getImageBundle().xml(), "Export this flow to Xml/gzip",
                                                    new NavigationListener(Controller.HISTORY_DELETE_FLOW
                                                        + pFlow.getFlow().getId())));
        tPanel.add(tLine);

        FlexTable tFlexTable = new FlexTable();
        tFlexTable.setWidget(0, 0, addLabel("JVM Name"));
        ExecutionFlowDTO tFlow = pFlow.getFlow();
        tFlexTable.setWidget(0, 1, addData(tFlow.getJvmIdentifier()));
        tFlexTable.setWidget(0, 2, addLabel("Thread Name"));
        tFlexTable.setWidget(0, 3, addData(tFlow.getThreadName()));

        tFlexTable.setWidget(1, 0, addLabel("Begin Time"));
        tFlexTable.setWidget(1, 1, addData(tFlow.getBeginTime()));
        tFlexTable.setWidget(1, 2, addLabel("End Time"));
        tFlexTable.setWidget(1, 3, addData(tFlow.getEndTime()));

        tFlexTable.setWidget(2, 0, addLabel("Duration"));
        tFlexTable.setWidget(2, 1, addData(tFlow.getDuration()));
        tFlexTable.setWidget(2, 2, addLabel("Group name"));
        MethodCallDTO tFirstMethodCall = tFlow.getFirstMethodCall();
        tFlexTable.setWidget(2, 3, addData(tFirstMethodCall.getGroupName()));

        tFlexTable.setWidget(3, 0, addLabel("Appel"));
        tFlexTable.setWidget(3, 1, addData(tFlow.getClassName() + "." + tFlow.getMethodName()));

        MapWidget tMap = addMap(pFlow.getImageMap());
        FlowPanel tFlowPanel = new FlowPanel();
        tFlowPanel.add(addImage(ExecutionFlowService.DURATION_IN_GROUP));
        tFlowPanel.add(addImage(ExecutionFlowService.NB_CALL_TO_GROUP));
        tFlexTable.setWidget(4, 0, tFlowPanel);
        tFlexTable.getFlexCellFormatter().setColSpan(4, 0, 4);

        Image tImage = addImage(ExecutionFlowService.CHART_BAR_FLOWS);
        tMap.bindImage(tImage);
        tFlexTable.setWidget(5, 0, tImage);
        tFlexTable.getFlexCellFormatter().setColSpan(5, 0, 4);

        tLine = new HorizontalPanel();
        tLine.add(new HTML("[" + tFirstMethodCall.getDuration() + "] " + tFlow.getJvmIdentifier() + " / "
            + tFlow.getThreadName() + " : " + tFirstMethodCall.getGroupName() + " -> "
            + tFirstMethodCall.getClassName() + tFirstMethodCall.getMethodName() + "()"));
        MethCallTreeItem tRoot = new MethCallTreeItem(tLine, tFirstMethodCall);
        tRoot.mIsLoaded = true;
        tRoot.setState(true);
        Tree tTree = new ExecFlowTree(tFlow.getId());
        tTree.addItem(tRoot);
        tTree.addTreeListener(new ExecFlowTreeListener());
        tFlexTable.setWidget(8, 0, tTree);
        tFlexTable.getFlexCellFormatter().setColSpan(8, 0, 4);

        tPanel.add(tFlexTable);
        add(tPanel);
    }

    public static class NavigationListener implements ClickListener
    {
        String mUrl;

        public NavigationListener(String pTarget)
        {
            mUrl = pTarget;
        }

        public void onClick(Widget pWidget)
        {
            History.newItem(mUrl);
            History.fireCurrentHistoryState();
        }
    }

    private Widget addLinkedImage(String pName)
    {
        return new Image(pName);
    }

    private MapWidget addMap(MapDto pImageMap)
    {
        List<MapAreaDTO> tAreas = pImageMap.getAreas();
        AreaWidget[] tWidgets = new AreaWidget[tAreas.size()];
        AreaWidget tCurWidget;
        MapAreaDTO tCurArea;
        for (int i = 0; i < tAreas.size(); i++)
        {
            tCurArea = tAreas.get(i);
            String tUrl = Controller.HISTORY_EDIT_METH + pImageMap.getFlowId() + "&" + tCurArea.getPosition();
            tCurWidget = new AreaWidget("RECT", tCurArea.getCoordinate(), new MethodCallCommand(tUrl), tUrl);
            tWidgets[i] = tCurWidget;
        }
        MapWidget tMap = new MapWidget(tWidgets, "ChartBar");
        add(tMap);
        return tMap;
    }

    private static class MethodCallCommand implements Command
    {
        private final String mUrl;

        public MethodCallCommand(String pUrl)
        {
            super();
            mUrl = pUrl;
        }

        public void execute()
        {
            History.newItem(mUrl);
            History.fireCurrentHistoryState();
        }
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
