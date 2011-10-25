package org.jmonitoring.console.gwt.client;

import org.jmonitoring.console.gwt.client.flow.detail.FlowDetailPlace;
import org.jmonitoring.console.gwt.client.flow.search.FlowSearchPlace;

import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class Menu extends VerticalPanel
{
    public Menu()
    {
        VerticalPanel tSubMenu = new VerticalPanel();
        // tSubMenu.add(getItem("Home", Controller.HISTORY_HOME));
        tSubMenu.add(getItem("Delete Flows", ClientFactory.getToken(new FlowDetailPlace(0))));
        tSubMenu.add(getItem("Flows Search", ClientFactory.getToken(new FlowSearchPlace())));
        tSubMenu.add(getItem("Search Methods", "search"));
        // tSubMenu.add(getItem("Import Flow", Controller.HISTORY_IMPORT_FLOW));
        // this.setStyleName("menu");
        this.add(tSubMenu);
    }

    private Widget getItem(String pLabel, String pHistoryToken)
    {
        Hyperlink tLink = new Hyperlink(pLabel, pHistoryToken);
        tLink.setStyleName("menu-item");
        return tLink;
    }

}
