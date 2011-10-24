package org.jmonitoring.console.gwt.client.methodcall;

import org.jmonitoring.console.gwt.client.ClientFactory;
import org.jmonitoring.console.gwt.client.flow.FlowService;
import org.jmonitoring.console.gwt.client.flow.FlowServiceAsync;
import org.jmonitoring.console.gwt.client.main.JMonitoringAsyncCallBack;
import org.jmonitoring.console.gwt.shared.flow.MethodCallDTO;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class MethodCallDetailActivity extends AbstractActivity
{
    private final MethodCallDetailPlace place;

    private final ClientFactory clientFactory;

    FlowServiceAsync service = GWT.create(FlowService.class);

    MethodCallDetailDriver driver = GWT.create(MethodCallDetailDriver.class);

    public MethodCallDetailActivity(MethodCallDetailPlace pMethodCallDetailPlace, ClientFactory pClientFactory)
    {
        place = pMethodCallDetailPlace;
        clientFactory = pClientFactory;
    }

    interface MethodCallDetailDriver extends SimpleBeanEditorDriver<MethodCallDTO, MethodCallDetail>
    {
    }

    public void start(final AcceptsOneWidget panel, EventBus eventBus)
    {
        service.loadMethodCall(place.flowId, place.position, new JMonitoringAsyncCallBack<MethodCallDTO>()
        {

            public void onSuccess(MethodCallDTO pResult)
            {
                MethodCallDetail tMethodCallDetail = clientFactory.getMethodCallDetail();
                driver.initialize(tMethodCallDetail);
                driver.edit(pResult);
                panel.setWidget(tMethodCallDetail.setPresenter(MethodCallDetailActivity.this));
                // tMethodCallDetail.durationInGroups.setUrl("image.dynamic?id=" + place.getId()
                // + "&type=DurationInGroups");
                // tMethodCallDetail.groupsCalls.setUrl("image.dynamic?id=" + place.getId() + "&type=GroupsCalls");
                // tMethodCallDetail.detailFlowImage.setUrl("image.dynamic?id=" + place.getId() +
                // "&type=MethodCallDetail");
                // tMethodCallDetail.detailFlowImageMap.add(new HTML(pResult.getDetailMap()));
                // tMethodCallDetail.detailFlowImage.getElement().setAttribute("usemap", "#ChartBar");
                // tMethodCallDetail.detailMap.
            }

        });
    }

}