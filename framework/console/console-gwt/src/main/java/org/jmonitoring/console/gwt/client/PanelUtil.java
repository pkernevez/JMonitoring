package org.jmonitoring.console.gwt.client;

import com.google.gwt.user.client.ui.HTML;
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
}
