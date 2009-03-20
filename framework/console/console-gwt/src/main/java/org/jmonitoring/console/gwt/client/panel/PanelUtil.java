package org.jmonitoring.console.gwt.client.panel;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.Widget;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class PanelUtil
{

    public static Widget addLabel(String pText)
    {
        return new HTML(pText);
    }

    public static Widget addData(String pText)
    {
        return new HTML(pText);
    }

    public static Widget addData(int pValue)
    {
        return new HTML("" + pValue);
    }

    public static Widget addData(long pValue)
    {
        return new HTML("" + pValue);
    }

    public static Widget addTitle(String pTitle)
    {
        return new HTML("<h1>" + pTitle + "</h1>");
    }

    public static Image createClickImage(AbstractImagePrototype pImagePrototype, String pTitle, String pUrl)
    {
        return createClickImage(pImagePrototype, pTitle, new NavigationListener(pUrl));
    }

    public static Image createClickImage(AbstractImagePrototype pImagePrototype, String pTitle, ClickListener pListener)
    {
        Image tImage = pImagePrototype.createImage();
        tImage.setStylePrimaryName("click-image");
        tImage.addMouseListener(new MouseListenerAdapter()
        {

            @Override
            public void onMouseEnter(Widget pWidget)
            {
                pWidget.addStyleDependentName("hover");
            }

            @Override
            public void onMouseLeave(Widget pWidget)
            {
                pWidget.removeStyleDependentName("hover");
            }
        });
        tImage.addClickListener(pListener);
        tImage.setTitle(pTitle);
        return tImage;
    }

    private static class NavigationListener implements ClickListener
    {
        String mUrl;

        public NavigationListener(String pTarget)
        {
            mUrl = pTarget;
        }

        public void onClick(Widget pWidget)
        {
            History.newItem(mUrl);
            History.fireCurrentHistoryState();
        }
    }
}
