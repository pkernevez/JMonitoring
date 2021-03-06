package org.jmonitoring.console.gwt.client.methodcall.search;

import it.pianetatecno.gwt.utility.client.table.Filter;

import java.util.ArrayList;

import org.jmonitoring.console.gwt.client.ClientFactory;
import org.jmonitoring.console.gwt.client.common.GwtRemoteService;
import org.jmonitoring.console.gwt.client.common.GwtRemoteServiceAsync;
import org.jmonitoring.console.gwt.shared.method.MethodCallSearchCriterion;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class MethodCallSearchActivity extends AbstractActivity
{
    MethodCallSearchDriver driver = GWT.create(MethodCallSearchDriver.class);

    interface MethodCallSearchDriver extends SimpleBeanEditorDriver<MethodCallSearchCriterion, MethodCallSearch>
    {
    }

    private MethodCallSearchPlace place;

    private final ClientFactory clientFactory;

    GwtRemoteServiceAsync service = GWT.create(GwtRemoteService.class);

    public MethodCallSearchActivity(MethodCallSearchPlace pMethodCallDetailPlace, ClientFactory pClientFactory)
    {
        place = pMethodCallDetailPlace;
        clientFactory = pClientFactory;
    }

    public void start(final AcceptsOneWidget pPanel, EventBus pEventBus)
    {
        MethodCallSearch tMethodCallSearch = clientFactory.getMethodCallSearch();
        pPanel.setWidget(tMethodCallSearch.setPresenter(MethodCallSearchActivity.this));
        driver.initialize(tMethodCallSearch);
        MethodCallSearchCriterion tCriterion = place.getCriterion();
        driver.edit(tCriterion);
        filterData(tCriterion, tMethodCallSearch, null);
    }

    void filterData(MethodCallSearch pView, KeyPressEvent pEvent)
    {
        if (pEvent == null || pEvent.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER)
        {
            MethodCallSearchCriterion tCriterion = driver.flush();
            place = new MethodCallSearchPlace(tCriterion);
            ClientFactory.addHistory(place);

            filterData(tCriterion, pView, pEvent);
        }
    }

    void filterData(MethodCallSearchCriterion pCriterion, MethodCallSearch pView, KeyPressEvent pEvent)
    {
        pView.setSearchCriterion(pCriterion);
        pView.table.filterData(new ArrayList<Filter<?>>());
    }

}