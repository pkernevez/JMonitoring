package org.jmonitoring.core.dto;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jmonitoring.core.configuration.Configuration;

/**
 * @author pke
 * 
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 * Templates
 */
public class MethodCall
{

    /** Flow Technical Id. */
    private int mFlowId;

    /** Technical Id. */
    private int mSequenceId;

    /** Lien sur le p�re de ce point dans la hierachie d'appel. */
    private MethodCall mParent;

    /** Liste des points de mesure fils dans la cha�ne d'appel. */
    private LinkedList mChildren = new LinkedList();

    /** Repr�sentation sous forme de <code>String</code> des param�tres pass�s lors de l'appel � la m�thode. */
    private String mParams;

    /** Date/Heure de d�but d'appel de la m�thode. */
    private long mBeginTime;

    /** Date/Heure de fin d'appel de la m�thode. */
    private long mEndTime;

    /** Nom de la classe sur laquelle est fait l'appel de la m�thode. */
    private String mClassName;

    /** Nom de la m�thode associ�e � ce point de mesure. */
    private String mMethodName;

    /** Exception qui est stock�e si l'ex�cution associ�e � ce point est lev�e durant son ex�cution. */
    private String mThrowableClass;

    private String mThrowableMessage;

    /** Valeur de retour si la m�thode associ�e � ce point est autre que 'void' . */
    private String mReturnValue;

    /** Nom du group associ� au point de mesure. */
    private String mGroupName;

    private static Log sLog;

    /**
     * Constructor used for Database, because original types are already converted to <code>String</code>.
     * 
     * @param pParent The <code>MethodCall</code> from which we made the call to the current <code>MethodCall</code>.
     * @param pClassName The name of the <code>Class</code> on which we call the statement associated with this
     *        <code>MethodCall</code>.
     * @param pMethodName The method name of the statement associated with this <code>MethodCall</code>.
     * @param pGroupName The name of the group associated to this <code>MethodCall</code>.
     * @param pParams The parameters serialized as <code>String</code> passed to the method <code>pMethodName</code>.
     */
    public MethodCall(MethodCall pParent, String pClassName, String pMethodName, String pGroupName, String pParams)
    {
        if (sLog == null)
        {
            sLog = LogFactory.getLog(this.getClass());
        }
        if (pParent != null)
        { // On chaine la hierachie
            pParent.addChild(this);
        }
        mClassName = pClassName;
        mMethodName = pMethodName;
        mBeginTime = System.currentTimeMillis();
        mParams = pParams;
        mGroupName = pGroupName;

    }

    /**
     * Constructor used for Database, because original types are already converted to <code>String</code>.
     * 
     * @param pParent The <code>MethodCall</code> from which we made the call to the current <code>MethodCall</code>.
     * @param pClassName The name of the <code>Class</code> on which we call the statement associated with this
     *        <code>MethodCall</code>.
     * @param pMethodName The method name of the statement associated with this <code>MethodCall</code>.
     * @param pGroupName The name of the group associated to this <code>MethodCall</code>.
     * @param pParams The <code>Object</code> array passed to the method <code>pMethodName</code>.
     */
    public MethodCall(MethodCall pParent, String pClassName, String pMethodName, String pGroupName, Object[] pParams)
    {
        if (pParent != null)
        { // On chaine la hierachie
            pParent.addChild(this);
        }
        mClassName = pClassName;
        mMethodName = pMethodName;
        mBeginTime = System.currentTimeMillis();
        mGroupName = pGroupName;
        StringBuffer tBuffer = new StringBuffer();
        try
        {
            if (pParams != null)
            {
                boolean tFistTime = true;
                tBuffer.append("[");
                for (int i = 0; i < pParams.length; i++)
                {
                    if (!tFistTime)
                    {
                        tBuffer.append(", ");
                    }
                    tBuffer.append("" + pParams[i]);
                    tFistTime = false;
                }
                tBuffer.append("]");
            }
        } catch (Throwable tT)
        {
            sLog.error("Unable to getArguments of class=[" + pClassName + "] and method=[" + pMethodName + "]", tT);
        }
        mParams = tBuffer.toString();
    }

    /**
     * Define the return value of the method associated with this <code>MethodCall</code> when it didn't throw a
     * <code>Throwable</code>.
     * 
     * @param pReturnValue The return value of the method.
     */
    public void endMethod(Object pReturnValue)
    {
        mEndTime = System.currentTimeMillis();
        if (pReturnValue != null)
        {
            try
            {
                mReturnValue = pReturnValue.toString();
            } catch (Throwable tT)
            {
                sLog.error("Unable to trace return value.", tT);
            }
        }
    }

    /**
     * Define the <code>Throwable</code> thrown by the method associated with this <code>MethodCall</code>.
     * 
     * @param pExceptionClassName The name of the <code>Class</code> of the <code>Exception</code>.
     * @param pExceptionMessage The message of the <code>Exception</code>.
     */
    public void endMethodWithException(String pExceptionClassName, String pExceptionMessage)
    {
        mEndTime = System.currentTimeMillis();
        mThrowableClass = pExceptionClassName;
        mThrowableMessage = pExceptionMessage;
    }

    /**
     * Allow to know if an exception has occured during call.
     * 
     * @return True if an exception was thrown by the call method.
     */
    public boolean isReturnCallException()
    {
        return mThrowableClass != null;

    }

