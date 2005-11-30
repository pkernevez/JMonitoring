package org.jmonitoring.core.utils;

import org.aspectj.lang.Signature;
import org.jmonitoring.core.configuration.Configuration;
import org.jmonitoring.core.dto.MeasurePointManager;
import org.jmonitoring.core.store.IStoreWriter;

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
    public void simulateExecutionFlow()
    {
        MeasurePointManager tManager = new MeasurePointManager(mStoreWriter, Configuration.getInstance());
        AspectLoggerEmulator.resetCounters();

        Signature tSignature;
        tSignature = new MockSignature("mainMethod", Parent.class);
        tManager.logBeginOfMethod(tSignature, new Object[] {new Param() }, "Parent");
        tSignature = new MockSignature("child1", Child1.class);
        tManager.logBeginOfMethod(tSignature, new Object[] {new Param() }, "Child1");
        tSignature = new MockSignature("child2", Child1.class);
        tManager.logBeginOfMethod(tSignature, new Object[] {new Param() }, "Child1");
        tManager.logEndOfMethodNormal(null); //child1_1
        tManager.logEndOfMethodNormal(null); //child1_2
        tSignature = new MockSignature("child1", Child2.class);
        tManager.logBeginOfMethod(tSignature, new Object[] {new Param() }, "Child2"); //child2_1
        tManager.logEndOfMethodWithException(new Exception("Funny Exception"));
        tManager.logEndOfMethodNormal(null); //mainMethod
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
}
