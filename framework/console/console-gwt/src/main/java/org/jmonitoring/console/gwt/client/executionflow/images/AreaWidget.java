package org.jmonitoring.console.gwt.client.executionflow.images;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.UIObject;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class AreaWidget extends UIObject
{
    private Command mCommand;

    public AreaWidget(String pUrl)
    {
        setElement(DOM.createElement("area"));
        DOM.setElementAttribute(getElement(), "href", "#" + pUrl);
    }

    public AreaWidget(String pShape, String pCoords, Command pCommand, String pUrl)
    {
        this(pUrl);
        setShape(pShape);
        setCoords(pCoords);
        // setAlt(pAlt);
        this.mCommand = pCommand;
    }

    Command getCommand()
    {
        return mCommand;
    }

    // void setAlt(String pAlt)
    // {
    // DOM.setElementAttribute(getElement(), "alt", (pAlt == null) ? "area" : pAlt);
    // }

    void setCoords(String pCoords)
    {
        DOM.setElementAttribute(getElement(), "coords", (pCoords == null) ? "" : pCoords);
    }

    void setShape(String pShape)
    {
        DOM.setElementAttribute(getElement(), "shape", (pShape == null) ? "" : pShape);
    }

}
