package org.jmonitoring.console.gwt.client.flow.detail;

import org.jmonitoring.console.gwt.client.ClientFactory;
import org.jmonitoring.console.gwt.client.common.GwtRemoteService;
import org.jmonitoring.console.gwt.client.common.GwtRemoteServiceAsync;
import org.jmonitoring.console.gwt.client.common.JMonitoringAsyncCallBack;
import org.jmonitoring.console.gwt.client.common.message.MessagePlace;
import org.jmonitoring.console.gwt.client.methodcall.detail.MethodCallDetailPlace;
import org.jmonitoring.console.gwt.shared.flow.ExecutionFlowDTO;
import org.jmonitoring.console.gwt.shared.flow.UnknownEntity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HTML;

class FlowDetailActivity extends AbstractActivity
{
    private final FlowDetailPlace place;

    private final ClientFactory clientFactory;

    GwtRemoteServiceAsync service = GWT.create(GwtRemoteService.class);

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
        exportMapMethClick();
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
                tFlowDetail.detailFlowImage.setUrl("image.dynamic?id=" + place.getId() + "&type=FlowDetail");
                tFlowDetail.detailFlowImageMap.add(new HTML(pResult.getDetailMap()));
                tFlowDetail.detailFlowImage.getElement().setAttribute("usemap", "#ChartBar");
            }

            @Override
            public void onFailure(Throwable pThrowable)
            {
                if (pThrowable instanceof UnknownEntity)
                {
                    ClientFactory.goTo(new MessagePlace("The ExecutionFlow id=[" + place.getId() + "] doesn't exist"));
                } else
                {
                    super.onFailure(pThrowable);
                }
            }

        });
    }

    public static native void exportMapMethClick() /*-{
                                                   $wnd.methClick = $entry(@org.jmonitoring.console.gwt.client.flow.detail.FlowDetailActivity::methClick(II));
                                                   }-*/;

    /** Method use to trap clicks on the map of the flow. Used by native javascript. */
    public static boolean methClick(int pFlowId, int pPosition)
    {
        ClientFactory.goTo(new MethodCallDetailPlace(pFlowId, pPosition));
        return false;
    }

    public void exportXml()
    {
        Window.open("/flow.export?id=" + place.getId(), "download", "");
    }

    public void deleteExecutionFlow()
    {
        if (Window.confirm("Do you really want to delete flow id=" + place.getId()))
        {
            service.delete(place.getId(), new JMonitoringAsyncCallBack<Void>()
            {
                public void onSuccess(Void pResult)
                {
                    ClientFactory.goTo(new MessagePlace("The Flow " + place.getId() + " has been deleted"));
                }
            });
            ClientFactory.goTo(new MessagePlace("The ExecutionFlow (id=" + place.getId() + " has been delete."));
        }
    }
}