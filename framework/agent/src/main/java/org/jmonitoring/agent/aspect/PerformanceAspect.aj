package org.jmonitoring.agent.aspect;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.Signature;
import org.jmonitoring.agent.info.impl.DefaultExceptionTracer;
import org.jmonitoring.agent.info.impl.ToStringParametersTracer;
import org.jmonitoring.agent.info.impl.ToStringResultTracer;
import org.jmonitoring.agent.store.StoreManager;
import org.jmonitoring.core.configuration.MeasureException;
import org.jmonitoring.core.info.IParamaterTracer;
import org.jmonitoring.core.info.IResultTracer;
import org.jmonitoring.core.info.IThrowableTracer;

/**
 * This abstract aspect should be extends by users. 
 * It provides all the mecanism for logging measure. Children have to describe the pointcuts they want to log, by providing the implementation of executionToLog();
 */
public abstract aspect PerformanceAspect
{

    /** Pointcuts to log */
    public abstract pointcut executionToLog();

    /** Log instance. */
    private Log mLog;

    /** Allow to trace the parameter of a method. */
    protected IParamaterTracer mParamTracer;

    /** Allow to trace the returnValue of a method. */
    protected IResultTracer mResultTracer;

    /** Allow to trace the d�tail of an Exception. */
    protected IThrowableTracer mThowableTracer;

    /** Nom du group associ� au pointcut. */
    protected String mGroupName = "Default";

    /** Default constructor. */
    public PerformanceAspect()
    {
        mParamTracer = new ToStringParametersTracer();
        mResultTracer = new ToStringResultTracer();
        mThowableTracer = new DefaultExceptionTracer();
        mLog = LogFactory.getLog(this.getClass());

    }

    pointcut executionToLogInternal() : executionToLog() 
        && !within(org.jmonitoring.core.*.*);

    Object around() : executionToLogInternal()
    {
        Object tResult = null;
        StoreManager tManager = StoreManager.getManager();
        Signature tSig = thisJoinPointStaticPart.getSignature();
        try
        {
            if (tManager != null)
            {
                tManager.logBeginOfMethod(tSig, mParamTracer, thisJoinPoint.getArgs(), mGroupName, thisJoinPoint
                    .getTarget());
            } else
            {
                mLog.error("executionToLogInternal Unable to log methode parameters");
            }
        } catch (MeasureException e)
        {
            mLog.error("Unable to log", e);
        }
        tResult = proceed();// If an error occurs, the code will be in the trigger "after()throwing..."
        try
        {
            if (tManager != null)
            {
                tManager.logEndOfMethodNormal(mResultTracer, thisJoinPoint.getTarget(), tResult);

            } else
            {
                mLog.error("executionToLogInternal Unable to log the method return value");
            }
        } catch (MeasureException e)
        {
            LogFactory.getLog(this.getClass()).error("Unable to log", e);
        }

        return tResult;
    }

    after() throwing (Throwable t): executionToLogInternal() {
        try
        {
            StoreManager tManager = StoreManager.getManager();
            tManager.logEndOfMethodWithException(mThowableTracer, t);
        } catch (Throwable tT)
        {
            mLog.error("Unable to log execution Throwable", tT);
        }
    }
}
