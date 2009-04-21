package org.jmonitoring.console.gwt.client.panel.methodcall;

import static org.jmonitoring.console.gwt.client.panel.PanelUtil.*;

import org.jmonitoring.console.gwt.client.JMonitoring;
import org.jmonitoring.console.gwt.client.dto.StatMethodCallDTO;
import org.jmonitoring.console.gwt.client.service.ExecutionFlowService;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class StatMethodCallPanel extends VerticalPanel implements ClickListener
{
    private final StatMethodCallDTO mData;

    private final TextBox mAggregationScope = createTextBox();

    public StatMethodCallPanel(StatMethodCallDTO pData)
    {
        super();
        mData = pData;

        add(createTitle("Method Call Details"));
        add(createSubTitle("Statistics of " + mData.getFullMethodCallName()));

        FlexTable tFlexTable = new FlexTable();
        int curLine = 0;
        tFlexTable.setWidget(curLine, 0, createLabel("Number of occurences found:"));
        tFlexTable.setWidget(curLine, 0, createData(mData.getNbOccurence()));

        tFlexTable.setWidget(++curLine, 0, createLabel("Duration min"));
        tFlexTable.setWidget(curLine, 0, createData(mData.getDurationMin()));

        tFlexTable.setWidget(++curLine, 0, createLabel("Duration avg"));
        tFlexTable.setWidget(curLine, 0, createData(mData.getDurationAvg()));

        tFlexTable.setWidget(++curLine, 0, createLabel("Duration max"));
        tFlexTable.setWidget(curLine, 0, createData(mData.getDurationMax()));

        tFlexTable.setWidget(++curLine, 0, createLabel("Duration deviance"));
        tFlexTable.setWidget(curLine, 0, createData(mData.getDurationDev()));
        add(tFlexTable);

        HorizontalPanel tPanel = new HorizontalPanel();
        tPanel.add(createLabel("Aggregation scope:"));
        tPanel.add(mAggregationScope);
        mAggregationScope.setText(mData.getAggregationScope());
        tPanel.add(createLabel("ms"));
        tPanel.add(createClickImage(JMonitoring.getImageBundle().refresh(),
                                    "Refresh the image with the new aggregation scope", this));
        add(tPanel);
        add(createImage(ExecutionFlowService.STAT_METHOD_CALL));
    }

    public void onClick(Widget pWidget)
    {

    }

}
