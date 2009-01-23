package org.jmonitoring.agent.store.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jmonitoring.agent.store.IStoreWriter;
import org.jmonitoring.core.domain.ExecutionFlowPO;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class HttpWriter implements IStoreWriter
{

    public static final String CONTENT_TYPE = "JMonitoring/flow";

    private final String mUri;

    private static Log sLog = LogFactory.getLog(HttpWriter.class);

    private final HttpClient mClient;

    public HttpWriter(String pHostName, int pPort, String pProtocole, String pUri)
    {
        mUri = pUri;
        mClient = new HttpClient(new SimpleHttpConnectionManager());
        mClient.getHostConfiguration().setHost(pHostName, pPort, pProtocole);
    }

    public void writeExecutionFlow(ExecutionFlowPO pExecutionFlow)
    {
        long tStartTime = System.currentTimeMillis();
        try
        {
            ByteArrayOutputStream tBytes = new ByteArrayOutputStream();
            ObjectOutputStream tStream = new ObjectOutputStream(tBytes);
            tStream.writeObject(pExecutionFlow);

            PostMethod tHttpPost = new PostMethod(mUri);
            tHttpPost.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
            byte[] tByteArray = tBytes.toByteArray();
            tHttpPost.setRequestEntity(new ByteArrayRequestEntity(tByteArray));
            tHttpPost.setContentChunked(true);
            tHttpPost.addRequestHeader("Content-Type", CONTENT_TYPE);
            try
            {
                mClient.executeMethod(tHttpPost);
                long tEndTime = System.currentTimeMillis();
                sLog.info("Inserted vith Http ExecutionFlow " + pExecutionFlow + " in " + (tEndTime - tStartTime)
                    + " ms.");

                if (tHttpPost.getStatusCode() != HttpStatus.SC_OK)
                {
                    sLog.error("Error while transfering Flow to HttpServer, return code was ["
                        + tHttpPost.getStatusCode() + "]");
                }
            } finally
            {
                tHttpPost.releaseConnection();
            }
        } catch (IOException e)
        {
            sLog.error("Unable to Write Flow to Http Server the ExecutionFlow has been lost. Cause is "
                + e.getMessage() + " Serveur is " + mClient.getHostConfiguration());
        }
    }
}
