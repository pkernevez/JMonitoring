package org.jmonitoring.agent.store;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.Signature;
import org.jmonitoring.core.configuration.ConfigurationHelper;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.jmonitoring.core.info.IParamaterTracer;
import org.jmonitoring.core.info.IResultTracer;
import org.jmonitoring.core.info.IThrowableTracer;

/**
 * Permet de logger sous forme XML l'ensemble des appels avec les signature et les temps d'ex�cution dans un fichier XML �
 * l'aide de log4j.
 * 
 * This class is not Synchronized.
 * 
 * @author PKE
 */
public class StoreManager {
    private MethodCallPO mCurrentLogPoint;

    /** <code>CommonsLog</code> instance. */
    private static Log sLog = LogFactory.getLog(StoreManager.class);

    /** End of Parameters */
    private static ThreadLocal sManager = new ThreadLocal();

    private IStoreWriter mStoreWriter;

    /**
     * Default constructor.
     */
    public StoreManager() {
        this(StoreFactory.getWriter());
    }

    /**
     * Constructor for testing purpose.
     * 
     * @param pStoreWriter The <code>IStoreWriter</code> to use.
     */
    public StoreManager(IStoreWriter pStoreWriter) {
        mStoreWriter = pStoreWriter;
    }

    /**
     * Trace a method with its arguments.
     * 
     * @param pSignature The method signature.
     * @param pArgs The method arguments.
     * @param pGroupName The name of the group associated with this <code>MethodCallDTO</code>.
     * @param pTarget The targeted object of this call.
     */
    public void logBeginOfMethod(Signature pSignature, IParamaterTracer pTracer, Object[] pArgs, String pGroupName,
            Object pTarget) {
        String tArgs;
        try {
            tArgs = (pTracer == null ? null : pTracer.convertToString(pTarget, pArgs));
        } catch (Throwable tT) {
            String tClassName = pSignature.getDeclaringTypeName();
            String tMethodName = pSignature.getName();
            sLog.error("Unable to getArguments of class=[" + tClassName + "] and method=[" + tMethodName + "]", tT);
            tArgs = "";
        }

        if (mCurrentLogPoint == null) { // Premier appel du Thread
            if (sLog.isDebugEnabled()) {
                sLog.debug("logBeginOfMethod First Time" + pSignature);
            }
            mCurrentLogPoint = new MethodCallPO(null, pSignature.getDeclaringTypeName(), pSignature.getName(),
                    pGroupName, tArgs);
        } else {
            if (sLog.isDebugEnabled()) {
                sLog.debug("logBeginOfMethod Any Time" + pSignature);
            }
            MethodCallPO tOldPoint = mCurrentLogPoint;
            mCurrentLogPoint = new MethodCallPO(tOldPoint, pSignature.getDeclaringTypeName(), pSignature.getName(),
                    pGroupName, tArgs);
        }
        if (pTarget != null && !pTarget.getClass().equals(pSignature.getDeclaringType())) {
            mCurrentLogPoint.setRuntimeClassName(pTarget.getClass().getName());
        }
    }

    /**
     * Trace the result of a method ended normally.
     * 
     * @param pTarget The target instance on which the Method was called.
     * @param pResult The result of the execution of the method.
     */
    public void logEndOfMethodNormal(IResultTracer pTracer, Object pTarget, Object pResult) {
        String tResultAsString;
        try {
            tResultAsString = (pTracer == null ? null : pTracer.convertToString(pTarget, pResult));
        } catch (Throwable tT) {
            String tClassName = (pResult == null ? "" : pResult.getClass().getName());
            String tTracerClassName = pTracer.getClass().getName();
            sLog.error("Unable to trace class=[" + tClassName + "] with tracer=[" + tTracerClassName + "]", tT);
            tResultAsString = "";
        }
        // To limit call to toString on business object, that could be expensive
        endMethod(mCurrentLogPoint, tResultAsString);
        if (mCurrentLogPoint.getParentMethodCall() == null) { // Dernier appel du Thread
            if (sLog.isDebugEnabled()) {
                sLog.debug("logEndOfMethodNormal Last Time" + tResultAsString);
            }
            ExecutionFlowPO tFlow = new ExecutionFlowPO(Thread.currentThread().getName(), mCurrentLogPoint,
                    ConfigurationHelper.getString("server.name", "Babasse"));
            mStoreWriter.writeExecutionFlow(tFlow);
            mCurrentLogPoint = null;
        } else {
            if (sLog.isDebugEnabled()) {
                sLog.debug("logEndOfMethodNormal Any Time" + tResultAsString);
            }
            mCurrentLogPoint = mCurrentLogPoint.getParentMethodCall();
        }
    }

