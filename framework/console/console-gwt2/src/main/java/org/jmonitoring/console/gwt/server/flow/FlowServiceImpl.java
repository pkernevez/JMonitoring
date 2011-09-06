package org.jmonitoring.console.gwt.server.flow;

import it.pianetatecno.gwt.utility.client.table.Request;
import it.pianetatecno.gwt.utility.client.table.SerializableResponse;

import javax.annotation.Resource;

import org.jmonitoring.console.gwt.client.flow.FlowService;
import org.jmonitoring.console.gwt.shared.flow.FlowExtractDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FlowServiceImpl implements FlowService
{

    static Logger sLog = getLogger();

    @Resource(name = "consoleDao")
    protected ConsoleDao dao;

    private static Logger getLogger()
    {
        return LoggerFactory.getLogger(FlowServiceImpl.class);
    }
    
    public SerializableResponse<FlowExtractDTO> search(Request pRequest)
    {
        sLog.info("call search");
        SerializableResponse<FlowExtractDTO> tResponse = new SerializableResponse<FlowExtractDTO>();
        tResponse.setRequest(pRequest);
        tResponse.setTotalResults(dao.countFlows(pRequest));
        tResponse.setRows(dao.search(pRequest));
        return tResponse;
    }

}
