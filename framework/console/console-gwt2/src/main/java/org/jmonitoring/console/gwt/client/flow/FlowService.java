package org.jmonitoring.console.gwt.client.flow;

import java.util.List;

import org.jmonitoring.console.gwt.shared.flow.FlowExtractDTO;
import org.jmonitoring.console.gwt.shared.flow.FlowSearchRequestDTO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("operationcaisse.rpc")
public interface FlowService extends RemoteService
{
    List<FlowExtractDTO> search(FlowSearchRequestDTO pRequest);
}
