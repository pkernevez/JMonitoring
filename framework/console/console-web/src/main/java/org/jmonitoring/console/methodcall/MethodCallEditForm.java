package org.jmonitoring.console.methodcall;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.util.HashMap;
import java.util.Map;

import org.jmonitoring.core.dto.MethodCallDTO;

/**
 * Form associated with <code>MethodCallDTO</code>.
 * 
 * @author pke
 */
public class MethodCallEditForm extends AbstractMethodCallForm
{
    private static final long serialVersionUID = 3618705218511058994L;

    /** The measure point associated to this request. */
    private MethodCallDTO mMethodCall;

    private String mNavAction;

    private String mMsg;

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
    public Map<String, Integer> getParentIdsMap()
    {
        Map<String, Integer> tHashMap = new HashMap<String, Integer>();
        if (mMethodCall.getParent() != null)
        {
            tHashMap.put("flowId", new Integer(mMethodCall.getFlowId()));
            tHashMap.put("position", new Integer(mMethodCall.getParent().getPosition()));
        }
        return tHashMap;
    }

    public Map<String, String> getPrevInGroupMap()
    {
        Map<String, String> tHashMap = getMyMap();
        tHashMap.put("navAction", MethodCallEditActionIn.NAV_ACTION_PREV_IN_GROUP);
        return tHashMap;
    }

    public Map<String, String> getPrevInThreadMap()
    {
        Map<String, String> tHashMap = getMyMap();
        tHashMap.put("navAction", MethodCallEditActionIn.NAV_ACTION_PREV_IN_THREAD);
        return tHashMap;
    }

    public Map<String, String> getNextInThreadMap()
    {
        Map<String, String> tHashMap = getMyMap();
        tHashMap.put("navAction", MethodCallEditActionIn.NAV_ACTION_NEXT_IN_THREAD);
        return tHashMap;
    }

    public Map<String, String> getNextInGroupMap()
    {
        Map<String, String> tHashMap = getMyMap();
        tHashMap.put("navAction", MethodCallEditActionIn.NAV_ACTION_NEXT_IN_GROUP);
        return tHashMap;
    }

    private Map<String, String> getMyMap()
    {
        Map<String, String> tHashMap = new HashMap<String, String>();
        tHashMap.put("flowId", "" + mMethodCall.getFlowId());
        tHashMap.put("position", "" + mMethodCall.getPosition());
        return tHashMap;
    }

    /**
     * Return the ids of the current <code>MethodCallDTO</code>. This <code>Map</code> is used for the link
     * generation by Struts.
     * 
     * @return The list of attributes to use for the link.
     */
    public Map<String, Integer> getMethodCallIdsMap()
    {
        Map<String, Integer> tHashMap = new HashMap<String, Integer>();
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
    public Map<String, Integer> getChildMethodCallIdsMap(int pPosition)
    {
        Map<String, Integer> tHashMap = new HashMap<String, Integer>();
        MethodCallDTO tMeasure = mMethodCall.getChild(pPosition);
        tHashMap.put("flowId", new Integer(tMeasure.getFlowId()));
        tHashMap.put("position", new Integer(tMeasure.getPosition()));
        return tHashMap;
    }

    public String getGroupColor()
    {
        return mMethodCall.getGroupColor();
    }

    /**
     * Accessor.
     * 
     * @return The name of the method.
     */
    @Override
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
    @Override
    public String getClassName()
    {
        String tResult = super.getClassName();
        if (tResult == null)
        {
            tResult = mMethodCall.getClassName();
        }
        return tResult;
    }

    /**
     * @return the navAction
     */
    public String getNavAction()
    {
        return mNavAction;
    }

    /**
     * @param pNavAction the navAction to set
     */
    public void setNavAction(String pNavAction)
    {
        mNavAction = pNavAction;
    }

    public void setMsg(String pMsg)
    {
        mMsg = pMsg;
    }

    public String getMsg()
    {
        return mMsg;
    }

}
