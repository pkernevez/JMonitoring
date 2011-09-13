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
 * Permet de logger sous forme XML l'ensemble des appels avec les signature et les temps d'ex�cution dans un fichier XML
 * � l'aide de log4j.
 * 
 * TODO Check this comment ! This class is not Synchronized.
 * 
 * @author PKE
 */
public class StoreManager
{

    public static final String STORE_MANAGER_NAME = "storeManager";

    final ThreadLocal<MethodCallPO> mCurrentLogPoint = new ThreadLocal<MethodCallPO>();

    private static Logger sLog = LoggerFactory.getLogger(StoreManager.class);

    @Resource(name = "storeWriter")
    private IStoreWriter mStoreWriter;

    @Resource(name = "processor")
    private PostProcessor mProcessor;

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

        MethodCallPO tCurrentMethodCallPO = mCurrentLogPoint.get();
        if (tCurrentMethodCallPO == null)
        { // Premier appel du Thread
            if (sLog.isDebugEnabled())
            {
                sLog.debug("logBeginOfMethod First Time" + pSignature);
            }
            tCurrentMethodCallPO =
                new MethodCallPO(null, pSignature.getDeclaringTypeName(), pSignature.getName(), pGroupName,
                                 (tArgs == null ? null : tArgs.toString()));
            mCurrentLogPoint.set(tCurrentMethodCallPO);
        } else
        {
            if (sLog.isDebugEnabled())
            {
                sLog.debug("logBeginOfMethod Any Time" + pSignature);
            }
            MethodCallPO tOldPoint = tCurrentMethodCallPO;
            tCurrentMethodCallPO =
                new MethodCallPO(tOldPoint, pSignature.getDeclaringTypeName(), pSignature.getName(), pGroupName,
                                 (tArgs == null ? null : tArgs.toString()));
            mCurrentLogPoint.set(tCurrentMethodCallPO);
        }
        if (pTarget != null && !pTarget.getClass().equals(pSignature.getDeclaringType()))
        {
            tCurrentMethodCallPO.setRuntimeClassName(pTarget.getClass().getName());
        }
    }

    /**
     * Trace the result of a method ended normally.
     * 
     * @param pTarget The target instance on which the Method was called.
     * @param pResult The result of the execution of the method.
     */
    public void logEndOfMethodNormal(IResultTracer pTracer, Object pTarget, Object pResult, Filter pFilter)
    {
        MethodCallPO tCurrentMethodCall = mCurrentLogPoint.get();
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
        tCurrentMethodCall.setEndTime(System.currentTimeMillis());
        tCurrentMethodCall.setReturnValue(tResultAsString);

        MethodCallPO tParentMethodCall = tCurrentMethodCall.getParentMethodCall();
        boolean tKeep = true;
        try
        {
            tKeep = (pFilter == null || pFilter.keep(tCurrentMethodCall));
        } catch (Throwable t)
        {
            sLog.error("The filter [" + pFilter.getClass().getName()
                + "] is buggy and throw an Exception during filtering, the measure will be kept");
        }
        if (!tKeep)
        {
            // Just forgot this measure
            if (sLog.isInfoEnabled())
            {
                sLog.info("The current measure have been forgiven class=[" + tCurrentMethodCall.getClassName()
                    + "], methodName=[" + tCurrentMethodCall.getMethodName() + "]");
            }
            if (tParentMethodCall != null)
            {
                tParentMethodCall.removeLastChildren();
            }
            mCurrentLogPoint.set(tParentMethodCall);
        } else
        {
            if (tParentMethodCall == null)
            { // Dernier appel du Thread
                if (sLog.isDebugEnabled())
                {
                    sLog.debug("logEndOfMethodNormal Last Time" + tResultAsString);
                }
                ExecutionFlowPO tFlow =
                    new ExecutionFlowPO(Thread.currentThread().getName(), tCurrentMethodCall, mServerName);
                // Process this flow
                if (mProcessor.process(tFlow))
                {
                    mStoreWriter.writeExecutionFlow(tFlow);
                } else
                {
                    sLog.info("Post processor decide to forgive the flow " + tFlow);
                }
                mCurrentLogPoint.set(null);
            } else
            {
                if (sLog.isDebugEnabled())
                {
                    sLog.debug("logEndOfMethodNormal Any Time" + tResultAsString);
                }
                mCurrentLogPoint.set(tParentMethodCall);
            }
        }
    }

    /**
     * Trace the <code>Exception</code> thrown during its execution.
     * 
     * @param pTracer The tracer for logging Exception.
     * @param pException The <code>Exception</code> to trace.
     * @param pOptionnalMsg Use by sql agent
     */
    public void logEndOfMethodWithException(IThrowableTracer pTracer, Throwable pException, CharSequence pOptionnalMsg,
        Filter pFilter)
    {
        if (sLog.isDebugEnabled())
        {
            sLog.debug("logEndOfMethodWithException " + (pException == null ? "" : pException.getMessage()));
        }
        MethodCallPO tCurrentMethodCall = mCurrentLogPoint.get();
        CharSequence tOutput = buildExceptionMsg(pTracer, pException, pOptionnalMsg);
        tCurrentMethodCall.setEndTime(System.currentTimeMillis());
        tCurrentMethodCall.setThrowableClass(pException.getClass().getName());
        tCurrentMethodCall.setThrowableMessage(tOutput.toString());
        boolean tKeep = true;
        try
        {
            tKeep = (pFilter == null || pFilter.keep(tCurrentMethodCall));
        } catch (Throwable t)
        {
            sLog.error("The filter [" + pFilter.getClass().getName()
                + "] is buggy and throw an Exception during filtering, the measure will be kept");
        }
        MethodCallPO tParentMethodCall = tCurrentMethodCall.getParentMethodCall();
        if (!tKeep)
        {
            // Just forgot this measure
            if (sLog.isInfoEnabled())
            {
                sLog.info("The current measure have been forgiven class=[" + tCurrentMethodCall.getClassName()
                    + "], methodName=[" + tCurrentMethodCall.getMethodName() + "]");
            }
            if (tParentMethodCall != null)
            {
                tParentMethodCall.removeLastChildren();
            }
            mCurrentLogPoint.set(tParentMethodCall);
        } else
        {
            if (tCurrentMethodCall.getParentMethodCall() == null)
            { // Dernier appel du Thread
                if (sLog.isDebugEnabled())
                {
                    sLog.debug("logEndOfMethodWithException Last Time" + pException.getMessage());
                }
                ExecutionFlowPO tFlow =
                    new ExecutionFlowPO(Thread.currentThread().getName(), tCurrentMethodCall, mServerName);
                if (mProcessor.process(tFlow))
                {
                    mStoreWriter.writeExecutionFlow(tFlow);
                } else
                {
                    sLog.info("Post processor decide to forgive the flow " + tFlow);
                }
                mCurrentLogPoint.set(null);
            } else
            {
                if (sLog.isDebugEnabled())
                {
                    sLog.debug("logEndOfMethodWithException Any Time"
                        + (pException == null ? "" : pException.getMessage()));
                }
                mCurrentLogPoint.set(tCurrentMethodCall.getParentMethodCall());
            }
        }
    }

    CharSequence buildExceptionMsg(IThrowableTracer pTracer, Throwable pException, CharSequence pOptionnalMsg)
    {
        StringBuilder tOutput = new StringBuilder("");
        try
        {
            CharSequence tSequ = (pTracer == null ? null : pTracer.convertToString(pException));
            if (tSequ != null)
            {
                tOutput.append(tSequ);
            }
        } catch (Throwable e)
        {
            String tExceptionClass = pException.getClass().getName();
            String tLogClass = (pTracer == null ? "" : pTracer.getClass().getName());
            sLog.error("The log of the Exception as Throw an exception during it, Exception=[" + tExceptionClass
                + "] Traccer=[" + tLogClass + "]");
        }
        if (pOptionnalMsg != null)
        {
            if (tOutput.length() > 0)
            {
                tOutput.append("\n");
            }
            tOutput.append(pOptionnalMsg);
        }
        return tOutput;
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

    /**
     * @return the processor
     */
    public PostProcessor getProcessor()
    {
        return mProcessor;
    }

    /**
     * @param pProcessor the processor to set
     */
    public void setProcessor(PostProcessor pProcessor)
    {
        mProcessor = pProcessor;
    }

}
