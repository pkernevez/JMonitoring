package org.jmonitoring.console.gwt.client.flow;

import java.util.List;

import org.jmonitoring.console.gwt.shared.flow.FlowExtractDTO;
import org.jmonitoring.console.gwt.shared.flow.FlowSearchRequestDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FlowServiceAsync
{

    void search(FlowSearchRequestDTO pRequest, AsyncCallback<List<FlowExtractDTO>> callback);

}
