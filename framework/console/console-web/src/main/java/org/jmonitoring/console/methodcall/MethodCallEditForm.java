package org.jmonitoring.console.methodcall;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.util.HashMap;
import java.util.Map;

import org.jmonitoring.core.configuration.Configuration;
import org.jmonitoring.core.dto.MethodCallDTO;

/**
 * Form associated with <code>MethodCallDTO</code>.
 * 
 * @author pke
 */
public class MethodCallEditForm extends AbstractMethodCallForm
{
    private static final long serialVersionUID = 3618705218511058994L;

    /** The measure point assoiciate to this request. */
    private MethodCallDTO mMethodCall;

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
            tHashMap.put("position", new Integer(mMethodCall.getParent().getPosition()));
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
        tHashMap.put("position", new Integer(mMethodCall.getPosition()));
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
        MethodCallDTO tMeasure = (MethodCallDTO) mMethodCall.getChild(pPosition);
        tHashMap.put("flowId", new Integer(tMeasure.getFlowId()));
        tHashMap.put("position", new Integer(tMeasure.getPosition()));
        return tHashMap;
    }

    public String getGroupColor()
    {
        return Configuration.getInstance().getGroupAsColorString(mMethodCall.getGroupName());
    }

    /**
     * Accessor.
     * 
     * @return The name of the method.
     */
    public String getMethodName()
    {
        String tResult = super.getMethodName();
        if (tResult == null)
        {
            tResult = mMethodCall.getMethodName();
        }
        return tResult;
    }

    /**
     * Accessor.
     * 
     * @return The name of the <code>Class</code>.
     */
    public String getClassName()
    {
        String tResult = super.getClassName();
        if (tResult == null)
        {
            tResult = mMethodCall.getClassName();
        }
        return tResult;
    }

}
