package org.jmonitoring.console.gwt.client;

import org.jmonitoring.console.gwt.client.flow.FlowDetailPlace.FlowDetailTokenizer;
import org.jmonitoring.console.gwt.client.flow.FlowSearchPlace.FlowSearchTokenizer;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;


@WithTokenizers({FlowSearchTokenizer.class, FlowDetailTokenizer.class})
public interface JMonitoringPlaceHistoryMapper extends PlaceHistoryMapper{

}
