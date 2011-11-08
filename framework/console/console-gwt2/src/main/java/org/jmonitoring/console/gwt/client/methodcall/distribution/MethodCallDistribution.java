package org.jmonitoring.console.gwt.client.methodcall.distribution;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class MethodCallDistribution extends Composite
{

    private static MethodCallDetailUiBinder uiBinder = GWT.create(MethodCallDetailUiBinder.class);

    interface MethodCallDetailUiBinder extends UiBinder<Widget, MethodCallDistribution>
    {
    }

    @UiField
    TextBox gapDuration;

    private MethodCallDistributionActivity presenter;

    public MethodCallDistribution()
    {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public MethodCallDistribution setPresenter(MethodCallDistributionActivity pActivity)
    {
        presenter = pActivity;
        return this;
    }

}
