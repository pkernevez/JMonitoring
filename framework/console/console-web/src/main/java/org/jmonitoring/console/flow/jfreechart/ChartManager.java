package org.jmonitoring.console.flow.jfreechart;

import java.util.HashMap;
import java.util.Map;

import net.sf.cglib.core.CollectionUtils;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class ChartManager
{
    private Map<String, Integer> mGroupOrder;

    private Map<Integer, String> mGroupInverse;

    public ChartManager()
    {
    }

    public ChartManager(Map<String, Integer> pGroupOrder)
    {
        setGroupOrder(pGroupOrder);
    }

    public int getPosition(String pGroupName)
    {
        Integer tInt = mGroupOrder.get(pGroupName);
        return (tInt == null ? createNewId(pGroupName) : tInt.intValue());
    }

    private int createNewId(String pGroupName)
    {
        int tFirstFreeIndex = -1, tCurIndex = 1;
        while (tFirstFreeIndex < 0)
        {
            if (mGroupInverse.get(tCurIndex) == null)
            {
                tFirstFreeIndex = tCurIndex;
                mGroupInverse.put(tFirstFreeIndex, pGroupName);
                mGroupOrder.put(pGroupName, tFirstFreeIndex);
            }
            tCurIndex++;
        }
        return tFirstFreeIndex;
    }

    /**
     * @return the groupOrder
     */
    public Map<String, Integer> getGroupOrder()
    {
        return mGroupOrder;
    }

    /**
     * @param pGroupOrder the groupOrder to set
     */
    public void setGroupOrder(Map<String, Integer> pGroupOrder)
    {
        mGroupOrder = pGroupOrder;
        mGroupInverse = new HashMap<Integer, String>();
        CollectionUtils.reverse(mGroupOrder, mGroupInverse);
    }
}
