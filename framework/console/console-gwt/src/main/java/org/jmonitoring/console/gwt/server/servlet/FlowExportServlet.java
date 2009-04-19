package org.jmonitoring.console.gwt.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jmonitoring.console.gwt.server.ConsoleManager;
import org.jmonitoring.console.gwt.server.executionflow.ExecutionFlowServiceImpl;
import org.jmonitoring.core.configuration.SpringConfigurationUtil;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlowExportServlet extends HttpServlet
{

    private static final long serialVersionUID = 2393628229673694881L;

    private static Logger sLog = LoggerFactory.getLogger(FlowExportServlet.class);

    @Override
    protected void doGet(HttpServletRequest pReq, HttpServletResponse pResp) throws ServletException, IOException
    {
        try
        {
            ExecutionFlowServiceImpl.before();
            doGetWithSpring(pReq, pResp);
            ExecutionFlowServiceImpl.after();
        } catch (RuntimeException r)
        {
            ExecutionFlowServiceImpl.afterRollBack();
            throw r;
        } catch (Error e)
        {
            ExecutionFlowServiceImpl.afterRollBack();
            throw e;
        }
    }

    protected void doGetWithSpring(HttpServletRequest pReq, HttpServletResponse pResp) throws ServletException,
        IOException
    {
        ConsoleManager tConsoleManager = (ConsoleManager) SpringConfigurationUtil.getBean("consoleManager");
        // List tList = new ArrayList();
        int tFlowId = Integer.parseInt(pReq.getParameter("id"));
        sLog.debug("Read flow from database, Id=[" + tFlowId + "]");
        ExecutionFlowPO tFlow = tConsoleManager.readExecutionFlow(tFlowId);
        sLog.debug("End Read from database of Flow, Id=[" + tFlowId + "]");
        pResp.setContentType("application/x-zip");
        String tFileName =
            "ExecutionFlow_" + tFlow.getJvmIdentifier() + "_Id" + tFlowId + "_" + tFlow.getBeginTime() + ".gzip";
        pResp.setHeader("Content-Disposition", "attachment; filename=\"" + tFileName + "\"");
        byte[] tFlowAsBytes = tConsoleManager.convertFlowToXml(tFlow);
        pResp.setContentLength(tFlowAsBytes.length);
        pResp.getOutputStream().write(tFlowAsBytes);
        pResp.getOutputStream().flush();
        pResp.getOutputStream().close();
    }

}
