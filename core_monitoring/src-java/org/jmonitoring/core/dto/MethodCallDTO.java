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
import org.jmonitoring.core.configuration.Configuration;

/**
 * @author pke
 * 
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 * Templates
 */
public class MethodCallDTO
{

    /** Flow Technical Id. */
    private int mFlowId;

    /** Technical Id. */
    private int mSequenceId;

    /** Lien sur le père de ce point dans la hierachie d'appel. */
    private MethodCallDTO mParent;

    /** Liste des points de mesure fils dans la chaîne d'appel. */
    private LinkedList mChildren = new LinkedList();

    /** Représentation sous forme de <code>String</code> des paramètres passés lors de l'appel à la méthode. */
    private String mParams;

    /** Date/Heure de début d'appel de la méthode. */
    private Date mBeginTime;

    /** Date/Heure de fin d'appel de la méthode. */
    private Date mEndTime;

    /** Nom de la classe sur laquelle est fait l'appel de la méthode. */
    private String mClassName;

    /** Nom de la méthode associée à ce point de mesure. */
    private String mMethodName;

    /** Exception qui est stockée si l'exécution associée à ce point est levée durant son exécution. */
    private String mThrowableClass;

    private String mThrowableMessage;

    /** Valeur de retour si la méthode associée à ce point est autre que 'void' . */
    private String mReturnValue;

    /** Nom du group associé au point de mesure. */
    private String mGroupName;

    private static Log sLog;

    public MethodCallDTO()
    {
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
     * Accessor, add a call to a sub-method of this <code>MethodCallDTO</code>.
     * 
     * @param pChild The <code>MethodCallDTO</code> associated with the sub-method call.
     */
    void addChild(MethodCallDTO pChild)
    {
        mChildren.add(pChild);
        pChild.mParent = this;
    }

    /**
     * Accessor.
     * 
     * @return The parent <code>MethodCallDTO</code> of this <code>MethodCallDTO</code>.
     */
    public MethodCallDTO getParent()
    {
        return mParent;
    }

    /**
     * Accessor.
     * 
     * @return The start time of the call to the method associated to thiis <code>MethodCallDTO</code>.
     */
    public Date getBeginTime()
    {
        return mBeginTime;
    }

    /**
     * Accessor.
     * 
     * @return The name of the <code>Class</code> on which we call the method associated to this
     *         <code>MethodCallDTO</code>.
     */
    public String getClassName()
    {
        return mClassName;
    }

    /**
     * Accessor.
     * 
     * @return The time of the execution of the methid associated with this <code>MethodCallDTO</code>.
     */
    public long getDuration()
    {
        return mEndTime.getTime() - mBeginTime.getTime();
    }

    /**
     * Accessor.
     * 
     * @return The end time of the method associated with this <code>MethodCallDTO</code>.
     */
    public Date getEndTime()
    {
        return mEndTime;
    }

    /**
     * Accessor.
     * 
     * @return The name of the method associated with this <code>MethodCallDTO</code>.
     */
    public String getMethodName()
    {
        return mMethodName;
    }

    /**
     * Accessor.
     * 
     * @return The parameters of the method associated with this <code>MethodCallDTO</code> as <code>String</code>.
     */
    public String getParams()
    {
        return mParams;
    }

    /**
     * Accessor.
     * 
     * @return The return value of the method associated with this <code>MethodCallDTO</code>. Null if the method
     *         ended with an <code>Exception</code>.
     */
    public String getReturnValue()
    {
        return mReturnValue;
    }

    /**
     * Accessor.
     * 
     * @return The name of the <code>Class</code> of the <code>Exception</code> thrown by the method associated with
     *         this <code>MethodCallDTO</code>. Null if the method ended normally.
     */
    public String getThrowableClassName()
    {
        return mThrowableClass;
    }

    /**
     * Accessor.
     * 
     * @return The message of the <code>Exception</code> thrown by the method associated with this
     *         <code>MethodCallDTO</code>. Null if the method ended normally.
     */
    public String getThrowableMessage()
    {
        return mThrowableMessage;
    }

    /**
     * Accessor.
     * 
     * @return The list of the sub-method of this <code>MethodCallDTO</code>.
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
    public void setBeginTime(Date pBeginTime)
    {
        mBeginTime = pBeginTime;
    }

    /**
     * @param pEndTime The mEndTime to set.
     */
    public void setEndTime(Date pEndTime)
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
        return Configuration.getInstance().getDateTimeFormater().format(mBeginTime);
    }

    /**
     * Delegator.
     * 
     * @return The end time of the firts measure.
     */
    public String getEndTimeAsString()
    {
        return Configuration.getInstance().getDateTimeFormater().format(mEndTime);
    }

    /**
     * Count the number of sub <code>MethodCallDTO</code> of this measure.
     * 
     * @return The number of measure.
     */
    int getSubMeasureCount()
    {
        int tNbMeasure = 1;
        MethodCallDTO curChild;
        for (Iterator tIte = mChildren.iterator(); tIte.hasNext();)
        {
            curChild = (MethodCallDTO) tIte.next();
            tNbMeasure += curChild.getSubMeasureCount();
        }
        return tNbMeasure;
    }

    /**
     * @param pClassName The className to set.
     */
    public void setClassName(String pClassName)
    {
        mClassName = pClassName;
    }

    /**
     * @param pMethodName The methodName to set.
     */
    public void setMethodName(String pMethodName)
    {
        mMethodName = pMethodName;
    }

    /**
     * @return Returns the throwableClass.
     */
    protected String getThrowableClass()
    {
        return mThrowableClass;
    }

    /**
     * @param pChildren The children to set.
     */
    protected void setChildren(LinkedList pChildren)
    {
        mChildren = pChildren;
    }

    /**
     * @param pChildren The children to set.
     */
    public void addChildren(MethodCallDTO pChildren)
    {
        mChildren.add(pChildren);
    }

    /**
     * @param pParams The params to set.
     */
    public void setParams(String pParams)
    {
        mParams = pParams;
    }

    /**
     * @param pParent The parent to set.
     */
    public void setParent(MethodCallDTO pParent)
    {
        mParent = pParent;
    }

}
