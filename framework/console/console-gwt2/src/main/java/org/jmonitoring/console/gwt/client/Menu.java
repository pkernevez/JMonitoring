package org.jmonitoring.console.gwt.client;

import org.jmonitoring.console.gwt.client.flow.delete.FlowDeletePlace;
import org.jmonitoring.console.gwt.client.flow.importt.FlowImportPlace;
import org.jmonitoring.console.gwt.client.flow.search.FlowSearchPlace;
import org.jmonitoring.console.gwt.client.methodcall.search.MethodCallSearchPlace;
import org.jmonitoring.console.gwt.client.methodcall.treesearch.MethodCallTreeSearchPlace;

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
        tSubMenu.add(getItem("Delete Flows", ClientFactory.getToken(new FlowDeletePlace())));
        tSubMenu.add(getItem("Flows Search", ClientFactory.getToken(new FlowSearchPlace())));
        tSubMenu.add(getItem("Methods Tree", ClientFactory.getToken(new MethodCallTreeSearchPlace())));
        tSubMenu.add(getItem("Methods Search", ClientFactory.getToken(new MethodCallSearchPlace())));
        tSubMenu.add(getItem("Import Flow", ClientFactory.getToken(new FlowImportPlace())));
        this.add(tSubMenu);
    }

    private Widget getItem(String pLabel, String pHistoryToken)
    {
        Hyperlink tLink = new Hyperlink(pLabel, pHistoryToken);
        tLink.setStyleName("menu-item");
        return tLink;
    }

}
