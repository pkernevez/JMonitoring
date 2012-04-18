package org.jmonitoring.console.gwt.client.flow.importt;

import org.jmonitoring.console.gwt.client.ClientFactory;
import org.jmonitoring.console.gwt.client.common.GwtRemoteService;
import org.jmonitoring.console.gwt.client.common.GwtRemoteServiceAsync;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class FlowImportActivity  extends AbstractActivity
    {
        private final FlowImportPlace place;

        private final ClientFactory clientFactory;

        GwtRemoteServiceAsync service = GWT.create(GwtRemoteService.class);

        public FlowImportActivity(FlowImportPlace pFlowImportPlace, ClientFactory pClientFactory)
        {
            place = pFlowImportPlace;
            clientFactory = pClientFactory;
        }

        public void start(final AcceptsOneWidget panel, EventBus eventBus)
        {
            FlowImport tPanel = clientFactory.getFlowImport();
            panel.setWidget(tPanel.setPresenter(this));
        }

//        public void deleteExecutionFlow()
//        {
//            if (Window.confirm("Do you really want to delete flow id=" + place.getId()))
//            {
//                service.delete(place.getId(), new JMonitoringAsyncCallBack<Void>()
//                {
//                    public void onSuccess(Void pResult)
//                    {
//                        ClientFactory.goTo(new MessagePlace("The Flow " + place.getId() + " has been deleted"));
//                    }
//                });
//                ClientFactory.goTo(new MessagePlace("The ExecutionFlow (id=" + place.getId() + " has been delete."));
//            }
//        }
//    }
}
