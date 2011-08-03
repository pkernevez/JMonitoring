package org.jmonitoring.agent.store.impl;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPK;
import org.jmonitoring.core.domain.MethodCallPO;

public class GzipFileTracerTest extends TestCase
{

    private static ExecutionFlowPO buildNewFullFlow()
    {
        MethodCallPO tPoint;
        MethodCallPO tSubPoint, tSubPoint2, tSubPoint3, tSubPoint4, tSubPoint5;
        long tStartTime = System.currentTimeMillis();

        tPoint = new MethodCallPO(null, GzipFileTracerTest.class.getName(), "builNewFullFlow", "GrDefault", "[]");
        tPoint.setBeginTime(tStartTime); // 35
        tSubPoint = new MethodCallPO(tPoint, GzipFileTracerTest.class.getName(), "builNewFullFlow2", "GrChild1", "[]");
        tSubPoint.setBeginTime(tStartTime + 2); // 3
        tSubPoint.setEndTime(tStartTime + 5);
        tSubPoint.setRuntimeClassName(GzipFileTracerTest.class.getName() + "iuiu");

        tSubPoint2 = new MethodCallPO(tPoint, GzipFileTracerTest.class.getName(), "builNewFullFlow3", "GrChild2", "[]");
        tSubPoint2.setBeginTime(tStartTime + 8);// 21

        tSubPoint3 =
            new MethodCallPO(tSubPoint2, GzipFileTracerTest.class.getName(), "builNewFullFlow3", "GrChild2", "[]");
        tSubPoint3.setBeginTime(tStartTime + 14);// 1
        tSubPoint3.setEndTime(tStartTime + 15);

        tSubPoint4 =
            new MethodCallPO(tSubPoint2, GzipFileTracerTest.class.getName(), "builNewFullFlow3", "GrChild2", "[]");
        tSubPoint4.setBeginTime(tStartTime + 16);// 12

        tSubPoint5 =
            new MethodCallPO(tSubPoint4, GzipFileTracerTest.class.getName(), "builNewFullFlow3", "GrChild2", "[]");
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

    public void testWriteExecutionFlow() throws IOException
    {
        GzipFileTracer tTestBean = new GzipFileTracer();
        File tTempDir = File.createTempFile("toto", "tmp").getParentFile();

        tTestBean.setDir(tTempDir.getAbsolutePath());
        tTestBean.writeExecutionFlow(buildNewFullFlow());
    }

    public void testSetDirInvalid()
    {
        GzipFileTracer tTestBean = new GzipFileTracer();
        try
        {
            tTestBean.setDir("/Totaly/Invalid/TEREWREWRW");
            fail();
        } catch (RuntimeException e)
        {
            assertEquals("Unable to create root dir for exporting ExecutionFlow, check your configuration (attribut dir of bean StoreWriter).",
                         e.getMessage());
        }
    }

    public void testSetDirInTempDir()
    {
        GzipFileTracer tTestBean = new GzipFileTracer();
        tTestBean.setDir("target/test/export");
    }

}
