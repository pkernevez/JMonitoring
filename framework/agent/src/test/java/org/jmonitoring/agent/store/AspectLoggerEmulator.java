package org.jmonitoring.agent.store;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.aspectj.lang.Signature;
import org.jmonitoring.agent.info.impl.DefaultExceptionTracer;
import org.jmonitoring.agent.info.impl.ToStringParametersTracer;
import org.jmonitoring.agent.info.impl.ToStringResultTracer;
import org.jmonitoring.agent.store.IStoreWriter;
import org.jmonitoring.agent.store.StoreManager;
import org.jmonitoring.core.configuration.ConfigurationHelper;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * Emulate the <code>PerformanceAspect</code> functionnality for test classes.
 * 
 * @author pke
 * 
 */
public class AspectLoggerEmulator
{
    private IStoreWriter mStoreWriter;

    public static void clear()
    {
        sReturnValueToString = 0;
    }

    /**
     * Default Constrictor.
     * 
     * @param pStoreWriter The logger to use.
     */
    public AspectLoggerEmulator(IStoreWriter pStoreWriter)
    {
        mStoreWriter = pStoreWriter;
    }

    /**
     * Simulate the Aspect interception and use the Logger mecanism.
     */
    public void simulateExecutionFlow(boolean pWithLog)
    {
        StoreManager tManager = new StoreManager(mStoreWriter, ConfigurationHelper.getInstance());
        StoreManager.setLog(new ErrorLogTracer(pWithLog));
        AspectLoggerEmulator.resetCounters();

        Signature tSignature;
        tSignature = new MockSignature("mainMethod", Parent.class);
        tManager.logBeginOfMethod(tSignature, new ToStringParametersTracer(), new Object[] {new Param() }, "Parent",
            new Parent());
        tSignature = new MockSignature("child1", Child1.class);
        tManager.logBeginOfMethod(tSignature, new ToStringParametersTracer(), null, "Child1", new Child1());
        tSignature = new MockSignature("child2", Child1.class);
        tManager.logBeginOfMethod(tSignature, new ToStringParametersTracer(), new Object[] {new Param() }, "Child1",
            new Child1());
        tManager.logEndOfMethodNormal(new ToStringResultTracer(), new Child1(), new NormalResult()); // child1_1
        tManager.logEndOfMethodNormal(new ToStringResultTracer(), new Child1(), null); // child1_2
        tSignature = new MockSignature("child1", Child2.class);
        tManager.logBeginOfMethod(tSignature, new ToStringParametersTracer(), new Object[] {new Param() }, "Child2",
            new Child2()); // child2_1
        tManager.logEndOfMethodWithException(new DefaultExceptionTracer(), new Exception("Funny Exception"));
        tManager.logEndOfMethodNormal(new ToStringResultTracer(), new Parent(), new ExceptionResult("Main")); // mainMethod
    }

    public void simulateExecutionFlowWithExceptioninMain(boolean pWithLog)
    {
        StoreManager tManager = new StoreManager(mStoreWriter, ConfigurationHelper.getInstance());
        StoreManager.setLog(new ErrorLogTracer(pWithLog));
        AspectLoggerEmulator.resetCounters();

        Signature tSignature;
        tSignature = new MockSignature("mainMethod", Parent.class);
        tManager.logBeginOfMethod(tSignature, new ToStringParametersTracer(), new Object[] {new Param(), new Param(),
            new Param() }, "Parent", new Parent());
        tSignature = new MockSignature("child1", Child1.class);
        tManager.logBeginOfMethod(tSignature, new ToStringParametersTracer(), null, "Child1", new Child1());
        tSignature = new MockSignature("child2", Child1.class);
        tManager.logBeginOfMethod(tSignature, new ToStringParametersTracer(), new Object[] {new Param() }, "Child1",
            new Child1());
        tManager.logEndOfMethodNormal(new ToStringResultTracer(), new Child1(), null); // child1_1
        tManager.logEndOfMethodNormal(new ToStringResultTracer(), new Child1(), new NormalResult()); // child1_2
        tSignature = new MockSignature("child1", Child2.class);
        tManager.logBeginOfMethod(tSignature, new ToStringParametersTracer(), new Object[] {new Param() }, "Child2",
            new Child2()); // child2_1
        tManager.logEndOfMethodWithException(new DefaultExceptionTracer(), null);
        tManager.logEndOfMethodWithException(new DefaultExceptionTracer(), new Exception("Funny Exception2")); // mainMethod
    }

    private static class NormalResult
    {
        public String toString()
        {
            nbReturnValueToStringIncrement();
            return "uyuy";
        }
    }

    private static class ExceptionResult
    {
        private String mMsg;

        public ExceptionResult(String pMsg)
        {
            mMsg = pMsg;
        }

