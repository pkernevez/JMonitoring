package org.jmonitoring.agent.store.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
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

    public static final String URL_PARAM_NAME = "measurepoint.writer.httpwriter.url";

    public static final String CONTENT_BINARY = "binary";

    private Log sLog = LogFactory.getLog(HttpWriter.class);

    private class AsynchroneHttpWriterRunnable implements Runnable
    {
        private ExecutionFlowPO mExecutionFlowToLog;

        public AsynchroneHttpWriterRunnable(ExecutionFlowPO pExecutionFlowToLog)
        {
            mExecutionFlowToLog = pExecutionFlowToLog;
        }

        public void run()
        {
            // multipart/form-data
            // TODO Auto-generated method stub
            // ObjectWriter tWriter = new ObjectWriter();
            // tWriter.
            // 
            // Content-Type=
            try
            {
                long tStartTime = System.currentTimeMillis();

                ByteArrayOutputStream tBytes = new ByteArrayOutputStream();
                ObjectOutputStream tStream = new ObjectOutputStream(tBytes);
                tStream.writeObject(mExecutionFlowToLog);
                String tUrl = ConfigurationHelper.getString(URL_PARAM_NAME);
                PostMethod tHttpPost = new PostMethod(tUrl);
                byte[] tByteArray = tBytes.toByteArray();
                tHttpPost.setRequestEntity(new InputStreamRequestEntity(new ByteArrayInputStream(tByteArray), CONTENT_TYPE));
                tHttpPost.setContentChunked(true);
                HttpClient client = new HttpClient();

                try
                {
                    client.executeMethod(tHttpPost);
                    long tEndTime = System.currentTimeMillis();
                    sLog.info("Inserted vith Http ExecutionFlow " + mExecutionFlowToLog + " in " + (tEndTime - tStartTime)
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
                    + e.getMessage());
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
