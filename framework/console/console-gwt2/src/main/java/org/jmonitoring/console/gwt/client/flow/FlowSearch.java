package org.jmonitoring.console.gwt.client.flow;

import it.pianetatecno.gwt.utility.client.table.Filter;
import it.pianetatecno.gwt.utility.client.table.PagingTable;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.jmonitoring.console.gwt.shared.flow.FlowExtractDTO;
import org.jmonitoring.console.gwt.shared.flow.FlowSearchRequestDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class FlowSearch extends Composite implements Editor<FlowSearchRequestDTO>
{

    private static FlowSearchUiBinder uiBinder = GWT.create(FlowSearchUiBinder.class);

    private static Logger sLog = Logger.getLogger(FlowSearch.class.getName());

    @UiField
    Image image;

    @UiField
    TextBox thread;

    @UiField
    TextBox minDuration;

    @UiField
    TextBox group;

    @UiField
    DateBox beginDate;

    @UiField
    TextBox firstMeasureClassName;

    @UiField
    TextBox firstMeasureMethodName;

    @UiField
    VerticalPanel vPanel;

    interface FlowSearchUiBinder extends UiBinder<Widget, FlowSearch>
    {
    }

    interface RequestDTO extends SimpleBeanEditorDriver<FlowSearchRequestDTO, FlowSearch>
    {
    }

    RequestDTO driver = GWT.create(RequestDTO.class);

    FlowServiceAsync service = GWT.create(FlowService.class);

    public FlowSearch()
    {
        initWidget(uiBinder.createAndBindUi(this));
        beginDate.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd/MM/yy")));
        beginDate.getTextBox().setVisibleLength(8);
        beginDate.getTextBox().setMaxLength(8);
        driver.initialize(this);
        driver.edit(new FlowSearchRequestDTO());
    }

    @UiHandler("image")
    void onImageClick(ClickEvent event)
    {
        PagingTable<FlowExtractDTO> tTable = new FlowSearchTableModel(null, service).getTable();
        vPanel.add(tTable);
//        List<Filter> tFilters = new LinkedList<Filter>();
//        tTable.filterData(tFilters);
    }

}
