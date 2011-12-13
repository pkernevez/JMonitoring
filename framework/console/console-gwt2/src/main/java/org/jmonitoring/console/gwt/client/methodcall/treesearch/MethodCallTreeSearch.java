package org.jmonitoring.console.gwt.client.methodcall.treesearch;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.Widget;

public class MethodCallTreeSearch extends Composite
{

    private static MethodCallTreeUiBinder uiBinder = GWT.create(MethodCallTreeUiBinder.class);

    @UiField
    Tree tree;

    interface MethodCallTreeUiBinder extends UiBinder<Widget, MethodCallTreeSearch>
    {
    }

    public MethodCallTreeSearch()
    {
        initWidget(uiBinder.createAndBindUi(this));
        Window.setTitle("Method call tree search");
    }

    private MethodCallTreeSearchActivity presenter;

    public MethodCallTreeSearch setPresenter(MethodCallTreeSearchActivity pActivity)
    {
        presenter = pActivity;
        return this;
    }

}
