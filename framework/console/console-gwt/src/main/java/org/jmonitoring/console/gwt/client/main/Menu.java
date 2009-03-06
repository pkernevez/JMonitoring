package org.jmonitoring.console.gwt.client.main;

import org.jmonitoring.console.gwt.client.JMonitoring;

import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class Menu extends VerticalPanel
{
    private final JMonitoring mMain;

    public Menu(JMonitoring pMain)
    {
        mMain = pMain;
        VerticalPanel tSubMenu = new VerticalPanel();
        tSubMenu.add(getItem("Home", Controller.HISTORY_HOME));
        tSubMenu.add(getItem("Delete Flows", null));
        tSubMenu.add(getItem("Search Flows", Controller.HISTORY_SEARCH));
        tSubMenu.add(getItem("Search Methods", null));
        tSubMenu.add(getItem("Import Flow", null));
        this.setStyleName("menu");
        this.add(tSubMenu);
    }

    private Widget getItem(String pLabel, String pHistoryToken)
    {
        Hyperlink tLink = new Hyperlink(pLabel, pHistoryToken);
        tLink.setStyleName("menu-item");
        return tLink;
    }

}
