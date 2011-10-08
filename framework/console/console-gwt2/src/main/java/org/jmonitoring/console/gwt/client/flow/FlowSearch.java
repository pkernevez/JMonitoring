package org.jmonitoring.console.gwt.client.flow;

import it.pianetatecno.gwt.utility.client.table.Filter;
import it.pianetatecno.gwt.utility.client.table.PagingTable;
import it.pianetatecno.gwt.utility.client.table.StringFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


import org.jmonitoring.console.gwt.client.resources.ConsoleImageBundle;
import org.jmonitoring.console.gwt.shared.flow.FlowExtractDTO;
import org.jmonitoring.console.gwt.shared.flow.HibernateConstant;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.LocalizableResource.Key;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.event.logical.shared.ValueChangeEvent;

public class FlowSearch extends Composite
{

    private static FlowSearchUiBinder sUiBinder = GWT.create(FlowSearchUiBinder.class);

    private static ConsoleImageBundle sResources = GWT.create(ConsoleImageBundle.class);

//     private static Logger sLog = Logger.getLogger(FlowSearch.class.getName());

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

//    @UiFieldd
//    VerticalPanel vPanel;
    @UiField SimplePanel searchResult;

    interface FlowSearchUiBinder extends UiBinder<Widget, FlowSearch>
    {
    }

    FlowServiceAsync service = GWT.create(FlowService.class);

    private PagingTable<FlowExtractDTO> table;

    public FlowSearch()
    {
        initWidget(sUiBinder.createAndBindUi(this));
        beginDate.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd/MM/yy")));
        beginDate.getTextBox().setVisibleLength(8);
        beginDate.getTextBox().setMaxLength(8);
        beginDate.getTextBox().addKeyPressHandler(new KeyPressHandler()
        {
            public void onKeyPress(KeyPressEvent pEvent)
            {
                filterData(pEvent);
            }
        });
        table = new FlowSearchTableModel(service).getTable();
//        table.setStyleName(sResources.mainStyle().searchresult());
//        table.get().t
        searchResult.add(table);
    }

    @SuppressWarnings("rawtypes")
    private List<Filter> createFilters()
    {
        List<Filter> tFilters = new ArrayList<Filter>();
        addFilter(tFilters, thread, HibernateConstant.THREAD);
        addFilter(tFilters, firstMeasureClassName, HibernateConstant.FIRST_MEASURE_CLASS_NAME);
        addFilter(tFilters, minDuration, HibernateConstant.MIN_DURATION);
        addFilter(tFilters, beginDate, HibernateConstant.BEGIN_DATE);
        addFilter(tFilters, firstMeasureMethodName, HibernateConstant.FIRST_MEASURE_METHOD_NAME);
        return tFilters;
    }

    private void addFilter(@SuppressWarnings("rawtypes") List<Filter> pFilters, DateBox pDate, String pPropertyName)
    {
        addFilter(pFilters, pDate.getTextBox(), pPropertyName);
    }

    @SuppressWarnings("rawtypes")
    private void addFilter(List<Filter> pFilters, TextBox pTextBox, String pPropertyName)
    {
        if (pTextBox.getText().length() > 0)
        {
            StringFilter curFilter = new StringFilter();
            curFilter.setPropertyName(pPropertyName);
            curFilter.setValue(pTextBox.getText());
            pFilters.add(curFilter);
        }
    }

    private void filterData(KeyPressEvent pEvent)
    {
        if (pEvent == null || (pEvent.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER))
        {
            table.filterData(createFilters());
        }
    }

    @UiHandler("buttonOk")
    void onButtonOkClick(ClickEvent pEvent)
    {
        filterData(null);
    }

    @UiHandler("thread")
    void onThreadKeyPress(KeyPressEvent pEvent)
    {
        filterData(pEvent);
    }

    @UiHandler("minDuration")
    void onMinDurationKeyPress(KeyPressEvent pEvent)
    {
        filterData(pEvent);
    }
    @UiHandler("firstMeasureClassName")
    void onFirstMeasureClassNameKeyPress(KeyPressEvent pEvent) {
        filterData(pEvent);
    }
    @UiHandler("firstMeasureMethodName")
    void onFirstMeasureMethodNameKeyPress(KeyPressEvent pEvent) {
        filterData(pEvent);
    }
  
}
