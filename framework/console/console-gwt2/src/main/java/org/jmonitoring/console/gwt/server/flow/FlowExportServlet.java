package org.jmonitoring.console.gwt.server.flow;

import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jmonitoring.console.gwt.server.common.HibernateServlet;
import org.jmonitoring.console.gwt.shared.flow.ExecutionFlowDTO;
import org.jmonitoring.core.configuration.MeasureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlowExportServlet extends HibernateServlet
{

    private static final long serialVersionUID = 2393628229673694881L;

    private static Logger sLog = LoggerFactory.getLogger(FlowExportServlet.class);

    @Override
    protected void doGetWithSpring(HttpServletRequest pReq, HttpServletResponse pResp) throws ServletException,
        IOException
    {
        // List tList = new ArrayList();
        int tFlowId = Integer.parseInt(pReq.getParameter("id"));
        sLog.debug("Read flow from database, Id=[" + tFlowId + "]");
        ExecutionFlowDTO tFlow = flowService.loadFull(tFlowId);
        // ExecutionFlowDTO tFlow = tConsoleManager.readFullExecutionFlow(tFlowId);
        sLog.debug("End Read from database of Flow, Id=[" + tFlowId + "]");
        pResp.setContentType("application/x-zip");
        String tFileName =
            "ExecutionFlow_" + tFlow.getJvmIdentifier() + "_Id" + tFlowId + "_" + tFlow.getBeginTime() + ".gzip";
        pResp.setHeader("Content-Disposition", "attachment; filename=\"" + tFileName + "\"");

        byte[] tFlowAsBytes = convertFlowToXml(tFlow);
        pResp.setContentLength(tFlowAsBytes.length);
        pResp.getOutputStream().write(tFlowAsBytes);
        pResp.getOutputStream().flush();
        pResp.getOutputStream().close();
    }

    /**
     * Serialize a full ExecutionFlow as Xml/GZip bytes.
     * 
     * @param pFlow The flow to serialize.
     * @return The bytes of a GZip.
     */
    public byte[] convertFlowToXml(ExecutionFlowDTO pFlow)
    {
        try
        {
            ByteArrayOutputStream tOutput = new ByteArrayOutputStream(10000);
            GZIPOutputStream tZipStream = new GZIPOutputStream(tOutput);
            XMLEncoder tEncoder = new XMLEncoder(tZipStream);
            tEncoder.writeObject(pFlow);
            tEncoder.close();
            return tOutput.toByteArray();
        } catch (IOException e)
        {
            throw new MeasureException("Unable to Zip Xml ExecutionFlow", e);
        }
    }

}
