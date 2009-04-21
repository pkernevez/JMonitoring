package org.jmonitoring.console.gwt.client.panel;

import org.jmonitoring.console.gwt.client.JMonitoring;
import org.jmonitoring.console.gwt.client.dto.ExecutionFlowDTO;
import org.jmonitoring.console.gwt.client.dto.MethodCallDTO;
import org.jmonitoring.console.gwt.client.main.Controller;
import org.jmonitoring.console.gwt.client.panel.methodcall.EditMethodCallPanel;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class PanelUtil
{

    public static Widget createLabel(String pText)
    {
        return new HTML(pText);
    }

    public static Widget createText(String pText)
    {
        HTML tHtml = new HTML(pText);
        tHtml.setStylePrimaryName("data-text");
        return tHtml;
    }

    public static Widget createData(String pText)
    {
        return new HTML(pText);
    }

    public static Widget createData(int pValue)
    {
        return new HTML("" + pValue);
    }

    public static Widget createData(long pValue)
    {
        return new HTML("" + pValue);
    }

    public static TextBox createTextBox()
    {
        return new TextBox();
    }

    public static Image createImage(String pChartBarFlows)
    {
        Image tImage = new Image("../DynamicImage?Id=" + pChartBarFlows);

        return tImage;
    }

    public static Widget createSubTitle(String pTitle)
    {
        return new HTML("<h2>" + pTitle + "</h2>");
    }

    public static Widget createTitle(String pTitle)
    {
        return new HTML("<h1>" + pTitle + "</h1>");
    }

    public static Widget createHyperLink(String pText, String pTargetHistoryToken)
    {
        return createHyperLink(pText, pTargetHistoryToken, null);
    }

    public static Widget createHyperLink(String pText, String pTargetHistoryToken, String pTitle)
    {
        Hyperlink tLink = new Hyperlink(pText, pTargetHistoryToken);
        tLink.setTitle(pTitle);
        return tLink;
    }

    public static Image createClickImage(AbstractImagePrototype pImagePrototype, String pTitle, String pUrl)
    {
        return createClickImage(pImagePrototype, pTitle, new NavigationListener(pUrl));
    }

    public static Image createClickImage(AbstractImagePrototype pImagePrototype, String pTitle, ClickListener pListener)
    {
        Image tImage = pImagePrototype.createImage();
        tImage.setStylePrimaryName("click-image");
        tImage.addMouseListener(new MouseListenerAdapter()
        {

            @Override
            public void onMouseEnter(Widget pWidget)
            {
                pWidget.addStyleDependentName("hover");
            }

            @Override
            public void onMouseLeave(Widget pWidget)
            {
                pWidget.removeStyleDependentName("hover");
            }
        });
        tImage.addClickListener(pListener);
        tImage.setTitle(pTitle);
        return tImage;
    }

    private static class NavigationListener implements ClickListener
    {
        String mUrl;

        public NavigationListener(String pTarget)
        {
            mUrl = pTarget;
        }

        public void onClick(Widget pWidget)
        {
            History.newItem(mUrl);
            History.fireCurrentHistoryState();
        }
    }

    public static Widget createExecutionFlowPanel(ExecutionFlowDTO pFlow, MethodCallDTO pCall)
    {
        HTML tResult =
            new HTML("[" + pCall.getDuration() + "] " + pFlow.getJvmIdentifier() + " / " + pFlow.getThreadName()
                + " : " + pCall.getGroupName() + " -> " + pCall.getClassName() + pCall.getMethodName() + "()");
        tResult.addStyleName("treeRoot");
        return tResult;
    }

    private static final String VIEW_EDIT_METH = "View detail...";

    public static Widget createMethodCallPanel(MethodCallDTO pCall)
    {
        Panel tResult = new HorizontalPanel();
        HTML curComp = new HTML("[" + pCall.getDurationFromPreviousCall() + "]");
        curComp.addStyleName("treePrevDuration");
        tResult.add(curComp);
        curComp =
            new HTML("[" + pCall.getDuration() + "] " + pCall.getGroupName() + " -> " + pCall.getClassName() + "."
                + pCall.getMethodName());
        tResult.add(curComp);
        String tStatHistoryToken = Controller.createStatToken(pCall);
        Image tStatImage =
            PanelUtil.createClickImage(JMonitoring.getImageBundle().graphique(), EditMethodCallPanel.VIEW_STAT_METH,
                                       tStatHistoryToken);
        tResult.add(tStatImage);
        String tEditHistoryToken = Controller.createEditMethToken(pCall);
        Image tEditImage =
            PanelUtil.createClickImage(JMonitoring.getImageBundle().edit(), VIEW_EDIT_METH, tEditHistoryToken);
        tResult.add(tEditImage);
        tResult.addStyleName("treeRoot");
        return tResult;
    }
}
