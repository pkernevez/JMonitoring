package org.jmonitoring.console.gwt.client.methodcall.stat;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class MethodCallStat extends Composite
{

    private static MethodCallDetailUiBinder uiBinder = GWT.create(MethodCallDetailUiBinder.class);

    interface MethodCallDetailUiBinder extends UiBinder<Widget, MethodCallStat>
    {
    }

    private MethodCallStatActivity presenter;

    public MethodCallStat()
    {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public MethodCallStat setPresenter(MethodCallStatActivity pActivity)
    {
        presenter = pActivity;
        return this;
    }

}
