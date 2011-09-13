package org.jmonitoring.agent.store;

import java.sql.SQLException;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.jmonitoring.agent.core.PerformanceAspect;
import org.jmonitoring.agent.store.AspectLoggerEmulator.ErrorLogTracer;
import org.jmonitoring.agent.store.impl.MockWriter;
import org.jmonitoring.core.configuration.SpringConfigurationUtil;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.jmonitoring.core.info.IThrowableTracer;
import org.jmonitoring.core.tests.JMonitoringTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

@ContextConfiguration(locations = {"/store-manager-test.xml" })
public class StoreManagerTest extends JMonitoringTestCase
{
    @Before
    public void initProcessor()
    {
        SpringConfigurationUtil.setContext(applicationContext);
        MockProcessor.setKeepFlow(true);
        MockProcessor.clear();
        MockWriter.clear();
        AspectLoggerEmulator.clear();
    }

    @Autowired
    StoreManager mManager;

    @After
    public void checkAndClearPerformanceAspect()
    {
        MethodCallPO tCurMeth = mManager.mCurrentLogPoint.get();
        mManager.mCurrentLogPoint.set(null);
        assertNull("The current MethodCall was not cleared", tCurMeth);
    }

    private void callOneExecutionFlow(boolean pLogDebugEnabled) throws InterruptedException
    {
        // Check the count
        assertEquals(0, MockWriter.getNbLog());

        AspectLoggerEmulator tEmulator = new AspectLoggerEmulator(applicationContext);
        tEmulator.simulateExecutionFlow(pLogDebugEnabled);
        assertEquals(1, MockWriter.getNbLog());
    }

    private void callOneExecutionFlowWithExceptionInMain(boolean pLogDebugEnabled) throws InterruptedException
    {
        // Check the count
        assertEquals(0, MockWriter.getNbLog());

        AspectLoggerEmulator tEmulator = new AspectLoggerEmulator(applicationContext);
        tEmulator.simulateExecutionFlowWithExceptioninMain(pLogDebugEnabled);

        assertEquals(1, MockWriter.getNbLog());

        assertEquals(1, MockProcessor.getNbCall());
    }

    /**
     * Check the number of toString() method called on the object associated to MethodCallDTO. This is important because
     * of the cost of a toString method on complexe objects.
     * 
     * @throws InterruptedException no doc
     */
    @Test
    public void testNbToStringMethodCallWithLogOfParameter() throws InterruptedException
    {
        MockProcessor.setKeepFlow(true);
        callOneExecutionFlow(true);

        // Now check the number of toString called
        assertEquals(3, AspectLoggerEmulator.Param.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Parent.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Child1.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Child2.getNbToString());
        assertEquals(2, AspectLoggerEmulator.getNbReturnValueToString());
        List<Object> tErrors = ((ErrorLogTracer) StoreManager.getLog()).mErrors;
        assertEquals(1, tErrors.size());
        assertEquals(String.class.getName(), tErrors.get(0).getClass().getName());
        assertEquals("Unable to trace class=[org.jmonitoring.agent.store.AspectLoggerEmulator$ExceptionResult] "
                         + "with tracer=[org.jmonitoring.agent.info.impl.ToStringResultTracer]Pour faire planter un appelMain",
                     (String) tErrors.get(0));
    }

    /**
     * Check the number of toString() method called on the object associated to MethodCallDTO. This is important because
     * of the cost of a toString method on complexe objects.
     * 
     * @throws InterruptedException no doc
     */
    @Test
    public void testNbToStringMethodCallWithoutLogOfParameter() throws InterruptedException
    {
        MockProcessor.setKeepFlow(true);
        callOneExecutionFlow(false);

        // Now check the number of toString called
        assertEquals(3, AspectLoggerEmulator.Param.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Parent.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Child1.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Child2.getNbToString());
        assertEquals(2, AspectLoggerEmulator.getNbReturnValueToString());
        List<Object> tErrors = ((ErrorLogTracer) StoreManager.getLog()).mErrors;
        assertEquals(1, tErrors.size());
        assertEquals(String.class.getName(), tErrors.get(0).getClass().getName());
        assertEquals("Unable to trace class=[org.jmonitoring.agent.store.AspectLoggerEmulator$ExceptionResult]"
                         + " with tracer=[org.jmonitoring.agent.info.impl.ToStringResultTracer]Pour faire planter un appelMain",
                     (String) tErrors.get(0));
    }

