package org.jmonitoring.sample.main;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.util.ArrayList;
import java.util.List;

/**
 * Sample Claas.
 */
public class Inventory
{

    private static final int TEMPO = 5;

    private final List<ItemPO> mItems = new ArrayList<ItemPO>();

    /**
     * For the Sample.
     * 
     * @param pItem For the Sample
     */
    public void addItem(ItemPO pItem)
    {
        try
        {
            Thread.sleep(TEMPO);
        } catch (InterruptedException e)
        {
            // @todo Auto-generated catch block
            e.printStackTrace();
        }
        mItems.add(pItem);
    }

    /**
     * For the Sample
     * 
     * @param pItem For the Sample
     */
    public void removeItem(ItemPO pItem)
    {
        try
        {
            Thread.sleep(TEMPO);
        } catch (InterruptedException e)
        {
            // @todo Auto-generated catch block
            e.printStackTrace();
        }
        mItems.remove(pItem);
    }

}