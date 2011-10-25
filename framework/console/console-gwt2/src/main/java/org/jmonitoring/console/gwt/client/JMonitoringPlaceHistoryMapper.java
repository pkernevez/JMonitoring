package org.jmonitoring.console.gwt.client;

import org.jmonitoring.console.gwt.client.flow.detail.FlowDetailPlace.FlowDetailTokenizer;
import org.jmonitoring.console.gwt.client.flow.search.FlowSearchPlace.FlowSearchTokenizer;
import org.jmonitoring.console.gwt.client.methodcall.detail.MethodCallDetailPlace.MethodCallDetailTokenizer;
import org.jmonitoring.console.gwt.client.methodcall.stat.MethodCallStatPlace.MethodCallStatTokenizer;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

@WithTokenizers({FlowSearchTokenizer.class, FlowDetailTokenizer.class, MethodCallDetailTokenizer.class,
    MethodCallStatTokenizer.class })
public interface JMonitoringPlaceHistoryMapper extends PlaceHistoryMapper
{

}
