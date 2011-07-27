package org.jmonitoring.console.gwt.server.servlet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.jmonitoring.console.gwt.client.dto.ExecutionFlowDTO;
import org.jmonitoring.console.gwt.server.ConsoleManager;
import org.jmonitoring.console.gwt.server.executionflow.ExecutionFlowServiceImpl;
import org.jmonitoring.core.configuration.SpringConfigurationUtil;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **************************************************************************/

public class FileUploadServlet extends HttpServlet
{

    private static final long serialVersionUID = 9183167655022412698L;

    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest pReq, HttpServletResponse pResp) throws ServletException, IOException
    {
        if (ServletFileUpload.isMultipartContent(pReq))
        {
            try
            {
                ExecutionFlowServiceImpl.before();
                File tRoot = File.createTempFile("temp", "tmp").getParentFile();
                ServletFileUpload tServlet = new ServletFileUpload(new DiskFileItemFactory(5 * 1024 * 1024, tRoot));
                List<FileItem> tFiles = tServlet.parseRequest(pReq);

                ConsoleManager tConsoleManager = (ConsoleManager) SpringConfigurationUtil.getBean("consoleManager");
                ExecutionFlowDTO tResult = tConsoleManager.insertFlowFromXml(tFiles.get(0).getInputStream());
                pResp.getWriter().print(tResult.getId());
                ExecutionFlowServiceImpl.after();
            } catch (RuntimeException r)
            {
                ExecutionFlowServiceImpl.afterRollBack();
                throw r;
            } catch (FileUploadException r)
            {
                ExecutionFlowServiceImpl.afterRollBack();
                throw new RuntimeException(r);
            } catch (Error e)
            {
                ExecutionFlowServiceImpl.afterRollBack();
                throw e;
            }
        }
    }
}