    /**
     * Check the number of toString() method called on the object associated to MethodCallDTO. This is important because
     * of the cost of a toString method on complexe objects.
     * 
     * @throws InterruptedException no doc
     */
    @Test
    public void testNbToStringMethodCallWithLogAndExceptionInMain() throws InterruptedException
    {
        MockProcessor.setKeepFlow(true);
        callOneExecutionFlowWithExceptionInMain(true);

        // Now check the number of toString called
        assertEquals(5, AspectLoggerEmulator.Param.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Parent.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Child1.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Child2.getNbToString());
        assertEquals(1, AspectLoggerEmulator.getNbReturnValueToString());
        List<Object> tErrors = ((ErrorLogTracer) StoreManager.getLog()).mErrors;
        assertEquals(0, tErrors.size());
    }

    /**
     * Check the number of toString() method called on the object associated to MethodCallDTO. This is important because
     * of the cost of a toString method on complexe objects.
     * 
     * @throws InterruptedException no doc
     */
    @Test
    public void testNbToStringMethodCallWithoutLogAndExceptionInMain() throws InterruptedException
    {
        MockProcessor.setKeepFlow(true);
        callOneExecutionFlowWithExceptionInMain(false);

        // Now check the number of toString called
        assertEquals(5, AspectLoggerEmulator.Param.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Parent.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Child1.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Child2.getNbToString());
        assertEquals(1, AspectLoggerEmulator.getNbReturnValueToString());
        List<Object> tErrors = ((ErrorLogTracer) StoreManager.getLog()).mErrors;
        assertEquals(0, tErrors.size());
    }

    private static final int NB_FLOW_TO_LOG = 100;

    @Test
    public void testAsynchronePublicationKeepByProcessor() throws InterruptedException
    {
        // Check the count
        assertEquals(0, MockWriter.getNbLog());

        AspectLoggerEmulator tEmulator = new AspectLoggerEmulator(applicationContext);
        // Keep all the flows
        MockProcessor.setKeepFlow(true);
        for (int i = 0; i < NB_FLOW_TO_LOG; i++)
        {
            tEmulator.simulateExecutionFlow(true);
        }
        assertEquals(NB_FLOW_TO_LOG, MockWriter.getNbLog());
    }

    @Test
    public void testAsynchronePublicationForgottenByProcessor() throws InterruptedException
    {
        MockProcessor.setKeepFlow(true);
        // Check the count
        assertEquals(0, MockWriter.getNbLog());

        AspectLoggerEmulator tEmulator = new AspectLoggerEmulator(applicationContext);
        // Keep all the flows
        MockProcessor.setKeepFlow(false);
        for (int i = 0; i < NB_FLOW_TO_LOG; i++)
        {
            tEmulator.simulateExecutionFlow(true);
        }
        assertEquals(0, MockWriter.getNbLog());
    }

    /**
     * Check the number of toString() method called on the object associated to MethodCallDTO. This is important because
     * of the cost of a toString method on complexe objects.
     * 
     * @throws InterruptedException no doc
     * @throws SQLException no doc
     */
    @Test
    public void testNbToStringMethodCall() throws InterruptedException
    {
        MockProcessor.setKeepFlow(true);

        AspectLoggerEmulator tEmulator = new AspectLoggerEmulator(applicationContext);
        tEmulator.simulateExecutionFlow(true);

        // Now check the number of toString called
        assertEquals(3, AspectLoggerEmulator.Param.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Parent.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Child1.getNbToString());
        assertEquals(0, AspectLoggerEmulator.Child2.getNbToString());

        assertEquals(1, MockWriter.getNbLog());
    }

    @Test
    public void testClearChangeManager()
    {
        MockProcessor.setKeepFlow(true);
        StoreManager tManager = (StoreManager) applicationContext.getBean(StoreManager.STORE_MANAGER_NAME);
        assertEquals("TestMachine", tManager.getServerName());
        assertNotNull(tManager.getStoreWriter());
        StoreManager tManager2 = (StoreManager) applicationContext.getBean(StoreManager.STORE_MANAGER_NAME);
        assertSame(tManager, tManager2);
    }

    private Filter trueFilter = new Filter()
    {
        public boolean keep(MethodCallPO pCurrent)
        {
            return true;
        }
    };

    private Filter falseFilter = new Filter()
    {
        public boolean keep(MethodCallPO pCurrent)
        {
            return false;
        }
    };

    private Filter buggyFilter = new Filter()
    {
        public boolean keep(MethodCallPO pCurrent)
        {
            throw new RuntimeException("Buggy Filter");
        }
    };

