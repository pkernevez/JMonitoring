package org.jmonitoring.console.gwt.client.flow;

import org.jmonitoring.console.gwt.shared.flow.FlowSearchRequestDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class FlowSearch extends Composite implements Editor<FlowSearchRequestDTO>
{

    private static FlowSearchUiBinder uiBinder = GWT.create(FlowSearchUiBinder.class);
    
    interface FlowSearchUiBinder extends UiBinder<Widget, FlowSearch>
    {
    }

    interface RequestDTO extends SimpleBeanEditorDriver<FlowSearchRequestDTO, FlowSearch> {
    }

    RequestDTO driver = GWT.create(RequestDTO.class);
    FlowServiceAsync service = GWT.create(FlowService.class);

    public FlowSearch()
    {
        initWidget(uiBinder.createAndBindUi(this));
    }

}
