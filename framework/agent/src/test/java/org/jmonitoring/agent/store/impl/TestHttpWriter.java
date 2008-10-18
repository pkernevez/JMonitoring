package org.jmonitoring.agent.store.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jmonitoring.agent.store.StoreFactory;
import org.jmonitoring.agent.store.StoreManager;
import org.jmonitoring.core.configuration.ConfigurationHelper;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPK;
import org.jmonitoring.core.domain.MethodCallPO;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class TestHttpWriter extends TestCase {

    protected static final Log sLog = LogFactory.getLog(TestHttpWriter.class);

    protected String error;

    private String mRequest;

    public void testWriteExecutionFlow() throws InterruptedException, IOException {
        ConfigurationHelper.setProperty(HttpWriter.HOSTNAME, "localhost");
        ConfigurationHelper.setProperty(HttpWriter.PORT, "83");
        startServer();
        StoreManager.changeStoreWriterClass(HttpWriter.class);
        HttpWriter.configure();
        ExecutionFlowPO tFlow = buildNewFullFlow();
        // for (int i = 0; i < 200; i++)
        // {
        StoreFactory.getWriter().writeExecutionFlow(tFlow);
        // }
        Thread.sleep(5000);
        assertEquals("POST /console-web/Store.do HTTP/1.1\r\n" + "Content-Type: JMonitoring/flow\r\n"
                + "User-Agent: Jakarta Commons-HttpClient/3.1-rc1\r\n" + "Host: localhost:83\r\n"
                + "Transfer-Encoding: chunked\r\n\r", mRequest);
    }

    private void startServer() throws IOException {
        Runnable tRun = new Runnable() {
            public void run() {
                try {
                    sLog.info("Server start on port 83");
                    ServerSocket tServerSock;
                    tServerSock = ServerSocketFactory.getDefault().createServerSocket(83);
                    Socket tSocket = tServerSock.accept();
                    InputStream tIn = tSocket.getInputStream();
                    int tRead = tIn.read();
                    StringBuffer tBuf = new StringBuffer();
                    boolean tIsActive = true;
                    int[] tLastRead = new int[4];
                    while (tRead != -1 && tIsActive) {
                        tBuf.append((char) tRead);
                        tRead = tIn.read();
                        tIsActive = checkActive(tRead, tLastRead);
                    }
                    mRequest = tBuf.toString();
                    OutputStreamWriter tWriter = new OutputStreamWriter(tSocket.getOutputStream());
                    tWriter.write("HTTP/1.1 200\r\n");
                    // tWriter.append( "Content-Type: text/html; charset=utf-8\r\n" );
                    // tWriter.append( "Content-Length: 0\r\n" );
                    // tWriter.append( "Connection: close\r\n" );
                    // tWriter.append( "Server: FitNesse-20050731\r\n\r" );
                    tSocket.close();
                    Thread.sleep(100);
                    tServerSock.close();
                } catch (IOException e) {
                    error = e.getMessage();
                } catch (InterruptedException e) {
                    error = e.getMessage();
                }
            }

            private boolean checkActive(int read, int[] lastRead) {
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

    public static ExecutionFlowPO buildNewFullFlow() {
        MethodCallPO tPoint;
        MethodCallPO tSubPoint, tSubPoint2, tSubPoint3, tSubPoint4, tSubPoint5;
        long tStartTime = System.currentTimeMillis();

        tPoint = new MethodCallPO(null, TestHttpWriter.class.getName(), "builNewFullFlow", "GrDefault", "[]");
        tPoint.setBeginTime(tStartTime); // 35
        tSubPoint = new MethodCallPO(tPoint, TestHttpWriter.class.getName(), "builNewFullFlow2", "GrChild1", "[]");
        tSubPoint.setBeginTime(tStartTime + 2); // 3
        tSubPoint.setEndTime(tStartTime + 5);
        tSubPoint.setRuntimeClassName(TestHttpWriter.class.getName() + "iuiu");

        tSubPoint2 = new MethodCallPO(tPoint, TestHttpWriter.class.getName(), "builNewFullFlow3", "GrChild2", "[]");
        tSubPoint2.setBeginTime(tStartTime + 8);// 21

        tSubPoint3 = new MethodCallPO(tSubPoint2, TestHttpWriter.class.getName(), "builNewFullFlow3", "GrChild2", "[]");
        tSubPoint3.setBeginTime(tStartTime + 14);// 1
        tSubPoint3.setEndTime(tStartTime + 15);

        tSubPoint4 = new MethodCallPO(tSubPoint2, TestHttpWriter.class.getName(), "builNewFullFlow3", "GrChild2", "[]");
        tSubPoint4.setBeginTime(tStartTime + 16);// 12

        tSubPoint5 = new MethodCallPO(tSubPoint4, TestHttpWriter.class.getName(), "builNewFullFlow3", "GrChild2", "[]");
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
