package org.jmonitoring.sample.main;

/***************************************************************************
 * Copyright 2005 Philippe Kernevez All rights reserved.                   *
 * Please look at license.txt for more license detail.                     *
 **************************************************************************/

/**
 * 
 * @author pke
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public final class ShoppingCartOperator
{
    private static final int TEMPO2 = 19;
    private static final int TEMPO = 18;
    private ShoppingCartOperator()
    {
    }
    /**
     * For the Sample
     * @param pSc For the Sample
     * @param pInventory For the Sample
     * @param pItem For the Sample
     */
    public static void addShoppingCartItem(ShoppingCart pSc, Inventory pInventory, Item pItem)
    {
        try
        {
            Thread.sleep(TEMPO);
        } catch (InterruptedException e)
        {
            // @todo Auto-generated catch block
            e.printStackTrace();
        }
        pInventory.removeItem(pItem);

        pSc.addItem(pItem);

    }

    /**
     * For the Sample
     * @param pSc For the Sample
     * @param pInventory For the Sample
     * @param pItem For the Sample
     */
    public static void removeShoppingCartItem(ShoppingCart pSc, Inventory pInventory, Item pItem)
    {
        try
        {
            Thread.sleep(TEMPO2);
        } catch (InterruptedException e)
        {
            // @todo Auto-generated catch block
            e.printStackTrace();
        }
        pSc.removeItem(pItem);

        pInventory.addItem(pItem);

    }
}
