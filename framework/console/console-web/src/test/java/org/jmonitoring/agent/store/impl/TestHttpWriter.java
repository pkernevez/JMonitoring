package org.jmonitoring.agent.store.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;

import junit.framework.TestCase;

import org.jmonitoring.agent.store.StoreFactory;
import org.jmonitoring.agent.store.StoreManager;
import org.jmonitoring.core.configuration.ConfigurationHelper;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.test.dao.PersistanceTestCase;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestHttpWriter extends TestCase
{

    protected String error;

    private String mRequest;

    public void testWriteExecutionFlow() throws InterruptedException, IOException
    {
        startServer();
        StoreManager.changeStoreManagerClass(HttpWriter.class);
        ConfigurationHelper.setProperty(HttpWriter.URL_PARAM_NAME, "http://localhost:83/console-web/Store.do");
        ExecutionFlowPO tFlow = PersistanceTestCase.buildNewFullFlow();
        StoreFactory.getWriter().writeExecutionFlow(tFlow);
        Thread.sleep(3000);
        assertEquals("POST /console-web/Store.do HTTP/1.1\r\n" + "User-Agent: Jakarta Commons-HttpClient/3.1-rc1\r\n"
            + "Host: localhost:83\r\n" + "Transfer-Encoding: chunked\r\n" + "Content-Type: JMonitoring/flow\r\n\r",
            mRequest);
    }

    private void startServer() throws IOException
    {
        Runnable tRun = new Runnable()
        {
            public void run()
            {
                try
                {
                    ServerSocket tServerSock;
                    tServerSock = ServerSocketFactory.getDefault().createServerSocket(83);
                    Socket tSocket = tServerSock.accept();
                    InputStream tIn = tSocket.getInputStream();
                    int tRead = tIn.read();
                    StringBuffer tBuf = new StringBuffer();
                    boolean tIsActive = true;
                    int[] tLastRead = new int[4];
                    while (tRead != -1 && tIsActive)
                    {
                        tBuf.append((char) tRead);
                        tRead = tIn.read();
                        tIsActive = checkActive(tRead, tLastRead);
                    }
                    mRequest = tBuf.toString();
                    OutputStreamWriter tWriter = new OutputStreamWriter(tSocket.getOutputStream());
                    tWriter.append("HTTP/1.1 200\r\n");
                    // tWriter.append( "Content-Type: text/html; charset=utf-8\r\n" );
                    // tWriter.append( "Content-Length: 0\r\n" );
                    // tWriter.append( "Connection: close\r\n" );
                    // tWriter.append( "Server: FitNesse-20050731\r\n\r" );
                    tSocket.close();
                    Thread.sleep(100);
                    tServerSock.close();
                } catch (IOException e)
                {
                    error = e.getMessage();
                } catch (InterruptedException e)
                {
                    error = e.getMessage();
                }
            }

            private boolean checkActive(int read, int[] lastRead)
            {
                lastRead[0] = lastRead[1];
                lastRead[1] = lastRead[2];
                lastRead[2] = lastRead[3];
                lastRead[3] = read;
                return !(lastRead[0] == 13 && lastRead[1] == 10 && lastRead[2] == 13 && lastRead[3] == 10);
            }
        };
        Thread tThread = new Thread(tRun);
        tThread.setDaemon(true);
        tThread.start();
    }

}
