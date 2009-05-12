package org.jmonitoring.console.gwt.client.panel.methodcall;

import static org.jmonitoring.console.gwt.client.panel.PanelUtil.*;

import java.util.List;

import org.jmonitoring.console.gwt.client.JMonitoring;
import org.jmonitoring.console.gwt.client.dto.StatMapAreaDTO;
import org.jmonitoring.console.gwt.client.dto.StatMethodCallDTO;
import org.jmonitoring.console.gwt.client.main.Controller;
import org.jmonitoring.console.gwt.client.main.DefaultCallBack;
import org.jmonitoring.console.gwt.client.panel.flow.image.AreaWidget;
import org.jmonitoring.console.gwt.client.panel.flow.image.MapWidget;
import org.jmonitoring.console.gwt.client.service.ExecutionFlowService;
import org.jmonitoring.console.gwt.client.service.ExecutionFlowServiceAsync;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class StatMethodCallPanel extends VerticalPanel implements ClickListener, KeyboardListener
{
    private static final String MS = " ms";

    private StatMethodCallDTO mData;

    private final TextBox mAggregationScope = createTextBox(6, 6, this);

    private final FlexTable mFlexTable;

    private final Panel mStatImage;

    public StatMethodCallPanel(StatMethodCallDTO pData)
    {
        super();
        mData = pData;

        add(createTitle("Method Call Details"));
        add(createSubTitle("Statistics of " + mData.getFullMethodCallName()));

        mFlexTable = new FlexTable();
        int curLine = 0;
        mFlexTable.setWidget(curLine, 0, createLabel("Number of occurences found:"));
        mFlexTable.setWidget(curLine, 1, createData(mData.getNbOccurence()));

        mFlexTable.setWidget(++curLine, 0, createLabel("Duration min"));
        mFlexTable.setWidget(curLine, 1, createData(mData.getDurationMin() + MS));

        mFlexTable.setWidget(++curLine, 0, createLabel("Duration avg"));
        mFlexTable.setWidget(curLine, 1, createData(mData.getDurationAvg() + MS));

        mFlexTable.setWidget(++curLine, 0, createLabel("Duration max"));
        mFlexTable.setWidget(curLine, 1, createData(mData.getDurationMax() + MS));

        mFlexTable.setWidget(++curLine, 0, createLabel("Duration deviance"));
        mFlexTable.setWidget(curLine, 1, createData(mData.getDurationDev() + MS));
        add(mFlexTable);

        HorizontalPanel tPanel = new HorizontalPanel();
        tPanel.add(createLabel("Aggregation scope:"));
        tPanel.add(mAggregationScope);
        mAggregationScope.setText("" + mData.getAggregationScope());
        tPanel.add(createLabel(MS + "&nbsp;&nbsp;"));
        tPanel.add(createClickImage(JMonitoring.getImageBundle().refresh(),
                                    "Refresh the image with the new aggregation scope", this));
        add(tPanel);
        MapWidget tMap = addMap(mData.getImageMap());
        mStatImage = new SimplePanel();
        Image tCreateImage = createImage(ExecutionFlowService.STAT_METHOD_CALL);
        mStatImage.add(tCreateImage);
        tMap.bindImage(tCreateImage);
        add(mStatImage);
    }

    private MapWidget addMap(List<StatMapAreaDTO> pImageMap)
    {
        AreaWidget[] tWidgets = new AreaWidget[pImageMap.size()];
        int i = 0;
        for (StatMapAreaDTO curArea : pImageMap)
        {
            String tUrl =
                Controller.createListMethodToken(mData.getClassName(), mData.getMethodName(), curArea.getDurationMin(),
                                                 curArea.getDurationMax());
            AreaWidget tCurWidget = new AreaWidget("RECT", curArea.getCoordinate(), new MethodListCommand(tUrl), tUrl);
            tWidgets[i++] = tCurWidget;
        }
        MapWidget tMap = new MapWidget(tWidgets, "ChartBar" + Math.random());
        add(tMap);
        return tMap;
    }

    private static class MethodListCommand implements Command
    {
        private final String mUrl;

        public MethodListCommand(String pUrl)
        {
            super();
            mUrl = pUrl;
        }

        public void execute()
        {
            History.newItem(mUrl);
        }
    }

    public void onClick(Widget pWidget)
    {
        reloadMap();
    }

    public void onKeyDown(Widget pWidget, char pC, int pI)
    {
    }

    public void onKeyPress(Widget pWidget, char pChar, int pI)
    {
        if (pChar == '\r')
        {
            reloadMap();
        }
    }

    private void reloadMap()
    {
        ExecutionFlowServiceAsync tService = Controller.getService();
        AsyncCallback<StatMethodCallDTO> tCallBack = new DefaultCallBack<StatMethodCallDTO>()
        {
            public void onSuccess(StatMethodCallDTO pMeth)
            {
                mData = pMeth;
                Image tCreateImage = createImage(ExecutionFlowService.STAT_METHOD_CALL);
                mStatImage.clear();
                mStatImage.add(tCreateImage);
                MapWidget tMap = addMap(mData.getImageMap());
                tMap.bindImage(tCreateImage);
            }

        };
        int tParseInt = Integer.parseInt(mAggregationScope.getText());
        String tToken = Controller.createStatToken(mData.getClassName(), mData.getMethodName(), tParseInt);
        History.newItem(tToken);
        tService.loadStat(mData.getClassName(), mData.getMethodName(), tParseInt, tCallBack);
    }

    public void onKeyUp(Widget pWidget, char pC, int pI)
    {
    }

}
