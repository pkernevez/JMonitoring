package org.jmonitoring.console.gwt.client.common;

import it.pianetatecno.gwt.utility.client.table.Request;
import it.pianetatecno.gwt.utility.client.table.SerializableResponse;

import org.jmonitoring.console.gwt.shared.flow.ExecutionFlowDTO;
import org.jmonitoring.console.gwt.shared.flow.FlowExtractDTO;
import org.jmonitoring.console.gwt.shared.flow.MethodCallDTO;
import org.jmonitoring.console.gwt.shared.method.MethodCallDistributionDTO;
import org.jmonitoring.console.gwt.shared.method.MethodCallSearchCriterion;
import org.jmonitoring.console.gwt.shared.method.MethodCallSearchExtractDTO;
import org.jmonitoring.console.gwt.shared.method.MethodNavType;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GwtRemoteServiceAsync
{

    void searchFlow(Request pRequest, AsyncCallback<SerializableResponse<FlowExtractDTO>> callback);

    void loadAndGenerateImage(int pFlowId, AsyncCallback<ExecutionFlowDTO> callback);

    void loadMethodCall(int pFlowId, int pPosition, AsyncCallback<MethodCallDTO> pJMonitoringAsyncCallBack);

    void getMethodPositionWhenNavigate(int pFlowId, int pCurrentPosition, String pGroupName, MethodNavType pType,
        AsyncCallback<Integer> pJMonitoringAsyncCallBack);

    void delete(int pFlowId, AsyncCallback<Void> callback);

    void getDistributionAndGenerateImage(int pFlowId, int pMethodPosition, long pGapDuration,
        AsyncCallback<MethodCallDistributionDTO> callback);

    void searchMethodCall(Request pRequest, MethodCallSearchCriterion pCriterion,
        AsyncCallback<SerializableResponse<MethodCallSearchExtractDTO>> callback);

}
