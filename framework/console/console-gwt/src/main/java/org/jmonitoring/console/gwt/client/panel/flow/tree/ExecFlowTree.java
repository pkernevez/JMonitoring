package org.jmonitoring.console.gwt.client.panel.flow.tree;

import com.google.gwt.user.client.ui.Tree;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class ExecFlowTree extends Tree
{
    final int mFlowId;

    /**
     * @return the flowId
     */
    public int getFlowId()
    {
        return mFlowId;
    }

    public ExecFlowTree(int pFlowId)
    {
        mFlowId = pFlowId;
    }
}