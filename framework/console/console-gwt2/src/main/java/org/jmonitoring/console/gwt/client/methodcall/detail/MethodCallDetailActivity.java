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
import org.jmonitoring.console.gwt.shared.method.MethodNavType;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Image;

public class MethodCallDetailActivity extends AbstractActivity
{
    private final MethodCallDetailPlace place;

    private final ClientFactory clientFactory;

    private MethodCallDetail methodCallDetail;

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
                methodCallDetail = clientFactory.getMethodCallDetail();
                driver.initialize(methodCallDetail);
                driver.edit(pResult);
                methodCallDetail.groupNameColor.getElement().setAttribute("style",
                                                                          "background-color: "
                                                                              + pResult.getGroupColor() + ";");
                methodCallDetail.goToFlow.addClickHandler(new NavHandler(new FlowDetailPlace(place.flowId)));
                methodCallDetail.goToStat.addClickHandler(new NavHandler(new MethodCallStatPlace(0, 0)));
                if (pResult.getThrowableClass() == null)
                {
                    methodCallDetail.throwableRow1.addClassName(methodCallDetail.customStyle.mask());
                    methodCallDetail.throwableRow2.addClassName(methodCallDetail.customStyle.mask());
                } else
                {
                    methodCallDetail.resultRow.addClassName(methodCallDetail.customStyle.mask());
                }
                if (pResult.getParentPosition() != null)
                {

                    MethodCallDetailPlace tPlace =
                        new MethodCallDetailPlace(pResult.getFlowId(), pResult.getParentPosition());
                    methodCallDetail.goToParent.addClickHandler(new NavHandler(tPlace));
                    methodCallDetail.goToParent.setVisible(true);
                } else
                {
                    methodCallDetail.goToParent.setVisible(false);
                }

                addNavHandler(methodCallDetail.prevInGroup, pResult, MethodNavType.PREV_IN_GROUP);
                addNavHandler(methodCallDetail.prevInThread, pResult, MethodNavType.PREV_IN_THREAD);
                addNavHandler(methodCallDetail.nextInThread, pResult, MethodNavType.NEXT_IN_THREAD);
                addNavHandler(methodCallDetail.nextInGroup, pResult, MethodNavType.NEXT_IN_GROUP);

                methodCallDetail.children.resize(pResult.getChildren().length, 1);
                int tRow = 0;
                for (MethodCallExtractDTO tChild : pResult.getChildren())
                {
                    SubMethodCall tLigne = new SubMethodCall(tChild, pResult.getFlowId());
                    tLigne.edit.addClickHandler(new NavHandler(new MethodCallDetailPlace(pResult.getFlowId(),
                                                                                         tChild.getPosition())));
                    methodCallDetail.children.setWidget(tRow, 0, tLigne);
                    tRow++;
                }
                methodCallDetail.fulClassName.setText(pResult.getClassName() + "." + pResult.getMethodName() + "()");
                panel.setWidget(methodCallDetail.setPresenter(MethodCallDetailActivity.this));
            }

            private void addNavHandler(Image pImage, MethodCallDTO pResult, MethodNavType pType)
            {
                pImage.addClickHandler(new MethodNavHandler(service, pResult, pType));
            }

        });
    }

    private static class MethodNavHandler implements ClickHandler
    {
        private int flowId;

        private int currentPosition;

        private MethodNavType type;

        private GwtRemoteServiceAsync service;

        private String groupName;

        public MethodNavHandler(GwtRemoteServiceAsync pService, MethodCallDTO pCurrentMeth, MethodNavType pType)
        {
            flowId = Integer.valueOf(pCurrentMeth.getFlowId());
            currentPosition = Integer.valueOf(pCurrentMeth.getPosition());
            type = pType;
            service = pService;
            groupName = pCurrentMeth.getGroupName();
        }

        public void onClick(ClickEvent pEvent)
        {
            if (MethodNavType.PREV_IN_THREAD == type)
            {
                if (currentPosition > 1)
                {
                    ClientFactory.goTo(new MethodCallDetailPlace(flowId, currentPosition - 1));
                } else
                {
                    Window.alert("You reach the bound of the call list.");
                }
            } else
            {
                service.getMethodPositionWhenNavigate(flowId, currentPosition, groupName, type,
                                                      new JMonitoringAsyncCallBack<Integer>()
                                                      {
                                                          public void onSuccess(Integer pResult)
                                                          {
                                                              if (pResult > 1)
                                                              {
                                                                  ClientFactory.goTo(new MethodCallDetailPlace(flowId,
                                                                                                               pResult));
                                                              } else
                                                              {
                                                                  Window.alert("You reach the bound of the call list.");
                                                              }
                                                          }
                                                      });
            }
        }

    }
}