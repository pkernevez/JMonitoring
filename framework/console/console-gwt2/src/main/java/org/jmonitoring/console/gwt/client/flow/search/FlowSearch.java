package org.jmonitoring.console.gwt.client.flow.search;

import it.pianetatecno.gwt.utility.client.table.PagingTable;

import org.jmonitoring.console.gwt.client.common.GwtRemoteService;
import org.jmonitoring.console.gwt.client.common.GwtRemoteServiceAsync;
import org.jmonitoring.console.gwt.shared.flow.FlowExtractDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class FlowSearch extends Composite
{

    private static FlowSearchUiBinder sUiBinder = GWT.create(FlowSearchUiBinder.class);

    // private static ConsoleImageBundle sResources = GWT.create(ConsoleImageBundle.class);

    // private static Logger sLog = Logger.getLogger(FlowSearch.class.getName());

    @UiField
    Image buttonOk;

    @UiField
    TextBox thread;

    @UiField
    TextBox minDuration;

    @UiField
    DateBox beginDate;

    @UiField
    TextBox firstMeasureClassName;

    @UiField
    TextBox firstMeasureMethodName;

    @UiField
    SimplePanel searchResult;

    private FlowSearchActivity presenter;

    interface FlowSearchUiBinder extends UiBinder<Widget, FlowSearch>
    {
    }

    GwtRemoteServiceAsync service = GWT.create(GwtRemoteService.class);

    PagingTable<FlowExtractDTO> table;

    public FlowSearch()
    {
        initWidget(sUiBinder.createAndBindUi(this));
        // TODO Replace this hack with a changeTitleEvent
        Window.setTitle("Flow search");
        beginDate.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd/MM/yy")));
        beginDate.getTextBox().setVisibleLength(8);
        beginDate.getTextBox().setMaxLength(8);
        beginDate.getTextBox().addKeyPressHandler(new KeyPressHandler()
        {
            public void onKeyPress(KeyPressEvent pEvent)
            {
                presenter.filterData(FlowSearch.this, pEvent);
            }
        });
        table = new FlowSearchTableModel(service).getTable();
        searchResult.add(table);
    }

    @UiHandler("buttonOk")
    void onButtonOkClick(ClickEvent pEvent)
    {
        presenter.filterData(this, null);
    }

    @UiHandler("thread")
    void onThreadKeyPress(KeyPressEvent pEvent)
    {
        presenter.filterData(this, pEvent);
    }

    @UiHandler("minDuration")
    void onMinDurationKeyPress(KeyPressEvent pEvent)
    {
        presenter.filterData(this, pEvent);
    }

    @UiHandler("firstMeasureClassName")
    void onFirstMeasureClassNameKeyPress(KeyPressEvent pEvent)
    {
        presenter.filterData(this, pEvent);
    }

    @UiHandler("firstMeasureMethodName")
    void onFirstMeasureMethodNameKeyPress(KeyPressEvent pEvent)
    {
        presenter.filterData(this, pEvent);
    }

    public FlowSearch setPresenter(FlowSearchActivity pFlowSearchActivity)
    {
        presenter = pFlowSearchActivity;
        return this;
    }

}
