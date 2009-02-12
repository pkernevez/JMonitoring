package org.jmonitoring.agent.store;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import javax.annotation.Resource;

import org.aspectj.lang.Signature;
import org.jmonitoring.core.domain.ExecutionFlowPO;
import org.jmonitoring.core.domain.MethodCallPO;
import org.jmonitoring.core.info.IParamaterTracer;
import org.jmonitoring.core.info.IResultTracer;
import org.jmonitoring.core.info.IThrowableTracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Permet de logger sous forme XML l'ensemble des appels avec les signature et les temps d'ex�cution dans un fichier XML �
 * l'aide de log4j.
 * 
 * This class is not Synchronized.
 * 
 * @author PKE
 */
public class StoreManager
{

    public static final String STORE_MANAGER_NAME = "storeManager";

    private MethodCallPO mCurrentLogPoint;

    private static Logger sLog = LoggerFactory.getLogger(StoreManager.class);

    @Resource(name = "storeWriter")
    private IStoreWriter mStoreWriter;

    private String mServerName;

    public StoreManager()
    {
        try
        {
            java.net.InetAddress localMachine = java.net.InetAddress.getLocalHost();
            mServerName = localMachine.getHostName();
        } catch (java.net.UnknownHostException ex)
        {
            mServerName = "babasse";
        }
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
        Object pTarget)
    {
        CharSequence tArgs;
        try
        {
            tArgs = (pTracer == null ? null : pTracer.convertToString(pTarget, pArgs));
        } catch (Throwable tT)
        {
            String tClassName = pSignature.getDeclaringTypeName();
            String tMethodName = pSignature.getName();
            sLog.error("Unable to getArguments of class=[" + tClassName + "] and method=[" + tMethodName + "]", tT);
            tArgs = "";
        }

        if (mCurrentLogPoint == null)
        { // Premier appel du Thread
            if (sLog.isDebugEnabled())
            {
                sLog.debug("logBeginOfMethod First Time" + pSignature);
            }
            mCurrentLogPoint =
                new MethodCallPO(null, pSignature.getDeclaringTypeName(), pSignature.getName(), pGroupName,
                                 (tArgs == null ? null : tArgs.toString()));
        } else
        {
            if (sLog.isDebugEnabled())
            {
                sLog.debug("logBeginOfMethod Any Time" + pSignature);
            }
            MethodCallPO tOldPoint = mCurrentLogPoint;
            mCurrentLogPoint =
                new MethodCallPO(tOldPoint, pSignature.getDeclaringTypeName(), pSignature.getName(), pGroupName,
                                 (tArgs == null ? null : tArgs.toString()));
        }
        if (pTarget != null && !pTarget.getClass().equals(pSignature.getDeclaringType()))
        {
            mCurrentLogPoint.setRuntimeClassName(pTarget.getClass().getName());
        }
    }

    /**
     * Trace the result of a method ended normally.
     * 
     * @param pTarget The target instance on which the Method was called.
     * @param pResult The result of the execution of the method.
     */
    public void logEndOfMethodNormal(IResultTracer pTracer, Object pTarget, Object pResult)
    {
        String tResultAsString;
        try
        {
            tResultAsString = (pTracer == null ? null : pTracer.convertToString(pTarget, pResult).toString());
        } catch (Throwable tT)
        {
            String tClassName = (pResult == null ? "" : pResult.getClass().getName());
            String tTracerClassName = pTracer.getClass().getName();
            sLog.error("Unable to trace class=[" + tClassName + "] with tracer=[" + tTracerClassName + "]", tT);
            tResultAsString = "";
        }
        // To limit call to toString on business object, that could be expensive
        endMethod(mCurrentLogPoint, tResultAsString);
        if (mCurrentLogPoint.getParentMethodCall() == null)
        { // Dernier appel du Thread
            if (sLog.isDebugEnabled())
            {
                sLog.debug("logEndOfMethodNormal Last Time" + tResultAsString);
            }
            ExecutionFlowPO tFlow =
                new ExecutionFlowPO(Thread.currentThread().getName(), mCurrentLogPoint, mServerName);
            mStoreWriter.writeExecutionFlow(tFlow);
            mCurrentLogPoint = null;
        } else
        {
            if (sLog.isDebugEnabled())
            {
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
    public void logEndOfMethodWithException(IThrowableTracer pTracer, Throwable pException)
    {
        if (sLog.isDebugEnabled())
        {
            sLog.debug("logEndOfMethodWithException " + (pException == null ? "" : pException.getMessage()));
        }
        if (pException == null)
        { // On ne logue pas le d�tail
            endMethodWithException(mCurrentLogPoint, null, null);
        } else
        {
            CharSequence tOutput;
            try
            {
                tOutput = (pTracer == null ? "" : pTracer.convertToString(pException));
            } catch (Throwable e)
            {
                String tExceptionClass = pException.getClass().getName();
                String tLogClass = (pTracer == null ? "" : pTracer.getClass().getName());
                sLog.error("The log of the Exception as Throw an exception during it, Exception=[" + tExceptionClass
                    + "] Traccer=[" + tLogClass + "]");
                tOutput = "";
            }
            endMethodWithException(mCurrentLogPoint, pException.getClass().getName(), tOutput.toString());
        }

        if (mCurrentLogPoint.getParentMethodCall() == null)
        { // Dernier appel du Thread
            if (sLog.isDebugEnabled())
            {
                sLog.debug("logEndOfMethodWithException Last Time" + pException.getMessage());
            }
            ExecutionFlowPO tFlow =
                new ExecutionFlowPO(Thread.currentThread().getName(), mCurrentLogPoint, mServerName);
            mStoreWriter.writeExecutionFlow(tFlow);
            mCurrentLogPoint = null;
        } else
        {
            if (sLog.isDebugEnabled())
            {
                sLog
                    .debug("logEndOfMethodWithException Any Time" + (pException == null ? "" : pException.getMessage()));
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
    public void endMethod(MethodCallPO pMethodCall, String pReturnValue)
    {
        pMethodCall.setEndTime(System.currentTimeMillis());
        if (pReturnValue != null)
        {
            try
            {
                pMethodCall.setReturnValue(pReturnValue);
            } catch (Throwable tT)
            {
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
    private void endMethodWithException(MethodCallPO pMethodCall, String pExceptionClassName, String pExceptionMessage)
    {
        pMethodCall.setEndTime(System.currentTimeMillis());
        pMethodCall.setThrowableClass(pExceptionClassName);
        pMethodCall.setThrowableMessage(pExceptionMessage);
    }

    static void setLog(Logger pLog)
    {
        sLog = pLog;
    }

    static Logger getLog()
    {
        return sLog;
    }

    IStoreWriter getStoreWriter()
    {
        return mStoreWriter;
    }

    public void setStoreWriter(IStoreWriter pWriter)
    {
        mStoreWriter = pWriter;
    }

    String getServerName()
    {
        return mServerName;
    }

    public void setServerName(String pServerName)
    {
        mServerName = pServerName;
    }

}
