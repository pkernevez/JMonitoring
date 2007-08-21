package org.jmonitoring.agent.store.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jmonitoring.core.configuration.ConfigurationHelper;
import org.jmonitoring.core.domain.ExecutionFlowPO;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class HttpWriter extends AbstractAsynchroneWriter
{

    public static final String FLOW_ATTR = "FlowContent";

    public static final String CONTENT_TYPE = "JMonitoring/flow";

    public static final String HOSTNAME = "httpwriter.hostname";

    public static final String PORT = "httpwriter.port";

    public static final String PROTOCOLE = "httpwriter.protocol";

    public static final String URI = "httpwriter.uri";

    public static final String sUri = ConfigurationHelper.getString(URI);

    private static Log sLog = LogFactory.getLog(HttpWriter.class);

    private static HttpClient sClient;

    private static MultiThreadedHttpConnectionManager sManager;

    private static String sUrl;

    static
    {
        configure();
    }

    static void configure()
    {
        MultiThreadedHttpConnectionManager.shutdownAll();
        sManager = new MultiThreadedHttpConnectionManager();
        sClient = new HttpClient(sManager);
        String tHost = ConfigurationHelper.getString(HOSTNAME);
        int tPort = ConfigurationHelper.getInt(PORT);
        String tProtocole = ConfigurationHelper.getString(PROTOCOLE);
        sClient.getHostConfiguration().setHost(tHost, tPort, tProtocole);

        String sUrl = ConfigurationHelper.getString(URI);
    }

    private class AsynchroneHttpWriterRunnable implements Runnable
    {
        private ExecutionFlowPO mExecutionFlowToLog;

        public AsynchroneHttpWriterRunnable(ExecutionFlowPO pExecutionFlowToLog)
        {
            mExecutionFlowToLog = pExecutionFlowToLog;
        }

        public void run()
        {
            try
            {
                long tStartTime = System.currentTimeMillis();

                ByteArrayOutputStream tBytes = new ByteArrayOutputStream();
                ObjectOutputStream tStream = new ObjectOutputStream(tBytes);
                tStream.writeObject(mExecutionFlowToLog);

                PostMethod tHttpPost = new PostMethod(sUri);
                tHttpPost.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
                byte[] tByteArray = tBytes.toByteArray();
                tHttpPost.setRequestEntity(new ByteArrayRequestEntity(tByteArray));
                tHttpPost.setContentChunked(true);
                tHttpPost.addRequestHeader("Content-Type", "JMonitoring/flow");
                try
                {
                    sClient.executeMethod(tHttpPost);
                    long tEndTime = System.currentTimeMillis();
                    sLog.info("Inserted vith Http ExecutionFlow " + mExecutionFlowToLog + " in "
                        + (tEndTime - tStartTime) + " ms.");

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
                    + e.getMessage() + " Serveur is " + sClient.getHostConfiguration());
            }
        }
    }

    /**
     * @see AbstractAsynchroneWriter#getAsynchroneLogTask(ExecutionFlowPO)
     */
    protected Runnable getAsynchroneLogTask(ExecutionFlowPO pFlow)
    {
        return new AsynchroneHttpWriterRunnable(pFlow);
    }

}
