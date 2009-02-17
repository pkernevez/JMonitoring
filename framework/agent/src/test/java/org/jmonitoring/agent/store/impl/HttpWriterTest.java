package org.jmonitoring.agent.store.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;

import org.jmonitoring.agent.store.IStoreWriter;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPK;
import org.jmonitoring.core.domain.MethodCallPO;
import org.jmonitoring.core.tests.JMonitoringTestCase;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/
@ContextConfiguration(locations = {"/http-test-writer.xml" })
public class HttpWriterTest extends JMonitoringTestCase
{

    protected static final Logger sLog = LoggerFactory.getLogger(HttpWriterTest.class);

    protected String error;

    private String mRequest;

    @Test
    public void testWriteExecutionFlow() throws InterruptedException, IOException
    {
        startServer();
        HttpWriter tWriter = (HttpWriter) applicationContext.getBean(IStoreWriter.STORE_WRITER_NAME);
        ExecutionFlowPO tFlow = buildNewFullFlow();
        tWriter.writeExecutionFlow(tFlow);
        Thread.sleep(1000);
        assertEquals("Test server response", "POST /console-web/Store.do HTTP/1.1\r\n"
            + "Content-Type: JMonitoring/flow\r\n" + "User-Agent: Jakarta Commons-HttpClient/3.1-rc1\r\n"
            + "Host: localhost:83\r\n" + "Transfer-Encoding: chunked\r\n\r", mRequest);
    }

    private void startServer() throws IOException
    {
        Runnable tRun = new Runnable()
        {
            public void run()
            {
                try
                {
                    sLog.info("Server start on port 83");
                    ServerSocket tServerSock;
                    tServerSock = ServerSocketFactory.getDefault().createServerSocket(83);
                    Socket tSocket = tServerSock.accept();
                    InputStream tIn = tSocket.getInputStream();
                    int tRead = tIn.read();
                    StringBuilder tBuf = new StringBuilder();
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
                    tWriter.write("HTTP/1.1 200\r\n");
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

    public static ExecutionFlowPO buildNewFullFlow()
    {
        MethodCallPO tPoint;
        MethodCallPO tSubPoint, tSubPoint2, tSubPoint3, tSubPoint4, tSubPoint5;
        long tStartTime = System.currentTimeMillis();

        tPoint = new MethodCallPO(null, HttpWriterTest.class.getName(), "builNewFullFlow", "GrDefault", "[]");
        tPoint.setBeginTime(tStartTime); // 35
        tSubPoint = new MethodCallPO(tPoint, HttpWriterTest.class.getName(), "builNewFullFlow2", "GrChild1", "[]");
        tSubPoint.setBeginTime(tStartTime + 2); // 3
        tSubPoint.setEndTime(tStartTime + 5);
        tSubPoint.setRuntimeClassName(HttpWriterTest.class.getName() + "iuiu");

        tSubPoint2 = new MethodCallPO(tPoint, HttpWriterTest.class.getName(), "builNewFullFlow3", "GrChild2", "[]");
        tSubPoint2.setBeginTime(tStartTime + 8);// 21

        tSubPoint3 = new MethodCallPO(tSubPoint2, HttpWriterTest.class.getName(), "builNewFullFlow3", "GrChild2", "[]");
        tSubPoint3.setBeginTime(tStartTime + 14);// 1
        tSubPoint3.setEndTime(tStartTime + 15);

        tSubPoint4 = new MethodCallPO(tSubPoint2, HttpWriterTest.class.getName(), "builNewFullFlow3", "GrChild2", "[]");
        tSubPoint4.setBeginTime(tStartTime + 16);// 12

        tSubPoint5 = new MethodCallPO(tSubPoint4, HttpWriterTest.class.getName(), "builNewFullFlow3", "GrChild2", "[]");
        tSubPoint5.setBeginTime(tStartTime + 26);// 1
        tSubPoint5.setEndTime(tStartTime + 27);

        tSubPoint4.setEndTime(tStartTime + 28);
        tSubPoint2.setEndTime(tStartTime + 29);

        tPoint.setEndTime(tStartTime + 35);
        ExecutionFlowPO tFlow = new ExecutionFlowPO("TEST-main", tPoint, "myJVM");
        tPoint.setMethId(new MethodCallPK(tFlow, 1));
        tSubPoint.setMethId(new MethodCallPK(tFlow, 2));
        tSubPoint2.setMethId(new MethodCallPK(tFlow, 3));
        tSubPoint3.setMethId(new MethodCallPK(tFlow, 4));
        tSubPoint4.setMethId(new MethodCallPK(tFlow, 5));
        tSubPoint5.setMethId(new MethodCallPK(tFlow, 6));
        return tFlow;
    }

}
