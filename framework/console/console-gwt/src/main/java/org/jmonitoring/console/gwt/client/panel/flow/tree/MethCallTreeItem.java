package org.jmonitoring.console.gwt.client.panel.flow.tree;

import org.jmonitoring.console.gwt.client.dto.MethodCallDTO;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class MethCallTreeItem extends TreeItem
{
    final int mMethId;

    boolean mIsLoaded = false;

    public MethCallTreeItem(Widget tWidget, MethodCallDTO pMeth)
    {
        super(tWidget);
        mMethId = (pMeth == null ? -1 : pMeth.getPosition());
        addSubItems(pMeth);
    }

    public MethCallTreeItem(MethodCallDTO pMeth)
    {
        this(getWidget(pMeth), pMeth);
    }

    private static Widget getWidget(MethodCallDTO pMeth)
    {
        return new HTML(pMeth.getClassName() + "." + pMeth.getMethodName());
    }

    private void addSubItems(MethodCallDTO pMethodCallDTO)
    {
        if (pMethodCallDTO.getChildren() != null)
        {
            for (MethodCallDTO tMeth : pMethodCallDTO.getChildren())
            {
                addItem(new MethCallTreeItem(tMeth));
            }
        }
    }

    /**
     * @return the methId
     */
    public int getMethId()
    {
        return mMethId;
    }

    /**
     * @return the isLoaded
     */
    public boolean isLoaded()
    {
        return mIsLoaded;
    }

    /**
     * @param pIsLoaded the isLoaded to set
     */
    public void setLoaded(boolean pIsLoaded)
    {
        mIsLoaded = pIsLoaded;
    }
}