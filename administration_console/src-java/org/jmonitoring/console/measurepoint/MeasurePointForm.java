package org.jmonitoring.console.measurepoint;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.util.HashMap;
import java.util.Map;

import org.jmonitoring.core.common.MeasureException;
import org.jmonitoring.core.dto.MethodCallDTO;

import org.apache.struts.action.ActionForm;

/**
 * Form associated with <code>MethodCallDTO</code>.
 * 
 * @author pke
 */
public class MeasurePointForm extends ActionForm
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
    private MethodCallDTO mMeasurePoint;

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
            throw new MeasureException("Invalid parameter expected flowId and sequenceId OR "
                            + "methodName and className.");
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
    public int getSequenceId()
    {
        return mSequenceId;
    }

    /**
     * Accessor.
     * 
     * @param pSequenceId The sequence identifier.
     */
    public void setSequenceId(int pSequenceId)
    {
        mSequenceId = pSequenceId;
    }

    /**
     * Accessor.
     * @return The name of the <code>Class</code>.
     */
    public String getClassName()
    {
        return mClassName;
    }

    /**
     * Accessor.
     * @param pClassName The name of the <code>Class</code>.
     */
    public void setClassName(String pClassName)
    {
        mClassName = pClassName;
    }

    /**
     * Accessor.
     * @return The name of the method.
     */
    public String getMethodName()
    {
        return mMethodName;
    }

    /**
     * Accessor.
     * @param pMethodName The name of the method.
     */
    public void setMethodName(String pMethodName)
    {
        mMethodName = pMethodName;
    }

    /**
     * Accessor.
     * @return The measure point associated with this parameters.
     */
    public MethodCallDTO getMeasurePoint()
    {
        return mMeasurePoint;
    }

    /**
     * Accessor.
     * @param pMeasurePoint The measure associated with this parameters.
     */
    public void setMeasurePoint(MethodCallDTO pMeasurePoint)
    {
        mMeasurePoint = pMeasurePoint;
    }

    /**
     * Return the ids of the parent <code>MethodCallDTO</code>.
     * This <code>Map</code> is used for the link generation by Struts.
     * @return The list of attributes to use for the link.
     */
    public Map getParentIdsMap()
    {
        Map tHashMap = new HashMap();
        if (mMeasurePoint.getParent() != null)
        {
            tHashMap.put("flowId", new Integer(mMeasurePoint.getParent().getFlowId()));
            tHashMap.put("sequenceId", new Integer(mMeasurePoint.getParent().getSequenceId()));
        }
        return tHashMap;
    }

    /**
     * Return the ids of the current <code>MethodCallDTO</code>.
     * This <code>Map</code> is used for the link generation by Struts.
     * @return The list of attributes to use for the link.
     */
    public Map getMeasureIdsMap()
    {
        Map tHashMap = new HashMap();
        tHashMap.put("flowId", new Integer(mMeasurePoint.getFlowId()));
        tHashMap.put("sequenceId", new Integer(mMeasurePoint.getSequenceId()));
        return tHashMap;
    }

    /**
     * Return the ids of a chid of the <code>MethodCallDTO</code>.
     * This <code>Map</code> is used for the link generation by Struts.
     * @param pPosition The index of the <code>MethodCallDTO</code>.
     * @return The list of attributes to use for the link.
     */
    public Map getChildMeasureIdsMap(int pPosition)
    {
        Map tHashMap = new HashMap();
        MethodCallDTO tMeasure = (MethodCallDTO) mMeasurePoint.getChildren().get(pPosition);
        tHashMap.put("flowId", new Integer(tMeasure.getFlowId()));
        tHashMap.put("sequenceId", new Integer(tMeasure.getSequenceId()));
        return tHashMap;
    }

}
