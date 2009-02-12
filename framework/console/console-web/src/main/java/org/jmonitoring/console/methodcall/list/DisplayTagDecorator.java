package org.jmonitoring.console.methodcall.list;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import org.displaytag.decorator.TableDecorator;
import org.jmonitoring.core.dto.MethodCallFullExtractDTO;

/**
 * @author pke Decorator for the column of the list of MEasurePoint.
 */
public class DisplayTagDecorator extends TableDecorator
{
    public CharSequence getUrlMeasurePoint()
    {
        MethodCallFullExtractDTO tMeth = (MethodCallFullExtractDTO) getCurrentRowObject();
        StringBuilder tResult = new StringBuilder();
        tResult.append("<A href=\"");
        tResult.append("MethodCallEditIn.do?flowId=").append(tMeth.getFlowId()).append("&position=");
        tResult.append(tMeth.getPosition());
        tResult.append("\" title=\"View this method call\"><IMG src=\"images/edit.png\"/></A>");
        return tResult;
    }

    public CharSequence getUrlFlow()
    {
        MethodCallFullExtractDTO tMeth = (MethodCallFullExtractDTO) getCurrentRowObject();
        StringBuilder tResult = new StringBuilder();
        tResult.append("<A href=\"");
        tResult.append("FlowEditIn.do?id=").append(tMeth.getFlowId())
               .append("\" title=\"View this Flow\"><IMG src=\"images/edit.png\"/></A>");
        return tResult;
    }
}
