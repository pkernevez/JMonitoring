package org.jmonitoring.console.gwt.client.flow;

import java.util.List;

import org.jmonitoring.console.gwt.client.main.JMonitoringAsyncCallBack;
import org.jmonitoring.console.gwt.shared.flow.FlowExtractDTO;
import org.jmonitoring.console.gwt.shared.flow.FlowSearchRequestDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class FlowSearch extends Composite implements Editor<FlowSearchRequestDTO>
{

    private static FlowSearchUiBinder uiBinder = GWT.create(FlowSearchUiBinder.class);

    @UiField
    Image image;

    interface FlowSearchUiBinder extends UiBinder<Widget, FlowSearch>
    {
    }

    interface RequestDTO extends SimpleBeanEditorDriver<FlowSearchRequestDTO, FlowSearch>
    {
    }

    RequestDTO driver = GWT.create(RequestDTO.class);

    FlowServiceAsync service = GWT.create(FlowService.class);

    public FlowSearch()
    {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("image")
    void onImageClick(ClickEvent event)
    {
        service.search(null, new JMonitoringAsyncCallBack<List<FlowExtractDTO>>()
        {
            public void onSuccess(List<FlowExtractDTO> arg0)
            {
                System.out.println("OK");
            }

        });
    }
}
