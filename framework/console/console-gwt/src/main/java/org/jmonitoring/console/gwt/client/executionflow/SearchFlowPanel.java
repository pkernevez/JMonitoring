package org.jmonitoring.console.gwt.client.executionflow;

import static org.jmonitoring.console.gwt.client.PanelUtil.addLabel;
import static org.jmonitoring.console.gwt.client.PanelUtil.addTitle;

import java.util.List;

import org.jmonitoring.console.gwt.client.JMonitoring;
import org.jmonitoring.console.gwt.client.dto.ExecutionFlowDTO;
import org.jmonitoring.console.gwt.client.main.Controller;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
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

    private final JMonitoring mMain;

    private Image mImage;

    private final VerticalPanel mCriteria = new VerticalPanel();

    private final SimplePanel mResult = new SimplePanel();

    public SearchFlowPanel(JMonitoring pMain)
    {
        mMain = pMain;
        addMask();
        add(mCriteria);
        add(mResult);
    }

    private void addMask()
    {
        FlexTable tTable = new FlexTable();
        tTable.setWidget(0, 0, addTitle("Search Flows"));
        tTable.getFlexCellFormatter().setColSpan(0, 0, 4);

        tTable.setWidget(1, 0, addLabel("Server"));
        tTable.setWidget(1, 1, mServer);

        tTable.setWidget(2, 0, addLabel("Thread name"));
        tTable.setWidget(2, 1, mThreadName);
        tTable.setWidget(2, 2, addLabel("Minimum Duration"));
        tTable.setWidget(2, 3, mMinimumDuration);

        tTable.setWidget(3, 0, addLabel("Group name"));
        tTable.setWidget(3, 1, mGroupName);
        tTable.setWidget(3, 2, addLabel("Begin date (dd/MM/yy)"));
        tTable.setWidget(3, 3, mBeginDate);

        tTable.setWidget(4, 0, addLabel("First measure class name"));
        tTable.setWidget(4, 1, mFirstMesureClassName);
        tTable.setWidget(4, 2, addLabel("First measure method name"));
        tTable.setWidget(4, 3, mFirstMeasureMethodName);

        mImage = createLinkedImage(mMain.getImageBundle().ok(), "Search corresponding flows", mSearchClickListener);
        tTable.setWidget(5, 0, mImage);
        mCriteria.add(tTable);
    }

    public static Image createLinkedImage(AbstractImagePrototype pImagePrototype, String pTitle, ClickListener pListener)
    {
        Image tImage = pImagePrototype.createImage();
        tImage.setStylePrimaryName("click-image");
        tImage.addMouseListener(new MouseListenerAdapter()
        {

            @Override
            public void onMouseEnter(Widget pWidget)
            {
                pWidget.addStyleDependentName("hover");
            }

            @Override
            public void onMouseLeave(Widget pWidget)
            {
                pWidget.removeStyleDependentName("hover");
            }
        });
        tImage.addClickListener(pListener);
        tImage.setTitle(pTitle);
        return tImage;
    }

    private final ClickListener mSearchClickListener = new ClickListener()
    {
        public void onClick(Widget pWidget)
        {
            callSearch();
        }
    };

    protected void callSearch()
    {
        ExecutionFlowServiceAsync tService = GWT.create(ExecutionFlowService.class);
        ServiceDefTarget tEndpoint = (ServiceDefTarget) tService;
        tEndpoint.setServiceEntryPoint(JMonitoring.SERVICE_URL);
        AsyncCallback<List<ExecutionFlowDTO>> tCallBack = new AsyncCallback<List<ExecutionFlowDTO>>()
        {
            public void onFailure(Throwable e)
            {
                GWT.log("Error", e);
                mResult.clear();
                mResult.add(new HTML("<h2 class=\"error\">Unexpected error on server</h2>"));
            }

            public void onSuccess(List<ExecutionFlowDTO> pList)
            {
                bind(pList);
            }

        };
        tService.search(getCriteria(), tCallBack);

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
            Hyperlink tLink = new Hyperlink("" + tDto.getId(), Controller.HISTORY_EDIT_FLOW + tDto.getId());
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

}
