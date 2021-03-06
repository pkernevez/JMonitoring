package org.jmonitoring.console.gwt.client.panel.flow.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jmonitoring.console.gwt.client.dto.MethodCallDTO;
import org.jmonitoring.console.gwt.client.main.DefaultCallBack;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class AsynchroneLoad extends DefaultCallBack<Map<Integer, MethodCallDTO>>
{
    private final MethCallTreeItem mParent;

    public AsynchroneLoad(MethCallTreeItem pParent)
    {
        mParent = pParent;
    }

    public void onSuccess(Map<Integer, MethodCallDTO> pMap)
    {
        List<Integer> tIds = new ArrayList<Integer>();
        List<MethCallTreeItem> tRemove = new ArrayList<MethCallTreeItem>();
        for (int i = 0; i < mParent.getChildCount(); i++)
        {
            MethCallTreeItem tChild = (MethCallTreeItem) mParent.getChild(i);
            if (!tChild.isLoaded())
            {
                tIds.add((tChild).getMethId());
                tRemove.add(tChild);
            }
        }
        for (MethCallTreeItem tMethCallTreeItem : tRemove)
        {
            mParent.removeItem(tMethCallTreeItem);
        }
        for (Integer tChildId : tIds)
        {
            MethCallTreeItem tChild = new MethCallTreeItem(pMap.get(tChildId));
            tChild.mIsLoaded = true;
            mParent.addItem(tChild);
        }

    }
}