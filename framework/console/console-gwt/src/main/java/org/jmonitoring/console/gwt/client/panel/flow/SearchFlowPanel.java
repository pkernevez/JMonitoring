package org.jmonitoring.console.gwt.client.panel.flow;

import static org.jmonitoring.console.gwt.client.panel.PanelUtil.*;

import java.util.List;

import org.jmonitoring.console.gwt.client.JMonitoring;
import org.jmonitoring.console.gwt.client.dto.ExecutionFlowDTO;
import org.jmonitoring.console.gwt.client.main.Controller;
import org.jmonitoring.console.gwt.client.main.DefaultCallBack;
import org.jmonitoring.console.gwt.client.panel.PanelUtil;
import org.jmonitoring.console.gwt.client.service.ExecutionFlowServiceAsync;
import org.jmonitoring.console.gwt.client.service.SearchCriteria;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class SearchFlowPanel extends VerticalPanel
{
    private final TextBox mThreadName = new TextBox();

    private final TextBox mGroupName = new TextBox();

    private final TextBox mMinimumDuration = new TextBox();

    private final TextBox mBeginDate = new TextBox();

    private final TextBox mServer = new TextBox();

    private final TextBox mFirstMesureClassName = new TextBox();

    private final TextBox mFirstMeasureMethodName = new TextBox();

    private Image mImage;

    private final VerticalPanel mCriteria = new VerticalPanel();

    private final SimplePanel mResult = new SimplePanel();

    private Panel mMessage;

    public SearchFlowPanel()
    {
        addMask();
        add(mCriteria);
        add(mResult);
    }

    private void addMask()
    {
        FlexTable tTable = new FlexTable();
        int curLine = 0;
        mMessage = new SimplePanel();
        tTable.setWidget(curLine, 0, mMessage);
        tTable.getFlexCellFormatter().setColSpan(0, 0, 4);

        tTable.setWidget(++curLine, 0, createTitle("Search Flows"));
        tTable.getFlexCellFormatter().setColSpan(0, 0, 4);

        tTable.setWidget(++curLine, 0, createLabel("Server"));
        tTable.setWidget(curLine, 1, mServer);

        tTable.setWidget(++curLine, 0, createLabel("Thread name"));
        tTable.setWidget(curLine, 1, mThreadName);
        tTable.setWidget(curLine, 2, createLabel("Minimum Duration"));
        tTable.setWidget(curLine, 3, mMinimumDuration);

        tTable.setWidget(++curLine, 0, createLabel("Group name"));
        tTable.setWidget(curLine, 1, mGroupName);
        tTable.setWidget(curLine, 2, createLabel("Begin date (dd/MM/yy)"));
        tTable.setWidget(curLine, 3, mBeginDate);

        tTable.setWidget(++curLine, 0, createLabel("First measure class name"));
        tTable.setWidget(curLine, 1, mFirstMesureClassName);
        tTable.setWidget(curLine, 2, createLabel("First measure method name"));
        tTable.setWidget(curLine, 3, mFirstMeasureMethodName);

        mImage = PanelUtil.createClickImage(JMonitoring.getImageBundle().ok(), "Search flows", mSearchClickListener);
        tTable.setWidget(++curLine, 0, mImage);
        mCriteria.add(tTable);
    }

    private final ClickListener mSearchClickListener = new ClickListener()
    {
        public void onClick(Widget pWidget)
        {
            callSearch();
        }
    };

    public void callSearch()
    {
        ExecutionFlowServiceAsync tService = Controller.getService();
        mResult.clear();
        clearMsg();
        tService.search(getCriteria(), new DefaultCallBack<List<ExecutionFlowDTO>>()
        {
            public void onSuccess(List<ExecutionFlowDTO> pList)
            {
                bind(pList);
            }
        });

    }

    private SearchCriteria getCriteria()
    {
        SearchCriteria tCrit = new SearchCriteria();
        tCrit.setThreadName(mThreadName.getText());
        tCrit.setServer(mServer.getText());
        tCrit.setMinimumDuration(mMinimumDuration.getText());
        tCrit.setGroupName(mGroupName.getText());
        tCrit.setBeginDate(mBeginDate.getText());
        tCrit.setClassName(mFirstMesureClassName.getText());
        tCrit.setMethodName(mFirstMeasureMethodName.getText());
        return tCrit;
    }

    private void bind(List<ExecutionFlowDTO> pList)
    {
        mResult.clear();
        FlexTable tTable = new FlexTable();
        tTable.setBorderWidth(1);
        tTable.setWidget(0, 0, new HTML("Tech Id"));
        tTable.setWidget(0, 1, new HTML("Thread"));
        tTable.setWidget(0, 2, new HTML("Server"));
        tTable.setWidget(0, 3, new HTML("Duration"));
        tTable.setWidget(0, 4, new HTML("Begin"));
        tTable.setWidget(0, 5, new HTML("End"));
        tTable.setWidget(0, 6, new HTML("Class"));
        tTable.setWidget(0, 7, new HTML("Method"));
        int i = 1;
        for (ExecutionFlowDTO tDto : pList)
        {
            Widget tLink = createHyperLink("" + tDto.getId(), Controller.HISTORY_EDIT_FLOW + tDto.getId());
            tTable.setWidget(i, 0, tLink);
            tTable.setWidget(i, 1, new HTML(tDto.getThreadName()));
            tTable.setWidget(i, 2, new HTML(tDto.getJvmIdentifier()));
            tTable.setWidget(i, 3, new HTML("" + tDto.getDuration()));
            tTable.setWidget(i, 4, new HTML(tDto.getBeginTime()));
            tTable.setWidget(i, 5, new HTML(tDto.getEndTime()));
            tTable.setWidget(i, 6, new HTML(tDto.getClassName()));
            tTable.setWidget(i, 7, new HTML(tDto.getMethodName()));
            i++;
        }
        mResult.add(tTable);
    }

    private void clearMsg()
    {
        mMessage.clear();
    }

    public void setInfoMsg(String pMsg)
    {
        mMessage.add(new HTML("<div class=\"info\">" + pMsg + "</div>"));
    }
}
