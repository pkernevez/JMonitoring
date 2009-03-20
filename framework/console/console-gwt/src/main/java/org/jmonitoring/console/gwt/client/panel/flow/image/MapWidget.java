package org.jmonitoring.console.gwt.client.panel.flow.image;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class MapWidget extends Widget
{
    private AreaWidget[] items;

    public MapWidget()
    {
        super();
        setElement(DOM.createElement("map"));
        sinkEvents(Event.ONCLICK);
    }

    public MapWidget(AreaWidget[] areas, String pId)
    {
        this();
        items = areas;
        if (items != null && items.length > 0)
        {
            for (int i = 0; i < items.length; i++)
            {
                DOM.appendChild(getElement(), items[i].getElement());
            }
        }
        setID(pId);
        setName(pId);
    }

    public void bindImage(Image image)
    {
        usemap(image.getElement(), getName());
    }

    native void usemap(Element element, String name)/*-{
                                     element.useMap = "#"+name;
                             }-*/;

    public String getID()
    {
        return DOM.getElementAttribute(getElement(), "id");
    }

    public String getName()
    {
        return DOM.getElementAttribute(getElement(), "name");
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onBrowserEvent(Event event)
    {
        switch (DOM.eventGetType(event))
        {
        case Event.ONCLICK:
            if (items != null && items.length > 0)
            {
                Element target = DOM.eventGetTarget(event);
                for (int i = 0; i < items.length; i++)
                {
                    if (DOM.compare(target, items[i].getElement()))
                    {
                        Command command = items[i].getCommand();
                        if (command != null)
                        {
                            DeferredCommand.addCommand(command);
                        }
                    }
                }
                DOM.eventPreventDefault(event);
                return;
            }
        }
        super.onBrowserEvent(event);
    }

    public void setID(String id)
    {
        DOM.setElementAttribute(getElement(), "id", (id == null) ? "" : id);
    }

    public void setName(String name)
    {
        DOM.setElementAttribute(getElement(), "name", (name == null) ? "" : name);
    }

}