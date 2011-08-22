package org.jmonitoring.console.gwt.server.flow;

import java.util.List;

import org.jmonitoring.console.gwt.client.flow.FlowService;
import org.jmonitoring.console.gwt.shared.flow.FlowExtractDTO;
import org.jmonitoring.console.gwt.shared.flow.FlowSearchRequestDTO;

public class FlowServiceImpl implements FlowService
{

    public List<FlowExtractDTO> search(FlowSearchRequestDTO pRequest)
    {
        System.out.println("YES !!!"+pRequest.getThread());
        return null;
    }

}
