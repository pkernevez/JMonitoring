package org.jmonitoring.console.flow.edit;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jmonitoring.core.dto.ExecutionFlowDTO;
import org.jmonitoring.core.process.JMonitoringProcess;
import org.jmonitoring.core.process.ProcessFactory;

public class FlowExportServlet extends HttpServlet
{

    private static final long serialVersionUID = 2393628229673694881L;

    private static Log sLog = LogFactory.getLog(FlowExportServlet.class);

    protected void doGet(HttpServletRequest pReq, HttpServletResponse pResp) throws ServletException, IOException
    {
        JMonitoringProcess tProcess = ProcessFactory.getInstance();
        // List tList = new ArrayList();
        int tFlowId = Integer.parseInt(pReq.getParameter("id"));
        sLog.debug("Read flow from database, Id=[" + tFlowId + "]");
        ExecutionFlowDTO tFlow = tProcess.readFullExecutionFlow(tFlowId);
        sLog.debug("End Read from database of Flow, Id=[" + tFlowId + "]");
        pResp.setContentType("application/x-zip");
        String tFileName = "ExecutionFlow_" + tFlow.getJvmIdentifier() + "_Id" + tFlowId + "_"
            + tFlow.getBeginDateAsString() + ".gzip";
        pResp.setHeader("Content-Disposition", "attachment; filename=\"" + tFileName + "\"");
        byte[] tFlowAsBytes = tProcess.convertFlowToXml(tFlow);
        pResp.setContentLength(tFlowAsBytes.length);
        pResp.getOutputStream().write(tFlowAsBytes);
        pResp.getOutputStream().flush();
        pResp.getOutputStream().close();
    }

}
