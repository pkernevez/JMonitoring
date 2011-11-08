package org.jmonitoring.console.gwt.client;

import org.jmonitoring.console.gwt.client.common.message.MessagePlace.MessageTokenizer;
import org.jmonitoring.console.gwt.client.flow.detail.FlowDetailPlace.FlowDetailTokenizer;
import org.jmonitoring.console.gwt.client.flow.search.FlowSearchPlace.FlowSearchTokenizer;
import org.jmonitoring.console.gwt.client.methodcall.detail.MethodCallDetailPlace.MethodCallDetailTokenizer;
import org.jmonitoring.console.gwt.client.methodcall.distribution.MethodCallDistributionPlace.MethodCallStatTokenizer;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

@WithTokenizers({FlowSearchTokenizer.class, FlowDetailTokenizer.class, MethodCallDetailTokenizer.class,
    MethodCallStatTokenizer.class, MessageTokenizer.class })
public interface JMonitoringPlaceHistoryMapper extends PlaceHistoryMapper
{

}
