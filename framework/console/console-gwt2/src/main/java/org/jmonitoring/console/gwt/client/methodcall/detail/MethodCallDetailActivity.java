package org.jmonitoring.console.gwt.client.methodcall.detail;

import org.jmonitoring.console.gwt.client.ClientFactory;
import org.jmonitoring.console.gwt.client.common.GwtRemoteService;
import org.jmonitoring.console.gwt.client.common.GwtRemoteServiceAsync;
import org.jmonitoring.console.gwt.client.common.NavHandler;
import org.jmonitoring.console.gwt.client.flow.detail.FlowDetailPlace;
import org.jmonitoring.console.gwt.client.main.JMonitoringAsyncCallBack;
import org.jmonitoring.console.gwt.client.methodcall.stat.MethodCallStatPlace;
import org.jmonitoring.console.gwt.shared.flow.MethodCallDTO;
import org.jmonitoring.console.gwt.shared.flow.MethodCallExtractDTO;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class MethodCallDetailActivity extends AbstractActivity
{
    private final MethodCallDetailPlace place;

    private final ClientFactory clientFactory;

    GwtRemoteServiceAsync service = GWT.create(GwtRemoteService.class);

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
                tMethodCallDetail.groupNameColor.getElement().setAttribute("style",
                                                                           "background-color: "
                                                                               + pResult.getGroupColor() + ";");
                tMethodCallDetail.goToFlow.addClickHandler(new NavHandler(new FlowDetailPlace(place.flowId)));
                tMethodCallDetail.goToStat.addClickHandler(new NavHandler(new MethodCallStatPlace(0, 0)));
                if (pResult.getParentPosition() != null)
                {

                    MethodCallDetailPlace tPlace =
                        new MethodCallDetailPlace(pResult.getFlowId(), pResult.getParentPosition());
                    tMethodCallDetail.goToParent.addClickHandler(new NavHandler(tPlace));
                    tMethodCallDetail.goToParent.setVisible(true);
                } else
                {
                    tMethodCallDetail.goToParent.setVisible(false);
                }
                for (MethodCallExtractDTO tChild : pResult.getChildren())
                {
                    SubMethodCall tLigne = new SubMethodCall(tChild, pResult.getFlowId());
                    tLigne.edit.addClickHandler(new NavHandler(new MethodCallDetailPlace(pResult.getFlowId(),
                                                                                         tChild.getPosition())));
                    tMethodCallDetail.children.add(tLigne);
                }
                tMethodCallDetail.fulClassName.setText(pResult.getClassName() + "." + pResult.getMethodName() + "()");
                panel.setWidget(tMethodCallDetail.setPresenter(MethodCallDetailActivity.this));
            }

        });
    }
}