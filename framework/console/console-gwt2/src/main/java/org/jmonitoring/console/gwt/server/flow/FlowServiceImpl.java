package org.jmonitoring.console.gwt.server.flow;

import java.util.ArrayList;
import java.util.List;

import it.pianetatecno.gwt.utility.client.table.Request;
import it.pianetatecno.gwt.utility.client.table.SerializableResponse;

import org.jmonitoring.console.gwt.client.flow.FlowService;
import org.jmonitoring.console.gwt.shared.flow.FlowExtractDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlowServiceImpl implements FlowService
{

    static Logger sLog = LoggerFactory.getLogger(FlowServiceImpl.class);
    
    public SerializableResponse<FlowExtractDTO> search(Request pRequest)
    {
        sLog.info("call search");
        SerializableResponse<FlowExtractDTO> tResponse = new SerializableResponse<FlowExtractDTO>();
        tResponse.setRequest(pRequest);
        tResponse.setTotalResults(306);
        List<FlowExtractDTO> tResult = new ArrayList<FlowExtractDTO>();
        for(int i=0; i<pRequest.getPageSize(); i++){
            FlowExtractDTO tFlow = new  FlowExtractDTO();
            tFlow.setThread("tread"+i);
            tResult.add(tFlow);
        }
        
        System.out.println("YES !!!" );
        tResponse.setRows(tResult);
        return tResponse;
    }

}
