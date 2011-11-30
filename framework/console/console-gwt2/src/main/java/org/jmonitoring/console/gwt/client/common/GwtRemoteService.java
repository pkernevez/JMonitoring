package org.jmonitoring.console.gwt.client.common;

import it.pianetatecno.gwt.utility.client.table.Request;
import it.pianetatecno.gwt.utility.client.table.SerializableResponse;

import org.jmonitoring.console.gwt.shared.flow.ExecutionFlowDTO;
import org.jmonitoring.console.gwt.shared.flow.FlowExtractDTO;
import org.jmonitoring.console.gwt.shared.flow.MethodCallDTO;
import org.jmonitoring.console.gwt.shared.flow.UnknownEntity;
import org.jmonitoring.console.gwt.shared.method.MethodCallDistributionDTO;
import org.jmonitoring.console.gwt.shared.method.MethodCallSearchCriterion;
import org.jmonitoring.console.gwt.shared.method.MethodCallSearchExtractDTO;
import org.jmonitoring.console.gwt.shared.method.MethodNavType;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("operationcaisse.rpc")
public interface GwtRemoteService extends RemoteService
{
    SerializableResponse<FlowExtractDTO> searchFlow(Request pRequest);

    ExecutionFlowDTO loadAndGenerateImage(int pFlowId) throws UnknownEntity;

    MethodCallDistributionDTO getDistributionAndGenerateImage(int pFlowId, int pMethodPosition, long pGapDuration);

    MethodCallDTO loadMethodCall(int pFlowId, int pPosition);

    int getMethodPositionWhenNavigate(int pFlowId, int pCurrentPosition, String pGroupName, MethodNavType pType);

    void delete(int pFlowId);

    SerializableResponse<MethodCallSearchExtractDTO> searchMethodCall(Request pRequest,
        MethodCallSearchCriterion pCriterion);

}
