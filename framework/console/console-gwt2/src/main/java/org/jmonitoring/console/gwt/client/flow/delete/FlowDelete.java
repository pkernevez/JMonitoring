package org.jmonitoring.console.gwt.client.flow.delete;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class FlowDelete extends Composite 
{

    private static FlowImportUiBinder uiBinder = GWT.create(FlowImportUiBinder.class);

    interface FlowImportUiBinder extends UiBinder<Widget, FlowDelete>
    {
    }
    private FlowDeleteActivity presenter;

    public FlowDelete()
    {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public FlowDelete(String firstName)
    {
        initWidget(uiBinder.createAndBindUi(this));
    }
    public FlowDelete setPresenter(FlowDeleteActivity pActivity)
    {
        presenter = pActivity;
        return this;
    }

    @UiHandler("buttonOk")
    void onButtonOkClick(ClickEvent event) {
        presenter.deleteAll();
    }
}