    /**
     * Accessor, add a call to a sub-method of this <code>MethodCall</code>.
     * 
     * @param pChild The <code>MethodCall</code> associated with the sub-method call.
     */
    private void addChild(MethodCall pChild)
    {
        mChildren.add(pChild);
        pChild.mParent = this;
    }

    /**
     * Accessor.
     * 
     * @return The parent <code>MethodCall</code> of this <code>MethodCall</code>.
     */
    public MethodCall getParent()
    {
        return mParent;
    }

    /**
     * Accessor.
     * 
     * @return The start time of the call to the method associated to thiis <code>MethodCall</code>.
     */
    public long getBeginTime()
    {
        return mBeginTime;
    }

    /**
     * Accessor.
     * 
     * @return The name of the <code>Class</code> on which we call the methid associated to this
     *         <code>MethodCall</code>.
     */
    public String getClassName()
    {
        return mClassName;
    }

    /**
     * Accessor.
     * 
     * @return The time of the execution of the methid associated with this <code>MethodCall</code>.
     */
    public long getDuration()
    {
        return mEndTime - mBeginTime;
    }

    /**
     * Accessor.
     * 
     * @return The end time of the method associated with this <code>MethodCall</code>.
     */
    public long getEndTime()
    {
        return mEndTime;
    }

    /**
     * Accessor.
     * 
     * @return The name of the method associated with this <code>MethodCall</code>.
     */
    public String getMethodName()
    {
        return mMethodName;
    }

    /**
     * Accessor.
     * 
     * @return The parameters of the method associated with this <code>MethodCall</code> as <code>String</code>.
     */
    public String getParams()
    {
        return mParams;
    }

    /**
     * Accessor.
     * 
     * @return The return value of the method associated with this <code>MethodCall</code>. Null if the method ended
     *         with an <code>Exception</code>.
     */
    public String getReturnValue()
    {
        return mReturnValue;
    }

    /**
     * Accessor.
     * 
     * @return The name of the <code>Class</code> of the <code>Exception</code> thrown by the method associated with
     *         this <code>MethodCall</code>. Null if the method ended normally.
     */
    public String getThrowableClassName()
    {
        return mThrowableClass;
    }

    /**
     * Accessor.
     * 
     * @return The message of the <code>Exception</code> thrown by the method associated with this
     *         <code>MethodCall</code>. Null if the method ended normally.
     */
    public String getThrowableMessage()
    {
        return mThrowableMessage;
    }

    /**
     * Accessor.
     * 
     * @return The list of the sub-method of this <code>MethodCall</code>.
     */
    public List getChildren()
    {
        return mChildren;
    }

    /**
     * Accessor.
     * 
     * @return Returns the sequence identifier.
     */
    public int getSequenceId()
    {
        return mSequenceId;
    }

    /**
     * @param pSequenceId The mId to set.
     */
    public void setSequenceId(int pSequenceId)
    {
        mSequenceId = pSequenceId;
    }

    /**
     * @param pBeginTime The mBeginTime to set.
     */
    public void setBeginTime(long pBeginTime)
    {
        mBeginTime = pBeginTime;
    }

    /**
     * @param pEndTime The mEndTime to set.
     */
    public void setEndTime(long pEndTime)
    {
        mEndTime = pEndTime;
    }

    /**
     * @param pReturnValue The mReturnValue to set.
     */
    public void setReturnValue(String pReturnValue)
    {
        mReturnValue = pReturnValue;
    }

    /**
     * @param pThrowableClass The mThrowableClass to set.
     */
    public void setThrowableClass(String pThrowableClass)
    {
        mThrowableClass = pThrowableClass;
    }

    /**
     * @param pThrowableMessage The mThrowableMessage to set.
     */
    public void setThrowableMessage(String pThrowableMessage)
    {
        mThrowableMessage = pThrowableMessage;
    }

    /**
     * @return Returns the mGroupName.
     */
    public String getGroupName()
    {
        return mGroupName;
    }

    /**
     * @param pGroupName The mGroupName to set.
     */
    public void setGroupName(String pGroupName)
    {
        mGroupName = pGroupName;
    }

    /**
     * Accessor.
     * 
     * @return The flow identifier.
     */
    public int getFlowId()
    {
        return mFlowId;
    }

    /**
     * Accessor.
     * 
     * @param pFlowId The flow identifier.
     */
    public void setFlowId(int pFlowId)
    {
        mFlowId = pFlowId;
    }

    /**
     * Delegator.
     * 
     * @return The begin time of the firts measure.
     */
    public String getBeginTimeAsString()
    {
        return Configuration.getInstance().getDateTimeFormater().format(new Date(mBeginTime));
    }

    /**
     * Delegator.
     * 
     * @return The end time of the firts measure.
     */
    public String getEndTimeAsString()
    {
        return Configuration.getInstance().getDateTimeFormater().format(new Date(mEndTime));
    }

    /**
     * Count the number of sub <code>MethodCall</code> of this measure.
     * 
     * @return The number of measure.
     */
    int getSubMeasureCount()
    {
        int tNbMeasure = 1;
        MethodCall curChild;
        for (Iterator tIte = mChildren.iterator(); tIte.hasNext();)
        {
            curChild = (MethodCall) tIte.next();
            tNbMeasure += curChild.getSubMeasureCount();
        }
        return tNbMeasure;
    }

}