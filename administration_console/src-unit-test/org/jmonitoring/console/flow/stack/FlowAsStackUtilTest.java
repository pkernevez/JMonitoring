package org.jmonitoring.console.flow.stack;

import org.apache.taglibs.standard.lang.jpath.encoding.HtmlEncoder;

import junit.framework.TestCase;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class FlowAsStackUtilTest extends TestCase
{

    public void testHtmlEncode()
    {
        String tTest = " & < > \" ";
        assertEquals(" &amp; &lt; &gt; &quot; ", HtmlEncoder.encode(tTest));
    }
}
