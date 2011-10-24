package org.jmonitoring.console.gwt.client.flow;

import org.jmonitoring.console.gwt.client.ClientFactory;
import org.jmonitoring.console.gwt.client.main.JMonitoringAsyncCallBack;
import org.jmonitoring.console.gwt.shared.flow.ExecutionFlowDTO;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

class FlowDetailActivity extends AbstractActivity
{
    private final FlowDetailPlace place;

    private final ClientFactory clientFactory;

    FlowServiceAsync service = GWT.create(FlowService.class);

    FlowDetailDriver driver = GWT.create(FlowDetailDriver.class);

    public FlowDetailActivity(FlowDetailPlace pFlowDetailPlace, ClientFactory pClientFactory)
    {
        place = pFlowDetailPlace;
        clientFactory = pClientFactory;
    }

    interface FlowDetailDriver extends SimpleBeanEditorDriver<ExecutionFlowDTO, FlowDetail>
    {
    }

    public void start(final AcceptsOneWidget panel, EventBus eventBus)
    {
        service.loadAndGenerateImage(place.getId(), new JMonitoringAsyncCallBack<ExecutionFlowDTO>()
        {

            public void onSuccess(ExecutionFlowDTO pResult)
            {
                FlowDetail tFlowDetail = clientFactory.getFlowDetail();
                driver.initialize(tFlowDetail);
                driver.edit(pResult);
                panel.setWidget(tFlowDetail.setPresenter(FlowDetailActivity.this));
                tFlowDetail.durationInGroups.setUrl("image.dynamic?id=" + place.getId() + "&type=DurationInGroups");
                tFlowDetail.groupsCalls.setUrl("image.dynamic?id=" + place.getId() + "&type=GroupsCalls");
            }

        });
    }

    public void exportXml()
    {
        Window.open("/flow.export?id=" + place.getId(), "download", "");
    }
}