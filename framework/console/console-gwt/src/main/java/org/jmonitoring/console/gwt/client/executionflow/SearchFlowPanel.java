package org.jmonitoring.console.gwt.client.executionflow;

import org.jmonitoring.console.gwt.client.images.ConsoleImageBundle;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class SearchFlowPanel extends VerticalPanel
{
    private final TextBox mThreadName = new TextBox();

    private final TextBox mGroupName = new TextBox();

    private final TextBox mMinimumDuration = new TextBox();

    private final TextBox mBeginDate = new TextBox();

    private final TextBox mFirstMesureClassName = new TextBox();

    private final TextBox mFirstMeasureMethodName = new TextBox();

    private final ConsoleImageBundle mImageBundle;

    private Image mImage;

    public SearchFlowPanel(ConsoleImageBundle pImageBundle)
    {
        mImageBundle = pImageBundle;
        addMask();
    }

    private void addMask()
    {
        FlexTable tTable = new FlexTable();
        tTable.setWidget(0, 0, new HTML("<h1>Search Flows</h1>"));
        // tTable.getFlexCellFormatter().getColSpan(arg0, arg1)
        tTable.setWidget(1, 0, new Label("Thread name"));
        tTable.setWidget(1, 1, mThreadName);
        tTable.setWidget(1, 2, new Label("Minimum Duration"));
        tTable.setWidget(1, 3, mMinimumDuration);

        tTable.setWidget(2, 0, new Label("Group name"));
        tTable.setWidget(2, 1, mGroupName);
        tTable.setWidget(2, 2, new Label("Begin date (dd/MM/yy)"));
        tTable.setWidget(2, 3, mBeginDate);

        tTable.setWidget(3, 0, new Label("First measure class name"));
        tTable.setWidget(3, 1, mFirstMesureClassName);
        tTable.setWidget(3, 2, new Label("First measure method name"));
        tTable.setWidget(3, 3, mFirstMeasureMethodName);

        mImage = mImageBundle.ok().createImage();;
        mImage.setStylePrimaryName("click-image");
        mImage.addMouseListener(new MouseListenerAdapter()
        {

            @Override
            public void onMouseEnter(Widget pWidget)
            {
                mImage.addStyleDependentName("hover");
            }

            @Override
            public void onMouseLeave(Widget pWidget)
            {
                mImage.removeStyleDependentName("hover");
            }
        });
        mImage.addClickListener(mSearchClickListener);
        tTable.setWidget(4, 0, mImage);
        add(tTable);
    }

    private final ClickListener mSearchClickListener = new ClickListener()
    {
        public void onClick(Widget pWidget)
        {
        }
    };

}
