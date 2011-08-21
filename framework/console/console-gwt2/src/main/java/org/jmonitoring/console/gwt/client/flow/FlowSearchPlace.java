package org.jmonitoring.console.gwt.client.flow;

import org.jmonitoring.console.gwt.client.JMonitoringActivityMapper.ActivityAware;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class FlowSearchPlace extends Place implements ActivityAware {

    public static class FlowSearchTokenizer implements PlaceTokenizer<FlowSearchPlace> {

        public FlowSearchPlace getPlace(String token) {
            return new FlowSearchPlace();
        }

        public String getToken(FlowSearchPlace place) {
            return "";
        }

    }

    static class FlowSearchActivity extends AbstractActivity {

        public void start(AcceptsOneWidget panel, EventBus eventBus) {
            panel.setWidget(new FlowSearch());
        }

        @Override
        public String mayStop() {
            // Return non null pour demander à l'utilisateur d'arreter le traitement
            return null;
        }
    }
    
    public Activity getActivity() {
        return new FlowSearchActivity();
    }
}