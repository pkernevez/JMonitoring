package org.jmonitoring.core.persistence;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * @todo Coder les méthodes equals et hashcode cf p54
 * @todo voir pour utiliser un composant hibernate MethodCallId pour la clé composite
 * @author pke
 */
public class MethodCallPO implements Serializable 
{

    private static final int MAX_STRING_SIZE = 65000;

    private MethodCallPK mId;

    /** Flow Technical Id. */
    private ExecutionFlowPO mFlow;

    /** Lien sur le père de ce point dans la hierachie d'appel. */
    private MethodCallPO mParent;

    /** Liste des points de mesure fils dans la chaîne d'appel. */
    private List mChildren = new LinkedList();

    /** Représentation sous forme de <code>String</code> des paramètres passés lors de l'appel à la méthode. */
    private String mParams;

    /** Date/Heure de début d'appel de la méthode. */
    private long mBeginTime;

    /** Date/Heure de fin d'appel de la méthode. */
    private long mEndTime;

    /** Name of the class on which the Method is defined. */
    private String mClassName;

    /** Name of the class on which the Method is called. Null if it's the same as <code>mClassName</code>. */
    private String mRuntimeClassName;

    /** Nom de la méthode associée à ce point de mesure. */
    private String mMethodName;

    /** Exception qui est stockée si l'exécution associée à ce point est levée durant son exécution. */
    private String mThrowableClass;

    private String mThrowableMessage;

    /** Valeur de retour si la méthode associée à ce point est autre que 'void' . */
    private String mReturnValue;

    /** Nom du group associé au point de mesure. */
    private String mGroupName;

    /** Constructor for hibernate. */
    public MethodCallPO()
    {
    }

    /**
     * 
     * @param pParent The <code>MethodCallPO</code> from which we made the call to the current
     *        <code>MethodCallPO</code>.
     * @param pClassName The name of the <code>Class</code> on which we call the statement associated with this
     *        <code>MethodCallDTO</code>.
     * @param pMethodName The method name of the statement associated with this <code>MethodCallDTO</code>.
     * @param pGroupName The name of the group associated to this <code>MethodCallDTO</code>.
     * @param pParameters The parameters passed to the method <code>pMethodName</code>.
     */
    public MethodCallPO(MethodCallPO pParent, String pClassName, String pMethodName, String pGroupName,
                        String pParameters)
    {
        if (pParent != null)
        { // On chaine la hierachie
            pParent.addChildren(this);
        }
        mClassName = pClassName;
        mMethodName = pMethodName;
        mBeginTime = System.currentTimeMillis();
        mParams = pParameters;
        mGroupName = pGroupName;
    }

    public void addChildren(MethodCallPO pChild)
    {
        mChildren.add(pChild);
        pChild.mParent = this;
    }

    public void removeChildren(MethodCallPO pChild)
    {
        mChildren.remove(pChild);
        pChild.mParent = null;

    }

    public List getChildren()
    {
        return mChildren;
    }

    public MethodCallPO getChild(int pPos)
    {
        return (MethodCallPO) mChildren.get(pPos);
    }

    public void setChildren(List pChildren)
    {
        mChildren = pChildren;
    }

    /**
     * Accessor.
     * 
     * @return Returns the sequence identifier.
     */
    public int getPosition()
    {
        return mId.getPosition();
    }

    /**
     * @param pId The primary key to set.
     */
    public void setPosition(int pId)
    {
        mId.setPosition(pId);
    }

    /**
     * Accessor.
     * 
     * @return The start time of the call to the method associated to thiis <code>MethodCallDTO</code>.
     */
    public long getBeginTime()
    {
        return mBeginTime;
    }

