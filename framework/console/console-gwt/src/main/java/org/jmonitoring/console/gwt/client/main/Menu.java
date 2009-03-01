package org.jmonitoring.console.gwt.client.main;

import org.jmonitoring.console.gwt.client.JMonitoring;
import org.jmonitoring.console.gwt.client.executionflow.SearchFlowPanel;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.SimplePanel;
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
        tSubMenu.add(getItem("Home", mHomeClickListener));
        tSubMenu.add(getItem("Delete Flows", null));
        tSubMenu.add(getItem("Search Flows", mSearchClickListener));
        tSubMenu.add(getItem("Search Methods", null));
        tSubMenu.add(getItem("Import Flow", null));
        this.setStyleName("menu");
        this.add(tSubMenu);
    }

    private Widget getItem(String pLabel, ClickListener pClickListener)
    {
        Hyperlink tLink = new Hyperlink(pLabel, pLabel);
        tLink.setStyleName("menu-item");
        tLink.addClickListener(pClickListener);
        return tLink;
    }

    private final ClickListener mHomeClickListener = new ClickListener()
    {
        public void onClick(Widget pWidget)
        {
            mMain.setContentMain(new SimplePanel());
        }
    };

    private final ClickListener mSearchClickListener = new ClickListener()
    {
        public void onClick(Widget pWidget)
        {
            mMain.setContentMain(new SearchFlowPanel(mMain.getImageBundle()));
        }
    };

}
