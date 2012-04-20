package org.jmonitoring.console.gwt.client.flow.importt;

import gwtupload.client.SingleUploaderModal;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class FlowImport extends Composite
{

    private FlowImportActivity presenter;

    private static FlowImportUiBinder uiBinder = GWT.create(FlowImportUiBinder.class);

    @UiField
    VerticalPanel vPanel;

    interface FlowImportUiBinder extends UiBinder<Widget, FlowImport>
    {
    }

    public FlowImport()
    {
        initWidget(uiBinder.createAndBindUi(this));
        vPanel.add(new SingleUploaderModal());
    }

    public FlowImport(String firstName)
    {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public FlowImport setPresenter(FlowImportActivity pActivity)
    {
        presenter = pActivity;
        return this;
    }

}
