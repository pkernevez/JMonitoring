package net.kernevez.performance.measure;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import net.kernevez.performance.configuration.Configuration;
import net.kernevez.performance.log.IMeasurePointTreeLogger;
import net.kernevez.performance.log.MeasurePointTreeLoggerFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.Signature;

/**
 * Permet de logger sous forme XML l'ensemble des appels avec les signature et les temps d'exécution dans un fichier XML
 * à l'aide de log4j.
 * 
 * This class is not Synchronized.
 * 
 * @author PKE
 */
public class MeasurePointManager
{
    private MeasurePoint mCurrentLogPoint;

    /** <code>CommonsLog</code> instance. */
    private static Log sLog;

    private IMeasurePointTreeLogger mLogger;

    private Configuration mConfiguration;

    /**
     * Default constructor.
     * 
     * @param pConfiguration The configuration instance to use.
     */
    public MeasurePointManager(Configuration pConfiguration)
    {
        if (sLog == null)
        {
            sLog = LogFactory.getLog(this.getClass());
        }
        mConfiguration = pConfiguration;
        mLogger = MeasurePointTreeLoggerFactory.getNewInstance();
    }

    /**
     * Constructor for testing purpose.
     * 
     * @param pLogger The <code>Looger</code> to use.
     * @param pConfiguration The configuration instance to use.
     */
    public MeasurePointManager(IMeasurePointTreeLogger pLogger, Configuration pConfiguration)
    {
        if (sLog == null)
        {
            sLog = LogFactory.getLog(this.getClass());
        }
        mConfiguration = pConfiguration;
        mLogger = pLogger;
    }

    /**
     * Trace a method with its arguments.
     * 
     * @param pSignature The method signature.
     * @param pArgs The method arguments.
     * @param pGroupName The name of the group associated with this <code>MeasurePoint</code>.
     */
    public void logBeginOfMethod(Signature pSignature, Object[] pArgs, String pGroupName)
    {
        if (mCurrentLogPoint == null)
        { // Premier appel du Thread
            if (sLog.isDebugEnabled())
            {
                sLog.debug("logBeginOfMethod First Time" + pSignature);
            }
            mCurrentLogPoint = new MeasurePoint(null, pSignature.getDeclaringTypeName(), pSignature.getName(),
                            pGroupName, pArgs);
        } else
        {
            if (sLog.isDebugEnabled())
            {
                sLog.debug("logBeginOfMethod Any Time" + pSignature);
            }
            MeasurePoint tOldPoint = mCurrentLogPoint;
            mCurrentLogPoint = new MeasurePoint(tOldPoint, pSignature.getDeclaringTypeName(),
                            pSignature.getName(), pGroupName, pArgs);
        }
    }

    /**
     * Trace the result of a method ended normally.
     * 
     * @param pResult The result of the execution of the method.
     */
    public void logEndOfMethodNormal(Object pResult)
    {
        mCurrentLogPoint.endMethod(pResult);
        if (mCurrentLogPoint.getParent() == null)
        { // Dernier appel du Thread
            if (sLog.isDebugEnabled())
            {
                try
                {
                    sLog.debug("logEndOfMethodNormal Last Time" + pResult);
                } catch (Throwable tT)
                {
                    sLog.error("Unable to trace return value.", tT);
                }
            }
            ExecutionFlow tFlow = new ExecutionFlow(Thread.currentThread().getName(), mCurrentLogPoint,
                            mConfiguration.getServerName());
            mLogger.logMeasurePointTree(tFlow);
            mCurrentLogPoint = null;
        } else
        {
            if (sLog.isDebugEnabled())
            {
                sLog.debug("logEndOfMethodNormal Any Time" + pResult);
            }
            mCurrentLogPoint = mCurrentLogPoint.getParent();
        }
    }

    /**
     * Trace the <code>Exception</code> thrown during its execution.
     * 
     * @param pException The <code>Exception</code> to trace.
     */
    public void logEndOfMethodWithException(Throwable pException)
    {
        if (sLog.isDebugEnabled())
        {
            sLog.debug("logEndOfMethodWithException " + pException.getMessage());
        }
        if (pException == null)
        { // On ne logue pas le détail
            mCurrentLogPoint.endMethodWithException(null, null);
        } else
        {
            mCurrentLogPoint.endMethodWithException(pException.getClass().getName(), pException.getMessage());
        }

        if (mCurrentLogPoint.getParent() == null)
        { // Dernier appel du Thread
            if (sLog.isDebugEnabled())
            {
                sLog.debug("logEndOfMethodWithException Last Time" + pException.getMessage());
            }
            ExecutionFlow tFlow = new ExecutionFlow(Thread.currentThread().getName(), mCurrentLogPoint,
                            mConfiguration.getServerName());
            mLogger.logMeasurePointTree(tFlow);
            mCurrentLogPoint = null;
        } else
        {
            if (sLog.isDebugEnabled())
            {
                sLog.debug("logEndOfMethodWithException Any Time" + pException.getMessage());
            }
            mCurrentLogPoint = mCurrentLogPoint.getParent();
        }

    }

}
