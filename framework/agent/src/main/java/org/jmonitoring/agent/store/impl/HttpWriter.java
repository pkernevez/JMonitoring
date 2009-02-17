package org.jmonitoring.agent.store.impl;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jmonitoring.agent.store.IStoreWriter;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class HttpWriter implements IStoreWriter
{

    public static final String CONTENT_TYPE = "JMonitoring/flow";

    private String mUri = "/console-web/Store.do";

    private static Logger sLog = LoggerFactory.getLogger(HttpWriter.class);

    private HttpClient mClient;

    private String mHostName = "localhost";

    private int mPort = 80;

    private String mProtocole = "http";

    private boolean mInit = false;

    public HttpWriter()
    {
    }

    public HttpWriter(String pHostName, int pPort, String pProtocole, String pUri)
    {
        mUri = pUri;
        mHostName = pHostName;
        mPort = pPort;
        mProtocole = pProtocole;
    }

    private void init()
    {
        if (!mInit)
        {
            mClient = new HttpClient(new SimpleHttpConnectionManager());
            mClient.getHostConfiguration().setHost(mHostName, mPort, mProtocole);
        }
    }

    public void writeExecutionFlow(ExecutionFlowPO pExecutionFlow)
    {
        init();
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
        } catch (Throwable e)
        {
            sLog.error("Unable to Write Flow to Http Server the ExecutionFlow has been lost. Cause is "
                + e.getMessage() + " Serveur is " + mClient.getHostConfiguration());
        }
    }

    /**
     * @param pUri the uri to set
     */
    public void setUri(String pUri)
    {
        mUri = pUri;
    }

    /**
     * @param pHost the host to set
     */
    public void setHostName(String pHost)
    {
        mHostName = pHost;
    }

    /**
     * @param pPort the port to set
     */
    public void setPort(int pPort)
    {
        mPort = pPort;
    }

    /**
     * @param pProtocole the protocole to set
     */
    public void setProtocole(String pProtocole)
    {
        mProtocole = pProtocole;
    }
}
