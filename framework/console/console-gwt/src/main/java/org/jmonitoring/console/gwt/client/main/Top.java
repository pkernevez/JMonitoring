package org.jmonitoring.console.gwt.client.main;

import org.jmonitoring.console.gwt.client.images.ConsoleImageBundle;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class Top extends HorizontalPanel
{
    public Top(ConsoleImageBundle pBundle)
    {
        setStyleName("header");
        AbstractImagePrototype tProto = pBundle.jmonitoring();
        Image tLogo = tProto.createImage();
        // tLogo.setStyleName("");
        add(tLogo);
    }
}
