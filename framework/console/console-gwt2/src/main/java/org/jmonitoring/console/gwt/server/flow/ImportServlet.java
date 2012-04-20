package org.jmonitoring.console.gwt.server.flow;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;
import gwtupload.shared.UConsts;

import java.beans.XMLDecoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.hibernate.SessionFactory;
import org.jmonitoring.console.gwt.server.common.GwtRemoteServiceImpl;
import org.jmonitoring.console.gwt.shared.flow.ExecutionFlowDTO;
import org.jmonitoring.core.configuration.MeasureException;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ImportServlet extends UploadAction
{

    private static final long serialVersionUID = -2114832310101589003L;

    Hashtable<String, String> receivedContentTypes = new Hashtable<String, String>();

    private SessionFactory sessionFactory;

    private GwtRemoteServiceImpl flowService;

    @Override
    public void init(ServletConfig pConfig) throws ServletException
    {
        super.init(pConfig);
        WebApplicationContext tSpringContext =
            WebApplicationContextUtils.getWebApplicationContext(pConfig.getServletContext());
        sessionFactory = tSpringContext.getBean(SessionFactory.class);
        flowService = tSpringContext.getBean(GwtRemoteServiceImpl.class);
    }

    /**
     * Maintain a list with received files and their content types.
     */
    Hashtable<String, File> receivedFiles = new Hashtable<String, File>();

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
                    // / Create a new file based on the remote file name in the client
                    // String saveName = item.getName().replaceAll("[\\\\/><\\|\\s\"'{}()\\[\\]]+", "_");
                    // File file =new File("/tmp/" + saveName);

                    // / Create a temporary file placed in /tmp (only works in unix)
                    // File file = File.createTempFile("upload-", ".bin", new File("/tmp"));

                    // / Create a temporary file placed in the default system temp folder
                    File file = File.createTempFile("upload-", ".bin");
                    item.write(file);
                    // / Save a list with the received files
                    receivedFiles.put(item.getFieldName(), file);
                    receivedContentTypes.put(item.getFieldName(), item.getContentType());

                    // / Send a customized message to the client.
                    response += "File saved as " + file.getAbsolutePath();

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

    /**
     * Concert an GZip/Xml serialized ExecutionFlow as an ExecutionFLow Object.
     * 
     * @param pFlowAsXml The GZip bytes.
     * @return The ExecutionFLow.
     */
    protected ExecutionFlowPO convertFlowFromXml(InputStream pFlowAsGzipXml)
    {
        try
        {
            GZIPInputStream tZipStream = new GZIPInputStream(pFlowAsGzipXml);
            XMLDecoder tDecoder = new XMLDecoder(tZipStream);
            Object tResult = tDecoder.readObject();
            tDecoder.close();
            if (tResult instanceof ExecutionFlowDTO)
            {
                ExecutionFlowDTO tResult2 = (ExecutionFlowDTO) tResult;
                ExecutionFlowPO tFlow = flowService.getDeepCopy(tResult2);
                tFlow.setEndTime(tFlow.getBeginTime() + Long.getLong(tResult2.getDuration()));
                return tFlow;
            } else if (tResult instanceof ExecutionFlowPO)
            {
                ExecutionFlowPO tFlow = (ExecutionFlowPO) tResult;
                tFlow.setId(-1);
                return tFlow;
            } else
            {
                throw new RuntimeException("invalid class in gzip file: " + tResult.getClass().getName());
            }

        } catch (IOException e)
        {
            throw new MeasureException("Unable to Zip Xml ExecutionFlow", e);
        }
    }

    /**
     * Get the content of an uploaded file.
     */
    @Override
    public void getUploadedFile(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String fieldName = request.getParameter(UConsts.PARAM_SHOW);
        File f = receivedFiles.get(fieldName);
        if (f != null)
        {
            response.setContentType(receivedContentTypes.get(fieldName));
            FileInputStream is = new FileInputStream(f);
            copyFromInputStreamToOutputStream(is, response.getOutputStream());
        } else
        {
            renderXmlResponse(request, response, XML_ERROR_ITEM_NOT_FOUND);
        }
    }

    /**
     * Remove a file when the user sends a delete request.
     */
    @Override
    public void removeItem(HttpServletRequest request, String fieldName) throws UploadActionException
    {
        File file = receivedFiles.get(fieldName);
        receivedFiles.remove(fieldName);
        receivedContentTypes.remove(fieldName);
        if (file != null)
        {
            file.delete();
        }
    }
}