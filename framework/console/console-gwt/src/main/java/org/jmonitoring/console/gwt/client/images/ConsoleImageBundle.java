package org.jmonitoring.console.gwt.client.images;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public interface ConsoleImageBundle extends ImageBundle
{

    @Resource("org/jmonitoring/console/gwt/client/images/jmonitoring.gif")
    public AbstractImagePrototype jmonitoring();

    public AbstractImagePrototype edit();
}
