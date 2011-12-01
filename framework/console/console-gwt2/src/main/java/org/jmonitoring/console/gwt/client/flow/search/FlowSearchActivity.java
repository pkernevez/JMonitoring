package org.jmonitoring.console.gwt.client.flow.search;

import it.pianetatecno.gwt.utility.client.table.Filter;
import it.pianetatecno.gwt.utility.client.table.StringFilter;

import java.util.ArrayList;
import java.util.List;

import org.jmonitoring.console.gwt.client.ClientFactory;
import org.jmonitoring.console.gwt.shared.flow.HibernateConstant;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

class FlowSearchActivity extends AbstractActivity
{
    private FlowSearchPlace place;

    private final ClientFactory clientFactory;

    public FlowSearchActivity(FlowSearchPlace pPlace, ClientFactory pClientFactory)
    {
        place = pPlace;
        clientFactory = pClientFactory;
    }

    public void start(AcceptsOneWidget panel, EventBus eventBus)
    {

        FlowSearch tPanel = clientFactory.getFlowSearch();
        tPanel.thread.setText(place.thread);
        tPanel.minDuration.setText(place.minDuration);
        tPanel.firstMeasureClassName.setText(place.firstMeasureClassName);
        tPanel.firstMeasureMethodName.setText(place.firstMeasureMethodName);
        tPanel.beginDate.getTextBox().setText(place.beginDate);

        tPanel.table.filterData(createFilters());
        panel.setWidget(tPanel.setPresenter(this));
    }

    void filterData(FlowSearch pFlowSearch, KeyPressEvent pEvent)
    {
        if (pEvent.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER)
        {
            place =
                new FlowSearchPlace(pFlowSearch.thread.getText(), pFlowSearch.minDuration.getText(),
                                    pFlowSearch.firstMeasureClassName.getText(),
                                    pFlowSearch.firstMeasureMethodName.getText(), pFlowSearch.beginDate.getTextBox()
                                                                                                       .getText());
            ClientFactory.addHistory(place);
            pFlowSearch.table.filterData(createFilters());
        }
    }

    List<Filter<?>> createFilters()
    {
        List<Filter<?>> tFilters = new ArrayList<Filter<?>>();
        addFilter(tFilters, place.thread, HibernateConstant.THREAD);
        addFilter(tFilters, place.firstMeasureClassName, HibernateConstant.FIRST_MEASURE_CLASS_NAME);
        addFilter(tFilters, place.minDuration, HibernateConstant.MIN_DURATION);
        addFilter(tFilters, place.beginDate, HibernateConstant.BEGIN_DATE);
        addFilter(tFilters, place.firstMeasureMethodName, HibernateConstant.FIRST_MEASURE_METHOD_NAME);
        return tFilters;
    }

    private void addFilter(List<Filter<?>> pFilters, String pText, String pPropertyName)
    {
        if (pText.length() > 0)
        {
            StringFilter curFilter = new StringFilter();
            curFilter.setPropertyName(pPropertyName);
            curFilter.setValue(pText);
            pFilters.add(curFilter);
        }
    }

}