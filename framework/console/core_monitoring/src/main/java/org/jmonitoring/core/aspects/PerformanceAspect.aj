package org.jmonitoring.core.aspects;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.Signature;
import org.jmonitoring.core.common.MeasureException;
import org.jmonitoring.core.info.IParamaterTracer;
import org.jmonitoring.core.info.IResultTracer;
import org.jmonitoring.core.info.IThrowableTracer;
import org.jmonitoring.core.info.impl.DefaultExceptionTracer;
import org.jmonitoring.core.info.impl.ToStringParametersTracer;
import org.jmonitoring.core.info.impl.ToStringResultTracer;
import org.jmonitoring.core.store.StoreManager;

/**
 * Cet aspect permet d'activer le log de toutes les exécutions de méthodes/constructeurs dans une application. Il permet
 * également de tracer toutes les appels à un driver de base de données.
 * 
 * Il contient la configuration des données à logguer.
 * 
 * @author pke
 */
public abstract aspect PerformanceAspect
{

    /** Permet de définir la liste des méthodes exécutées à loguer. */
    public abstract pointcut executionToLog();

    /** Log instance. */
    private Log mLog;

    /** End of Parameters */
    private static ThreadLocal sManager = new ThreadLocal();

    /** Allow to trace the parameter of a method. */
    protected IParamaterTracer mParamTracer;

    /** Allow to trace the returnValue of a method. */
    protected IResultTracer mResultTracer;

    /** Allow to trace the détail of an Exception. */
    protected IThrowableTracer mThowableTracer;

    /** Nom du group associé au pointcut. */
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
        StoreManager tManager = getManager();
        Signature tSig = thisJoinPointStaticPart.getSignature();
        try
        {
            if (tManager != null)
            {
                tManager.logBeginOfMethod(tSig, mParamTracer, thisJoinPoint.getArgs(), mGroupName, thisJoinPoint.getTarget());
            } else
            {
                mLog.error("executionToLogInternal Impossible de logger l'entrée de la methode");
            }
        } catch (MeasureException e)
        {
            mLog.error("Unable to log", e);
        }
        tResult = proceed();// En cas d'exception le code est dans le trigger "after()throwing..."
        try
        {
            if (tManager != null)
            {
                tManager.logEndOfMethodNormal(mResultTracer, thisJoinPoint.getTarget(), tResult);

            } else
            {
                mLog.error("executionToLogInternal Impossible de logger la sortie de la methode");
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
            StoreManager tManager = getManager();
            tManager.logEndOfMethodWithException(mThowableTracer, t);
        } catch (Throwable tT)
        {
            mLog.error("Unable to log execution Throwable",tT);
        }
    }

    /**
     * Permet d'obtenir un logger par Thread.
     * 
     * @return Une instance de la classe de logger parametrée par mLoggerClass. <code>numm</code> si un erreur se
     *         produit pendant l'initalisation.
     */
    private StoreManager getManager()
    {
        StoreManager tResult = (StoreManager) sManager.get();
        if (tResult == null)
        {
            try
            {
                tResult = new StoreManager();
                sManager.set(tResult);
            } catch (Exception e)
            {
                // Impossible de laisser remonter l'erreur car elle se confond avec l'erreur
                // de la méthode fonctionelle invoquée.
                mLog.error("Impossible d'instancier un logger pour tracer les appels", e);
            }
        }
        return tResult;
    }

}
