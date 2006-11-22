package org.jmonitoring.console.flow;

import org.apache.struts.action.ActionForm;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class FlowIdForm extends ActionForm
{
    private static final long serialVersionUID = -1057898735932617667L;

    private int mId;

    /**
     * @return Returns the id.
     */
    public int getId()
    {
        return mId;
    }

    /**
     * @param pId The id to set.
     */
    protected void setId(int pId)
    {
        mId = pId;
    }

}
