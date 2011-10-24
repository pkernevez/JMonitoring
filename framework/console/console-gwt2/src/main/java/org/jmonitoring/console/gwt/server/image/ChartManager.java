package org.jmonitoring.console.gwt.server.image;

import java.util.HashMap;
import java.util.Map;

import net.sf.cglib.core.CollectionUtils;

/***********************************************************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved. * Please look at license.txt for more license detail. *
 **********************************************************************************************************************/

public class ChartManager
{
    private Map<String, Integer> groupOrder;

    private Map<Integer, String> groupInverse;

    public ChartManager()
    {
        this(new HashMap<String, Integer>());
    }

    public ChartManager(Map<String, Integer> pGroupOrder)
    {
        setGroupOrder(pGroupOrder);
    }

    public int getPosition(String pGroupName)
    {
        Integer tInt = groupOrder.get(pGroupName);
        return (tInt == null ? createNewId(pGroupName) : tInt.intValue());
    }

    private int createNewId(String pGroupName)
    {
        int tFirstFreeIndex = -1, tCurIndex = 1;
        while (tFirstFreeIndex < 0)
        {
            if (groupInverse.get(tCurIndex) == null)
            {
                tFirstFreeIndex = tCurIndex;
                groupInverse.put(tFirstFreeIndex, pGroupName);
                groupOrder.put(pGroupName, tFirstFreeIndex);
            }
            tCurIndex++;
        }
        return tFirstFreeIndex;
    }

    /**
     * @param pGroupOrder the groupOrder to set
     */
    private void setGroupOrder(Map<String, Integer> pGroupOrder)
    {
        groupOrder = pGroupOrder;
        groupInverse = new HashMap<Integer, String>();
        CollectionUtils.reverse(groupOrder, groupInverse);
    }
}
