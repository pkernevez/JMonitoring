package org.jmonitoring.console.gwt.client.executionflow;

import static org.jmonitoring.console.gwt.client.PanelUtil.addData;
import static org.jmonitoring.console.gwt.client.PanelUtil.addLabel;
import static org.jmonitoring.console.gwt.client.PanelUtil.addTitle;

import java.util.List;

import org.jmonitoring.console.gwt.client.JMonitoring;
import org.jmonitoring.console.gwt.client.dto.ExecutionFlowDTO;
import org.jmonitoring.console.gwt.client.dto.MethodCallDTO;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
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
    private JMonitoring mMain;

    public EditFlowPanel(JMonitoring pMain, ExecutionFlowDTO pFlow)
    {
        FlexTable tFlexTable = new FlexTable();
        tFlexTable.setWidget(0, 0, addTitle("Flow Edition"));
        tFlexTable.getFlexCellFormatter().setColSpan(0, 0, 4);

        tFlexTable.setWidget(1, 0, addLabel("Action Delete"));
        tFlexTable.setWidget(1, 1, addLabel("Action Export"));

        tFlexTable.setWidget(2, 0, addLabel("JVM Name"));
        tFlexTable.setWidget(2, 1, addData(pFlow.getJvmIdentifier()));
        tFlexTable.setWidget(2, 2, addLabel("Thread Name"));
        tFlexTable.setWidget(2, 3, addData(pFlow.getThreadName()));

        tFlexTable.setWidget(3, 0, addLabel("Begin Time"));
        tFlexTable.setWidget(3, 1, addData(pFlow.getBeginTime()));
        tFlexTable.setWidget(3, 2, addLabel("End Time"));
        tFlexTable.setWidget(3, 3, addData(pFlow.getEndTime()));

        tFlexTable.setWidget(4, 0, addLabel("Duration"));
        tFlexTable.setWidget(4, 1, addData(pFlow.getDuration()));
        tFlexTable.setWidget(4, 2, addLabel("Group name"));
        MethodCallDTO tFirstMethodCall = pFlow.getFirstMethodCall();
        tFlexTable.setWidget(4, 3, addData(tFirstMethodCall.getGroupName()));

        tFlexTable.setWidget(5, 0, addLabel("Appel"));
        tFlexTable.setWidget(5, 1, addData(pFlow.getClassName() + "." + pFlow.getMethodName()));

        HorizontalPanel tPanel = new HorizontalPanel();
        tPanel.add(new HTML("[" + tFirstMethodCall.getDuration() + "] " + pFlow.getJvmIdentifier() + " / "
            + pFlow.getThreadName() + " : " + tFirstMethodCall.getGroupName() + " -> "
            + tFirstMethodCall.getClassName() + tFirstMethodCall.getMethodName() + "()"));
        MethCallTreeItem tRoot = new MethCallTreeItem(-1, tPanel);
        tRoot.mIsLoaded = true;

        for (MethodCallDTO tFirstLevel : tFirstMethodCall.getChildren())
        {
            tRoot.addItem(getTreeItem(tFirstLevel));
        }
        ExecFlowTree tTree = new ExecFlowTree(pFlow.getId());
        tTree.addItem(tRoot);
        tFlexTable.setWidget(6, 0, tTree);
        tFlexTable.getFlexCellFormatter().setColSpan(6, 0, 4);

        add(tFlexTable);
    }

    private MethCallTreeItem getTreeItem(MethodCallDTO pMeth)
    {
        return new MethCallTreeItem(pMeth.getPosition(), pMeth);
    }

    private static class ExecFlowTree extends Tree
    {
        private int mFlowId;

        private ExecFlowTree(int pFlowId)
        {

        }
    }

    private static class MethCallTreeItem extends TreeItem
    {
        private final int mMethId;

        private boolean mIsLoaded;

        public MethCallTreeItem(int pMethId, Widget tWidget)
        {
            super(tWidget);
            mMethId = pMethId;
        }

        public MethCallTreeItem(int pMethId, MethodCallDTO pMeth)
        {
            this(pMethId, getWidget(pMeth));
        }

        private static Widget getWidget(MethodCallDTO pMeth)
        {
            return new HTML(pMeth.getClassName() + "." + pMeth.getMethodName());
        }

        private void addSubItem(List<MethodCallDTO> pItem)
        {
            mIsLoaded = true;
            for (MethodCallDTO tMethodCallDTO : pItem)
            {
                addItem(getWidget(tMethodCallDTO));
            }
        }
    }

    private static class ExecFlowTreeListener implements TreeListener
    {

        public void onTreeItemSelected(TreeItem pTreeitem)
        {
            // @todo Auto-generated method stub

        }

        public void onTreeItemStateChanged(TreeItem pTreeitem)
        {
            MethCallTreeItem tItem = (MethCallTreeItem) pTreeitem;
            if (tItem.getState())
            {

            }
        }

    }
}