    /**
     * Trace the <code>Exception</code> thrown during its execution.
     * 
     * @param pTracer The tracer for logging Exception.
     * @param pException The <code>Exception</code> to trace.
     */
    public void logEndOfMethodWithException(IThrowableTracer pTracer, Throwable pException) {
        if (sLog.isDebugEnabled()) {
            sLog.debug("logEndOfMethodWithException " + (pException == null ? "" : pException.getMessage()));
        }
        if (pException == null) { // On ne logue pas le d�tail
            endMethodWithException(mCurrentLogPoint, null, null);
        } else {
            String tOutput;
            try {
                tOutput = (pTracer == null ? "" : pTracer.convertToString(pException));
            } catch (Throwable e) {
                String tExceptionClass = pException.getClass().getName();
                String tLogClass = (pTracer == null ? "" : pTracer.getClass().getName());
                sLog.error("The log of the Exception as Throw an exception during it, Exception=[" + tExceptionClass
                        + "] Traccer=[" + tLogClass + "]");
                tOutput = "";
            }
            endMethodWithException(mCurrentLogPoint, pException.getClass().getName(), tOutput);
        }

        if (mCurrentLogPoint.getParentMethodCall() == null) { // Dernier appel du Thread
            if (sLog.isDebugEnabled()) {
                sLog.debug("logEndOfMethodWithException Last Time" + pException.getMessage());
            }
            ExecutionFlowPO tFlow = new ExecutionFlowPO(Thread.currentThread().getName(), mCurrentLogPoint,
                    ConfigurationHelper.getString("server.name", "Babasse"));
            mStoreWriter.writeExecutionFlow(tFlow);
            mCurrentLogPoint = null;
        } else {
            if (sLog.isDebugEnabled()) {
                sLog.debug("logEndOfMethodWithException Any Time" + (pException == null ? "" : pException.getMessage()));
            }
            mCurrentLogPoint = mCurrentLogPoint.getParentMethodCall();
        }

    }

    /**
     * Define the return value of the method associated with this <code>MethodCallPO</code> when it didn't throw a
     * <code>Throwable</code>.
     * 
     * @param pMethodCall the current methodcall to manage.
     * @param pReturnValue The return value of the method.
     */
    public void endMethod(MethodCallPO pMethodCall, String pReturnValue) {
        pMethodCall.setEndTime(System.currentTimeMillis());
        if (pReturnValue != null) {
            try {
                pMethodCall.setReturnValue(pReturnValue);
            } catch (Throwable tT) {
                sLog.error("Unable to trace return value of call.", tT);
            }
        }
    }

    /**
     * Define the <code>Throwable</code> thrown by the method associated with this <code>MethodCallDTO</code>.
     * 
     * @param pMethodCall the current methodcall to manage.
     * @param pExceptionClassName The name of the <code>Class</code> of the <code>Exception</code>.
     * @param pExceptionMessage The message of the <code>Exception</code>.
     */
    private void endMethodWithException(MethodCallPO pMethodCall, String pExceptionClassName, String pExceptionMessage) {
        pMethodCall.setEndTime(System.currentTimeMillis());
        pMethodCall.setThrowableClass(pExceptionClassName);
        pMethodCall.setThrowableMessage(pExceptionMessage);
    }

    public static void setLog(Log pLog) {
        sLog = pLog;
    }

    public static Log getLog() {
        return sLog;
    }

    /**
     * Permet d'obtenir un logger par Thread.
     * 
     * @return Une instance de la classe de logger parametr�e par mLoggerClass. <code>numm</code> si un erreur se
     *         produit pendant l'initalisation.
     */
    public static StoreManager getManager() {
        StoreManager tResult = (StoreManager) sManager.get();
        if (tResult == null) {
            try {
                tResult = new StoreManager();
                sManager.set(tResult);
            } catch (Exception e) {
                // Impossible de laisser remonter l'erreur car elle se confond avec l'erreur
                // de la m�thode fonctionelle invoqu�e.
                sLog.error("Impossible d'instancier un logger pour tracer les appels", e);
            }
        }
        return tResult;
    }

    public static void clear() {
        sManager = new ThreadLocal();
    }

    public static void changeStoreManagerClass(Class pClass) {
        ConfigurationHelper.setProperty(ConfigurationHelper.STORE_CLASS, pClass.getName());
        StoreFactory.clear();
        sManager = new ThreadLocal();
    }

    IStoreWriter getStoreWriter() {
        return mStoreWriter;
    }
}
