package org.jmonitoring.console.gwt.client.methodcall.detail;

import org.jmonitoring.console.gwt.client.common.ULPanel;
import org.jmonitoring.console.gwt.shared.flow.MethodCallDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class MethodCallDetail extends Composite implements Editor<MethodCallDTO>
{

    private static MethodCallDetailUiBinder uiBinder = GWT.create(MethodCallDetailUiBinder.class);

    @UiField
    Label runtimeClassName;

    @UiField
    @Ignore
    Label fulClassName;

    @UiField
    Label groupName;

    @UiField
    Label flowId;

    @UiField
    Label position;

    @UiField
    Label beginTimeString;

    @UiField
    Label endTimeString;

    @UiField
    Label params;

    @UiField
    Label throwableClass;

    @UiField
    Label throwableMessage;

    @UiField
    @Ignore
    Label groupNameColor;

    @UiField
    Image goToFlow;

    @UiField
    Image goToStat;

    @UiField
    ULPanel children;

    @UiField
    Label returnValue;

    @UiField
    Image goToParent;

    @UiField
    @Path("parent.position")
    Label parentId;

    interface MethodCallDetailUiBinder extends UiBinder<Widget, MethodCallDetail>
    {
    }

    private MethodCallDetailActivity presenter;

    public MethodCallDetail()
    {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public MethodCallDetail setPresenter(MethodCallDetailActivity pActivity)
    {
        presenter = pActivity;
        return this;
    }

}
