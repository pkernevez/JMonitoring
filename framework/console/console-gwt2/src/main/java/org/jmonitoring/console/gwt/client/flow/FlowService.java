package org.jmonitoring.console.gwt.client.flow;

import it.pianetatecno.gwt.utility.client.table.Request;
import it.pianetatecno.gwt.utility.client.table.SerializableResponse;

import org.jmonitoring.console.gwt.shared.flow.ExecutionFlowDTO;
import org.jmonitoring.console.gwt.shared.flow.FlowExtractDTO;
import org.jmonitoring.console.gwt.shared.flow.MethodCallDTO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("operationcaisse.rpc")
public interface FlowService extends RemoteService
{
    SerializableResponse<FlowExtractDTO> search(Request pRequest);

    ExecutionFlowDTO loadAndGenerateImage(int pFlowId);

    MethodCallDTO loadMethodCall(int pId, int pPosition);

}
