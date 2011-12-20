package org.jmonitoring.console.gwt.client.methodcall.detail;

import org.jmonitoring.console.gwt.shared.flow.MethodCallDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
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
    Grid children;

    @UiField
    Label returnValue;

    @UiField
    Image goToParent;

    @UiField
    Label parentPosition;

    @UiField
    Style customStyle;

    @UiField
    TableRowElement resultRow;

    @UiField
    TableRowElement throwableRow1;

    @UiField
    TableRowElement throwableRow2;

    @UiField
    Image prevInGroup;

    @UiField
    Image prevInThread;

    @UiField
    Image nextInThread;

    @UiField
    Image nextInGroup;

    interface Style extends CssResource
    {
        String mask();
    }

    interface MethodCallDetailUiBinder extends UiBinder<Widget, MethodCallDetail>
    {
    }

    public MethodCallDetail()
    {
        initWidget(uiBinder.createAndBindUi(this));
        Window.setTitle("Method call detail");
    }

}