    @Test
    public void testNormalWithFiltersNullExpectedBoth()
    {
        StoreManager2LevelAspect.setFilter(null);
        StoreManager1LevelAspect.setFilter(null);
        callWithFilterOk2Level();
        assertEquals(1, MockWriter.getNbLog());
        ExecutionFlowPO tExecutionFlowPO = MockWriter.getExecutionsFlows().get(0);
        assertEquals("callWithFilterOk2Level", tExecutionFlowPO.getFirstMethodName());
        assertEquals(1, tExecutionFlowPO.getFirstMethodCall().getChildren().size());
        assertEquals("callWithFilterOk1Level", tExecutionFlowPO.getFirstMethodCall().getChild(0).getMethodName());
    }

    @Test
    public void testNormalWithFiltersOkExpectedBoth()
    {
        StoreManager2LevelAspect.setFilter(trueFilter);
        StoreManager1LevelAspect.setFilter(trueFilter);
        callWithFilterOk2Level();
        assertEquals(1, MockWriter.getNbLog());
        ExecutionFlowPO tExecutionFlowPO = MockWriter.getExecutionsFlows().get(0);
        assertEquals("callWithFilterOk2Level", tExecutionFlowPO.getFirstMethodName());
        assertEquals(1, tExecutionFlowPO.getFirstMethodCall().getChildren().size());
        assertEquals("callWithFilterOk1Level", tExecutionFlowPO.getFirstMethodCall().getChild(0).getMethodName());
    }

    @Test
    public void testNormalWithSubFilterKoExpectedParent()
    {
        StoreManager2LevelAspect.setFilter(trueFilter);
        StoreManager1LevelAspect.setFilter(falseFilter);
        callWithFilterOk2Level();
        assertEquals(1, MockWriter.getNbLog());
        ExecutionFlowPO tExecutionFlowPO = MockWriter.getExecutionsFlows().get(0);
        assertEquals("callWithFilterOk2Level", tExecutionFlowPO.getFirstMethodName());
        assertEquals(0, tExecutionFlowPO.getFirstMethodCall().getChildren().size());
    }

    @Test
    public void testNormalWithParentFilterKoExpectedNothing()
    {
        StoreManager2LevelAspect.setFilter(falseFilter);
        StoreManager1LevelAspect.setFilter(trueFilter);
        callWithFilterOk2Level();
        assertEquals(0, MockWriter.getNbLog());
    }

    @Test
    public void testNormalWithBuggyFilter()
    {
        StoreManager2LevelAspect.setFilter(trueFilter);
        StoreManager1LevelAspect.setFilter(buggyFilter);
        callWithFilterOk2Level();
        assertEquals(1, MockWriter.getNbLog());
        ExecutionFlowPO tExecutionFlowPO = MockWriter.getExecutionsFlows().get(0);
        assertEquals("callWithFilterOk2Level", tExecutionFlowPO.getFirstMethodName());
        assertEquals(1, tExecutionFlowPO.getFirstMethodCall().getChildren().size());
        assertEquals("callWithFilterOk1Level", tExecutionFlowPO.getFirstMethodCall().getChild(0).getMethodName());
    }

    @Test
    public void testExceptionWithBuggyFilter()
    {
        StoreManager2LevelAspect.setFilter(trueFilter);
        StoreManager1LevelAspect.setFilter(buggyFilter);
        callWithException();
        assertEquals(1, MockWriter.getNbLog());
        ExecutionFlowPO tExecutionFlowPO = MockWriter.getExecutionsFlows().get(0);
        assertEquals("callWithFilterOk2LevelAndException", tExecutionFlowPO.getFirstMethodName());
        assertEquals(1, tExecutionFlowPO.getFirstMethodCall().getChildren().size());
        assertEquals("callWithFilterOk1LevelAndException", tExecutionFlowPO.getFirstMethodCall().getChild(0).getMethodName());
    }

    @Test
    public void testNormalWithFiltersKoExpectedNone()
    {
        StoreManager2LevelAspect.setFilter(falseFilter);
        StoreManager1LevelAspect.setFilter(falseFilter);
        callWithFilterOk2Level();
        assertEquals(0, MockWriter.getNbLog());
    }