        public String toString()
        {
            nbReturnValueToStringIncrement();
            throw new RuntimeException("Pour faire planter un appel" + mMsg);
        }
    }

    public static class ErrorLogTracer implements Log
    {
        public List mErrors = new ArrayList();

        private boolean mLogDebugEnabled;

        public ErrorLogTracer(boolean pLogDebugEnabled)
        {
            mLogDebugEnabled = pLogDebugEnabled;
        }

        public boolean isDebugEnabled()
        {
            return mLogDebugEnabled;
        }

        public boolean isErrorEnabled()
        {
            return true;
        }

        public boolean isFatalEnabled()
        {
            return true;
        }

        public boolean isInfoEnabled()
        {
            return true;
        }

        public boolean isTraceEnabled()
        {
            return true;
        }

        public boolean isWarnEnabled()
        {
            return true;
        }

        public void trace(Object pArg0)
        {
        }

        public void trace(Object pArg0, Throwable pArg1)
        {
        }

        public void debug(Object pArg0)
        {
        }

        public void debug(Object pArg0, Throwable pArg1)
        {
        }

        public void info(Object pArg0)
        {
        }

        public void info(Object pArg0, Throwable pArg1)
        {
        }

        public void warn(Object pArg0)
        {
        }

        public void warn(Object pArg0, Throwable pArg1)
        {
        }

        public void error(Object pArg0)
        {
            mErrors.add("" + pArg0);
        }

        public void error(Object pArg0, Throwable pArg1)
        {
            mErrors.add("" + pArg0 + pArg1.getMessage());
        }

        public void fatal(Object pArg0)
        {
        }

        public void fatal(Object pArg0, Throwable pArg1)
        {
        }

    }

    /**
     * Param class for the simulator flow
     */
    public static class Param
    {
        private static int sNbToStringParam;

        private static synchronized void incrementToString()
        {
            sNbToStringParam++;
        }

        /**
         * No doc
         * 
         * @return no doc
         */
        public static synchronized int getNbToString()
        {
            return sNbToStringParam;
        }

        private static synchronized void resetToString()
        {
            sNbToStringParam = 0;
        }

        /**
         * No doc
         * 
         * @return no doc
         */
        public String toString()
        {
            incrementToString();
            return "Param";
        }
    }

    /**
     * Parent class for the simulation flow.
     */
    public static class Parent
    {
        private static int sNbToStringParent;

        private static synchronized void incrementToString()
        {
            sNbToStringParent++;
        }

        /**
         * No doc
         * 
         * @return no doc
         */
        public static synchronized int getNbToString()
        {
            return sNbToStringParent;
        }

        private static synchronized void resetToString()
        {
            sNbToStringParent = 0;
        }

        /**
         * No doc
         * 
         * @return no doc
         */
        public String toString()
        {
            incrementToString();
            return "Parent";
        }

        /**
         * Main method.
         */
        public void mainMethod()
        {
            Child1 tChild1 = new Child1();
            tChild1.child1();
            tChild1.child2();
            Child2 tChild2 = new Child2();
            tChild2.child1();
        }
    }

    /**
     * First child class for the simulation flow.
     */
    public static class Child1
    {
        private static int sNbToStringChild1;

        private static synchronized void incrementToString()
        {
            sNbToStringChild1++;
        }

        /**
         * No doc
         * 
         * @return no doc
         */
        public static synchronized int getNbToString()
        {
            return sNbToStringChild1;
        }

        private static synchronized void resetToString()
        {
            sNbToStringChild1 = 0;
        }

        /**
         * No doc
         * 
         * @return no doc
         */
        public String toString()
        {
            incrementToString();
            return "Child1";
        }

        /**
         * First method of the flow.
         */
        public void child1()
        {
        }

        /**
         * Second method of the flow.
         */
        public void child2()
        {
        }
    }

    /**
     * Second child class for the simulation flow.
     */
    public static class Child2
    {
        private static int sNbToStringChild2;

        private static synchronized void incrementToString()
        {
            sNbToStringChild2++;
        }

        private static synchronized void resetToString()
        {
            sNbToStringChild2 = 0;
        }

        /**
         * No doc
         * 
         * @return no doc
         */
        public static synchronized int getNbToString()
        {
            return sNbToStringChild2;
        }

        /**
         * No doc
         * 
         * @return no doc
         */
        public String toString()
        {
            incrementToString();
            return "Child2";
        }

        /**
         * Third method of the flow.
         */
        public void child1()
        {
        }
    }

    public static void resetCounters()
    {
        Param.resetToString();
        Parent.resetToString();
        Child1.resetToString();
        Child2.resetToString();

    }

    private static int sReturnValueToString;

    public static int getNbReturnValueToString()
    {
        return sReturnValueToString;
    }

    public static void nbReturnValueToStringIncrement()
    {
        sReturnValueToString++;
    }
}
