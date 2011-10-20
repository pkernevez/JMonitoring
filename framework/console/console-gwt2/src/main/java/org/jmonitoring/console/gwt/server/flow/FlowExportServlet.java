package org.jmonitoring.console.gwt.server.flow;

import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jmonitoring.console.gwt.shared.flow.ExecutionFlowDTO;
import org.jmonitoring.core.configuration.MeasureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class FlowExportServlet extends HttpServlet
{

    private static final long serialVersionUID = 2393628229673694881L;

    private static Logger sLog = LoggerFactory.getLogger(FlowExportServlet.class);

    private SessionFactory sessionFactory;

    private FlowServiceImpl flowService;

    @Override
    protected void doGet(HttpServletRequest pReq, HttpServletResponse pResp) throws ServletException, IOException
    {
        Session tSession = null;
        Transaction tCurrentTx = null;
        try
        {
            tSession = sessionFactory.openSession();
            tCurrentTx = tSession.beginTransaction();
            TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(tSession));
            doGetWithSpring(pReq, pResp);
            tCurrentTx.commit();
        } catch (RuntimeException r)
        {
            tCurrentTx.rollback();
            throw r;
        } catch (Error e)
        {
            tCurrentTx.rollback();
            throw e;
        } finally
        {
            if (tSession != null)
            {
                tSession.close();
                TransactionSynchronizationManager.unbindResource(sessionFactory);
            }
        }
    }

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

    @Override
    public void init(ServletConfig pConfig) throws ServletException
    {
        super.init(pConfig);
        WebApplicationContext tSpringContext =
            WebApplicationContextUtils.getWebApplicationContext(pConfig.getServletContext());
        sessionFactory = tSpringContext.getBean(SessionFactory.class);
        flowService = tSpringContext.getBean(FlowServiceImpl.class);
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
