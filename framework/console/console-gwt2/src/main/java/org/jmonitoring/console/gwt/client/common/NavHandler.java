package org.jmonitoring.console.gwt.client.common;

import org.jmonitoring.console.gwt.client.ClientFactory;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.place.shared.Place;

public class NavHandler implements ClickHandler
{
    private Place placeToGo;

    public NavHandler(Place pPlace)
    {
        placeToGo = pPlace;
    }

    public void onClick(ClickEvent pEvent)
    {
        ClientFactory.goTo(placeToGo);
    }

}
