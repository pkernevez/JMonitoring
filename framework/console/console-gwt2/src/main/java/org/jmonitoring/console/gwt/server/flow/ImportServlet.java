package org.jmonitoring.console.gwt.server.flow;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;

import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.hibernate.SessionFactory;
import org.jmonitoring.console.gwt.server.common.converter.BeanConverterUtil;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ImportServlet extends UploadAction
{

    private static final long serialVersionUID = -2114832310101589003L;

    private SessionFactory sessionFactory;

    private ConsoleDao consoleDao;

    private BeanConverterUtil converter;

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
        for (FileItem item : sessionFiles)
        {
            if (false == item.isFormField())
            {
                cont++;
                try
                {
                    ExecutionFlowPO tFlow = converter.convertFlowFromXml(item.getInputStream());
                    // TODO Transaction !
                    int tFlowId = consoleDao.insertFullExecutionFlow(tFlow);
                    // / Send a customized message to the client.
                    response += "Flow imported with id=" + tFlowId;

                } catch (Exception e)
                {
                    throw new UploadActionException(e);
                }
            }
        }

        // / Remove files from session because we have a copy of them
        removeSessionFileItems(request);

        // / Send your customized message to the client.
        return response;
    }

}