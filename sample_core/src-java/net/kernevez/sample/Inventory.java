package net.kernevez.sample;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

import java.util.List;
import java.util.Vector;

/**
 * Sample Claas.
 */
public class Inventory
{

    private static final int TEMPO = 5;
    private List mItems = new Vector();

    /**
     * For the Sample.
     * @param pItem For the Sample
     */
    public void addItem(Item pItem)
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
     * @param pItem For the Sample
     */
    public void removeItem(Item pItem)
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