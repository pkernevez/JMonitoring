package org.jmonitoring.console.flow.stack;

import junit.framework.TestCase;

import org.apache.taglibs.standard.lang.jpath.encoding.HtmlEncoder;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestFlowAsStackUtil extends TestCase
{

    public void testHtmlEncode()
    {
        String tTest = " & < > \" ";
        assertEquals(" &amp; &lt; &gt; &quot; ", HtmlEncoder.encode(tTest));
    }
}
