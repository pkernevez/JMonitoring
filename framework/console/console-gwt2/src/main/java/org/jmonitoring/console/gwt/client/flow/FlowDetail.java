package org.jmonitoring.console.gwt.client.flow;

import org.jmonitoring.console.gwt.shared.flow.ExecutionFlowDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class FlowDetail extends Composite implements Editor<ExecutionFlowDTO>
{

    private static FlowDetailUiBinder uiBinder = GWT.create(FlowDetailUiBinder.class);

    interface FlowDetailUiBinder extends UiBinder<Widget, FlowDetail>
    {
    }

    public FlowDetail()
    {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiField
    Button delete;

    @UiField
    Button exportXml;

    @UiField
    Label jvmIdentifier;

    @UiField
    Label threadName;

    @UiField
    Label beginTime;

    @UiField
    Label endTime;

    @UiField
    Label duration;

    @UiField
    Label groupName;

    @UiField
    Label firstCall;

    public FlowDetail(String firstName)
    {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("delete")
    void onDeleteClick(ClickEvent event)
    {
        Window.alert("Delete !");
    }

    @UiHandler("exportXml")
    void onExportXmlClick(ClickEvent event)
    {
        Window.alert("Export !");
    }
}
