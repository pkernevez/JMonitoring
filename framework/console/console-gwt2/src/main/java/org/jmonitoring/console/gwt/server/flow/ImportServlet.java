package org.jmonitoring.console.gwt.server.flow;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;
import org.apache.commons.fileupload.FileItem;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jmonitoring.console.gwt.server.common.converter.BeanConverterUtil;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class ImportServlet extends UploadAction
{

    private static final long serialVersionUID = -2114832310101589003L;

    private SessionFactory sessionFactory;

    private ConsoleDao consoleDao;

    private BeanConverterUtil converter;

    private static Logger sLog = LoggerFactory.getLogger(ImportServlet.class);

    @Override
    public void init(ServletConfig pConfig) throws ServletException
    {
        super.init(pConfig);
        WebApplicationContext tSpringContext =
            WebApplicationContextUtils.getWebApplicationContext(pConfig.getServletContext());
        sessionFactory = tSpringContext.getBean(SessionFactory.class);
        consoleDao = tSpringContext.getBean(ConsoleDao.class);
        converter = tSpringContext.getBean(BeanConverterUtil.class);
    }

    /**
     * Override executeAction to save the received files in a custom place and delete this items from session.
     */
    @Override
    public String executeAction(HttpServletRequest request, List<FileItem> sessionFiles) throws UploadActionException
    {
        String response = "";
        int cont = 0;
        Session session = null;
        Transaction tx = null;
        try {
            sLog.info("receive {} files", sessionFiles.size());
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            sLog.info("Tx opened");
            TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
            for (FileItem item : sessionFiles) {
                if (false == item.isFormField()) {
                    cont++;
                    try {
                        sLog.info("Import new flow");
//                        InputStream in = new GZIPInputStream(item.getInputStream());
//                        int n = in.available();
//                        byte[] bytes = new byte[n];
//                        in.read(bytes, 0, n);
//                        String s = new String(bytes, "UTF-8");

                        ExecutionFlowPO tFlow = converter.convertFlowFromXml(item.getInputStream());
                        // TODO Transaction !
                        int tFlowId = consoleDao.insertFullExecutionFlow(tFlow);
                        // / Send a customized message to the client.
                        response += "Flow imported with id=" + tFlowId;
                    } catch (Exception e) {
                        sLog.warn("Fail to import this flow");
                        throw new UploadActionException(e);
                    }
                }
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } catch (Error e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }

        // / Remove files from session because we have a copy of them
        removeSessionFileItems(request);

        // / Send your customized message to the client.
        return response;
    }

}