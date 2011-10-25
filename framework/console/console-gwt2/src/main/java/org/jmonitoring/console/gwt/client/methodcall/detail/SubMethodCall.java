package org.jmonitoring.console.gwt.client.methodcall.detail;

import org.jmonitoring.console.gwt.client.common.NavHandler;
import org.jmonitoring.console.gwt.client.methodcall.stat.MethodCallStatPlace;
import org.jmonitoring.console.gwt.shared.flow.MethodCallDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class SubMethodCall extends Composite
{

    private static SubMethodCallUiBinder uiBinder = GWT.create(SubMethodCallUiBinder.class);

    @UiField
    Label prevDuration;

    @UiField
    Label localDuration;

    @UiField
    Label text;

    @UiField
    Image edit;

    @UiField
    Image stats;

    interface SubMethodCallUiBinder extends UiBinder<Widget, SubMethodCall>
    {
    }

    public SubMethodCall()
    {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public SubMethodCall(MethodCallDTO pChild)
    {
        initWidget(uiBinder.createAndBindUi(this));
        prevDuration.setText(String.valueOf(pChild.getBeginMilliSeconds() - pChild.getParent().getBeginMilliSeconds()));
        localDuration.setText(String.valueOf(pChild.getEndMilliSeconds() - pChild.getBeginMilliSeconds()));
        text.setText(pChild.getGroupName() + " --> " + pChild.getRuntimeClassName() + "." + pChild.getMethodName()
            + "()");
        edit.addClickHandler(new NavHandler(new MethodCallDetailPlace(pChild.getFlowId(), pChild.getPosition())));
        stats.addClickHandler(new NavHandler(new MethodCallStatPlace(0, 0)));
    }
}
