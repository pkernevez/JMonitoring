package org.jmonitoring.agent.store.impl;

import org.jmonitoring.agent.store.IStoreWriter;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPK;
import org.jmonitoring.core.domain.MethodCallPO;
import org.jmonitoring.core.tests.JMonitoringTestCase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

@ContextConfiguration(locations = {"/asynchrone-test-writer.xml" })
public class AsynchroneWriterTest extends JMonitoringTestCase
{

    private static final int NB_TIME_TO_WAIT = 20;

    private static final int TIME_TO_WAIT = 1000;

    private static final int NB_FLOW_TO_LOG = 100;

    @Before
    public void init() throws Exception
    {
        MockWriter.clear();
    }

    @Test
    public void testAsynchronePublication() throws InterruptedException
    {
        assertEquals(0, MockWriter.getNbLog());

        IStoreWriter tWriter = (IStoreWriter) applicationContext.getBean(IStoreWriter.STORE_WRITER_NAME);
        // Log NB_FLOW_TO_LOG
        ExecutionFlowPO[] tFlows = new ExecutionFlowPO[NB_FLOW_TO_LOG];
        for (int i = 0; i < NB_FLOW_TO_LOG; i++)
        {
            tFlows[i] = buildNewFullFlow();
        }
        for (int i = 0; i < NB_FLOW_TO_LOG; i++)
        {
            tWriter.writeExecutionFlow(tFlows[i]);
        }
        assertTrue("All flow should not be writtren synchronously", NB_FLOW_TO_LOG > MockWriter.getNbLog());
        int i = 0;
        while (i<NB_TIME_TO_WAIT &&  NB_FLOW_TO_LOG != MockWriter.getNbLog())
        {
            Thread.sleep(TIME_TO_WAIT);
            i++;
        }
        assertEquals(NB_FLOW_TO_LOG, MockWriter.getNbLog());
    }

    public static ExecutionFlowPO buildNewFullFlow()
    {
        MethodCallPO tPoint;
        MethodCallPO tSubPoint, tSubPoint2, tSubPoint3, tSubPoint4, tSubPoint5;
        long tStartTime = System.currentTimeMillis();

        tPoint = new MethodCallPO(null, AsynchroneWriterTest.class.getName(), "builNewFullFlow", "GrDefault", "[]");
        tPoint.setBeginTime(tStartTime); // 35
        tSubPoint =
            new MethodCallPO(tPoint, AsynchroneWriterTest.class.getName(), "builNewFullFlow2", "GrChild1", "[]");
        tSubPoint.setBeginTime(tStartTime + 2); // 3
        tSubPoint.setEndTime(tStartTime + 5);
        tSubPoint.setRuntimeClassName(AsynchroneWriterTest.class.getName() + "iuiu");

        tSubPoint2 =
            new MethodCallPO(tPoint, AsynchroneWriterTest.class.getName(), "builNewFullFlow3", "GrChild2", "[]");
        tSubPoint2.setBeginTime(tStartTime + 8);// 21

        tSubPoint3 =
            new MethodCallPO(tSubPoint2, AsynchroneWriterTest.class.getName(), "builNewFullFlow3", "GrChild2", "[]");
        tSubPoint3.setBeginTime(tStartTime + 14);// 1
        tSubPoint3.setEndTime(tStartTime + 15);

        tSubPoint4 =
            new MethodCallPO(tSubPoint2, AsynchroneWriterTest.class.getName(), "builNewFullFlow3", "GrChild2", "[]");
        tSubPoint4.setBeginTime(tStartTime + 16);// 12

        tSubPoint5 =
            new MethodCallPO(tSubPoint4, AsynchroneWriterTest.class.getName(), "builNewFullFlow3", "GrChild2", "[]");
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
