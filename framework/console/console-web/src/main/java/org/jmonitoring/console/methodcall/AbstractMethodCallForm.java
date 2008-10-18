package org.jmonitoring.console.methodcall;

import org.apache.struts.action.ActionForm;
import org.jmonitoring.core.configuration.MeasureException;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class AbstractMethodCallForm extends ActionForm
{

    private static final long serialVersionUID = -4211188006931205583L;

    /** Flow technical identifier. */
    private int mFlowId = -1;

    /** Sequence identifier. */
    private int mPosition = -1;

    /** Name of the method for which we want to find static. */
    private String mMethodName;

    /** Name of the class for which we want to find static. */
    private String mClassName;

    /**
     * Allow to know if the parameters passed by the query was by name or by identifier.
     * 
     * @return true if we pass parameters <code>ClassName</code> and <code>MethodName</code>.
     */
    public boolean isParametersByName()
    {
        boolean tResult;
        if (mMethodName != null && mClassName != null)
        { // We give name parameters
            tResult = true;
        } else if (mFlowId != -1 && mPosition != -1)
        { // We give identifier parameters
            tResult = false;
        } else
        { // Invalid parameters
            throw new MeasureException(
                                       "Invalid parameter expected 'flowId' and 'position' OR " + "'methodName' and 'className'.");
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
    public int getPosition()
    {
        return mPosition;
    }

    /**
     * Accessor.
     * 
     * @param pSequenceId The sequence identifier.
     */
    public void setPosition(int pSequenceId)
    {
        mPosition = pSequenceId;
    }

    /**
     * Accessor.
     * 
     * @return The name of the <code>Class</code>.
     */
    public String getClassName()
    {
        return mClassName;
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
        return mMethodName;
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

}
