package org.jmonitoring.console.measurepoint.list;

import org.apache.commons.beanutils.DynaBean;
import org.displaytag.decorator.TableDecorator;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * @author pke Decorator for the column of the list of MEasurePoint.
 */
public class DisplayTagDecorator extends TableDecorator
{
    public String getUrlMeasurePoint()
    {
        DynaBean tBean = (DynaBean) getCurrentRowObject();
        StringBuffer tResult = new StringBuffer();
        tResult.append("<A href=\"");
        tResult.append("MeasurePointEdit.do?flowId=").append( tBean.get("FLOW_ID")).append("&sequenceId=");
        tResult.append( tBean.get("SEQUENCE_ID")).append("\" title=\"View this MeasurePoint\"><IMG src=\"images/edit.png\"/></A>");
        return tResult.toString();
    }
    public String getUrlFlow()
    {
        DynaBean tBean = (DynaBean) getCurrentRowObject();
        StringBuffer tResult = new StringBuffer();
        tResult.append("<A href=\"");
        tResult.append("FlowEdit.do?id=").append( tBean.get("FLOW_ID")).append("\" title=\"View this Flow\"><IMG src=\"images/edit.png\"/></A>");
        return tResult.toString();
    }
}
