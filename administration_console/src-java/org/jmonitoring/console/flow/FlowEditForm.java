package org.jmonitoring.console.flow;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import org.jmonitoring.core.measure.ExecutionFlow;

import org.apache.struts.action.ActionForm;

/**
 * @author pke
 * 
 * @todo To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 *       Templates
 */
public class FlowEditForm extends ActionForm
{

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3617294527896041011L;

    private ExecutionFlow mExecutionFlow;

    private int mId;

    private int mDurationMin;
    
    /**
     * @return Returns the mDurationMin.
     */
    public int getDurationMin()
    {
        return mDurationMin;
    }
    /**
     * @param pDurationMin The mDurationMin to set.
     */
    public void setDurationMin(int pDurationMin)
    {
        mDurationMin = pDurationMin;
    }
    /**
     * Allow to know what to do when there is a lot of <code>MeasuePoint</code>
     * and that the tree could be very slow to show.
     */
    private int mKindOfAction;
    
    public static final int ACTION_DEFAULT = 0;
    
    public static final int ACTION_ONLY_GRAPH = 1;
    
    public static final int ACTION_FORCE = 2;
    
    public static final int ACTION_DURATION_FILTER = 3;
    
    /**
     * @return Returns the mId.
     */
    public int getId()
    {
        return mId;
    }

    /**
     * @param pId The mId to set.
     */
    public void setId(int pId)
    {
        mId = pId;
    }

    /**
     * @return Returns the mExecutionFlow.
     */
    public ExecutionFlow getExecutionFlow()
    {
        return mExecutionFlow;
    }

    /**
     * @param pExecutionFlow The mExecutionFlow to set.
     */
    public void setExecutionFlow(ExecutionFlow pExecutionFlow)
    {
        mExecutionFlow = pExecutionFlow;
    }
    
    /**
     * Accessor
     * @return The kind of action.
     */
    public int getKindOfAction()
    {
        return mKindOfAction;
    }
    /**
     * Accessor.
     * @param pKindOfAction The Kind of action.
     */
    public void setKindOfAction(int pKindOfAction)
    {
        mKindOfAction = pKindOfAction;
    }
}
