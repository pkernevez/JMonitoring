package org.jmonitoring.console.gwt.client.methodcall.tree;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class MethodCallTreeSearch extends Composite
{

    private static MethodCallTreeUiBinder uiBinder = GWT.create(MethodCallTreeUiBinder.class);

    interface MethodCallTreeUiBinder extends UiBinder<Widget, MethodCallTreeSearch>
    {
    }

    private MethodCallTreeSearchActivity presenter;

    public MethodCallTreeSearch()
    {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public MethodCallTreeSearch setPresenter(MethodCallTreeSearchActivity pActivity)
    {
        presenter = pActivity;
        return this;
    }

}