    /**
     * @param pBeginTime The start time of the call to the method associated to thiis <code>MethodCallDTO</code>.
     */
    public void setBeginTime(long pBeginTime)
    {
        mBeginTime = pBeginTime;
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
     * @param pClassName The name of the <code>Class</code> on which we call the method associated to this
     *        <code>MethodCallDTO</code>.
     */
    public void setClassName(String pClassName)
    {
        mClassName = pClassName;
    }

    /**
     * Accessor.
     * 
     * @return The time of the execution of the methid associated with this <code>MethodCallDTO</code>.
     */
    public long getDuration()
    {
        return mEndTime - mBeginTime;
    }

    /**
     * Accessor.
     * 
     * @todo Voir comment supprimer cette méthode pour la remplacer par la conf hibernate
     * @param pDuration The time of the execution of the methid associated with this <code>MethodCallDTO</code>.
     */
    public void setDuration(long pDuration)
    {
        // Nothing to do
    }

    /**
     * Accessor.
     * 
     * @return The end time of the method associated with this <code>MethodCallDTO</code>.
     */
    public long getEndTime()
    {
        return mEndTime;
    }

    /**
     * Accesor.
     * 
     * @param pEndTime The end time of the method associated with this <code>MethodCallDTO</code>.
     */
    public void setEndTime(long pEndTime)
    {
        mEndTime = pEndTime;
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
     * @param pMethodName The name of the method associated with this <code>MethodCallDTO</code>.
     */
    public void setMethodName(String pMethodName)
    {
        mMethodName = pMethodName;
    }

    /**
     * Accessor.
     * 
     * @return The name of the <code>Class</code> of the <code>Exception</code> thrown by the method associated with
     *         this <code>MethodCallDTO</code>. Null if the method ended normally.
     */
    public String getThrowableClass()
    {
        return mThrowableClass;
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
     * @return The return value of the method associated with this <code>MethodCallDTO</code>. Null if the method
     *         ended with an <code>Exception</code>.
     */
    public String getReturnValue()
    {
        return mReturnValue;
    }

    /**
     * @param pReturnValue The mReturnValue to set.
     * @todo Write a test for MAX_STRING_SIZE
     */
    public void setReturnValue(String pReturnValue)
    {
        if (pReturnValue != null && pReturnValue.length() > MAX_STRING_SIZE)
        {
            mReturnValue = pReturnValue.substring(MAX_STRING_SIZE);
        } else
        {
            mReturnValue = pReturnValue;
        }
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
     * @return The parameters of the method associated with this <code>MethodCallDTO</code> as <code>String</code>.
     */
    public String getParams()
    {
        return mParams;
    }

    /**
     * @param pParams The parameters of the method associated with this <code>MethodCallDTO</code> as
     *        <code>String</code>.
     */
    public void setParams(String pParams)
    {
        if (pParams != null && pParams.length() > MAX_STRING_SIZE)
        {
            mParams = pParams.substring(MAX_STRING_SIZE);
        } else
        {
            mParams = pParams;
        }
    }

    /**
     * @return Returns the mParent.
     */
    public MethodCallPO getParentMethodCall()
    {
        return mParent;
    }

    /**
     * Internal use by Hibernate
     * 
     * @return Returns the mParent.
     */
    MethodCallPO getParent()
    {
        return mParent;
    }

    /**
     * Internal use by Hibernate
     * 
     * @param pParent The mParent to set.
     */
    void setParent(MethodCallPO pParent)
    {
        mParent = pParent;
    }

    /**
     * @param pParent The mParent to set.
     */
    public void setParentMethodCall(MethodCallPO pParent)
    {
        if (pParent == null)
        {
            if (this.mParent != null)
            {
                this.mParent.mChildren.remove(this);
            }
        } else
        {
            pParent.mChildren.add(this);
        }
        mParent = pParent;
    }

    /**
     * @return Returns the flow.
     */
    public ExecutionFlowPO getFlow()
    {
        return mFlow;
    }

    /**
     * @param pFlow The flow to set.
     */
    public void setFlow(ExecutionFlowPO pFlow)
    {
        mFlow = pFlow;
    }

    public void setFlowRecusivly(ExecutionFlowPO pFlowPO)
    {
        mFlow = pFlowPO;
        MethodCallPO curMeth;
        for (Iterator tIt = mChildren.iterator(); tIt.hasNext();)
        {
            curMeth = (MethodCallPO) tIt.next();
            curMeth.setFlowRecusivly(pFlowPO);
        }

    }

    public MethodCallPK getMethId()
    {
        return mId;
    }

    public void setMethId(MethodCallPK pId)
    {
        mId = pId;
    }

    public String getRuntimeClassName()
    {
        return mRuntimeClassName;
    }

    public void setRuntimeClassName(String pRuntimeClassName)
    {
        mRuntimeClassName = pRuntimeClassName;
    }

}
