package org.jmonitoring.console.gwt.client;

import org.jmonitoring.console.gwt.client.common.message.MessagePlace.MessageTokenizer;
import org.jmonitoring.console.gwt.client.flow.delete.FlowDeletePlace.FlowDeleteTokenizer;
import org.jmonitoring.console.gwt.client.flow.detail.FlowDetailPlace.FlowDetailTokenizer;
import org.jmonitoring.console.gwt.client.flow.importt.FlowImportPlace.FlowImportTokenizer;
import org.jmonitoring.console.gwt.client.flow.search.FlowSearchPlace.FlowSearchTokenizer;
import org.jmonitoring.console.gwt.client.methodcall.detail.MethodCallDetailPlace.MethodCallDetailTokenizer;
import org.jmonitoring.console.gwt.client.methodcall.distribution.MethodCallDistributionPlace.MethodCallDistributionTokenizer;
import org.jmonitoring.console.gwt.client.methodcall.search.MethodCallSearchPlace.MethodCallSearchTokenizer;
import org.jmonitoring.console.gwt.client.methodcall.treesearch.MethodCallTreeSearchPlace.MethodCallTreeSearchTokenizer;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

@WithTokenizers({FlowSearchTokenizer.class, FlowDetailTokenizer.class, MethodCallDetailTokenizer.class,
    MethodCallDistributionTokenizer.class, MessageTokenizer.class, MethodCallSearchTokenizer.class,
    MethodCallTreeSearchTokenizer.class, FlowImportTokenizer.class, FlowDeleteTokenizer.class })
public interface JMonitoringPlaceHistoryMapper extends PlaceHistoryMapper
{

}
