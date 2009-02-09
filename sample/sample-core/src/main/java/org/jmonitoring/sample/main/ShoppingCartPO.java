package org.jmonitoring.sample.main;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author pke
 * 
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code
 * Templates
 */
public class ShoppingCartPO
{

    private int mId = -1;

    private static final int TEMPO5 = 17;

    private static final int TEMPO4 = 16;

    private static final int TEMPO3 = 15;

    private static final int TEMPO = 7;

    private List<ItemPO> mItems = new ArrayList<ItemPO>();

    private static ThreadLocal<Integer> sCounter = new ThreadLocal<Integer>();

    public static void setCounter(int pCounter)
    {
        sCounter.set(new Integer(pCounter));
    }

    /**
     * For the Sample
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
        if (sCounter.get() == null)
        {
            sCounter.set(new Integer(0));
        }
        sCounter.set(new Integer(sCounter.get() + 1));
        if (3 == sCounter.get())
        {
            throw new RuntimeException("Pour declancher une exception sur item3");
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
            Thread.sleep(TEMPO3);
        } catch (InterruptedException e)
        {
            // @todo Auto-generated catch block
            e.printStackTrace();
        }
        mItems.remove(pItem);
    }

    /**
     * For the Sample
     * 
     */
    public void empty()
    {
        try
        {
            Thread.sleep(TEMPO4);
        } catch (InterruptedException e)
        {
            // @todo Auto-generated catch block
            e.printStackTrace();
        }
        mItems.clear();
    }

    /**
     * For the Sample
     * 
     * @return For the Sample
     */
    public float totalValue()
    {
        // unimplemented... free!
        try
        {
            Thread.sleep(TEMPO5);
        } catch (InterruptedException e)
        {
            // @todo Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    public int getId()
    {
        return mId;
    }

    public void setId(int pId)
    {
        mId = pId;
    }

    public List<ItemPO> getItems()
    {
        return mItems;
    }

    public void setItems(List<ItemPO> pItems)
    {
        mItems = pItems;
    }

}