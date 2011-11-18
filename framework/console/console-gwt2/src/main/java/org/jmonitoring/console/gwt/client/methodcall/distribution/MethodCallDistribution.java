package org.jmonitoring.console.gwt.client.methodcall.distribution;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class MethodCallDistribution extends Composite
{

    private static MethodCallDetailUiBinder uiBinder = GWT.create(MethodCallDetailUiBinder.class);

    interface MethodCallDetailUiBinder extends UiBinder<Widget, MethodCallDistribution>
    {
    }

    @UiField
    TextBox interval;

    @UiField
    Label fullMethodName;

    @UiField
    Label nbOccurences;

    @UiField
    Label durationMin;

    @UiField
    Label durationAvg;

    @UiField
    Label durationDeviance;

    @UiField
    Label durationMax;

    @UiField
    Image distribution;

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

    @UiHandler("buttonOk")
    void onButtonOkClick(ClickEvent pEvent)
    {
        presenter.changeInterval(interval.getText());
    }

    @UiHandler("interval")
    void onIntervalKeyPress(KeyPressEvent pEvent)
    {
        if (pEvent.getCharCode() == 13)
        {
            presenter.changeInterval(interval.getText());
        }

    }
}
