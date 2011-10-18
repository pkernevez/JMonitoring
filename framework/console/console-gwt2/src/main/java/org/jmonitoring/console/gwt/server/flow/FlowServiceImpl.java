package org.jmonitoring.console.gwt.server.flow;

import it.pianetatecno.gwt.utility.client.table.Request;
import it.pianetatecno.gwt.utility.client.table.SerializableResponse;

import javax.annotation.Resource;

import org.jmonitoring.console.gwt.client.flow.FlowService;
import org.jmonitoring.console.gwt.shared.flow.ExecutionFlowDTO;
import org.jmonitoring.console.gwt.shared.flow.FlowExtractDTO;
import org.jmonitoring.core.configuration.FormaterBean;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FlowServiceImpl implements FlowService
{

    static Logger sLog = getLogger();

    @Resource(name = "consoleDao")
    protected ConsoleDao dao;

    @Resource(name = "formater")
    private FormaterBean formater;

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

    public ExecutionFlowDTO load(int pFlowId)
    {
        return convertToFlowDto(dao.loadFlow(pFlowId));
    }

    private ExecutionFlowDTO convertToFlowDto(ExecutionFlowPO pLoadFlow)
    {
        ExecutionFlowDTO tResult = new ExecutionFlowDTO();
        tResult.setId(pLoadFlow.getId());
        tResult.setThreadName(pLoadFlow.getThreadName());
        tResult.setJvmIdentifier(pLoadFlow.getJvmIdentifier());
        tResult.setBeginTime(formater.formatDateTime(pLoadFlow.getBeginTimeAsDate()));
        tResult.setEndTime(formater.formatDateTime(pLoadFlow.getEndTime()));
        tResult.setDuration(String.valueOf(pLoadFlow.getDuration()));
        tResult.setGroupName(pLoadFlow.getFirstMethodCall().getGroupName());
        tResult.setClassName(pLoadFlow.getFirstClassName());
        tResult.setMethodName(pLoadFlow.getFirstMethodName());
        return tResult;
    }

}
