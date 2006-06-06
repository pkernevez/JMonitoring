package org.jmonitoring.console.methodcall;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.util.HashMap;
import java.util.Map;

import org.apache.struts.action.ActionForm;
import org.jmonitoring.core.common.MeasureException;
import org.jmonitoring.core.configuration.Configuration;
import org.jmonitoring.core.dto.MethodCallDTO;

/**
 * Form associated with <code>MethodCallDTO</code>.
 * 
 * @author pke
 */
public class MethodCallForm extends ActionForm
{
    private static final long serialVersionUID = 3618705218511058994L;

    /** Flow technical identifier. */
    private int mFlowId = -1;

    /** Sequence identifier. */
    private int mSequenceId = -1;

    /** Name of the method for which we want to find static. */
    private String mMethodName;

    /** Name of the class for which we want to find static. */
    private String mClassName;

    /** The measure point assoiciate to this request. */
    private MethodCallDTO mMethodCall;

    /**
     * Allow to know if the parameters passed by the query was by name or by identifier.
     * 
     * @return true if we pass parameters <code>ClassName</code> and <code>MethodName</code>.
     */
    public boolean isParametersByName()
    {
        boolean tResult;
        if (mMethodName != null && mClassName != null)
        { // We pass name parameters
            tResult = true;
        } else if (mFlowId != -1 && mSequenceId != -1)
        { // We pass identifier parameters
            tResult = false;
        } else
        { // Invalid parameters
            throw new MeasureException(
                            "Invalid parameter expected flowId and sequenceId OR " + "methodName and className.");
        }
        return tResult;
    }

    /**
     * Accessor.
     * 
     * @return The Flow identifier.
     */
    public int getFlowId()
    {
        return mFlowId;
    }

    /**
     * Accessor.
     * 
     * @param pFlowId The Flow identifier.
     */
    public void setFlowId(int pFlowId)
    {
        mFlowId = pFlowId;
    }

    /**
     * Accessor.
     * 
     * @return The sequence identifier.
     */
    public int getId()
    {
        return mSequenceId;
    }

    /**
     * Accessor.
     * 
     * @param pSequenceId The sequence identifier.
     */
    public void setId(int pSequenceId)
    {
        mSequenceId = pSequenceId;
    }

    /**
     * Accessor.
     * 
     * @return The name of the <code>Class</code>.
     */
    public String getClassName()
    {
        if (mMethodName != null)
        {
        return mClassName;
        } else {
            return mMethodCall.getClassName();
        }
    }

    /**
     * Accessor.
     * 
     * @param pClassName The name of the <code>Class</code>.
     */
    public void setClassName(String pClassName)
    {
        mClassName = pClassName;
    }

    /**
     * Accessor.
     * 
     * @return The name of the method.
     */
    public String getMethodName()
    {
        if (mMethodName != null)
        {
            return mMethodName;
        } else
        {
            return mMethodCall.getMethodName();
        }
    }

    /**
     * Accessor.
     * 
     * @param pMethodName The name of the method.
     */
    public void setMethodName(String pMethodName)
    {
        mMethodName = pMethodName;
    }

    /**
     * Accessor.
     * 
     * @return The measure point associated with this parameters.
     */
    public MethodCallDTO getMethodCall()
    {
        return mMethodCall;
    }

    /**
     * Accessor.
     * 
     * @param pMeasurePoint The measure associated with this parameters.
     */
    public void setMethodCall(MethodCallDTO pMeasurePoint)
    {
        mMethodCall = pMeasurePoint;
    }

    /**
     * Return the ids of the parent <code>MethodCallDTO</code>. This <code>Map</code> is used for the link
     * generation by Struts.
     * 
     * @return The list of attributes to use for the link.
     */
    public Map getParentIdsMap()
    {
        Map tHashMap = new HashMap();
        if (mMethodCall.getParent() != null)
        {
            tHashMap.put("flowId", new Integer(mMethodCall.getFlowId()));
            tHashMap.put("id", new Integer(mMethodCall.getParent().getSequenceId()));
        }
        return tHashMap;
    }

    /**
     * Return the ids of the current <code>MethodCallDTO</code>. This <code>Map</code> is used for the link
     * generation by Struts.
     * 
     * @return The list of attributes to use for the link.
     */
    public Map getMethodCallIdsMap()
    {
        Map tHashMap = new HashMap();
        tHashMap.put("flowId", new Integer(mMethodCall.getFlowId()));
        tHashMap.put("id", new Integer(mMethodCall.getSequenceId()));
        return tHashMap;
    }

    /**
     * Return the ids of a chid of the <code>MethodCallDTO</code>. This <code>Map</code> is used for the link
     * generation by Struts.
     * 
     * @param pPosition The index of the <code>MethodCallDTO</code>.
     * @return The list of attributes to use for the link.
     */
    public Map getChildMethodCallIdsMap(int pPosition)
    {
        Map tHashMap = new HashMap();
        MethodCallDTO tMeasure = (MethodCallDTO) mMethodCall.getChildren().get(pPosition);
        tHashMap.put("flowId", new Integer(tMeasure.getFlowId()));
        tHashMap.put("id", new Integer(tMeasure.getSequenceId()));
        return tHashMap;
    }

    public String getGroupColor()
    {
        return Configuration.getInstance().getGroupAsColorString(mMethodCall.getGroupName());
    }
}