    @Test
    public void testExceptionWithFiltersNullExpectedBoth()
    {
        StoreManager2LevelAspect.setFilter(null);
        StoreManager1LevelAspect.setFilter(null);
        callWithException();
        assertEquals(1, MockWriter.getNbLog());
        ExecutionFlowPO tExecutionFlowPO = MockWriter.getExecutionsFlows().get(0);
        assertEquals("callWithFilterOk2LevelAndException", tExecutionFlowPO.getFirstMethodName());
        assertEquals(1, tExecutionFlowPO.getFirstMethodCall().getChildren().size());
        assertEquals("callWithFilterOk1LevelAndException", tExecutionFlowPO.getFirstMethodCall().getChild(0)
                                                                           .getMethodName());
        assertNotNull(tExecutionFlowPO.getFirstMethodCall().getChild(0).getThrowableClass());
    }

    private void callWithException()
    {
        try
        {
            callWithFilterOk2LevelAndException();
            fail("Shoulf throw an exception");
        } catch (RuntimeException e)
        {
            assertEquals("Error", e.getMessage());
        }
    }

    @Test
    public void testExceptionWithFiltersOkExpectedBoth()
    {
        StoreManager2LevelAspect.setFilter(trueFilter);
        StoreManager1LevelAspect.setFilter(trueFilter);
        callWithException();
        assertEquals(1, MockWriter.getNbLog());
        ExecutionFlowPO tExecutionFlowPO = MockWriter.getExecutionsFlows().get(0);
        assertEquals("callWithFilterOk2LevelAndException", tExecutionFlowPO.getFirstMethodName());
        assertEquals(1, tExecutionFlowPO.getFirstMethodCall().getChildren().size());
        assertEquals("callWithFilterOk1LevelAndException", tExecutionFlowPO.getFirstMethodCall().getChild(0)
                                                                           .getMethodName());
        assertNotNull(tExecutionFlowPO.getFirstMethodCall().getChild(0).getThrowableClass());
    }

    @Test
    public void testExceptionWithSubFilterKoExpectedParent()
    {
        StoreManager2LevelAspect.setFilter(trueFilter);
        StoreManager1LevelAspect.setFilter(falseFilter);
        callWithException();
        assertEquals(1, MockWriter.getNbLog());
        ExecutionFlowPO tExecutionFlowPO = MockWriter.getExecutionsFlows().get(0);
        assertEquals("callWithFilterOk2LevelAndException", tExecutionFlowPO.getFirstMethodName());
        assertEquals(0, tExecutionFlowPO.getFirstMethodCall().getChildren().size());
    }

    @Test
    public void testExceptionWithParentFilterKoExpectedNothing()
    {
        StoreManager2LevelAspect.setFilter(falseFilter);
        StoreManager1LevelAspect.setFilter(trueFilter);
        callWithException();
        assertEquals(0, MockWriter.getNbLog());
    }

    @Test
    public void testExceptionWithFiltersKoExpectedNone()
    {
        StoreManager2LevelAspect.setFilter(falseFilter);
        StoreManager1LevelAspect.setFilter(falseFilter);
        callWithException();
        assertEquals(0, MockWriter.getNbLog());
    }

    /** Use by test pointcut */
    private void callWithFilterOk2Level()
    {
        callWithFilterOk1Level();
    }

    /** Use by test pointcut */
    private void callWithFilterOk1Level()
    {
    }

    /** Use by test pointcut */
    private void callWithFilterOk2LevelAndException()
    {
        callWithFilterOk1LevelAndException();
    }

    /** Use by test pointcut */
    private void callWithFilterOk1LevelAndException()
    {
        throw new RuntimeException("Error");
    }

    private static class NullTracer implements IThrowableTracer{

        public CharSequence convertToString(Throwable pException)
        {
            return null;
        }
        
    }
    private static class MsgTracer implements IThrowableTracer{

        public CharSequence convertToString(Throwable pException)
        {
            return pException.getMessage();
        }
        
    }
    @Test
    public void testBuildExceptionMsg()
    {
        assertEquals("", mManager.buildExceptionMsg(null, new RuntimeException("Not use"), null).toString());
        assertEquals("", mManager.buildExceptionMsg(new NullTracer(), new RuntimeException("Not use"), null).toString());
        assertEquals("Other info", mManager.buildExceptionMsg(new NullTracer(), new RuntimeException("Not use"), "Other info").toString());
        assertEquals("Info", mManager.buildExceptionMsg(new MsgTracer(), new RuntimeException("Info"), null).toString());
        assertEquals("", mManager.buildExceptionMsg(null, new RuntimeException("Info"), null).toString());
        assertEquals("Info\nOther info", mManager.buildExceptionMsg(new MsgTracer(), new RuntimeException("Info"), "Other info").toString());
    }

    // TODO Factorize test code ! Do repeat it !

}
