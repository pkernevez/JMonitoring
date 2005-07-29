package net.kernevez.performance.aspects;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import net.kernevez.performance.configuration.Configuration;
import net.kernevez.performance.measure.MeasureException;
import net.kernevez.performance.measure.MeasurePointManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.Signature;

/**
 * Cet aspect permet d'activer le log de toutes les ex�cutions de m�thodes/constructeurs dans une application. Il permet
 * �galement de tracer toutes les appels � un driver de base de donn�es.
 * 
 * Il contient la configuration des donn�es � logguer.
 * 
 * @author pke
 */
public abstract aspect PerformanceAspect
{

    /** Permet de d�finir la liste des m�thodes ex�cut�es � loguer. */
    public abstract pointcut executionToLog();

    /** Log instance. */
    private Log mLog;

    /** End of Parameters */
    private static ThreadLocal sManager = new ThreadLocal();

    /** Allow the parameter log. */
    protected boolean mLogParameter;

    /** Nom du group associ� au pointcut. */
    protected String mGroupName = "Default";

    /** Default constructor. */
    public PerformanceAspect()
    {
        mLogParameter = true;//Configuration.LOG_METHOD_PARAMETER;
        mLog = LogFactory.getLog(this.getClass());
        
    }

    pointcut executionToLogInternal() : executionToLog() 
        && !within(net.kernevez.performance.*.*) 
        && !within(net.kernevez.performance.measure.MeasurePointManager);

    Object around() : executionToLogInternal()
    {
        Object tResult = null;
        MeasurePointManager tManager = getManager();
        Signature tSig = thisJoinPointStaticPart.getSignature();
        Object [] tArgs = null;
        if (mLogParameter)
        { // On log les param�tres d'appel, de retour et les exceptions
            tArgs = thisJoinPoint.getArgs();
        }
        try
        {
            if (tManager != null)
            {
                tManager.logBeginOfMethod(tSig, tArgs, mGroupName);
            } else
            {
                mLog.error("executionToLogInternal Impossible de logger l'entr�e de la methode");
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
                if (mLogParameter)
                {
                    tManager.logEndOfMethodNormal(tResult);
                } else
                {
                    tManager.logEndOfMethodNormal(null);
                }
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
            MeasurePointManager tManager = getManager();
            tManager.logEndOfMethodWithException(t);
        } catch (Throwable tT)
        {
            mLog.error("Unable to log execution Throwable");
        }
    }

    /**
     * Permet d'obtenir un logger par Thread.
     * 
     * @return Une instance de la classe de logger parametr�e par mLoggerClass. <code>numm</code> si un erreur se
     *         produit pendant l'initalisation.
     */
    private MeasurePointManager getManager()
    {
        MeasurePointManager tResult = (MeasurePointManager) sManager.get();
        if (tResult == null)
        {
            try
            {
                // tResult = (Log4jMethodCaller) mLoggerClass.getConstructor(new Class[0]).newInstance(new Object[0]);
                tResult = new MeasurePointManager(Configuration.getInstance());
                sManager.set(tResult);
            } catch (Exception e)
            {
                // Impossible de laisser remonter l'erreur car elle se confond avec l'erreur
                // de la m�thode fonctionelle invoqu�e.
                mLog.error("Impossible d'instancier un logger pour tracer les appels", e);
            }
        }
        return tResult;
    }

}
