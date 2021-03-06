package org.jmonitoring.console.gwt.client.panel.flow;

import static org.jmonitoring.console.gwt.client.panel.PanelUtil.*;

import java.util.ArrayList;
import java.util.List;

import org.jmonitoring.console.gwt.client.JMonitoring;
import org.jmonitoring.console.gwt.client.dto.ExecutionFlowDTO;
import org.jmonitoring.console.gwt.client.dto.FullExecutionFlowDTO;
import org.jmonitoring.console.gwt.client.dto.MapAreaDTO;
import org.jmonitoring.console.gwt.client.dto.MapDto;
import org.jmonitoring.console.gwt.client.dto.MethodCallDTO;
import org.jmonitoring.console.gwt.client.main.Controller;
import org.jmonitoring.console.gwt.client.main.DefaultCallBack;
import org.jmonitoring.console.gwt.client.panel.PanelUtil;
import org.jmonitoring.console.gwt.client.panel.flow.image.AreaWidget;
import org.jmonitoring.console.gwt.client.panel.flow.image.MapWidget;
import org.jmonitoring.console.gwt.client.panel.flow.tree.AsynchroneLoad;
import org.jmonitoring.console.gwt.client.panel.flow.tree.ExecFlowTree;
import org.jmonitoring.console.gwt.client.panel.flow.tree.MethCallTreeItem;
import org.jmonitoring.console.gwt.client.service.ExecutionFlowService;
import org.jmonitoring.console.gwt.client.service.ExecutionFlowServiceAsync;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
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
    private final FullExecutionFlowDTO mFlow;

    private final ClickListener mDeleteListener = new ClickListener()
    {
        public void onClick(Widget pWidget)
        {
            if (Window.confirm("Delete this flow ?"))
            {
                ExecutionFlowServiceAsync tService = Controller.getService();
                tService.delete(mFlow.getFlow().getId(), new DefaultCallBack<Void>()
                {
                    public void onSuccess(Void pArg0)
                    {
                        Controller.returnToSearch("Flow has been deleted...");
                    }
                });
            }
        }
    };

    private final ClickListener mExportListener = new ClickListener()
    {
        public void onClick(Widget pWidget)
        {
            Window.Location.assign("../ExportXml?id=" + mFlow.getFlow().getId());
        }
    };

    public EditFlowPanel(FullExecutionFlowDTO pFlow)
    {
        mFlow = pFlow;
        VerticalPanel tPanel = new VerticalPanel();
        tPanel.add(createTitle("Flow Edition"));

        HorizontalPanel tLine = new HorizontalPanel();
        tLine
             .add(PanelUtil
                           .createClickImage(JMonitoring.getImageBundle().delete(), "Delete this flow", mDeleteListener));
        tLine.add(PanelUtil.createClickImage(JMonitoring.getImageBundle().xml(), "Export this flow to Xml/gzip",
                                             mExportListener));
        tPanel.add(tLine);

        FlexTable tFlexTable = new FlexTable();
        tFlexTable.setWidget(0, 0, createLabel("JVM Name"));
        ExecutionFlowDTO tFlow = pFlow.getFlow();
        tFlexTable.setWidget(0, 1, createData(tFlow.getJvmIdentifier()));
        tFlexTable.setWidget(0, 2, createLabel("Thread Name"));
        tFlexTable.setWidget(0, 3, createData(tFlow.getThreadName()));

        tFlexTable.setWidget(1, 0, createLabel("Begin Time"));
        tFlexTable.setWidget(1, 1, createData(tFlow.getBeginTime()));
        tFlexTable.setWidget(1, 2, createLabel("End Time"));
        tFlexTable.setWidget(1, 3, createData(tFlow.getEndTime()));

        tFlexTable.setWidget(2, 0, createLabel("Duration"));
        tFlexTable.setWidget(2, 1, createData(tFlow.getDuration()));
        tFlexTable.setWidget(2, 2, createLabel("Group name"));
        MethodCallDTO tFirstMethodCall = tFlow.getFirstMethodCall();
        tFlexTable.setWidget(2, 3, createData(tFirstMethodCall.getGroupName()));

        tFlexTable.setWidget(3, 0, createLabel("Appel"));
        tFlexTable.setWidget(3, 1, createData(tFlow.getClassName() + "." + tFlow.getMethodName()));

        MapWidget tMap = addMap(pFlow.getImageMap());
        FlowPanel tFlowPanel = new FlowPanel();
        tFlowPanel.add(createImage(ExecutionFlowService.DURATION_IN_GROUP));
        tFlowPanel.add(createImage(ExecutionFlowService.NB_CALL_TO_GROUP));
        tFlexTable.setWidget(4, 0, tFlowPanel);
        tFlexTable.getFlexCellFormatter().setColSpan(4, 0, 4);
        tFlexTable.getFlexCellFormatter().setAlignment(4, 0, HasHorizontalAlignment.ALIGN_CENTER,
                                                       HasVerticalAlignment.ALIGN_MIDDLE);
        Image tImage = createImage(ExecutionFlowService.CHART_BAR_FLOWS);
        tMap.bindImage(tImage);
        tFlexTable.setWidget(5, 0, tImage);
        tFlexTable.getFlexCellFormatter().setColSpan(5, 0, 4);

        tLine = new HorizontalPanel();
        tLine.add(PanelUtil.createExecutionFlowPanel(tFlow, tFirstMethodCall));
        MethCallTreeItem tRoot = MethCallTreeItem.create(tLine, tFirstMethodCall);
        tRoot.setLoaded(true);
        tRoot.setState(true);
        Tree tTree = new ExecFlowTree(tFlow.getId());
        tTree.addItem(tRoot);
        tTree.addTreeListener(new ExecFlowTreeListener());
        tFlexTable.setWidget(8, 0, tTree);
        tFlexTable.getFlexCellFormatter().setColSpan(8, 0, 4);

        tPanel.add(tFlexTable);
        add(tPanel);
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
                    if (!tChild.isLoaded())
                    {
                        tList.add(tChild.getMethId());
                    }
                }
                loadChildren(pTreeitem, tList);
            }
        }

        private void loadChildren(TreeItem pTreeitem, List<Integer> pList)
        {
            if (pList.size() > 0)
            {
                ExecutionFlowServiceAsync tService = Controller.getService();
                int tFlowId = ((ExecFlowTree) pTreeitem.getTree()).getFlowId();
                tService.load(tFlowId, pList, new AsynchroneLoad((MethCallTreeItem) pTreeitem));
            }
        }
    }
}
