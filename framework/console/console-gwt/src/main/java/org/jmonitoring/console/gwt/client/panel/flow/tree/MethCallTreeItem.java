package org.jmonitoring.console.gwt.client.panel.flow.tree;

import org.jmonitoring.console.gwt.client.dto.MethodCallDTO;
import org.jmonitoring.console.gwt.client.panel.PanelUtil;

import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class MethCallTreeItem extends TreeItem
{
    final int mMethId;

    boolean mIsLoaded = false;

    public static MethCallTreeItem create(Widget pParent, MethodCallDTO pMeth)
    {
        MethCallTreeItem tRoot = new MethCallTreeItem(pParent, pMeth);
        MethCallTreeItem tMethCallTreeItem = new MethCallTreeItem(pMeth);
        tMethCallTreeItem.setState(true);
        tRoot.addItem(tMethCallTreeItem);
        return tRoot;
    }

    public MethCallTreeItem(Widget pParent, MethodCallDTO pMeth)
    {
        super(pParent);

        mMethId = (pMeth == null ? -1 : pMeth.getPosition());
    }

    public MethCallTreeItem(MethodCallDTO pMeth)
    {
        this(PanelUtil.createMethodCallPanel(pMeth), pMeth);
        addSubItems(pMeth);
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