package org.jmonitoring.console.persistence;

import java.io.IOException;
import java.io.ObjectInputStream;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jmonitoring.agent.store.IStoreWriter;
import org.jmonitoring.agent.store.impl.HttpWriter;
import org.jmonitoring.core.domain.ExecutionFlowPO;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class StoreServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    @Resource(name = "writer")
    private IStoreWriter mWriter;

    private static Log sLog = LogFactory.getLog(StoreServlet.class);

    @Override
    public void init() throws ServletException
    {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest pReq, HttpServletResponse pResp) throws ServletException, IOException
    {
        doPost(pReq, pResp);
    }

    @Override
    protected void doPost(HttpServletRequest pReq, HttpServletResponse pResp) throws ServletException, IOException
    {
        if (!HttpWriter.CONTENT_TYPE.equals(pReq.getContentType()))
        {
            sLog.error("Invalid content type=[" + pReq.getContentType() + "]");
            throw new RuntimeException("Invalid content type=[" + pReq.getContentType() + "]");
        }
        ObjectInputStream tStream = new ObjectInputStream(pReq.getInputStream());
        ExecutionFlowPO tFlow;
        try
        {

            tFlow = (ExecutionFlowPO) tStream.readObject();
            mWriter.writeExecutionFlow(tFlow);
        } catch (ClassNotFoundException e)
        {
            sLog.error("Unable to deserialize Post Object");
            throw new RuntimeException("Unable to deserialize Post Object", e);
        }
    }

}
