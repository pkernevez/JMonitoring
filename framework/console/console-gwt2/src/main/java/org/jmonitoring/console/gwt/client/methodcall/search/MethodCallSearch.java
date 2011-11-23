package org.jmonitoring.console.gwt.client.methodcall.search;

import it.pianetatecno.gwt.utility.client.table.PagingTable;

import org.jmonitoring.console.gwt.client.common.GwtRemoteService;
import org.jmonitoring.console.gwt.client.common.GwtRemoteServiceAsync;
import org.jmonitoring.console.gwt.client.flow.search.FlowSearchTableModel;
import org.jmonitoring.console.gwt.shared.flow.FlowExtractDTO;
import org.jmonitoring.console.gwt.shared.method.MethodCallSearchCriterion;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class MethodCallSearch extends Composite implements Editor<MethodCallSearchCriterion>
{
    @UiField
    TextBox flowThread;

    @UiField
    TextBox flowServer;

    @UiField
    DateBox flowBeginDate;

    @UiField
    TextBox flowMinDuration;

    @UiField
    TextBox className;

    @UiField
    TextBox methodName;

    @UiField
    TextBox position;

    @UiField
    TextBox parentPosition;

    @UiField
    TextBox parameters;

    @UiField
    TextBox returnValue;

    @UiField
    TextBox thrownExceptionClass;

    @UiField
    TextBox thrownExceptionMessage;

    PagingTable<FlowExtractDTO> table;

    @UiField
    SimplePanel searchResult;

    private static MethodCallSearchUiBinder uiBinder = GWT.create(MethodCallSearchUiBinder.class);

    private MethodCallSearchActivity presenter;

    GwtRemoteServiceAsync service = GWT.create(GwtRemoteService.class);

    interface MethodCallSearchUiBinder extends UiBinder<Widget, MethodCallSearch>
    {
    }

    public MethodCallSearch()
    {
        initWidget(uiBinder.createAndBindUi(this));
        Window.setTitle("Method call detail");
        flowBeginDate.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd/MM/yy")));
        flowBeginDate.getTextBox().setVisibleLength(8);
        flowBeginDate.getTextBox().setMaxLength(8);
        flowBeginDate.getTextBox().addKeyPressHandler(new KeyPressHandler()
        {
            public void onKeyPress(KeyPressEvent pEvent)
            {
                presenter.filterData(MethodCallSearch.this, pEvent);
            }
        });
        table = new FlowSearchTableModel(service).getTable();
        searchResult.add(table);

    }

    public MethodCallSearch setPresenter(MethodCallSearchActivity pActivity)
    {
        presenter = pActivity;
        return this;
    }

}
